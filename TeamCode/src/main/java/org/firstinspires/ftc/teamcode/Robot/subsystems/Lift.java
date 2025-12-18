package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Lift {

    private Servo lift1, lift2;
    private final double LIFT1 = .22;
    private final double LIFT2 = .22;

    public void init (HardwareMap hwMap) {
        lift1 = hwMap.get(Servo.class, "lift1");
        lift2 = hwMap.get(Servo.class, "lift2");
    }

    public void lift () {
        lift1.setPosition(LIFT1);
        lift2.setPosition(LIFT2);
    }

}
