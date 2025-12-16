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


            double y = -gamepad1.left_stick_y; // Remember, Y stick is reversed!
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            Drivetrain.drive();


        }

    }
}
