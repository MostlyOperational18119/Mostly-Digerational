package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {


    static private DcMotorEx intake;

    private static float power;

    public static void init (HardwareMap hwMap) {
        intake = hwMap.get(DcMotorEx.class, "intake_motor");
    }
    public static void switchIntake(boolean intake) {
        if (intake) {
            intakeGo();
        } else {
            intakeIdle();
        }
    }
    public static void intakeGo () {
        power = 1;
        intake.setPower(power);
    }
    public static void intakeIdle () {
        power = 0.3f;
        intake.setPower(power);
    }
    public static void intakeStop () {
        power = 0;
        intake.setPower(power);
    }

    public static float intakeTelemetry() {
        return power;
    }

}
