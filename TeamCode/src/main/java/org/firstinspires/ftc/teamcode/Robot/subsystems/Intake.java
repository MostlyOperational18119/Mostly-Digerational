package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    static private DcMotorEx intake;
    private static float power;

    
    public static void init (HardwareMap hwMap) {
        intake = hwMap.get(DcMotorEx.class, "intake_motor");
    }
    public static void switchIntake(boolean intake) { //switches between idle and intaking (teleop)
        if (intake) {
            intakeGo();
        } else {
            intakeIdle();
        }
    }
    public static void intakeGo () { //think really hard about what this does
        power = 1;
        intake.setPower(power);
    }
    public static void intakeIdle () { //teleop "off" still spins to keep balls from fleeing
        power = 0.3f;
        intake.setPower(power);
    }
    public static void intakeStop () { //for auto: need to full stop at end
        power = 0;
        intake.setPower(power);
    }

    //telemetry for intake (probably not going to be anything but this)
    public static float intakeTelemetry() {
        return power;
    }

}
