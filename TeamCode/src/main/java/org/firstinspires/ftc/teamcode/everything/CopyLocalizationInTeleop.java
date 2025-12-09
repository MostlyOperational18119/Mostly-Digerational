package org.firstinspires.ftc.teamcode.everything;

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
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.function.Supplier;

@TeleOp(group = "Teleop Test", name = "Localization Test With Auto Position")
public class CopyLocalizationInTeleop extends OpMode {
    public static Pose startingPose;
    private Follower follower;
    private boolean automatedDrive = false;
    private Supplier<PathChain> pathChain;
    private TelemetryManager telemetryM;
    private boolean slowMode = false;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startingPose == null ? new Pose() : startingPose);
        follower.update();
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        DcMotor motorFR = hardwareMap.dcMotor.get("motorFR"); //also contains encoder for outtake
        motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        DcMotor motorFL = hardwareMap.dcMotor.get("motorFL");
        motorFL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        DcMotor motorBR = hardwareMap.dcMotor.get("motorBR");
        motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        DcMotor motorBL = hardwareMap.dcMotor.get("motorBL");
        motorBL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //curve?
        pathChain = () -> follower.pathBuilder()
                .addPath(new Path(new BezierLine(follower::getPose, new Pose(56, 9))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(90), 0.8))
                .build();
    }

    @Override
    public void start() {
        follower.startTeleOpDrive();
    }

    @Override
    public void loop() {
        follower.update();

        if (!automatedDrive) {
            if (!slowMode) follower.setTeleOpDrive(
                    -gamepad1.left_stick_y,
                    gamepad1.left_stick_x,
                    gamepad1.right_stick_x,
                    true
            );
            else follower.setTeleOpDrive(
                    -gamepad1.left_stick_y * 0.5,
                    gamepad1.left_stick_x * 0.5,
                    gamepad1.right_stick_x * 0.5,
                    true
            );
        }

        if (gamepad1.rightBumperWasPressed()) {
            slowMode = !slowMode;
        }

        if (gamepad1.aWasPressed()) {
            follower.followPath(pathChain.get());
            automatedDrive = true;
        }

        if (gamepad1.bWasPressed() && automatedDrive) {
            follower.startTeleOpDrive();
            automatedDrive = false;
        }

        if (automatedDrive && !follower.isBusy()) {
            follower.startTeleOpDrive();
            automatedDrive = false;
        }

        telemetryM.addData("position", follower.getPose());
        telemetryM.addData("velocity", follower.getVelocity());
        telemetryM.addData("automatedDrive", automatedDrive);
        telemetryM.addData("startingPose", follower.getPose());
        telemetryM.update();
    }
}
