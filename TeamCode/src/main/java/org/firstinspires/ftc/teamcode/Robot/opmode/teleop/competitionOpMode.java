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

            //drivetrain controller input variable declarations
            float y = -gamepad1.left_stick_y; // y stick is reversed
            float x = gamepad1.left_stick_x;
            float rx = gamepad1.right_stick_x;
            
            Drivetrain.drive(y, x, rx);


        }

    }
}
