package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Robot.Robot;

public class Intake {


    public DcMotorEx intake;
    float power;


    public void init (HardwareMap hwMap) {
        intake = hwMap.get(DcMotorEx.class, "intake_motor");
    }

    public void intake () {
        intake.setPower(power);
    }
}
