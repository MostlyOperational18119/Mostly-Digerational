package org.firstinspires.ftc.teamcode.everything.test;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.function.Supplier;

@TeleOp(group = "Teleop Test", name = "Localization Test With Auto Position")
public class copyLocalizationInTeleop extends OpMode {
    private Follower follower;
    public static Pose startingPose;
    private boolean automatedDrive;
    private Supplier<PathChain> pathChain;
    private TelemetryManager telemetryM;
    private boolean slowMode = false;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startingPose == null ? new Pose() : startingPose);
        follower.update();
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        //curve?
        pathChain = () -> follower.pathBuilder()
                .addPath(new Path(new BezierLine(follower::getPose, new Pose(0,0))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(45), 0.8))
                .build();
    }

    @Override
    public void start() {
        follower.startTeleOpDrive();
    }

    @Override
    public void loop(){
        follower.update();
        telemetryM.update();

        if(!automatedDrive) {
            if (!slowMode) follower.setTeleOpDrive(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x,
                    true
            );
            else follower.setTeleOpDrive(
                    -gamepad1.left_stick_y * 0.5,
                    -gamepad1.left_stick_x * 0.5,
                    -gamepad1.right_stick_x * 0.5,
                    true
            );
        }

        if(gamepad1.rightBumperWasPressed()) {
            slowMode = !slowMode;
        }

        if(gamepad1.aWasPressed()) {
            follower.followPath(pathChain.get());
            automatedDrive = true;
        }

        telemetryM.debug("position", follower.getPose());
        telemetryM.debug("velocity", follower.getVelocity());
        telemetryM.debug("automatedDrive", automatedDrive);
    }
}
