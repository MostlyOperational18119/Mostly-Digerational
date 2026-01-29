package org.firstinspires.ftc.teamcode.Robot.opmode.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Lift;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;

@TeleOp(name="TeleOp")
public class compOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        //placeholders
        int launch = -1;
        long startTime = 0;
        long startTime0 = 0, startTime1, startTime2;
        final long WAIT = 300;
        final long LAUNCH_TIME = 150;
        long launchDelayTimer = 0;
        boolean isLaunching = false;
        int launchCount = 0;
        int currentTransfer = 0;
        int count = 0;
        int adjust = 0;
        double noCorrectAdjust = 0.0;
        boolean isCorrecting = true;

//        Outtake.isBlue = false;
//        Outtake.start = new Pose(16.641,16.1903, Math.toRadians(90));

        Drivetrain.init(hardwareMap);
        Outtake.init(hardwareMap);
        Intake.init(hardwareMap);
        Indexer.init(hardwareMap);
        Lift.init(hardwareMap);

        Follower follower;
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Outtake.start);

        waitForStart();

        launchDelayTimer = System.currentTimeMillis();

        while (opModeIsActive()) {

            //drivetrain controller input variable declarations
            float y = -gamepad1.left_stick_y;
            float x = gamepad1.left_stick_x;
            float rx = gamepad1.right_stick_x;
            boolean X = gamepad1.xWasPressed();
            boolean B = gamepad1.bWasPressed();
            boolean A = gamepad1.aWasPressed();
            boolean intake = gamepad1.right_trigger > 0.5;
            boolean spit = gamepad1.left_trigger > 0.5;
            boolean Y = gamepad1.yWasPressed();
            boolean lb = gamepad1.leftBumperWasPressed();
            boolean rb = gamepad1.rightBumperWasPressed();
            boolean leftD = gamepad1.dpadLeftWasPressed();
            boolean rightD = gamepad1.dpadRightWasPressed();

            follower.update();
            Outtake.robotY = follower.getPose().getY();
            Outtake.robotX = follower.getPose().getX();
            Outtake.robotOrientation = Math.toDegrees(follower.getHeading());

            //look at chamber or shoot
//            if (X) {
//                launch *= -1;
//            }

            if (isCorrecting) {
                if (leftD) {
                    adjust -= 3;
                }
                if (rightD) {
                    adjust += 3;
                }
            } else {
                if (leftD) {
                    noCorrectAdjust -= 0.02;
                }
                if (rightD) {
                    noCorrectAdjust += 0.02;
                }
            }

            Drivetrain.drive(y, x, rx, rb);
            if (isCorrecting) {
                Outtake.outtakeUpdate(-1, true);
            } else {
                Outtake.update(Outtake.getRotationPosition(noCorrectAdjust));
            }

            if (lb) {
                isCorrecting = !isCorrecting;
            }
            Outtake.outtakeSpeed();

            Indexer.updateSlot0();
            Indexer.updateSlot1();
            Indexer.updateSlot2();


            if (intake) {
                Intake.intakeGo();
            } else if (spit) {
                Intake.intakeSpit();
            } else {
                Intake.intakeStop();
            }

            if (isLaunching) {
                if (System.currentTimeMillis() - launchDelayTimer > 600) {
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

//            if (lb) {
//                Outtake.angleOffset += 1;
//            }
//            if (rb) {
//                Outtake.angleOffset -= 1;
//            }

            if (Y) {
                if (Outtake.goalX == Outtake.redX) {
                    Outtake.goalX = Outtake.blueX + adjust;
                } else {
                    Outtake.goalX = Outtake.redX + adjust;
                }
            }

//            int[] slots = Indexer.slotColors();
//            telemetry.addData("supposed dist", Outtake.distance);
            telemetry.addData("speed const very close", Outtake.SPEED_CONST_VERY_CLOSE);
            telemetry.addData("angle adjustment", adjust);
//            telemetry.addData("speed const close")
//            telemetry.addData("hood angle", Outtake.CLOSE_HOOD);
//            telemetry.addData("odometry X", Outtake.robotX);
//            telemetry.addData("odometry Y", Outtake.robotY);
//            telemetry.addData("up position 0", Indexer.UP_POS_0);
//            telemetry.addData("up position 1", Indexer.UP_POS_1);
//            telemetry.addData("up position 2", Indexer.UP_POS_2);
//            telemetry.addData("Left motor velo", Outtake.testTelemetryMotor1());
//            telemetry.addData("Right motor velo", Outtake.testTelemetryMotor2());
            telemetry.addData("current distance", Outtake.currentDistance);
            telemetry.addData("left flywheel velocity", Outtake.outtakeMotorLeft.getVelocity());
            telemetry.addData("right flywheel velocity", Outtake.outtakeMotorRight.getVelocity());
            telemetry.addData("speed", Outtake.speed);
            telemetry.addData("Position", Drivetrain.StaticVars.endPose);
            telemetry.update();
//            telemetry.addData("launch", launch);
//            telemetry.addData("slot0", slots[0]);
//            telemetry.addData("slot1", slots[1]);
//            telemetry.addData("slot2", slots[2]);
//            telemetry.update();
        }
    }
}
