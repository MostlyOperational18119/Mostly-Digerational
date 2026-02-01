package org.firstinspires.ftc.teamcode.Robot.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Robot.opmode.teleop.configurableTeleop;

import java.io.File;

public class Outtake {

    //motors+servos
    public static DcMotorEx outtakeMotorLeft;
    public static DcMotorEx outtakeMotorRight;
    private static DcMotor encoderMotor;
    public static Servo hood;
    private static CRServo rotateServo;

    //outtake speed stuff
    //hood min: 0.58, hood max: 0
    public static double SPEED_CONST_VERY_CLOSE = 240, SPEED_CONST_CLOSE = 180, SPEED_CONST_FAR = 167.5, VERY_CLOSE_HOOD = 0.58, FAR_HOOD = 0, CLOSE_HOOD = 0;

    //configurable testing
    //public static double SPEED_CONST_VERY_CLOSE = configurableTeleop.VERY_CLOSE_SPEED, SPEED_CONST_CLOSE = configurableTeleop.CLOSE_SPEED, SPEED_CONST_FAR = configurableTeleop.FAR_SPEED, VERY_CLOSE_HOOD = configurableTeleop.CLOSER_HOOD, FAR_HOOD = configurableTeleop.FAR_HOOD, CLOSE_HOOD = configurableTeleop.CLOSE_HOOD;

    //testing/telemetry variables
    public static double distance, speed;

    //stuff for aiming
    private static int maxClicks =  24680;
    private static int tolerance = 50;
    static double outtakePower = 0.0;
    public static int blueX = 9, redX = 135;
    public static int closeBlue = 2, closeRed = 142, farBlue = blueX, farRed = redX;
    public static int chamberBX = 0, chamberRX = 144;

    //stuff for auto aiming
    public static boolean isBlue = false;
    public static Pose start = new Pose(16.641,16.1903, Math.toRadians(90));
    public static double robotX = 72;
    public static double robotY = 72;
    public static double robotOrientation = 0;
    public static int angleOffset = 90;
    public static double goalX, aimGoalX;
    public static double chamberX;
    static double goalY = 144;
    static double chamberY = 96;

    //temporarily commented out for configurableTeleop
    public static double VERY_CLOSE_P = 12, VERY_CLOSE_I = 0.5, VERY_CLOSE_D = 0.5;
    public static double CLOSE_P = 13, CLOSE_I = 1.5, CLOSE_D = 1;
    public static double FAR_P = 13, FAR_I = 2, FAR_D = 0;
    //public static double p = configurableTeleop.p, i = configurableTeleop.i, d = configurableTeleop.d, f = configurableTeleop.f;

    //state machine
    public static States currentState = States.AIM_CHAMBER;
    public static String currentDistance = "";
    public static int outtakePosAuto = 0;

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

        outtakeMotorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        outtakeMotorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //initialize to start positions (placeholders)
        hood.setPosition(FAR_HOOD);
        rotateServo.setPower(0);

        //set blue/red aiming
        isBlue = Outtake.StaticVars.isBlue;
        start = Outtake.StaticVars.endPose;

        String fileName = "OuttakeVars.txt";

        String outtakePos = "";

        File myFile = AppUtil.getInstance().getSettingsFile(fileName);
        if (myFile.exists()) {
            outtakePos = ReadWriteFile.readFile(myFile);
        }

        try {
            outtakePosAuto = Integer.parseInt(outtakePos);
        }
        catch (NumberFormatException e) {
            outtakePosAuto = 0;
        }


        if (isBlue) {goalX = blueX; chamberX = chamberBX;} else {goalX = redX; chamberX = chamberRX;}
    }

    public static void update(int targetClicks, boolean isTeleOp) {
        int clicks = Drivetrain.outtakePosition() + (isTeleOp ? outtakePosAuto : 0);

        if (Math.abs(targetClicks - clicks) <= tolerance) {
            rotateServo.setPower(0);
        } else {
            outtakePower = (double) (targetClicks - clicks) / (maxClicks / 8.22666666667);
            outtakePower = Math.max(-1, Math.min(1, outtakePower));
            rotateServo.setPower(outtakePower);
        }
    }
    public static int getRotationPosition(double target) {
        target = Math.max(0, Math.min(1, target));
        return (int) (maxClicks * target);
    }
    public static int setTarget(double target, int adjust) {
        target = Math.max(0, Math.min(maxClicks, target));
        return (int) target + adjust;
    }

    public static double pointAtGoal() {
        double dx = aimGoalX - robotX;
        double dy = goalY - robotY;

        double absoluteAngleToGoal = Math.toDegrees(Math.atan2(dy, dx));
        absoluteAngleToGoal = (((absoluteAngleToGoal) % 360) + 360) % 360;

        // Flip the angle calculation
        double relativeAngle = 360 - (absoluteAngleToGoal - (angleOffset + robotOrientation));

        relativeAngle = ((relativeAngle % 360) + 360) % 360;

        return (relativeAngle * 65.871345) + 4000;
    }
    public static double pointAtChamber() {
        double dx = chamberX - robotX;
        double dy = chamberY - robotY;
        Indexer.chamberIncrease = 0;

        double absoluteAngleToChamber = Math.toDegrees(Math.atan2(dy, dx));
        absoluteAngleToChamber = (((absoluteAngleToChamber) % 360) + 360) % 360;

        // Flip the angle calculation
        double relativeAngle = 360 - (absoluteAngleToChamber -(angleOffset + robotOrientation));

        relativeAngle = ((relativeAngle % 360) + 360) % 360;

        return (relativeAngle * 65.871345) + 2816;
    }


    public static void outtakeUpdate(int launch, boolean isTeleOp, int adjust) {

        if (launch > 0) {
            currentState = States.AIM_CHAMBER;
        } else {
            currentState = States.AIM_GOAL;
        }

        switch (currentState) {
            case AIM_GOAL:
                update(setTarget(pointAtGoal(), adjust), isTeleOp);
                break;
            case AIM_CHAMBER:
                update(setTarget(pointAtChamber(), adjust), isTeleOp);
                break;
        }
    }

    public static void outtakeSpeed() {
        //distance from the goal
        double dx = goalX - robotX;
        double dy = goalY - robotY;

        distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        //logic for setting motor speeds, launch angles, and PID values
        if (distance < 60){ //when the robot is extremely close to the goal (very short distance)
            hood.setPosition(VERY_CLOSE_HOOD); //position of the hood -> small value
            speed = SPEED_CONST_VERY_CLOSE * Math.sqrt(distance); //speed set to a tested value multiplied by distance
            currentDistance = "very close";

            //modify constants for different zones
            if (isBlue) {
                aimGoalX = closeBlue;
            } else {
                aimGoalX = closeRed;
            }
            Indexer.LAUNCH_WAIT = 300;

            //unique PIDF coefficients to better control recoil from Artifacts
            outtakeMotorLeft.setVelocityPIDFCoefficients(VERY_CLOSE_P, VERY_CLOSE_I, VERY_CLOSE_D, 0);
            outtakeMotorRight.setVelocityPIDFCoefficients(VERY_CLOSE_P, VERY_CLOSE_I, VERY_CLOSE_D, 0);
        } else if (distance < 108) { //this is maximum minimum distance of the close zone
            hood.setPosition(CLOSE_HOOD);
            speed = SPEED_CONST_CLOSE * Math.sqrt(distance);
            currentDistance = "close";
            if (isBlue) {
                aimGoalX = closeBlue;
            } else {
                aimGoalX = closeRed;
            }
            Indexer.LAUNCH_WAIT = 400;
            outtakeMotorLeft.setVelocityPIDFCoefficients(CLOSE_P, CLOSE_I, CLOSE_D, 0);
            outtakeMotorRight.setVelocityPIDFCoefficients(CLOSE_P, CLOSE_I, CLOSE_D, 0);
        } else { //in all other situations, we'll be launching from the far zone
            hood.setPosition(FAR_HOOD);
            speed = SPEED_CONST_FAR * Math.sqrt(distance);
            currentDistance = "far";
            if (isBlue) {
                aimGoalX = farBlue;
            } else {
                aimGoalX = farRed;
            }
            outtakeMotorLeft.setVelocityPIDFCoefficients(FAR_P, FAR_I, FAR_D, 0);
            outtakeMotorRight.setVelocityPIDFCoefficients(FAR_P, FAR_I, FAR_D, 0);
        }

        //set the motors to the calculated speed
        outtakeMotorLeft.setVelocity(speed);
        outtakeMotorRight.setVelocity(speed);
    }



    //testing and making variables configurable
    public static void updatePID(double p, double i, double d, double f) {
        outtakeMotorLeft.setVelocityPIDFCoefficients(p, i, d, 0);
        outtakeMotorRight.setVelocityPIDFCoefficients(p, i, d, 0);
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

    public static class StaticVars {
        public static boolean isBlue = false;
         public static int outtakePos = 0;
        public static Pose endPose = new Pose(16.641,16.1903, Math.toRadians(90));
    }
}
