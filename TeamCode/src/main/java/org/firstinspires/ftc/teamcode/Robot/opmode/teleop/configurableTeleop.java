package org.firstinspires.ftc.teamcode.Robot.opmode.teleop;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;

@Configurable
@TeleOp(name = "configurable")
public class configurableTeleop extends LinearOpMode {
    public static double p = 6, i = 2, d = 6, f = 2;
    public static double CLOSE_SPEED = 60, VERY_CLOSE_SPEED = 40, FAR_SPEED = 362;
    public static double CLOSE_HOOD = 0.48, CLOSER_HOOD = 0.3, FAR_HOOD = 0;
    public static double LAUNCH_WAIT = 300;

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

        Follower follower;
        follower =  Constants.createFollower(hardwareMap);
        follower.setStartingPose(Outtake.start);

        waitForStart();

        launchDelayTimer = System.currentTimeMillis();

        while (opModeIsActive()) {
            float y = -gamepad1.left_stick_y;
            float x = gamepad1.left_stick_x;
            float rx = gamepad1.right_stick_x;
            boolean A = gamepad1.aWasPressed();
            boolean intake = gamepad1.right_trigger > 0.5;


            follower.update();
            Outtake.robotY = follower.getPose().getY();
            Outtake.robotX = follower.getPose().getX();
            Outtake.robotOrientation = Math.toDegrees(follower.getHeading());

            //Drivetrain.drive(y, x, rx);
            Outtake.outtakeUpdate(-1, true, 0);
            Outtake.outtakeSpeed();

            //indexer state machines
            Indexer.updateSlot0();
            Indexer.updateSlot1();
            Indexer.updateSlot2();
            //Indexer.updateBrakePad();

            if (intake) {
                Intake.intakeGo();
            } else {
                Intake.intakeStop();
            }

            if (isLaunching) {
                if (System.currentTimeMillis() - launchDelayTimer > 500) {
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

            telemetry.addData("speed const close", Outtake.SPEED_CONST_CLOSE);
            telemetry.addData("speed const far", Outtake.SPEED_CONST_FAR);
            telemetry.addData("hood angle", Outtake.returnHoodPos());
            telemetry.addData("odometry X", Outtake.robotX);
            telemetry.addData("odometry Y", Outtake.robotY);
            telemetry.addData("up position 0", Indexer.UP_POS_0);
            telemetry.addData("up position 1", Indexer.UP_POS_1);
            telemetry.addData("up position 2", Indexer.UP_POS_2);
            telemetry.addData("Left motor velo", Outtake.testTelemetryMotor1());
            telemetry.addData("Right motor velo", Outtake.testTelemetryMotor2());
            telemetry.update();
        }
    }
}
