package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Drivetrain {
    static public DcMotor frontLeft, backLeft, backRight, frontRight;

    public static final int DOWNPOS = 0;
    public final static int UPPOS = 1;

    static public Servo BreakPad;
    public static void init (HardwareMap hwMap) {
        frontLeft = hwMap.get(DcMotor.class, "motorFL");
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight = hwMap.get(DcMotor.class, "motorFR");
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft = hwMap.get(DcMotor.class, "motorBL");
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight = hwMap.get(DcMotor.class, "motorBR");
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BreakPad = hwMap.get(Servo.class, "BreakPad");
    }

    static public void BreakPadDown () {
        BreakPad.setPosition(DOWNPOS);
    }

    static public void BreakPadUp () {
        BreakPad.setPosition(UPPOS);
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
    public static int outtakePosition() {
        return -Intake.intakeFront.getCurrentPosition();
    }

}


