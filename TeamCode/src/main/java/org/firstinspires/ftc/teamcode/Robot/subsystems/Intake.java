package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {


    static private DcMotorEx intake;
    private final float POWER = 1;

    public void init (HardwareMap hwMap) {
        intake = hwMap.get(DcMotorEx.class, "intake_motor");
    }

    public void intake () {
        intake.setPower(POWER);
    }
}
