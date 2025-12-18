package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Robot.Robot;

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

    static public void drive(float y, float x, float rx) {

            float denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            float fl = (y + x + rx) / denominator;
            float bl = (y - x + rx) / denominator;
            float fr = (y - x - rx) / denominator;
            float br = (y + x - rx) / denominator;

        frontLeft.setPower(Range.clip(fl, -1, 1));
        frontRight.setPower(Range.clip(fr, -1, 1));
        backLeft.setPower(Range.clip(bl, -1, 1));
        backRight.setPower(Range.clip(br, -1, 1));



    }

}
