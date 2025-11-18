package org.firstinspires.ftc.teamcode.everything;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "auto!!!!!")
public class auto extends Methods {

    Pose start = new Pose(32.614, 134.376, Math.toRadians(90));
    Pose launch = new Pose(60.000, 84.000, Math.toRadians(126));
    Pose park = new Pose(36, 134.376, Math.toRadians(90));
    Follower follower;
    PathChain startToLaunch, launchToPark;

    int state = 0;
    boolean toGreen = false;

    Indexer indexer = new Indexer(this);
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

        waitForStart();

        //set all of indexer array to one color
        indexer.oneColor(Indexer.BallColor.GREEN);
        outtakeFlywheel.setPower(0.6);

        while (opModeIsActive()) {
            follower.update();
            launchState.update();
            indexer.update();
            outtake.update();

            if (!follower.isBusy()) {
                switch (state) {
                    case 0:
                        follower.followPath(startToLaunch, 0.5, true);
                        state = 1;
                        break;
                    case 1:
                        toGreen = true;
                        for (int i = 0; i < 3; i++) {
                            launchState.startLaunch();
                            sleep(500);
                        }
                        state = 2;
                        break;
                    case 2:
                        follower.followPath(launchToPark, 0.5, true);
                        state = -1;
                        break;
                }
            }
        }
    }
}
