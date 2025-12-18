package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Outtake {

    public DcMotorEx outtakeMotor1, outtakeMotor2;
    public Servo hood, clutch, rotate1, rotate2;

    public void init (HardwareMap hwMap) {
        outtakeMotor2 = hwMap.get(DcMotorEx.class, "outtake_motor_2");
        outtakeMotor1 = hwMap.get(DcMotorEx.class, "outtake_motor_1");
        rotate1 = hwMap.get(Servo.class, "rotate1");
        rotate2 = hwMap.get(Servo.class, "rotate2");
        clutch = hwMap.get(Servo.class, "clutch");
        hood = hwMap.get(Servo.class, "hood");
    }

}
