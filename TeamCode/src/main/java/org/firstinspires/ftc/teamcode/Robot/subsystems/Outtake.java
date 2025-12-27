package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Outtake {

    private static DcMotorEx outtakeMotor1, outtakeMotor2;
    private static Servo hood, rotateServo;
    private static final double SPEED_CONST = 5, HOOD_UP = .76, HOOD_DOWN = .25;
    private static final int CHAMBER_POS = 96, OFFSET = 200;
    private static double speed = 2800;

    public static States currentState;

    public enum States {
        AIM_CHAMBER_BLUE,
        AIM_GOAL_BLUE,
        AIM_CHAMBER_RED,
        AIM_GOAL_RED
    }

    public static void outtakeUpdate (double x, double y, int launch, boolean red) {

        if (red) {
            if (launch > 0) {
                currentState = States.AIM_CHAMBER_RED;
            } else {
                currentState = States.AIM_GOAL_RED;
            }
        } else {
            if (launch < 0) {
                currentState = States.AIM_CHAMBER_BLUE;
            } else {
                currentState = States.AIM_GOAL_BLUE;
            }
        }

        switch (currentState) {
            case AIM_GOAL_BLUE:
                autoAim(144-x, y);
                break;
            case AIM_CHAMBER_BLUE:
                autoAim(CHAMBER_POS-x,y);
                break;
            case AIM_GOAL_RED:
                autoAim(144-x, 144-y);
                break;
            case AIM_CHAMBER_RED:
                autoAim(CHAMBER_POS-x,144-y);
                break;
        }
    }


    public static void init(HardwareMap hwMap) {
        outtakeMotor2 = hwMap.get(DcMotorEx.class, "outtakeMotor2");
        outtakeMotor1 = hwMap.get(DcMotorEx.class, "outtakeMotor1");
        outtakeMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
        //rotateServo = hwMap.get(Servo.class, "rotate");
        //hood = hwMap.get(Servo.class, "hood");

        //initialize to start positions (placeholders)
        //hood.setPosition(FRONT_HOOD);
        //rotateServo.setPosition(0);

        outtakeMotor1.setVelocityPIDFCoefficients(11, 3, 2, 2);
        outtakeMotor2.setVelocityPIDFCoefficients(11, 3, 2, 2);
    }

    public static void startOuttake(boolean red, boolean launch) {

    }
    public static double testTelemetryMotor1() { //clutch test program
        return outtakeMotor1.getVelocity();
    }
    public static double testTelemetryMotor2() { //test program
        return outtakeMotor2.getVelocity();
    }


    public static void run() { //clutch test program
        outtakeMotor2.setVelocity(speed);
        outtakeMotor1.setVelocity(speed);
    }

    public static void stop() { //clutch test program
        outtakeMotor2.setVelocity(0);
        outtakeMotor1.setVelocity(0);
    }


    private static void autoAim (double x, double y) {
        double target, dist, pos;

        target = Math.atan((y) / x);

        pos = target * (2048 / 360) + OFFSET;

        dist = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

        if (dist < 96) {
            hood.setPosition(HOOD_UP);
        } else {
            hood.setPosition(HOOD_DOWN);
        }

        speed = Math.sqrt(dist) * SPEED_CONST;

        rotateServo.setPosition((int) pos);
    }
}
