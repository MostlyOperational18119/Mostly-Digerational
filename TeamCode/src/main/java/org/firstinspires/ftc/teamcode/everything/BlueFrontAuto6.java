package org.firstinspires.ftc.teamcode.everything;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "BFA")
public class BlueFrontAuto6 extends Methods {
    Pose start = new Pose(32.614, 134.376, Math.toRadians(90));
    Pose launch = new Pose(60.000, 84.000, Math.toRadians(140));
    Pose park = new Pose(36, 134.376, Math.toRadians(90));
    Pose prep = new Pose(41, 84, Math.toRadians(0));
    Pose intake1 = new Pose(34, 84, Math.toRadians(0));
    Pose intake2 = new Pose(29, 84, Math.toRadians(0));
    Pose intake3 = new Pose(15, 84, Math.toRadians(0));
    Follower follower;
    PathChain startToLaunch, launchToPark, launchToPrep, prepToIntake1, intake1ToIntake2, intake2ToIntake3, intake3ToLaunch;

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
        initialize();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(start);

        startToLaunch = follower.pathBuilder()
                .addPath(new BezierLine(start, launch))
                .setLinearHeadingInterpolation(start.getHeading(), launch.getHeading())
                .build();

        launchToPark = follower.pathBuilder()
                .addPath(new BezierLine(launch, park))
                .setLinearHeadingInterpolation(launch.getHeading(), park.getHeading())
                .build();

        launchToPrep = follower.pathBuilder()
                .addPath(new BezierLine(launch, prep))
                .setLinearHeadingInterpolation(launch.getHeading(), prep.getHeading())
                .build();

        prepToIntake1 = follower.pathBuilder()
                .addPath(new BezierLine(prep, intake1))
                .setLinearHeadingInterpolation(prep.getHeading(), intake1.getHeading())
                .build();

        intake1ToIntake2 = follower.pathBuilder()
                .addPath(new BezierLine(intake1, intake2))
                .setLinearHeadingInterpolation(intake1.getHeading(), intake2.getHeading())
                .build();

        intake2ToIntake3 = follower.pathBuilder()
                .addPath(new BezierLine(intake2, intake3))
                .setLinearHeadingInterpolation(intake2.getHeading(), intake3.getHeading())
                .build();

        intake3ToLaunch = follower.pathBuilder()
                .addPath(new BezierLine(intake3, launch))
                .setLinearHeadingInterpolation(intake3.getHeading(), launch.getHeading())
                .build();

        waitForStart();

        launchDelayTimer = System.currentTimeMillis();
        intake.setPower(0.2);

        //set all of indexer array to one color
        //indexer.oneColor(Indexer.BallColor.GREEN);
        indexer.badColorWorkaround();
        outtakeFlywheel.setPower(0.5);

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
                                        toGreen = false;
                                        toPurple = true;
                                    } else {
                                        toGreen = true;
                                        toPurple = false;
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
                        follower.followPath(launchToPrep, 1, true);
                        intake.setPower(1);
                        intakeSequence.start();
                        state = 3;
                        break;
                    case 3:
                        follower.followPath(intake1ToIntake2);
                        intake.setPower(1);
                        intakeSequence.start();
                        state = 4;
                        break;
                    case 4:
                        follower.followPath(intake2ToIntake3);
                    case 5:
                        follower.followPath(intake3ToLaunch);
                    case 6:
                        follower.followPath(launchToPark, 1, true);
                        intake.setPower(0);
                        outtakeFlywheel.setPower(0);
                        outtake.setRotationPosition(0);
                        state = -67;
                        break;
                }
            }
        }
    }
}