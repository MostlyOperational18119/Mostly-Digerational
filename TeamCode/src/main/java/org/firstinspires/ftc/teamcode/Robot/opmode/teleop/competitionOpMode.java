package org.firstinspires.ftc.teamcode.Robot.opmode.teleop;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

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

@TeleOp(name="TeleOp")
public class competitionOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();

        //placeholders
        boolean red = false;
        int launch = -1;
        long startTime = 0;
        final long WAIT = 300;



        Drivetrain.init(hardwareMap);
        Outtake.init(hardwareMap);
        Intake.init(hardwareMap);
        Indexer.init(hardwareMap);

        final Pose startPose = new Pose(0, 0, 0);
        Pose currentPose = startPose;
        Follower follower;
        follower =  Constants.createFollower(hardwareMap);


        while (opModeIsActive()) {

            //drivetrain controller input variable declarations
            float y = -gamepad1.left_stick_y; // y stick is not reversed
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
            Outtake.outtakeUpdate(currentPose.getX(), currentPose.getY(), launch, red);

            if (launch > 0) {
                Indexer.update(true);
            } else {
                Indexer.update(false);
            }

            if ((A) && (launch > 0)) {
                long currentTime = System.currentTimeMillis();
                if (currentTime-startTime > WAIT) {
                    startTime = Indexer.startLaunch(4);
                }
            }


            if (lt > .5) {
                Intake.intakeGo();
            } else {
                Intake.intakeStop();
            }

            int[] slots = Indexer.slotColors();

            telemetry.addData("launch", launch);
            telemetry.addData("slot0", slots[0]);
            telemetry.addData("slot1", slots[1]);
            telemetry.addData("slot2", slots[2]);
            telemetry.update();
        }
    }
}
