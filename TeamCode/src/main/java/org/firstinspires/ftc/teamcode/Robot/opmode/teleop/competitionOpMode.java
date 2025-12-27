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
import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;

@TeleOp(name="drivetrain testing")
public class competitionOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();

        //placeholders
        boolean red = false;
        int launch = 1;

        Drivetrain.init(hardwareMap);
        Outtake.init(hardwareMap);
        Intake.init(hardwareMap);
        Indexer.init(hardwareMap);

        Pose currentPose;

        Follower follower;
        follower =  Constants.createFollower(hardwareMap);


        while (opModeIsActive()) {

            //drivetrain controller input variable declarations
            float y = gamepad1.left_stick_y; // y stick is not reversed
            float x = -gamepad1.left_stick_x;
            float rx = gamepad1.right_stick_x;
            boolean X = gamepad1.xWasPressed();
            boolean A = gamepad1.aWasPressed();
            float lt = gamepad1.left_trigger;
            float rt = gamepad1.right_trigger;

            if (X) {
                launch *= -1;
            }

            
            Drivetrain.drive(y, x, rx);
//          Outtake.outtakeUpdate(currentPose.getX(), currentPose.getY(), launch);

            if (A) {
                Indexer.update(Indexer.slotColors());
            }


            if (lt > .5) {
                Intake.intakeGo();
            } else {
                Intake.intakeIdle();
            }




        }
    }
}
