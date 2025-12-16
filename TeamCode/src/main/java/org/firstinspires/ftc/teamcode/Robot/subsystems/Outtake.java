package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robot.Robot;

public class Outtake {

    public DcMotorEx rotate, outtakeMotor1, outtakeMotor2;
    public Servo hood;

    public void init (HardwareMap hwMap) {
        outtakeMotor2 = hwMap.get(DcMotorEx.class, "outtake_motor_2");
        outtakeMotor1 = hwMap.get(DcMotorEx.class, "outtake_motor_1");
        rotate = hwMap.get(DcMotorEx.class, "rotate");
        hood = hwMap.get(Servo.class, "hood");
    }

}
