package org.firstinspires.ftc.teamcode.Robot.auto;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Robot.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Intake;

@Autonomous(name = "BFA3")
public class BlueFrontAuto extends LinearOpMode {
    Pose start = new Pose(32.614, 134.376, Math.toRadians(90));
    Pose launch = new Pose(60.000, 84.000, Math.toRadians(135));
    Pose park = new Pose(36, 134, Math.toRadians(90));
    Follower follower;
    PathChain startToLaunch, launchToPark;

    int state = -1;
    int launchCount = 0;
    long launchDelayTimer = 0;
    int LAUNCH_DELAY_MS = 2500;

    @Override
    public void runOpMode() {
        //isRed = false;
        //initalization

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

        waitForStart();

        launchDelayTimer = System.currentTimeMillis();
        while (opModeIsActive()) {
//            telemetry.addData("velocity coefficients", outtakeFlywheel.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
//            telemetry.update();
            follower.update();
//            launchState.update();
//            indexer.update();
//            outtake.update();

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
                        launchDelayTimer = System.currentTimeMillis();
                        break;
                    case 1:
                        if (state == 1)//launchState.currentState == LaunchSequence.State.IDLE) {
                            // Check if enough time has passed since the last launch
                            if (System.currentTimeMillis() - launchDelayTimer > LAUNCH_DELAY_MS) {
                                if (launchCount < 3) {
                                    if (launchCount == 1) {
//                                        toGreen = true;
//                                        toPurple = false;
                                    } else {
//                                        toGreen = false;
//                                        toPurple = true;
                                    }
//                                    launchState.startLaunch();
                                    launchCount++;
                                    launchDelayTimer = System.currentTimeMillis(); // Reset timer after starting launch
                                } else {
                                    state = 2;
                                    launchCount = 0; // Reset for next time
                                }
                            }
                        break;
                    case 2:
                        follower.followPath(launchToPark, 1, true);
                        Intake.intakeStop();
//                        outtakeFlywheel.setPower(0);
//                        outtake.setRotationPosition(0);
                        state = -67;
                        break;
                }
            }
        }
    }
}
