package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {


    static private DcMotorEx intake;

    public static void init (HardwareMap hwMap) {
        intake = hwMap.get(DcMotorEx.class, "intake_motor");
    }

    public static void intakeGo () {
        float POWER = 1;
        intake.setPower(POWER);
    }

    public static void intakeIdle () {
        float POWER = 0.3f;
        intake.setPower(POWER);
    }

}
