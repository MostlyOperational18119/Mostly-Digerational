package org.firstinspires.ftc.teamcode.everything;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.everything.limelight.AprilTagResult;
import org.firstinspires.ftc.teamcode.everything.limelight.BetterLimelight;
import org.firstinspires.ftc.teamcode.everything.limelight.ToRobotMsg;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.ArrayList;
import java.util.Optional;

@Autonomous(name = "BFA9")
public class BlueFrontAuto9 extends Methods {
    Pose start = new Pose(32.614, 134.376, Math.toRadians(90));
    Pose launch = new Pose(60, 84, Math.toRadians(140));
    Pose prep1 = new Pose(47, 84, Math.toRadians(0));
    Pose intake1 = new Pose(17, 84, Math.toRadians(0));
    Pose prep2 = new Pose(47, 60, Math.toRadians(0));
    Pose intake2 = new Pose(17, 60, Math.toRadians(0));
    Pose park = new Pose(17, 100, Math.toRadians(180));
    Follower follower;
    PathChain startToLaunch, launchToPrep1, prep1ToIntake1, intake1ToLaunch, launchToPrep2, prep2ToIntake2, intake2ToLaunch, launchToPark;
    int outtakeVelocity = 1100;
    double intakeIdlePower = 0.2;
    double intakeActivePower = 1.0;
    int state = -1;
    int launchCount = 0;
    long delayTimer = 0;
    int launchDelay = 2000; // Adjust this value for more/less delay between launches
    int initialDelay = 3000;
    boolean intakeHasStarted = false;
    boolean canLimelight = true;
    int tagID = -1;

    Indexer indexer = new Indexer(this);
    Intake intakeSequence = new Intake(this, indexer);
    Outtake outtake = new Outtake(this);
    LaunchSequence launchState = new LaunchSequence(this, indexer);
    BetterLimelight limelight;
    ArrayList<AprilTagResult> tagResults;


    public boolean getTags() {
        if (canLimelight) {
            Optional<Object> res = limelight.getResult(ToRobotMsg.ResultType.AprilTag);

            if (res.isPresent()) {
                tagResults = (ArrayList<AprilTagResult>) res.get();
                return true;
            }
        }

        return false;
    }

    @Override
    public void runOpMode() {
        try {
            limelight = new BetterLimelight();
        } catch (Exception e) {
            canLimelight = false;
        }

        isRed = false;
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
                //.addPath(new BezierCurve(launch, /*new Pose(65, 62),*/ prep2))
                .addPath(new BezierLine(launch, prep2))
                .setLinearHeadingInterpolation(launch.getHeading(), prep2.getHeading())
                .build();

        prep2ToIntake2 = follower
                .pathBuilder()
                .addPath(new BezierLine(prep2, intake2))
                .setLinearHeadingInterpolation(prep2.getHeading(), intake2.getHeading())
                .build();

        intake2ToLaunch = follower
                .pathBuilder()
                //.addPath(new BezierCurve(intake2, /*new Pose(33, 61),*/ launch))
                .addPath(new BezierLine(intake2, launch))
                .setLinearHeadingInterpolation(intake2.getHeading(), launch.getHeading())
                .build();

        launchToPark = follower
                .pathBuilder()
                .addPath(new BezierLine(launch, park))
                .setLinearHeadingInterpolation(launch.getHeading(), park.getHeading())
                .build();

        updateID();

        waitForStart();

        delayTimer = System.currentTimeMillis();
        intake.setPower(intakeIdlePower);

        //set all of indexer array to one color
        //indexer.oneColor(BallColor.PURPLE);
        //indexer.badColorWorkaround();
        //indexer.redoColors();
        outtakeFlywheel.setVelocity(outtakeVelocity);

        while (opModeIsActive()) {
            telemetry.addData("current state", intakeSequence.currentStateIntake);
            telemetry.addData("slot 0", indexer.slots[0]);
            telemetry.addData("slot 1", indexer.slots[1]);
            telemetry.addData("slot 2", indexer.slots[2]);
            intakeSequence.updateAuto();
            follower.update();
            launchState.update();
            indexer.update();
            outtake.update();

            if (!follower.isBusy()) {
                switch (state) {
                    case -1:
                        if (System.currentTimeMillis() - delayTimer > initialDelay) {
                            state = 0;
                        }
                        break;
                    case 0:
                        follower.followPath(startToLaunch, 1, true);
                        state = 2;
                        break;
                    case 1:
                        susLaunch();
                        break;
                    case 2:
                        follower.followPath(launchToPrep1, 1, true);
                        intake.setPower(intakeActivePower);
                        indexer.rotateToColor(Indexer.BallColor.EMPTY);
                        if (!intakeHasStarted) {
                            intakeSequence.start();
                            intakeHasStarted = true;
                        }
                        state = 3;
                        break;
                    case 3:
                        follower.followPath(prep1ToIntake1, 0.1, false);
                        state = 4;
                        break;
                    case 4:
                        follower.followPath(intake1ToLaunch, 1, true);
                        intake.setPower(intakeIdlePower);
                        intakeHasStarted = false;
                        state = 6;
                        break;
                    case 5:
                        susLaunch();
                        break;
                    case 6:
                        follower.followPath(launchToPrep2, 1, true);
                        intake.setPower(intakeActivePower);
                        indexer.rotateToColor(Indexer.BallColor.EMPTY);
                        if (!intakeHasStarted) {
                            intakeSequence.start();
                            intakeHasStarted = true;
                        }
                        intakeSequence.start();
                        state = 7;
                        break;
                    case 7:
                        follower.followPath(prep2ToIntake2, 0.1, true);
                        state = 8;
                        break;
                    case 8:
                        follower.followPath(intake2ToLaunch, 1, true);
                        intake.setPower(intakeIdlePower);
                        state = 10;
                        break;
                    case 9:
                        susLaunch();
                        break;
                    case 10:
                        follower.followPath(launchToPark, 1, true);
                        outtakeFlywheel.setVelocity(0);
                        outtake.setRotationPosition(0);
                        state = -67;
                        break;
                }
            }
            telemetry.addData("launch state", launchState.currentState);
            telemetry.update();
        }
    }

    public boolean getColor() {
        int i = (launchCount +1) % 3 - 1;
        if (i < 0) i = 2;

        switch (tagID) {
            case 21:
                return TAG_21_PATTERN[i] == 0;
            case 22:
                return TAG_22_PATTERN[i] == 0;
            case 23:
                return TAG_23_PATTERN[i] == 0;
            default:
                return launchCount == 1;
        }
    }

    public void updateID() {
        if (getTags()) {
            tagID = -1;

            for (AprilTagResult tagResult : tagResults) {
                if (tagResult.tagID >= 21 && tagResult.tagID <= 23) tagID = tagResult.tagID;
            }
        }
    }

    public void susLaunch() {
        if (launchIdle && launchState.currentState == LaunchSequence.State.IDLE) {
            // Check if enough time has passed since the last launch
            if (System.currentTimeMillis() - delayTimer > launchDelay) {
                if (launchCount < 3) {
                    toGreen = getColor();
                    toPurple = !toGreen;

                    launchState.startLaunch();
                    launchCount++;
                    delayTimer = System.currentTimeMillis(); // Reset timer after starting launch
                } else {
                    state++;
                    launchCount = 0; // Reset for next time
                }
            }
        }
    }
}