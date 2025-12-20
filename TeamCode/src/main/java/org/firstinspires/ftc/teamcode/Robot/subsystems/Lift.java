package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Lift {

    private Servo lift1, lift2;

    public void init (HardwareMap hwMap) {
        lift1 = hwMap.get(Servo.class, "lift1");
        lift2 = hwMap.get(Servo.class, "lift2");

        //initialize to start position
        lift1.setPosition(0);
        lift2.setPosition(0);
    }

    public void lift () {
        double LIFT = .22;
        lift1.setPosition(LIFT);
        lift2.setPosition(LIFT);
    }

}
