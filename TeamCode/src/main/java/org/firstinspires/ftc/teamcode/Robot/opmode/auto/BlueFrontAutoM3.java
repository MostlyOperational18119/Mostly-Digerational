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

@Autonomous(name = "BlueFrontM3")
public class BlueFrontAutoM3 extends LinearOpMode {
    Pose start = new Pose(32.5, 134.5, Math.toRadians(90));
    Pose shoot1 = new Pose(47.5, 96.5, Math.toRadians(140));
    Follower follower;
    PathChain startToShoot1;

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

        startToShoot1 = follower.pathBuilder()
                .addPath(new BezierLine(start, shoot1))
                .setLinearHeadingInterpolation(start.getHeading(), shoot1.getHeading())
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
                        targetClicks = Outtake.setTarget(Outtake.setRotationPosition(0.35));
                        follower.followPath(startToShoot1, 1, true);
                        state = 1;
                        launchDelayTimer = System.currentTimeMillis();
                        break;
                    case 1:
                        if (System.currentTimeMillis() - launchDelayTimer > 2500) {
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
                                    state = 2;
                                    launchCount = 0;
                                    break;
                            }
                        }
                        break;
                }
            }
        }
    }
}
