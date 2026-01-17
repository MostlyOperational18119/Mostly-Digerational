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

@Autonomous(name = "BlueBackM3")
public class BlueBackAutoM3 extends LinearOpMode {
    Pose start = new Pose(56, 8, Math.toRadians(0));
    Pose intakePrep = new Pose(43, 36, Math.toRadians(0));
    Pose intakeEnd = new Pose(15, 36, Math.toRadians(0));
    Pose park = new Pose(36, 8, Math.toRadians(0));
    Follower follower;
    PathChain startToIntakePrep, intake, intakeToLaunch, launchToPark;
    int state = 0;
    int targetClicks = 0;
    long launchDelayTimer = 0;
    int launchCount = 0;

    @Override
    public void runOpMode() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(start);
        Outtake.init(hardwareMap);
        Intake.init(hardwareMap);
        Indexer.init(hardwareMap);

        startToIntakePrep = follower.pathBuilder()
                .addPath(new BezierLine(start, intakePrep))
                .setLinearHeadingInterpolation(start.getHeading(), intakePrep.getHeading())
                .build();
        intake = follower.pathBuilder()
                .addPath(new BezierLine(intakePrep, intakeEnd))
                .setLinearHeadingInterpolation(intakePrep.getHeading(), intakeEnd.getHeading())
                .build();
        intakeToLaunch = follower.pathBuilder()
                .addPath(new BezierLine(intakeEnd, start))
                .setLinearHeadingInterpolation(intakeEnd.getHeading(), start.getHeading())
                .build();
        launchToPark = follower.pathBuilder()
                .addPath(new BezierLine(start, park))
                .setLinearHeadingInterpolation(start.getHeading(), park.getHeading())
                .build();

        waitForStart();

        Outtake.outtakeSpeed();

        while (opModeIsActive()) {
            Outtake.update(targetClicks);
            follower.update();
            Indexer.updateSlot0();
            Indexer.updateSlot1();
            Indexer.updateSlot2();

            telemetry.addData("clicks", Drivetrain.outtakePosition());
            telemetry.addData("time delta", System.currentTimeMillis() - launchDelayTimer);
            telemetry.addData("slot 1 state", Indexer.currentState1);
            telemetry.update();

            if (!follower.isBusy()) {
                switch (state) {
                    case 0:
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
                                    state = 1;
                                    launchCount = 0;
                                    break;
                            }
                        }
                        break;
                    case 1:
                        //targetClicks = Outtake.setTarget(Outtake.setRotationPosition(0.42));
                        follower.followPath(startToIntakePrep, 1, true);
                        state = 2;
                        launchDelayTimer = System.currentTimeMillis();
                        break;
                    case 2:
                        follower.followPath(intake, 1, true);
                        state = 3;
                        break;
                    case 3:
                        follower.followPath(intakeToLaunch);
                        state = 4;
                        launchDelayTimer = System.currentTimeMillis();
                        break;
                    case 4:
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
                                    state = 5;
                                    launchCount = 0;
                                    break;
                            }
                        }
                        break;
                    case 5:
                        follower.followPath(launchToPark);
                        state = 6;
                        break;
                }
            }
        }
    }
}
