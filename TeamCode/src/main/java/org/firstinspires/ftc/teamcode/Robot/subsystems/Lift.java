package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Lift {

    private static Servo lift1, lift2;

    public static void init (HardwareMap hwMap) {
        lift1 = hwMap.get(Servo.class, "lift1");
        lift2 = hwMap.get(Servo.class, "lift2");

        //initialize to start position
        lift1.setPosition(0);
        lift2.setPosition(0);
    }

    public static void lift () {
        double LIFT = .22;
        lift1.setPosition(LIFT);
        lift2.setPosition(LIFT);
    }

}
