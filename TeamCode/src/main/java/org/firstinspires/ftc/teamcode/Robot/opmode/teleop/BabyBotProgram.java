package org.firstinspires.ftc.teamcode.Robot.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "baby bot teleop")
public class BabyBotProgram extends LinearOpMode {
    public void runOpMode() {
        DcMotor driveRight = hardwareMap.get(DcMotor.class, "driveRight");
        driveRight.setDirection(DcMotorSimple.Direction.REVERSE);
        DcMotor driveLeft = hardwareMap.get(DcMotor.class, "driveLeft");
        driveLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        DcMotor rotate = hardwareMap.get(DcMotor.class, "rotate");
        rotate.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotate.setTargetPosition(0);
        rotate.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rotate.setPower(0.5);
        Servo release = hardwareMap.get(Servo.class, "release");
        release.setPosition(0.65);
        waitForStart();
        while (opModeIsActive()) {
            double leftY = (-gamepad1.left_stick_y) * 1.25;
            double rightX = (gamepad1.right_stick_x) * 0.75;
            driveLeft.setPower(Math.max(-1.0, Math.min(1.0, leftY - rightX)) / 2.0);
            driveRight.setPower(Math.max(-1.0, Math.min(1.0, leftY + rightX)) / 2.0);
            if (gamepad1.rightBumperWasPressed()) {
                rotate.setTargetPosition(rotate.getTargetPosition() - 25);
            } else if (gamepad1.leftBumperWasPressed()) {
                rotate.setTargetPosition(rotate.getTargetPosition() + 25);
            }
            if (gamepad1.aWasPressed()) {
                release.setPosition(0.8);
                sleep(1000);
                release.setPosition(0.61);
            }
        }
    }
}
