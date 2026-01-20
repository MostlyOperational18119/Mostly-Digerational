package org.firstinspires.ftc.teamcode.Robot.opmode.teleop;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;

@Configurable
public class configurableTeleop extends LinearOpMode {
    public static double p = 0, i = 0, d = 0, f = 0;
    public static double CLOSE_SPEED = 0, VERY_CLOSE_SPEED = 0, FAR_SPEED = 0;
    public static double CLOSE_HOOD = 0, CLOSER_HOOD = 0, FAR_HOOD = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        long launchDelayTimer = 0;
        boolean isLaunching = false;
        int launchCount = 0;
        int currentTransfer = 0;

        Outtake.isBlue = false;

        Drivetrain.init(hardwareMap);
        Outtake.init(hardwareMap);
        Intake.init(hardwareMap);
        Indexer.init(hardwareMap);

        Pose start = new Pose(16.641,16.1903, Math.toRadians(90));
        Follower follower;
        follower =  Constants.createFollower(hardwareMap);
        follower.setStartingPose(start);

        waitForStart();

        launchDelayTimer = System.currentTimeMillis();

        while (opModeIsActive()) {
            float y = -gamepad1.left_stick_y;
            float x = gamepad1.left_stick_x;
            float rx = gamepad1.right_stick_x;
            boolean X = gamepad1.xWasPressed();
            boolean B = gamepad1.bWasPressed();
            boolean A = gamepad1.aWasPressed();
            boolean intake = gamepad1.right_trigger > 0.5;
            boolean Y = gamepad1.yWasPressed();
            boolean lt = gamepad1.left_trigger > 0.5;
            boolean lb = gamepad1.leftBumperWasPressed();
            boolean rb = gamepad1.rightBumperWasPressed();
            boolean dpadUp = gamepad1.dpadUpWasPressed();
            boolean dpadDown = gamepad1.dpadDownWasPressed();
            boolean dpadRight = gamepad1.dpadRightWasPressed();
            boolean dpadLeft = gamepad1.dpadLeftWasPressed();

            follower.update();
            Outtake.robotY = follower.getPose().getY();
            Outtake.robotX = follower.getPose().getX();
            Outtake.robotOrientation = Math.toDegrees(follower.getHeading());

            Drivetrain.drive(y, x, rx);
            Outtake.outtakeUpdate(-1);
            Outtake.outtakeSpeed();

            //configurable testing
            Outtake.updatePID(p, i, d, f);
            Outtake.updateSpeedConsts(CLOSE_SPEED, VERY_CLOSE_SPEED, FAR_SPEED);
            Outtake.updateHoodPositions(CLOSE_HOOD, CLOSER_HOOD, FAR_HOOD);

            Indexer.updateSlot0();
            Indexer.updateSlot1();
            Indexer.updateSlot2();

            if (intake) {
                Intake.intakeGo();
            } else {
                Intake.intakeStop();
            }

            if (isLaunching) {
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
                            isLaunching = false;
                            launchCount = 0;
                            break;
                    }
                }
            }

            if (A) {
                isLaunching = true;
            }

            if (gamepad1.backWasPressed()) {
                if (Outtake.goalX == 140) {
                    Outtake.goalX = 4;
                } else {
                    Outtake.goalX = 140;
                }
            }
        }
    }
}
