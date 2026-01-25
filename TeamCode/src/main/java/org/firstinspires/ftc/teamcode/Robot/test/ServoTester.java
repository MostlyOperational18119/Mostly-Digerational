package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;

@TeleOp(name = "ServoTester")
public class ServoTester extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        double Position = 0.5;
        Servo dropperServo = hardwareMap.servo.get("lift1");

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.aWasPressed()) {
                Position += 0.01;
            }
            if (gamepad1.bWasPressed()) {
                Position -= 0.01;
            }

            dropperServo.setPosition(Position);

            telemetry.addData("position", Position);
            telemetry.update();
        }
    }
}