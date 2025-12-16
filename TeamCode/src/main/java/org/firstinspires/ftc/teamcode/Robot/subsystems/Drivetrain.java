package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Robot.Robot;

public class Drivetrain {

   static private DcMotor frontLeft, frontRight, backLeft, backRight;

    static private float frPower, flPower, blPower, brPower;

    public void init (HardwareMap hwMap) {
        frontLeft = hwMap.get(DcMotor.class, "front_left");
        frontRight = hwMap.get(DcMotor.class, "front_Right");
        backLeft = hwMap.get(DcMotor.class, "back_left");
        backRight = hwMap.get(DcMotor.class, "back_right");
    }

    static public void drive() {

        frPower = (float) Range.clip(frPower, -1.0, 1.0);
        flPower = (float) Range.clip(flPower, -1.0, 1.0);
        brPower = (float) Range.clip(brPower, -1.0, 1.0);
        blPower = (float) Range.clip(blPower, -1.0, 1.0);

        frontLeft.setPower(flPower);
        frontRight.setPower(frPower);
        backLeft.setPower(blPower);
        backRight.setPower(brPower);





    }

}
