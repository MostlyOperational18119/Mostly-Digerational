package org.firstinspires.ftc.teamcode.everything;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "RFA")
public class RedFrontAuto extends Methods {
    Pose startRed = new Pose(111.386, 134.376, Math.toRadians(90));
    Pose launch = new Pose(88.000, 94.000, Math.toRadians(50));
    Pose park = new Pose(100, 134.376, Math.toRadians(90));
    Follower follower;
    PathChain startToLaunch, launchToPark;

    int state = -1;
    int launchCount = 0;
    long launchDelayTimer = 0;
    int LAUNCH_DELAY_MS = 2000; // Adjust this value for more/less delay between launches

    Indexer indexer = new Indexer(this);
    Outtake outtake = new Outtake(this);
    LaunchSequence launchState = new LaunchSequence(this, indexer);

    @Override
    public void runOpMode() {
        isRed = true;
        initialize();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startRed);

        startToLaunch = follower.pathBuilder()
                .addPath(new BezierLine(startRed, launch))
                .setLinearHeadingInterpolation(startRed.getHeading(), launch.getHeading())
                .build();

        launchToPark = follower.pathBuilder()
                .addPath(new BezierLine(launch, park))
                .setLinearHeadingInterpolation(launch.getHeading(), park.getHeading())
                .build();

        waitForStart();

        launchDelayTimer = System.currentTimeMillis();
        intake.setPower(0.2);

        //set all of indexer array to one color
        //indexer.oneColor(BallColor.PURPLE);
        //indexer.badColorWorkaround();
        indexer.redoColors();
        outtakeFlywheel.setPower(0.50);

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
