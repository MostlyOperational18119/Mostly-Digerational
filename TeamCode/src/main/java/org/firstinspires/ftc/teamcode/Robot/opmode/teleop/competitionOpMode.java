package org.firstinspires.ftc.teamcode.Robot.opmode.teleop;

import android.widget.AdapterView;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.Robot.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;

@TeleOp(name="drivetrain testing")
public class competitionOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();

        //placeholders
        boolean color = false;

        Drivetrain.init(hardwareMap);
        Outtake.init(hardwareMap);

        Pose currentPose;

        Follower follower;
        follower =  Constants.createFollower(hardwareMap);


        while (opModeIsActive()) {

            //drivetrain controller input variable declarations
            float y = gamepad1.left_stick_y; // y stick is not reversed
            float x = -gamepad1.left_stick_x;
            float rx = gamepad1.right_stick_x;
            boolean A = gamepad1.aWasPressed();

            int launch = -1;
            if (A) {
                launch *= -1;
            }

            
            Drivetrain.drive(y, x, rx);
//            Outtake.run();
//
//            if (launch > 0) {
//                if (color) {
//                    Outtake.autoAimBlue(currentPose.getX(), currentPose.getY(), true);
//                } else {
//                    Outtake.autoAimRed(currentPose.getX(), currentPose.getY(), true);
//                }
//            } else {
//                if (color) {
//                    Outtake.autoAimBlue(currentPose.getX(), currentPose.getY(), false);
//                } else {
//                    Outtake.autoAimRed(currentPose.getX(), currentPose.getY(), false);
//                }
//            }




        }
    }
}
