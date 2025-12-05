package org.firstinspires.ftc.teamcode.everything;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "BFA9")
public class BlueFrontAuto9 extends Methods {
    Pose start = new Pose(32.614, 134.376, Math.toRadians(90));
    Pose launch = new Pose(60, 84, Math.toRadians(140));
    Pose prep1 = new Pose(45, 84, Math.toRadians(0));
    Pose intake1 = new Pose(16, 84, Math.toRadians(0));
    Pose prep2 = new Pose(45, 60, Math.toRadians(0));
    Pose intake2 = new Pose(16, 60, Math.toRadians(0));
    Pose park = new Pose(16, 100, Math.toRadians(180));
    Follower follower;
    PathChain startToLaunch, launchToPrep1, prep1ToIntake1, intake1ToLaunch, launchToPrep2, prep2ToIntake2, intake2ToLaunch, launchToPark;

    int state = -1;
    int launchCount = 0;
    long launchDelayTimer = 0;
    int LAUNCH_DELAY_MS = 2000; // Adjust this value for more/less delay between launches

    Indexer indexer = new Indexer(this);
    Intake intakeSequence = new Intake(this, indexer);
    Outtake outtake = new Outtake(this);
    LaunchSequence launchState = new LaunchSequence(this, indexer);

    @Override
    public void runOpMode() {
        isBlue = true;
        initialize();
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(start);
        startToLaunch = follower
            .pathBuilder()
            .addPath(new BezierLine(start, launch))
            .setLinearHeadingInterpolation(start.getHeading(), launch.getHeading())
            .build();

        launchToPrep1 = follower
                .pathBuilder()
                .addPath(new BezierLine(launch, prep1))
                .setLinearHeadingInterpolation(launch.getHeading(), prep1.getHeading())
                .build();

        prep1ToIntake1 = follower
                .pathBuilder()
                .addPath(new BezierLine(prep1, intake1))
                .setLinearHeadingInterpolation(prep1.getHeading(), intake1.getHeading())
                .build();

        intake1ToLaunch = follower
                .pathBuilder()
                .addPath(new BezierLine(intake1, launch))
                .setLinearHeadingInterpolation(intake1.getHeading(), launch.getHeading())
                .build();

        launchToPrep2 = follower
                .pathBuilder()
                .addPath(new BezierCurve(launch, /*new Pose(63.381, 58.343),*/ prep2))
                .setLinearHeadingInterpolation(launch.getHeading(), prep2.getHeading())
                .build();

        prep2ToIntake2 = follower
                .pathBuilder()
                .addPath(new BezierLine(prep2, intake2))
                .setLinearHeadingInterpolation(prep2.getHeading(), intake2.getHeading())
                .build();

        intake2ToLaunch = follower
                .pathBuilder()
                .addPath(new BezierCurve(intake2, /*new Pose(33.149, 61.260),*/ launch))
                .setLinearHeadingInterpolation(intake2.getHeading(), launch.getHeading())
                .build();

        launchToPark = follower
                .pathBuilder()
                .addPath(new BezierLine(launch, park))
                .setLinearHeadingInterpolation(launch.getHeading(), park.getHeading())
                .build();

        waitForStart();

        launchDelayTimer = System.currentTimeMillis();
        intake.setPower(0.2);

        //set all of indexer array to one color
        //indexer.oneColor(BallColor.PURPLE);
        indexer.badColorWorkaround();
        //indexer.redoColors();
        outtakeFlywheel.setVelocity(1100);

        while (opModeIsActive()) {
            follower.update();
            launchState.update();
            indexer.update();
            outtake.update();

            if (!follower.isBusy()) {
                switch (state) {
                    case -1:
                        if (System.currentTimeMillis() - launchDelayTimer > 3000) {
                            state = 0;
                        }
                        break;
                    case 0:
                        follower.followPath(startToLaunch, 1, true);
                        state = 1;
                        break;
                    case 1:
                        if (launchIdle && launchState.currentState == LaunchSequence.State.IDLE) {
                            // Check if enough time has passed since the last launch
                            if (System.currentTimeMillis() - launchDelayTimer > LAUNCH_DELAY_MS) {
                                if (launchCount < 3) {
                                    if (launchCount == 1) {
                                        toGreen = true;
                                        toPurple = false;
                                    } else {
                                        toGreen = false;
                                        toPurple = true;
                                    }
                                    launchState.startLaunch();
                                    launchCount++;
                                    launchDelayTimer = System.currentTimeMillis(); // Reset timer after starting launch
                                } else {
                                    state = 2;
                                    launchCount = 0; // Reset for next time
                                }
                            }
                        }
                        break;
                    case 2:
                        follower.followPath(launchToPrep1, 1, true);
                        intake.setPower(1);
                        intakeSequence.start();
                        state = 3;
                        break;
                    case 3:
                        follower.followPath(prep1ToIntake1, 0.3, true);
                        intakeSequence.start();
                        state = 4;
                        break;
                    case 4:
                        follower.followPath(intake1ToLaunch, 1, true);
                        intake.setPower(0.2);
                        intakeSequence.start();
                        state = 5;
                        break;
                    case 5:
                        if (launchIdle && launchState.currentState == LaunchSequence.State.IDLE) {
                            // Check if enough time has passed since the last launch
                            if (System.currentTimeMillis() - launchDelayTimer > LAUNCH_DELAY_MS) {
                                if (launchCount < 3) {
                                    if (launchCount == 1) {
                                        toGreen = true;
                                        toPurple = false;
                                    } else {
                                        toGreen = false;
                                        toPurple = true;
                                    }
                                    launchState.startLaunch();
                                    launchCount++;
                                    launchDelayTimer = System.currentTimeMillis(); // Reset timer after starting launch
                                } else {
                                    state = 6;
                                    launchCount = 0; // Reset for next time
                                }
                            }
                        }
                    case 6:
                        follower.followPath(launchToPrep2, 1, true);
                        intake.setPower(1);
                        state = 7;
                    case 7:
                        follower.followPath(prep2ToIntake2, 0.3, true);
                        state = 8;
                    case 8:
                        follower.followPath(intake2ToLaunch, 1, true);
                        intake.setPower(0.2);
                        state = 9;
                        break;
                    case 9:
                        if (launchIdle && launchState.currentState == LaunchSequence.State.IDLE) {
                            // Check if enough time has passed since the last launch
                            if (System.currentTimeMillis() - launchDelayTimer > LAUNCH_DELAY_MS) {
                                if (launchCount < 3) {
                                    if (launchCount == 1) {
                                        toGreen = true;
                                        toPurple = false;
                                    } else {
                                        toGreen = false;
                                        toPurple = true;
                                    }
                                    launchState.startLaunch();
                                    launchCount++;
                                    launchDelayTimer = System.currentTimeMillis(); // Reset timer after starting launch
                                } else {
                                    state = 10;
                                    launchCount = 0; // Reset for next time
                                }
                            }
                        }
                    case 10:
                        follower.followPath(launchToPark, 1, true);
                        outtakeFlywheel.setVelocity(0);
                        outtake.setRotationPosition(0);
                        state = -67;
                        break;
                }
            }
        }
    }
}