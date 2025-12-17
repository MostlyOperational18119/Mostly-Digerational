package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Robot.Robot;

public class Drivetrain {

   static private DcMotor frontLeft, frontRight, backLeft, backRight;

    static private float frPower, flPower, blPower, brPower;

    public static void init (HardwareMap hwMap) {
        frontLeft = hwMap.get(DcMotor.class, "front_left");
        frontRight = hwMap.get(DcMotor.class, "front_Right");
        backLeft = hwMap.get(DcMotor.class, "back_left");
        backRight = hwMap.get(DcMotor.class, "back_right");
    }

    static public void drive(double fl, double fr, double bl, double br) {

//        frPower = (float) Range.clip(frPower, -1.0, 1.0);
//        flPower = (float) Range.clip(flPower, -1.0, 1.0);
//        brPower = (float) Range.clip(brPower, -1.0, 1.0);
//        blPower = (float) Range.clip(blPower, -1.0, 1.0);

        frontLeft.setPower(Range.clip(fl, -1, 1));
        frontRight.setPower(Range.clip(fr, -1, 1));
        backLeft.setPower(Range.clip(bl, -1, 1));
        backRight.setPower(Range.clip(br, -1, 1));



    }

}
