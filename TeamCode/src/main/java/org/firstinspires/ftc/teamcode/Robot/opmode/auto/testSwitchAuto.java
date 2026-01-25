package org.firstinspires.ftc.teamcode.Robot.opmode.auto;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;

@Autonomous(name = "testSwitchAuto")
public class testSwitchAuto extends LinearOpMode {
    @Override
    public void runOpMode() {
        waitForStart();
        while (opModeIsActive()) {
            Drivetrain.StaticVars.isBlue = false;
            Drivetrain.StaticVars.endPose = new Pose(10, 50, 0);
        }
    }
}
