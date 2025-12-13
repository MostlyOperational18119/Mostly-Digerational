package org.firstinspires.ftc.teamcode.everything.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.everything.teleop.Indexer;
import org.firstinspires.ftc.teamcode.everything.teleop.LaunchSequence;
import org.firstinspires.ftc.teamcode.everything.Methods;
import org.firstinspires.ftc.teamcode.everything.teleop.Outtake;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "BBA3")
public class BlueBackAuto extends Methods {
    Pose start = new Pose(59, 9, Math.toRadians(90));
    Pose park = new Pose(37, 9, Math.toRadians(90));
    Follower follower;
    PathChain startToPark;
    int state = 0;
    int launchCount = 0;
    long launchDelayTimer = 0;
    int LAUNCH_DELAY_MS = 3300;

    //0.16, 0.57 for outtake
    Indexer indexer = new Indexer(this);
    Outtake outtake = new Outtake(this);
    LaunchSequence launchState = new LaunchSequence(this, indexer);
    @Override
    public void runOpMode() {
        StaticMatchData.isRed = false;
        initialize();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(start);
        outtake.setRotationPosition(0.26);
        daHood.setPosition(0.15);

        startToPark = follower.pathBuilder()
                .addPath(new BezierLine(start, park))
                .setLinearHeadingInterpolation(start.getHeading(), park.getHeading())
                .build();

        waitForStart();

        launchDelayTimer = System.currentTimeMillis();
        intake.setPower(0.2);

        //set all of indexer array to one color
        //indexer.oneColor(BallColor.PURPLE);
        indexer.badColorWorkaround();
        //look at balls inside
        //indexer.redoColors();
        outVelo = 1450;
        outtakeFlywheel.setVelocity(outVelo);

        // Absolutely need these variables for teleop, no matter what happens

        StaticMatchData.isRed = false;
        StaticMatchData.endPosition = follower.getPose();

        launchDelayTimer = System.currentTimeMillis();

        while (opModeIsActive()) {
            telemetry.addData("current position", indexer.rotation);
            telemetry.addData("slot 0", indexer.slots[0]);
            telemetry.addData("slot 1", indexer.slots[1]);
            telemetry.addData("slot 2", indexer.slots[2]);
            telemetry.addData("velocity", outtakeFlywheel.getVelocity());
            telemetry.addData("launch state", launchState.currentState);
            telemetry.addData("velocity coefficients", outtakeFlywheel.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
            telemetry.addData("isRed", StaticMatchData.isRed);
            telemetry.update();
            follower.update();
            launchState.update();
            indexer.update();
            robotX = follower.getPose().getX();
            robotY = follower.getPose().getY();
            //outtake.setTarget(nicksLittleHelper());
            outtake.update();

            if (!follower.isBusy()) {
                switch (state) {
                    case 0:
                        if (launchState.currentState == LaunchSequence.State.IDLE) {
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
                                    state = 1;
                                    launchCount = 0; // Reset for next time
                                }
                            }
                        }
                        break;
                    case 1:
                        follower.followPath(startToPark, 1, true);
                        intake.setPower(0);
                        outtake.setTarget(0);
                        StaticMatchData.endPosition = follower.getPose();
                        state = -67;
                        break;
                }
            }
        }
    }
}