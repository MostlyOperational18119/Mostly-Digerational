package org.firstinspires.ftc.teamcode.Robot.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot.opmode.teleop.configurableTeleop;

public class Outtake {

    //motors+servos
    private static DcMotorEx outtakeMotorLeft, outtakeMotorRight;
    private static DcMotor encoderMotor;
    public static Servo hood;
    private static CRServo rotateServo;

    //outtake speed stuff
    //hood min: 0.58, hood max: 0
    public static double SPEED_CONST_VERY_CLOSE = 40, SPEED_CONST_CLOSE = 60, SPEED_CONST_FAR = 362, VERY_CLOSE_HOOD = 0.58, FAR_HOOD = 0, CLOSE_HOOD = .3;

    //configurable testing
    //public static double SPEED_CONST_VERY_CLOSE = configurableTeleop.VERY_CLOSE_SPEED, SPEED_CONST_CLOSE = configurableTeleop.CLOSE_SPEED, SPEED_CONST_FAR = configurableTeleop.FAR_SPEED, VERY_CLOSE_HOOD = configurableTeleop.CLOSER_HOOD, FAR_HOOD = configurableTeleop.FAR_HOOD, CLOSE_HOOD = configurableTeleop.CLOSE_HOOD;

    //testing/telemetry variables
    public static double distance, velocity;

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
    public static double goalX;
    static double goalY = 144;
    static double chamberY = 96;

    //temporarily commented out for configurableTeleop
    public static double p = 6, i = 2, d = 6, f = 2;
    //public static double p = configurableTeleop.p, i = configurableTeleop.i, d = configurableTeleop.d, f = configurableTeleop.f;

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
        hood = hwMap.get(Servo.class, "hood"); //min = 0.58, max = 0

        encoderMotor = hwMap.get(DcMotor.class, "frontIntake");
        encoderMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encoderMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //initialize to start positions (placeholders)
        hood.setPosition(FAR_HOOD);
        rotateServo.setPower(0);

        outtakeMotorLeft.setVelocityPIDFCoefficients(p, i, d, f);
        outtakeMotorRight.setVelocityPIDFCoefficients(p, i, d, f);

        //set blue/red aiming
        if (isBlue) {goalX = 4;} else {goalX = 140;}
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
        double dy = goalY - robotY;
        double speed;

        double dist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        distance = dist;

        if (robotY > 60 && robotY <= 80) {
            hood.setPosition(CLOSE_HOOD);
            speed = SPEED_CONST_CLOSE * Math.sqrt(dist);
            velocity = speed;
        } else if (robotY > 80 && (robotX < 60 || robotX > 84)){
            hood.setPosition(VERY_CLOSE_HOOD);
            speed = SPEED_CONST_VERY_CLOSE * Math.sqrt(dist);
            velocity = speed;
        } else {
            hood.setPosition(FAR_HOOD);
            speed = SPEED_CONST_FAR * Math.sqrt(dist);
            velocity = speed;
        }

        outtakeMotorLeft.setVelocity(speed);
        outtakeMotorRight.setVelocity(speed);
    }



    //testing and making variables configurable
    public static void updatePID(double p, double i, double d, double f) {
        outtakeMotorLeft.setVelocityPIDFCoefficients(p, i, d, f);
        outtakeMotorRight.setVelocityPIDFCoefficients(p, i, d, f);
    }
    public static void updateSpeedConsts(double close, double closer, double far){
        SPEED_CONST_CLOSE = close;
        SPEED_CONST_VERY_CLOSE = closer;
        SPEED_CONST_FAR = far;
    }
    public static void updateHoodPositions(double close, double closer, double far){
        VERY_CLOSE_HOOD = closer;
        CLOSE_HOOD = close;
        FAR_HOOD = far;
    }
    public static double testTelemetryMotor1() {
        return outtakeMotorLeft.getVelocity();
    }
    public static double testTelemetryMotor2() {
        return outtakeMotorRight.getVelocity();
    }
    public static double returnHoodPos(){
        return hood.getPosition();
    }
}
