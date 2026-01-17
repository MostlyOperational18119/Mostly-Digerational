package org.firstinspires.ftc.teamcode.Robot.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Outtake {

    //motors+servos
    private static DcMotorEx outtakeMotorLeft, outtakeMotorRight;
    private static DcMotor encoderMotor;
    private static Servo hood;
    private static CRServo rotateServo;

    //outtake speed stuff
    private static final double SPEED_CONST_CLOSE = 100w, SPEED_CONST_FAR = 204, FAR_HOOD = .0, CLOSE_HOOD = .0;

    //stuff for aiming
    private static int maxClicks =  24680;
    private static int tolerance = 50;
    static double outtakePower = 0.0;

    //stuff for auto aiming
    public static boolean isBlue = false;
    public static double robotX = 72;
    public static double robotY = 72;
    public static double robotOrientation = 0;
    public static int angleOffset = 90;
    static double goalX;
    static double goalY = 144;
    static double chamberY = 96;

    //state machine
    public static States currentState = States.AIM_CHAMBER;

    public enum States {
        AIM_CHAMBER,
        AIM_GOAL
    }

    public static void init(HardwareMap hwMap) {
        outtakeMotorRight = hwMap.get(DcMotorEx.class, "outtakeMotorRight");
        outtakeMotorLeft = hwMap.get(DcMotorEx.class, "outtakeMotorLeft");
        outtakeMotorLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        rotateServo = hwMap.get(CRServo.class, "rotate");
        hood = hwMap.get(Servo.class, "hood"); //min = 0.72, max = 0.01

        encoderMotor = hwMap.get(DcMotor.class, "frontIntake");
        encoderMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encoderMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //initialize to start positions (placeholders)
        hood.setPosition(FAR_HOOD);
        rotateServo.setPower(0);

        outtakeMotorLeft.setVelocityPIDFCoefficients(6, 2, 6, 2);
        outtakeMotorRight.setVelocityPIDFCoefficients(6, 2, 6, 2);

        //set blue/red aiming
        if (isBlue) {goalX = 0;} else {goalX = 144;}
    }

    public static void update(int targetClicks) {
        int clicks = Drivetrain.outtakePosition();

        if (Math.abs(targetClicks - clicks) <= tolerance) {
            rotateServo.setPower(0);
        } else {
            outtakePower = (double) (targetClicks - clicks) / (maxClicks / 8.22666666667);
            outtakePower = Math.max(-1, Math.min(1, outtakePower));
            rotateServo.setPower(outtakePower);
        }
    }
    public static int setRotationPosition(double target) {
        target = Math.max(0, Math.min(1, target));
        return (int) (maxClicks * target);
    }
    public static int setTarget(double target) {
        target = Math.max(0, Math.min(maxClicks, target));
        return (int) target;
    }

    public static double pointAtGoal() {
        double dx = goalX - robotX;
        double dy = goalY - robotY;

        double absoluteAngleToGoal = Math.toDegrees(Math.atan2(dy, dx));
        absoluteAngleToGoal = ((absoluteAngleToGoal % 360) + 360) % 360;

        // Flip the angle calculation
        double relativeAngle = 360 - (absoluteAngleToGoal - (angleOffset + robotOrientation));

        relativeAngle = ((relativeAngle % 360) + 360) % 360;

        return (relativeAngle * 65.871345) + 4000;
    }
    public static double pointAtChamber() {
        double dx = goalX - robotX;
        double dy = chamberY - robotY;
        Indexer.chamberIncrease = 0;

        double absoluteAngleToGoal = Math.toDegrees(Math.atan2(dy, dx));
        absoluteAngleToGoal = ((absoluteAngleToGoal % 360) + 360) % 360;

        // Flip the angle calculation
        double relativeAngle = 360 - (absoluteAngleToGoal + 90 - robotOrientation);

        relativeAngle = ((relativeAngle % 360) + 360) % 360;

        return (relativeAngle * 68.555) + 2816;
    }


    public static void outtakeUpdate(int launch) {

        if (launch > 0) {
            currentState = States.AIM_CHAMBER;
        } else {
            currentState = States.AIM_GOAL;
        }

        switch (currentState) {
            case AIM_GOAL:
                update(setTarget(pointAtGoal()));
                break;
            case AIM_CHAMBER:
                update(setTarget(pointAtChamber()));
                break;
        }
    }

    public static void outtakeSpeed() {
        double dx = goalX - robotX;
        double dy = goalX - robotY;
        double speed;

        double dist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        if (robotY > 60) {
            hood.setPosition(CLOSE_HOOD);
            speed = SPEED_CONST_CLOSE * Math.sqrt(dist);
        } else {
            hood.setPosition(FAR_HOOD);
            speed = SPEED_CONST_FAR * Math.sqrt(dist);
      }

        outtakeMotorLeft.setVelocity(speed);
        outtakeMotorRight.setVelocity(speed);
    }

    public static double testTelemetryMotor1() { //clutch test program
        return outtakeMotorLeft.getVelocity();
    }

    public static double testTelemetryMotor2() { //test program
        return outtakeMotorRight.getVelocity();
    }


}
