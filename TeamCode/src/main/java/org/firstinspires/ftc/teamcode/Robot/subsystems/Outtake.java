package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Outtake {

    static private DcMotorEx outtakeMotor1, outtakeMotor2, rotate;
    private Servo hood, clutch, rotateServo;
    private final double CLUTCH_IN = .75;
    private final double CLUTCH_OUT = .75;
    private final double BACK_HOOD = .75;
    private final double FRONT_HOOD = .25;
    private final int CHAMBER_OFFSET = 200;
    private final int OFFSET = 200;

    private double speed;

    public void init (HardwareMap hwMap) {
        outtakeMotor2 = hwMap.get(DcMotorEx.class, "outtake_motor_2");
        outtakeMotor1 = hwMap.get(DcMotorEx.class, "outtake_motor_1");
        rotateServo = hwMap.get(Servo.class, "rotate");
        clutch = hwMap.get(Servo.class, "clutch");
        hood = hwMap.get(Servo.class, "hood");
        clutch.setPosition(CLUTCH_OUT);
    }

    public void autoAim (double x, double y, boolean launch) {
        double target;
        double dist;
        if (launch) {
            target = Math.atan((y - OFFSET - CHAMBER_OFFSET)/x);
        } else {
            target =  Math.atan((y - OFFSET)/x);
        }
        dist = Math.pow(x, 2) + Math.pow(y, 2); Math.pow(x, 2);
        
    }

    public void run (double speed) {
        double rpm1, rpm2;

        outtakeMotor1.setVelocity(speed);
        outtakeMotor2.setVelocity(speed);

        do {
            rpm1 = outtakeMotor1.getVelocity()/28*60;
            rpm2 = outtakeMotor2.getVelocity()/28*60;
        } while (rpm1 < 3000 || rpm2 < 3000);
        clutch.setPosition(CLUTCH_IN);
    }
}
