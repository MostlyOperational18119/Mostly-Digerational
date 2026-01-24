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
        int count = 0;
        int adjust = 0;

        Outtake.isBlue = false;

        Drivetrain.init(hardwareMap);
        Outtake.init(hardwareMap);
        Intake.init(hardwareMap);
        Indexer.init(hardwareMap);

        Pose start = new Pose(144-16.641,16.1903, Math.toRadians(90));
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
            boolean spit = gamepad1.left_trigger > 0.5;
            boolean Y = gamepad1.yWasPressed();
            boolean lt = gamepad1.left_trigger > 0.5;
            boolean lb = gamepad1.leftBumperWasPressed();
            boolean rb = gamepad1.rightBumperWasPressed();
            boolean dpadUp = gamepad1.dpadUpWasPressed();
            boolean dpadDown = gamepad1.dpadDownWasPressed();
            boolean dpadRight = gamepad1.dpadRightWasPressed();
            boolean dpadLeft = gamepad1.dpadLeftWasPressed();
            boolean back = gamepad1.backWasPressed();
            boolean startButton = gamepad1.startWasPressed();





            follower.update();
            Outtake.robotY = follower.getPose().getY();
            Outtake.robotX = follower.getPose().getX();
            Outtake.robotOrientation = Math.toDegrees(follower.getHeading());

            //look at chamber or shoot
//            if (X) {
//                launch *= -1;
//            }

            if (back) {
                adjust -= 1;
            }
            if (startButton) {
                adjust += 1;
            }

            Drivetrain.drive(y, x, rx);
            Outtake.outtakeUpdate(-1, adjust);
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
            } else if (spit) {
                Intake.intakeSpit();
            } else {
                Intake.intakeStop();
            }

            if (isLaunching) {
                if (System.currentTimeMillis() - launchDelayTimer > 600) {
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
            if (dpadLeft) {
                count -=1;
            }
            if (dpadRight) {
                count +=1;
            }

            if (rb) {
                Outtake.SPEED_CONST_VERY_CLOSE += 5;
            }
            if (lb) {
                Outtake.SPEED_CONST_VERY_CLOSE -= 5;
            }


            count = Math.abs(count%3);

            switch (Outtake.currentDistance) {
                case "very close":
                    switch (count) {
                        case 0:
                            if (dpadUp) {
                                Outtake.VERY_CLOSE_P += 0.5;
                            }
                            if (dpadDown) {
                                Outtake.VERY_CLOSE_P -= 0.5;
                            }
                            telemetry.addLine("tuning p");
                            break;
                        case 1:
                            if (dpadUp) {
                                Outtake.VERY_CLOSE_I += 0.5;
                            }
                            if (dpadDown) {
                                Outtake.VERY_CLOSE_I -= 0.5;
                            }
                            telemetry.addLine("tuning i");
                            break;
                        case 2:
                            if (dpadUp) {
                                Outtake.VERY_CLOSE_D += 0.5;
                            }
                            if (dpadDown) {
                                Outtake.VERY_CLOSE_D -= 0.5;
                            }
                            telemetry.addLine("tuning d");
                            break;
                    }
                    Outtake.updatePID(Outtake.VERY_CLOSE_P, Outtake.VERY_CLOSE_I, Outtake.VERY_CLOSE_D, 0);
                    telemetry.addData("p", Outtake.VERY_CLOSE_P);
                    telemetry.addData("i", Outtake.VERY_CLOSE_I);
                    telemetry.addData("d", Outtake.VERY_CLOSE_D);
                    break;
                case "close":
                    switch (count) {
                        case 0:
                            if (dpadUp) {
                                Outtake.CLOSE_P += 0.5;
                            }
                            if (dpadDown) {
                                Outtake.CLOSE_P -= 0.5;
                            }
                            telemetry.addLine("tuning p");
                            break;
                        case 1:
                            if (dpadUp) {
                                Outtake.CLOSE_I += 0.5;
                            }
                            if (dpadDown) {
                                Outtake.CLOSE_I -= 0.5;
                            }
                            telemetry.addLine("tuning i");
                            break;
                        case 2:
                            if (dpadUp) {
                                Outtake.CLOSE_D += 0.5;
                            }
                            if (dpadDown) {
                                Outtake.CLOSE_D -= 0.5;
                            }
                            telemetry.addLine("tuning d");
                            break;
                    }
                    Outtake.updatePID(Outtake.CLOSE_P, Outtake.CLOSE_I, Outtake.CLOSE_D, 0);
                    telemetry.addData("p", Outtake.CLOSE_P);
                    telemetry.addData("i", Outtake.CLOSE_I);
                    telemetry.addData("d", Outtake.CLOSE_D);
                    break;
                case "far":
                    switch (count) {
                        case 0:
                            if (dpadUp) {
                                Outtake.FAR_P += 0.5;
                            }
                            if (dpadDown) {
                                Outtake.FAR_P -= 0.5;
                            }
                            telemetry.addLine("tuning p");
                            break;
                        case 1:
                            if (dpadUp) {
                                Outtake.FAR_I += 0.5;
                            }
                            if (dpadDown) {
                                Outtake.FAR_I -= 0.5;
                            }
                            telemetry.addLine("tuning i");
                            break;
                        case 2:
                            if (dpadUp) {
                                Outtake.FAR_D += 0.5;
                            }
                            if (dpadDown) {
                                Outtake.FAR_D -= 0.5;
                            }
                            telemetry.addLine("tuning d");
                            break;
                    }
                    Outtake.updatePID(Outtake.FAR_P, Outtake.FAR_I, Outtake.FAR_D, 0);
                    telemetry.addData("p", Outtake.FAR_P);
                    telemetry.addData("i", Outtake.FAR_I);
                    telemetry.addData("d", Outtake.FAR_D);
                    break;
            }
//            if (dpadLeft) {
//                //Outtake.SPEED_CONST_FAR += 2;
//                if (Outtake.d <= 20) {
//                    Outtake.d += 0.5;
//                } else
//                    Outtake.d = 0;
//            }
//            if (dpadRight) {
//                //Outtake.SPEED_CONST_FAR -= 2;
//                if (Outtake.f <= 10) {
//                    Outtake.f += 0.5;
//                } else
//                    Outtake.f = 0;
//            }
            if (X) {
               Outtake.VERY_CLOSE_HOOD += 0.01;
            }
            if (B) {
                Outtake.VERY_CLOSE_HOOD -= 0.01;
            }
//
//            if (Y) {
//                currentTransfer += 1;
//                if (currentTransfer >= 3) {
//                    currentTransfer = 1;
//                }
//            }
//
//            if (lb) {
//                switch (currentTransfer) {
//                    case 0:
//                        Indexer.UP_POS_0 += 0.01;
//                        break;
//                    case 1:
//                        Indexer.UP_POS_1 += 0.01;
//                        break;
//                    case 2:
//                        Indexer.UP_POS_2 += 0.01;
//                        break;
//                }
//            }
//
//            if (rb) {
//                switch (currentTransfer) {
//                    case 0:
//                        Indexer.UP_POS_0 -= 0.01;
//                        break;
//                    case 1:
//                        Indexer.UP_POS_1 -= 0.01;
//                        break;
//                    case 2:
//                        Indexer.UP_POS_2 -= 0.01;
//                        break;
//                }
//            }

            if (Y) {
                if (Outtake.goalX == 140) {
                    Outtake.goalX = 4;
                } else {
                    Outtake.goalX = 140;
                }
            }

//            int[] slots = Indexer.slotColors();
//            telemetry.addData("supposed dist", Outtake.distance);
            telemetry.addData("speed const very close", Outtake.SPEED_CONST_VERY_CLOSE);
            telemetry.addData("angle adjustment", adjust);
//            telemetry.addData("speed const close")
//            telemetry.addData("hood angle", Outtake.CLOSE_HOOD);
//            telemetry.addData("odometry X", Outtake.robotX);
//            telemetry.addData("odometry Y", Outtake.robotY);
//            telemetry.addData("up position 0", Indexer.UP_POS_0);
//            telemetry.addData("up position 1", Indexer.UP_POS_1);
//            telemetry.addData("up position 2", Indexer.UP_POS_2);
//            telemetry.addData("Left motor velo", Outtake.testTelemetryMotor1());
//            telemetry.addData("Right motor velo", Outtake.testTelemetryMotor2());
            telemetry.addData("current distance", Outtake.currentDistance);
            telemetry.addData("left flywheel velocity", Outtake.outtakeMotorLeft.getVelocity());
            telemetry.addData("right flywheel velocity", Outtake.outtakeMotorRight.getVelocity());
            telemetry.addData("speed", Outtake.speed);
            telemetry.update();
//            telemetry.addData("launch", launch);
//            telemetry.addData("slot0", slots[0]);
//            telemetry.addData("slot1", slots[1]);
//            telemetry.addData("slot2", slots[2]);
//            telemetry.update();
        }
    }
}
