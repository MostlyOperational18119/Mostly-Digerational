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
        int launch = -1;
        long startTime = 0;
        long startTime0 = 0, startTime1, startTime2;
        final long WAIT = 300;
        final long LAUNCH_TIME = 150;
        long launchDelayTimer = 0;
        boolean isLaunching = false;
        int launchCount = 0;
        int currentTransfer = 0;

        Outtake.isBlue = false;

        Drivetrain.init(hardwareMap);
        Outtake.init(hardwareMap);
        Intake.init(hardwareMap);
        Indexer.init(hardwareMap);

        Pose start = new Pose(16.641,16.1903, Math.toRadians(90));
        Follower follower;
        follower =  Constants.createFollower(hardwareMap);
        follower.setStartingPose(start);

        waitForStart();

        launchDelayTimer = System.currentTimeMillis();

        while (opModeIsActive()) {

            //drivetrain controller input variable declarations
            float y = -gamepad1.left_stick_y;
            float x = gamepad1.left_stick_x;
            float rx = gamepad1.right_stick_x;
            boolean X = gamepad1.xWasPressed();
            boolean B = gamepad1.bWasPressed();
            boolean A = gamepad1.aWasPressed();
            boolean intake = gamepad1.right_trigger > 0.5;
            boolean Y = gamepad1.yWasPressed();
            boolean lt = gamepad1.left_trigger > 0.5;
            boolean lb = gamepad1.leftBumperWasPressed();
            boolean rb = gamepad1.rightBumperWasPressed();
            boolean dpadUp = gamepad1.dpadUpWasPressed();
            boolean dpadDown = gamepad1.dpadDownWasPressed();
            boolean dpadRight = gamepad1.dpadRightWasPressed();
            boolean dpadLeft = gamepad1.dpadLeftWasPressed();

            follower.update();
            Outtake.robotY = follower.getPose().getY();
            Outtake.robotX = follower.getPose().getX();
            Outtake.robotOrientation = Math.toDegrees(follower.getHeading());

            //look at chamber or shoot
//            if (X) {
//                launch *= -1;
//            }

            
            Drivetrain.drive(y, x, rx);
            Outtake.outtakeUpdate(-1);
            Outtake.outtakeSpeed();

            Indexer.updateSlot0();
            Indexer.updateSlot1();
            Indexer.updateSlot2();

//            if ((A) && (launch < 0)) {
//                long currentTime = System.currentTimeMillis();
//                if (currentTime-startTime > WAIT) {
//                    startTime = Indexer.startLaunch(4);
//                }
//            }

//            if (lt) {
//                Drivetrain.BreakPadDown();
//
//            } else {
//                Drivetrain.BreakPadUp();
//            }

            if (intake) {
                Intake.intakeGo();
            } else {
                Intake.intakeStop();
            }

            if (isLaunching) {
                if (System.currentTimeMillis() - launchDelayTimer > 1000) {
                    switch (launchCount) {
                        case 0:
                            launchDelayTimer = Indexer.launch0();
                            launchCount = 1;
                            break;
                        case 1:
                            launchDelayTimer = Indexer.launch1();
                            launchCount = 2;
                            break;
                        case 2:
                            launchDelayTimer = Indexer.launch2();
                            isLaunching = false;
                            launchCount = 0;
                            break;
                    }
                }
            }

            if (A) {
                isLaunching = true;
            }

            //if (lb) {
            //    Outtake.angleOffset += 1;
            //}
            //if (rb) {
            //    Outtake.angleOffset -= 1;
            //}

            if (dpadUp) {
                Outtake.SPEED_CONST_CLOSE += 2;
            }
            if (dpadDown) {
                Outtake.SPEED_CONST_CLOSE -= 2;
            }
            if (dpadLeft) {
                Outtake.SPEED_CONST_FAR += 2;
            }
            if (dpadRight) {
                Outtake.SPEED_CONST_FAR -= 2;
            }
            if (X) {
               Outtake.CLOSE_HOOD += 0.01;
               Outtake.FAR_HOOD += 0.01;
            }
            if (B) {
                Outtake.CLOSE_HOOD -= 0.01;
                Outtake.FAR_HOOD -= 0.01;
            }

            if (Y) {
                currentTransfer += 1;
                if (currentTransfer >= 3) {
                    currentTransfer = 1;
                }
            }

            if (lb) {
                switch (currentTransfer) {
                    case 0:
                        Indexer.UP_POS_0 += 0.01;
                        break;
                    case 1:
                        Indexer.UP_POS_1 += 0.01;
                        break;
                    case 2:
                        Indexer.UP_POS_2 += 0.01;
                        break;
                }
            }

            if (rb) {
                switch (currentTransfer) {
                    case 0:
                        Indexer.UP_POS_0 -= 0.01;
                        break;
                    case 1:
                        Indexer.UP_POS_1 -= 0.01;
                        break;
                    case 2:
                        Indexer.UP_POS_2 -= 0.01;
                        break;
                }
            }

            if (gamepad1.backWasPressed()) {
                if (Outtake.goalX == 140) {
                    Outtake.goalX = 4;
                } else {
                    Outtake.goalX = 140;
                }
            }

//            int[] slots = Indexer.slotColors();
            telemetry.addData("speed const close", Outtake.SPEED_CONST_CLOSE);
            telemetry.addData("speed const far", Outtake.SPEED_CONST_FAR);
            telemetry.addData("hood angle", Outtake.CLOSE_HOOD);
            telemetry.addData("odometry X", Outtake.robotX);
            telemetry.addData("odometry Y", Outtake.robotY);
            telemetry.addData("up position 0", Indexer.UP_POS_0);
            telemetry.addData("up position 1", Indexer.UP_POS_1);
            telemetry.addData("up position 2", Indexer.UP_POS_2);
            telemetry.update();
//            telemetry.addData("launch", launch);
//            telemetry.addData("slot0", slots[0]);
//            telemetry.addData("slot1", slots[1]);
//            telemetry.addData("slot2", slots[2]);
//            telemetry.update();
        }
    }
}
