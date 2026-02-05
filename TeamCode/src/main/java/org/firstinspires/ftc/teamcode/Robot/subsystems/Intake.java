package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    static public DcMotorEx intakeFront, intakeRear;
    private static float power;


    public static void init(HardwareMap hwMap) {
        intakeFront = hwMap.get(DcMotorEx.class, "frontIntake");
        intakeRear = hwMap.get(DcMotorEx.class, "backIntake");
    }

    public static void intakeGo() { //think really hard about what this does
        power = 1f;
        intakeFront.setPower(power);
        intakeRear.setPower(power);
    }

    public static void intakeSpit() { //teleop "off" still spins to keep balls from fleeing
        power = -1f;
        intakeFront.setPower(power);
        intakeRear.setPower(power);

    }

    public static void intakeStop() { //for auto: need to full stop at end
        power = 0;
        intakeFront.setPower(power);
        intakeRear.setPower(power);
    }

    //telemetry for intake (probably not going to be anything but this)
    public static float intakeTelemetry() {
        return power;
    }

}
