package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Outtake {

    private static DcMotorEx outtakeMotor1, outtakeMotor2, rotate;
    private static Servo hood, clutch, rotateServo;
    private static final double CLUTCH_IN = .75, CLUTCH_OUT = .75, BACK_HOOD = .75,FRONT_HOOD = .25, SPEED_DIV = 5;
    private static final int CHAMBER_OFFSET = 200, OFFSET = 200;
    private static double speed;

    public static void init (HardwareMap hwMap) {
        outtakeMotor2 = hwMap.get(DcMotorEx.class, "outtakemotor2");
        outtakeMotor1 = hwMap.get(DcMotorEx.class, "outtakemotor1");
        rotateServo = hwMap.get(Servo.class, "rotate");
        clutch = hwMap.get(Servo.class, "clutch");
        hood = hwMap.get(Servo.class, "hood");

        //initialize to start positions (placeholders)
        clutch.setPosition(CLUTCH_OUT);
        hood.setPosition(FRONT_HOOD);
        rotateServo.setPosition(0);

        outtakeMotor1.setVelocityPIDFCoefficients(11, 3, 2, 2);
        outtakeMotor2.setVelocityPIDFCoefficients(11, 3, 2, 2);
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
    public static void run () {
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
