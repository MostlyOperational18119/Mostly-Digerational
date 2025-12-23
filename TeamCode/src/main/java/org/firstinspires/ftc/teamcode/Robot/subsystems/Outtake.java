package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Outtake {

    static private DcMotorEx outtakeMotor1, outtakeMotor2, rotate;
    static private Servo hood, clutch, rotateServo;
    static private final double CLUTCH_IN = .75, CLUTCH_OUT = .75, BACK_HOOD = .75,FRONT_HOOD = .25, SPEED_DIV = 5;
    static private final int CHAMBER_OFFSET = 200, OFFSET = 200;
    private static double speed;

    public static void init (HardwareMap hwMap) {
        outtakeMotor2 = hwMap.get(DcMotorEx.class, "outtakemotor2");
        outtakeMotor1 = hwMap.get(DcMotorEx.class, "outtakemotor1");
        rotateServo = hwMap.get(Servo.class, "rotate");
        clutch = hwMap.get(Servo.class, "clutch");
        hood = hwMap.get(Servo.class, "hood");

        //initialize to start positions (placeholders)
        clutch.setPosition(CLUTCH_OUT);
        hood.setPosition(0);
        rotateServo.setPosition(0);
    }

    public static void autoAimBlue (double x, double y, boolean launch) {
        double target, dist, pos;

        target = Math.atan((144-y)/x);

        if (launch) {
            pos = target*(2048/360) + OFFSET + CHAMBER_OFFSET;
        } else {
            pos = target*(2048/360) + OFFSET;
        }

        dist = Math.sqrt(Math.pow(x, 2) + Math.pow(144-y, 2));

        speed  = dist/SPEED_DIV;

        rotate.setTargetPosition((int)pos);
    }

    public static void autoAimRed (double x, double y, boolean launch) {
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
    public static void run (double speed) {
        double rpm1, rpm2;

        outtakeMotor1.setVelocity(speed);
        outtakeMotor2.setVelocity(speed);

        rpm1 = outtakeMotor1.getVelocity()/28*60;
        rpm2 = outtakeMotor2.getVelocity()/28*60;

        if (rpm1 >= 3000 || rpm2 >= 3000) {
            clutch.setPosition(CLUTCH_IN);
        } else {
            clutch.setPosition(CLUTCH_OUT);
        }

    }
}
