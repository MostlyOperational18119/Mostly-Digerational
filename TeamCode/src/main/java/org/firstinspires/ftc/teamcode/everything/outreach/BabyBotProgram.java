package org.firstinspires.ftc.teamcode.everything.outreach;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "baby bot teleop")
public class BabyBotProgram extends LinearOpMode {
    public void runOpMode() {
        DcMotor driveRight = (DcMotor) this.hardwareMap.get(DcMotor.class, "driveRight");
        driveRight.setDirection(DcMotorSimple.Direction.REVERSE);
        DcMotor driveLeft = (DcMotor) this.hardwareMap.get(DcMotor.class, "driveLeft");
        driveLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        DcMotor rotate = (DcMotor) this.hardwareMap.get(DcMotor.class, "rotate");
        rotate.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotate.setTargetPosition(0);
        rotate.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rotate.setPower(0.5d);
        Servo release = (Servo) this.hardwareMap.get(Servo.class, "release");
        release.setPosition(0.61d);
        waitForStart();
        while (opModeIsActive()) {
            double leftY = ((double) (-this.gamepad1.left_stick_y)) * 1.25d;
            double rightX = ((double) this.gamepad1.right_stick_x) * 0.75d;
            driveLeft.setPower(Math.max(-1.0d, Math.min(1.0d, leftY - rightX)) / 2.0d);
            driveRight.setPower(Math.max(-1.0d, Math.min(1.0d, leftY + rightX)) / 2.0d);
            if (this.gamepad1.rightBumperWasPressed()) {
                rotate.setTargetPosition(rotate.getTargetPosition() - 25);
            } else if (this.gamepad1.leftBumperWasPressed()) {
                rotate.setTargetPosition(rotate.getTargetPosition() + 25);
            }
            if (this.gamepad1.aWasPressed()) {
                release.setPosition(0.8d);
                sleep(300);
                release.setPosition(0.61d);
            }
        }
    }
}