package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class Drivetrain {

   static private DcMotor frontLeft, frontRight, backLeft, backRight;

    static private float frPower, flPower, blPower, brPower;

    public static void init (HardwareMap hwMap) {
        frontLeft = hwMap.get(DcMotor.class, "front_left");
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight = hwMap.get(DcMotor.class, "front_Right");
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft = hwMap.get(DcMotor.class, "back_left");
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight = hwMap.get(DcMotor.class, "back_right");
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    static public void drive(float ly, float lx, float rx) {

        float denominator = Math.max(Math.abs(ly) + Math.abs(lx) + Math.abs(rx), 1);
        float fl = (ly + lx + rx) / denominator;
        float bl = (ly - lx + rx) / denominator;
        float fr = (ly - lx - rx) / denominator;
        float br = (ly + lx - rx) / denominator;

        frontLeft.setPower(Range.clip(fl, -1, 1));
        frontRight.setPower(Range.clip(fr, -1, 1));
        backLeft.setPower(Range.clip(bl, -1, 1));
        backRight.setPower(Range.clip(br, -1, 1));

    }

}
