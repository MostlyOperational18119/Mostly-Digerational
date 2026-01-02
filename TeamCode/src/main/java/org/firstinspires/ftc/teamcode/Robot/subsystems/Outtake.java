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

    private static DcMotorEx outtakeMotor1, outtakeMotor2;
    private static DcMotor encoderMotor;
    private static Servo hood;
    private static CRServo rotateServo;
    private static final double SPEED_CONST_CLOSE = 231.2, SPEED_CONST_FAR = 204, FAR_HOOD = .20, CLOSE_HOOD = .76;
    private static final int CHAMBER_POS = 96, OFFSET = 200, HEIGHT = 44;
    private static double speed = 2800;
    private static int maxClicks =  22000;
    private static int tolerance = 5;
    public static States currentState = States.AIM_CHAMBER_BLUE;

    public enum States {
        AIM_CHAMBER_BLUE,
        AIM_GOAL_BLUE,
        AIM_CHAMBER_RED,
        AIM_GOAL_RED
    }

    public static void init(HardwareMap hwMap) {
        outtakeMotor2 = hwMap.get(DcMotorEx.class, "outtakeMotor2");
        outtakeMotor1 = hwMap.get(DcMotorEx.class, "outtakeMotor1");
        outtakeMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
        rotateServo = hwMap.get(CRServo.class, "rotate");
        hood = hwMap.get(Servo.class, "hood"); //min = 0.72, max = 0.01

        encoderMotor = hwMap.get(DcMotor.class, "motorFR");
        encoderMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encoderMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //initialize to start positions (placeholders)
        hood.setPosition(FAR_HOOD);
        rotateServo.setPower(0);

        outtakeMotor1.setVelocityPIDFCoefficients(11, 3, 2, 2);
        outtakeMotor2.setVelocityPIDFCoefficients(11, 3, 2, 2);
    }

    public static void update(int targetClicks) {
        int clicks = Drivetrain.outtakePosition();

        if (Math.abs(targetClicks - clicks) <= tolerance) {
            rotateServo.setPower(0);
        } else {
            double outtakePower = (double) (targetClicks - clicks) / (maxClicks / 2);
            outtakePower = Math.max(-1, Math.min(1, outtakePower));
            rotateServo.setPower(outtakePower);
        }
    }
    public static int setRotationPosition(double target) {
        target = Math.max(0, Math.min(1, target));
        return (int) (maxClicks * target);
    }
    public static int setTarget(double target) {
        target = Math.max(0, Math.min(22000, target));
        return (int) target;
    }
    public static double servoPower(){
        return rotateServo.getPower();
    }



//    public static void outtakeUpdate(double x, double y, int launch, boolean red) {
//
//        if (red) {
//            if (launch > 0) {
//                currentState = States.AIM_CHAMBER_RED;
//            } else {
//                currentState = States.AIM_GOAL_RED;
//            }
//        } else {
//            if (launch < 0) {
//                currentState = States.AIM_CHAMBER_BLUE;
//            } else {
//                currentState = States.AIM_GOAL_BLUE;
//            }
//        }
//
//        switch (currentState) {
//            case AIM_GOAL_BLUE:
//                autoAimHoodPlusVelo(144 - x, y);
//                break;
//            case AIM_CHAMBER_BLUE:
//                autoAimHoodPlusVelo(CHAMBER_POS - x, y);
//                break;
//            case AIM_GOAL_RED:
//                autoAimHoodPlusVelo(144 - x, 144 - y);
//            break;
//            case AIM_CHAMBER_RED:
//                autoAimHoodPlusVelo(CHAMBER_POS - x, 144 - y);
//                break;
//        }
//    }

//    public static void startOuttake(boolean red, boolean launch) {
//
//    }
//
//    public static double testTelemetryMotor1() { //clutch test program
//        return outtakeMotor1.getVelocity();
//    }
//
//    public static double testTelemetryMotor2() { //test program
//        return outtakeMotor2.getVelocity();
//    }
//
//    private static void autoAim(double x, double y) {
//        double target, dist, pos;
//
//        target = Math.atan((y) / x);
//
//        pos = target * (65.871) + OFFSET;
//
//        dist = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
//
//        if (dist < 96) {
//            hood.setPosition(CLOSE_HOOD);
//            speed = Math.sqrt(dist) * SPEED_CONST_CLOSE;
//        } else {
//            hood.setPosition(FAR_HOOD);
//            speed = Math.sqrt(dist) * SPEED_CONST_FAR;
//        }
//
//
//        //rotateServo.setPosition(pos);
//    }
//
//    public static void autoAimHoodPlusVelo(double x, double y) {
//        double target, dist, pos, hoodPos, hoodAngle;
//        //encoderMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//        target = Math.atan(y / x);
//
//        pos = target * (65.871) + OFFSET;
//
//        dist = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
//
//        hoodAngle = acot(dist / (4 * HEIGHT));
//
//        speed = Math.sqrt((2 * HEIGHT * 386.089) / (Math.pow(Math.sin(hoodAngle), 2)));
//
//        hoodPos = 0.76 - (0.56 / 17) * (64 - hoodAngle);
//
//        if (hoodPos > .7) {
//            hoodPos = .7;
//        }
//        if (hoodPos < .25) {
//            hoodPos = .25;
//        }
//        hood.setPosition(hoodPos);
//        outtakeMotor1.setVelocity(speed);
//        outtakeMotor2.setVelocity(speed);
//        update(setRotationPosition(pos));
//
//    }
//
//    private static double acot(double x) {
//        if (x == 0.0) {
//            return Math.PI / 2.0;
//        } else if (x > 0.0) {
//            return Math.atan(1.0 / x);
//        } else {
//            // For x < 0, add PI to atan(1/x) to get the correct range (0, PI)
//            return Math.PI + Math.atan(1.0 / x);
//        }
//    }
}
