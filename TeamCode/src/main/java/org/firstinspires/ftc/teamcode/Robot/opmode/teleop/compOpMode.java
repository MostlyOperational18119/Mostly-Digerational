package org.firstinspires.ftc.teamcode.Robot.opmode.teleop;

import android.util.Log;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Lift;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.Robot.subsystems.limelight.Limelight;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@TeleOp(name = "TeleOp")
public class compOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        //placeholders
        int launch = -1;
        long startTime = 0;
        long startTime0 = 0, startTime1, startTime2;
        final long WAIT = 300;
        final long LAUNCH_TIME = 150;
        long delayTimer = 0;
        long launchDelayTimer = 0;
        boolean isLaunching = false;
        int launchCount = 0;
        int currentTransfer = 0;
        int count = 0;
        int adjust = 0;
        double noCorrectAdjust = 0.0;
        boolean isCorrecting = true;
        boolean limelightAvailable = true;
        boolean canRuinPattern = true;
        int numBalls = -1;

//        Outtake.isBlue = false;
//        Outtake.start = new Pose(16.641,16.1903, Math.toRadians(90));

        Drivetrain.init(hardwareMap);
        Outtake.init(hardwareMap);
        Intake.init(hardwareMap);
        Indexer.init(hardwareMap);
        Lift.init(hardwareMap);
        Limelight limelight;
        try {
            limelight = new Limelight();
            limelight.setChosenGoal(Outtake.StaticVars.isBlue ? 0 : 1);
        } catch (IOException e) {
            limelightAvailable = false;
            limelight = null;
        }

        Follower follower;
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Outtake.start);

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
            boolean lb = gamepad1.leftBumperWasPressed();
            boolean rb = gamepad1.rightBumperWasPressed();
            boolean leftD = gamepad1.dpadLeftWasPressed();
            boolean rightD = gamepad1.dpadRightWasPressed();
            boolean dpadDown = gamepad1.dpadDownWasPressed();
            boolean dpadUp = gamepad1.dpadUpWasPressed();
            boolean startButton = gamepad1.startWasPressed();
            boolean backButton = gamepad1.backWasPressed();

            follower.update();
            Outtake.robotY = follower.getPose().getY();
            Outtake.robotX = follower.getPose().getX();
            Outtake.robotOrientation = Math.toDegrees(follower.getHeading());

            //look at chamber or shoot
            if (X) {
                if (launch != -2) launch *= -1;
            }

            if (limelightAvailable) {
                if (!limelight.update()) {
                    Log.i("compOpMode", "Limelight encountered some issue, hopefully it's reconnected");
                }
            }

            if (limelightAvailable && Outtake.currentState == Outtake.States.AIM_CHAMBER) {
                Optional<Integer> ballCountUnsafe = limelight.getBallCount();
                if (ballCountUnsafe.isPresent() && ballCountUnsafe.get() != -1)
                    numBalls = ballCountUnsafe.get();
            }

            if (limelightAvailable && Outtake.currentState == Outtake.States.AIM_OBELISK) {
                Optional<Integer[]> patternUnsafe = limelight.getPattern();
                // Essentially just update the pattern with the result thingy(tm) if it's present
                patternUnsafe.ifPresent(integers -> {
                    Log.i("compOpMode", String.format("Pattern found: %s", Arrays.toString(integers)));
                    Indexer.updatePattern(Arrays.stream(integers).mapToInt(i -> i).toArray());
                });
            }

            if (leftD) {
                adjust -= 200;
            }
            if (rightD) {
                adjust += 200;
            }

            Drivetrain.drive(y, x, rx, rb);

            Outtake.outtakeUpdate(launch, true, adjust);
            Outtake.outtakeSpeed();

            Indexer.updateSlot0();
            Indexer.updateSlot1();
            Indexer.updateSlot2();


            if (intake) {
                Intake.intakeGo();
            } else if (spit) {
                Intake.intakeSpit();
            } else {
                Intake.intakeStop();
            }

            if (numBalls == -1 || !limelightAvailable) isLaunching = false;

            if (isLaunching && !limelightAvailable) {
                if (System.currentTimeMillis() - launchDelayTimer > 500) {
                    // Check and launch any remaining balls in the indexer
                    if (Indexer.slotColors()[0] != 0) {
                        launchDelayTimer = Indexer.launch0();
                    } else if (Indexer.slotColors()[2] != 0) {
                        launchDelayTimer = Indexer.launch2();
                    } else if (Indexer.slotColors()[1] != 0) {
                        launchDelayTimer = Indexer.launch1();
                    } else {
                        // All slots empty, move to next state
                        isLaunching = false;
                    }
                }
            } else if (isLaunching && numBalls != -1) { // Don't wanna be useless if there are -1 balls due to... reasons...
                if (System.currentTimeMillis() - launchDelayTimer > 1500) {
                    Log.i("compOpMode", String.format("Calling Limelight launch (we %s ruin the pattern)", (canRuinPattern ? "can" : "cannot")));
                    isLaunching = !Indexer.startLaunch(numBalls, canRuinPattern);

                    // Maybe this is a good idea?
                    if (!isLaunching) Indexer.chamberIncrease = 0;

                    launchDelayTimer = System.currentTimeMillis();
                }
            }

            if (A && !isLaunching) {
                // PREP FOR LAUNCH, VERY MUCH NEEDED
                Indexer.preLaunch();

                isLaunching = true;
                canRuinPattern = false;
            }

            if (B && launchCount == 0) {
                delayTimer = Indexer.launch0();
                launchCount = 1;
            } else if (launchCount > 0 && System.currentTimeMillis() - delayTimer > 700 && Outtake.outtakeMotorLeft.getVelocity() >= Outtake.speed - 20) {
                    switch (launchCount) {
                        case 1:
                            delayTimer = Indexer.launch2();
                            launchCount = 2;
                            break;
                        case 2:
                            delayTimer = Indexer.launch1();
                            launchCount = 0;
                            break;
                    }
                }

            switch (Outtake.currentDistance) {
                case "very close":
                    if (dpadUp) {
                        Outtake.SPEED_CONST_VERY_CLOSE += 3;
                    }
                    if (dpadDown) {
                        Outtake.SPEED_CONST_VERY_CLOSE -= 3;
                    }
                    telemetry.addLine("tuning close constant");
                    telemetry.addData("SPEED_CONST_VERY_CLOSE", Outtake.SPEED_CONST_VERY_CLOSE);
                    break;
                case "close":
                    if (dpadUp) {
                        Outtake.SPEED_CONST_CLOSE += 3;
                    }
                    if (dpadDown) {
                        Outtake.SPEED_CONST_FAR -= 3;
                    }
                    telemetry.addLine("tuning far constant");
                    telemetry.addData("SPEED_CONST_CLOSE", Outtake.SPEED_CONST_CLOSE);
                    break;
                case "far":
                    if (dpadUp) {
                        Outtake.SPEED_CONST_FAR += 3;
                    }
                    if (dpadDown) {
                        Outtake.SPEED_CONST_FAR -= 3;
                    }
                    telemetry.addLine("tuning close constant");
                    telemetry.addData("SPEED_CONST_FAR", Outtake.SPEED_CONST_FAR);
                    break;
            }

//            } else if (A && !limelightAvailable) {
//                isLaunching = true;
//            }

//            if (lb) {
//                Outtake.angleOffset += 1;
//            }
//            if (rb) {
//                Outtake.angleOffset -= 1;
//            }
            if (Y) {
                Outtake.isBlue = !Outtake.isBlue;
            }

            if (startButton) {
                launch = -2;
            } else if (backButton) {
                launch = -1;
            }

//            if (startButton) {
//                if (Lift.lift1.getPosition() == Lift.UP1) {
//                    Lift.lift();
//                } else {
//                    Lift.unlift();
//                }
//            }
            int[] slots = Indexer.slotColors();

            Log.i("compOpMode", String.format("Current slot colors: %s", Arrays.toString(slots)));

//            telemetry.addData("supposed dist", Outtake.distance);
            telemetry.addData("manual adjust", isCorrecting);
            telemetry.addData("launching", isLaunching);
            telemetry.addData("can we ruin the pattern", canRuinPattern);
            telemetry.addData("angle adjustment", adjust);
            telemetry.addData("limelight available", limelightAvailable);
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
            telemetry.addData("Position", Outtake.StaticVars.endPose);
            telemetry.addData("outtake position static variable", Outtake.StaticVars.outtakePos);
            telemetry.addData("outtake pos auto", Outtake.outtakePosAuto);
            telemetry.addData("chamber count", numBalls);
            telemetry.addData("outtake aim location (current state)", Outtake.currentState);
            telemetry.update();
//            telemetry.addData("launch", launch);
//            telemetry.addData("slot0", slots[0]);
//            telemetry.addData("slot1", slots[1]);
//            telemetry.addData("slot2", slots[2]);
//            telemetry.update();
        }
    }
}
