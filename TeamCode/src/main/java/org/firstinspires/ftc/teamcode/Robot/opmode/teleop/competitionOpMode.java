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

        //placeholders
        boolean red = false;
        int launch = -1;
        long startTime = 0;
        long startTime0 = 0, startTime1, startTime2;
        final long WAIT = 300;
        final long LAUNCH_TIME = 150;



        Drivetrain.init(hardwareMap);
        Outtake.init(hardwareMap);
        Intake.init(hardwareMap);
        Indexer.init(hardwareMap);

        final Pose startPose = new Pose(0, 0, 0);
        Pose currentPose = startPose;
        Follower follower;
        follower =  Constants.createFollower(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {

            //drivetrain controller input variable declarations
            float y = -gamepad1.left_stick_y; // y stick is not reversed
            float x = -gamepad1.left_stick_x;
            float rx = gamepad1.right_stick_x;
            boolean X = gamepad1.xWasPressed();
            boolean A = gamepad1.aWasPressed();
            boolean intake = gamepad1.right_trigger > 0.5;
            boolean Y = gamepad1.yWasPressed();
            boolean lt = gamepad1.left_trigger > 0.5;

            //look at chamber or shoot
            if (X) {
                launch *= -1;
            }

            
            Drivetrain.drive(y, x, rx);
            //Outtake.outtakeUpdate(currentPose.getX(), currentPose.getY(), launch, red);

            if (launch > 0) {
                Indexer.update(true);
            } else {
                Indexer.update(false);
            }

            if ((A) && (launch < 0)) {
                long currentTime = System.currentTimeMillis();
                if (currentTime-startTime > WAIT) {
                    startTime = Indexer.startLaunch(4);
                }
            }

            if (lt) {
                Drivetrain.BreakPadDown();

            } else {
                Drivetrain.BreakPadUp();
            }



//            if (Y) {
//                long currentTime = System.currentTimeMillis();
//                if (currentTime - startTime0 > WAIT) {
//                    startTime0 = Outtake.launch0();
//                    if (currentTime - startTime0 > LAUNCH_TIME) {
//                        Outtake.down0();
//                    }
//                    if (currentTime - startTime0 > WAIT) {
//                        startTime1 = Outtake.launch1();
//                        if (currentTime - startTime1 > LAUNCH_TIME) {
//                            Outtake.down1();
//                        }
//                        if (currentTime - startTime1 > WAIT) {
//                            startTime2 = Outtake.launch2();
//                            if (currentTime - startTime2 > LAUNCH_TIME) {
//                                Outtake.down2();
//                            }
//                        }
//                    }
//
//                }
//            }

            //intake balls
            Intake.switchIntake(intake);

            int[] slots = Indexer.slotColors();

            telemetry.addData("launch", launch);
            telemetry.addData("slot0", slots[0]);
            telemetry.addData("slot1", slots[1]);
            telemetry.addData("slot2", slots[2]);
            telemetry.update();
        }
    }
}
