package org.firstinspires.ftc.teamcode.Robot.opmode.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;
import org.opencv.core.Mat;

@Autonomous(name = "BlueBackM3")
public class BlueBackAutoM3 extends LinearOpMode {
    Pose start = new Pose(56, 8, Math.toRadians(180));
    Pose intakePrep1 = new Pose(43, 36, Math.toRadians(180));
    Pose intakePrep2 = new Pose(43, 60, Math.toRadians(180));
    Pose intakePrep3 = new Pose(43, 84, Math.toRadians(180));
    Pose intakeEnd1 = new Pose(15, 36, Math.toRadians(180));
    Pose intakeEnd2 = new Pose(15, 60, Math.toRadians(180));
    Pose intakeEnd3 = new Pose(15, 84, Math.toRadians(180));
    Pose park = new Pose(36, 8, Math.toRadians(180));
    Follower follower;
    PathChain toIntakePrep1, intake1, intakeToLaunch1, toIntakePrep2, intake2, intakeToLaunch2, toIntakePrep3, intake3, intakeToLaunch3, launchToPark;
    int state = 0;
    int targetClicks = 0;
    long launchDelayTimer = 0;
    int launchCount = 0;

    @Override
    public void runOpMode() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(start);
        Outtake.isBlue = true;
        Outtake.init(hardwareMap);
        Intake.init(hardwareMap);
        Indexer.init(hardwareMap);

        toIntakePrep1 = follower.pathBuilder()
                .addPath(new BezierLine(start, intakePrep1))
                .setLinearHeadingInterpolation(start.getHeading(), intakePrep1.getHeading())
                .build();
        intake1 = follower.pathBuilder()
                .addPath(new BezierLine(intakePrep1, intakeEnd1))
                .setLinearHeadingInterpolation(intakePrep1.getHeading(), intakeEnd1.getHeading())
                .build();
        intakeToLaunch1 = follower.pathBuilder()
                .addPath(new BezierLine(intakeEnd1, start))
                .setLinearHeadingInterpolation(intakeEnd1.getHeading(), start.getHeading())
                .build();
        toIntakePrep2 = follower.pathBuilder()
                .addPath(new BezierLine(start, intakePrep2))
                .setLinearHeadingInterpolation(start.getHeading(), intakePrep2.getHeading())
                .build();

        intake2 = follower.pathBuilder()
                .addPath(new BezierLine(intakePrep2, intakeEnd2))
                .setLinearHeadingInterpolation(intakePrep2.getHeading(), intakeEnd2.getHeading())
                .build();

        intakeToLaunch2 = follower.pathBuilder()
                .addPath(new BezierLine(intakeEnd2, start))
                .setLinearHeadingInterpolation(intakeEnd2.getHeading(), start.getHeading())
                .build();

        toIntakePrep3 = follower.pathBuilder()
                .addPath(new BezierLine(start, intakePrep3))
                .setLinearHeadingInterpolation(start.getHeading(), intakePrep3.getHeading())
                .build();

        intake3 = follower.pathBuilder()
                .addPath(new BezierLine(intakePrep3, intakeEnd3))
                .setLinearHeadingInterpolation(intakePrep3.getHeading(), intakeEnd3.getHeading())
                .build();

        intakeToLaunch3 = follower.pathBuilder()
                .addPath(new BezierLine(intakeEnd3, start))
                .setLinearHeadingInterpolation(intakeEnd3.getHeading(), start.getHeading())
                .build();

        launchToPark = follower.pathBuilder()
                .addPath(new BezierLine(start, park))
                .setLinearHeadingInterpolation(start.getHeading(), park.getHeading())
                .build();

        waitForStart();

        Outtake.outtakeSpeed();

        while (opModeIsActive()) {
//            Outtake.update(targetClicks);
            follower.update();
            Indexer.updateSlot0();
            Indexer.updateSlot1();
            Indexer.updateSlot2();
            Outtake.robotOrientation = Math.toDegrees(follower.getHeading());
            Outtake.robotY = follower.getPose().getY();
            Outtake.robotX = follower.getPose().getX();
            Outtake.outtakeSpeed();
            Outtake.outtakeUpdate(-1);

            telemetry.addData("clicks", Drivetrain.outtakePosition());
            telemetry.addData("time delta", System.currentTimeMillis() - launchDelayTimer);
            telemetry.addData("slot 1 state", Indexer.currentState1);
            telemetry.addData("robot x follower", follower.getPose().getX());
            telemetry.update();

            if (!follower.isBusy()) {
                switch (state) {
                    case -1:
                        if (System.currentTimeMillis() - launchDelayTimer > 5000) {
                            state = 0;
                            launchDelayTimer = System.currentTimeMillis();
                        }
                        break;
                    case 0:
                        if (System.currentTimeMillis() - launchDelayTimer > 2000) {
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
                                    state = 1;
                                    launchCount = 0;
                                    break;
                            }
                        }
                        break;
                    case 1:
                        //targetClicks = Outtake.setTarget(Outtake.setRotationPosition(0.42));
                        follower.followPath(toIntakePrep1, 0.8, true);
                        state = 2;
                        launchDelayTimer = System.currentTimeMillis();
                        Intake.intakeGo();
                        break;
                    case 2:
                        follower.followPath(intake1, 0.6, true);
                        state = 3;
                        break;
                    case 3:
                        follower.followPath(intakeToLaunch1);
                        state = 4;
                        launchDelayTimer = System.currentTimeMillis();
                        Intake.intakeStop();
                        break;
                    case 4:
                        if (System.currentTimeMillis() - launchDelayTimer > 2000) {
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
                                    state = 5;
                                    launchCount = 0;
                                    break;
                            }
                        }
                        break;
                    case 5:
                        follower.followPath(toIntakePrep2, 1, true);
                        state = 6;
                        break;
                    case 6:
                        follower.followPath(intake2, 1, true);
                        state = 7;
                        break;
                    case 7:
                        follower.followPath(intakeToLaunch2, 1, true);
                        state = 8;
                        launchDelayTimer = System.currentTimeMillis();
                        break;
                    case 8:
                        if (System.currentTimeMillis() - launchDelayTimer > 2000) {
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
                                    state = 9;
                                    launchCount = 0;
                                    break;
                            }
                        }
                        break;
                    case 9:
                        follower.followPath(toIntakePrep3, 1, true);
                        state = 10;
                        break;
                    case 10:
                        follower.followPath(intake3, 1, true);
                        state = 11;
                        break;
                    case 11:
                        follower.followPath(intakeToLaunch3, 1, true);
                        state = 12;
                        launchDelayTimer = System.currentTimeMillis();
                        break;
                    case 12:
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
                                    state = 13;
                                    launchCount = 0;
                                    break;
                            }
                        }
                    case 13:
                        follower.followPath(launchToPark);
                        state = 14;
                        break;
                }
            }
        }
    }
}
