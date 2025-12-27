package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Outtake {

    private static DcMotorEx outtakeMotor1, outtakeMotor2, rotate;
    private static Servo hood, clutch, rotateServo;
    private static final double STAGE_1 = .48, STAGE_2 = .53, BACK_HOOD = .75, FRONT_HOOD = .25, SPEED_DIV = 5;
    private static final int CHAMBER_OFFSET = 200, OFFSET = 200;
    private static double speed = .75 * 2800;

    public enum ClutchStates {
        STAGE_ONE,
        STAGE_TWO
    }

    public static Outtake.ClutchStates currentState;

    public static void init(HardwareMap hwMap) {
        outtakeMotor2 = hwMap.get(DcMotorEx.class, "outtakemotor2");
        outtakeMotor1 = hwMap.get(DcMotorEx.class, "outtakemotor1");
        rotateServo = hwMap.get(Servo.class, "rotate");
        clutch = hwMap.get(Servo.class, "clutch");
        hood = hwMap.get(Servo.class, "hood");

        //initialize to start positions (placeholders)
        clutch.setPosition(STAGE_1);
        hood.setPosition(FRONT_HOOD);
        rotateServo.setPosition(0);

        outtakeMotor1.setVelocityPIDFCoefficients(11, 3, 2, 2);
        outtakeMotor2.setVelocityPIDFCoefficients(11, 3, 2, 2);
    }

    public static void startOuttake() {
        currentState = ClutchStates.STAGE_ONE;
    }

    public static void clutchUpdate() {
        double rpm1, rpm2;

        outtakeMotor1.setVelocity(speed);
        outtakeMotor2.setVelocity(speed);

        rpm1 = outtakeMotor1.getVelocity();
        rpm2 = outtakeMotor2.getVelocity();

        switch (currentState) {
            case STAGE_ONE:
                if (rpm1 >= speed - 100 || rpm2 >= speed - 100) {
                    clutch.setPosition(STAGE_2);
                }
                currentState = ClutchStates.STAGE_TWO;
            case STAGE_TWO:
                break;
        }
    }

    public static void autoAimBlue(double x, double y, boolean launch) {
        double target, dist, pos;

        target = Math.atan((144 - y) / x);

        if (launch) {
            pos = target * (2048 / 360) + OFFSET + CHAMBER_OFFSET;
        } else {
            pos = target * (2048 / 360) + OFFSET;
        }

        dist = Math.sqrt(Math.pow(x, 2) + Math.pow(144 - y, 2));

        speed = dist / SPEED_DIV;

        rotate.setTargetPosition((int) pos);
    }

    public static void autoAimRed(double x, double y, boolean launch) {
        double target, dist, pos;

        target = Math.atan((144 - y) / 144 - x);

        if (launch) {
            pos = target * (2048 / 360) + OFFSET + CHAMBER_OFFSET;
        } else {
            pos = target * (2048 / 360) + OFFSET;
        }
        dist = Math.sqrt(Math.pow(144 - x, 2) + Math.pow(144 - y, 2));

        speed = dist / SPEED_DIV;

        rotate.setTargetPosition((int) pos);

    }
}
