package org.firstinspires.ftc.teamcode.Robot.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;

public class competitionOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();

        Drivetrain.init(hardwareMap);

        while (opModeIsActive()) {


            float y = -gamepad1.left_stick_y; // Remember, Y stick is reversed!
            float x = gamepad1.left_stick_x;
            float rx = gamepad1.right_stick_x;

            float denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            float frontLeftPower = (y + x + rx) / denominator;
            float backLeftPower = (y - x + rx) / denominator;
            float frontRightPower = (y - x - rx) / denominator;
            float backRightPower = (y + x - rx) / denominator;

            Drivetrain.drive(frontLeftPower, frontRightPower, backLeftPower, backRightPower);


        }

    }
}
