package org.firstinspires.ftc.teamcode.everything;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.everything.limelight.BetterLimelight;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.Arrays;

//flicker down 0.21
//up 0

@TeleOp(name = "TeleOp")
public class MainTeleOp extends Methods {
    @Override
    public void runOpMode() {
        teamHateLoveButton();
        initialize();
        waitForStart();
        double outPower = 0.6;
        double launcherYawRotation = 0.5;
        double voltageMultiplier = 1;
        float speedDivider = 1.2F;
        boolean aAlreadyPressed = false;
        boolean intaking = true;
        boolean canLimelight = true;
        BetterLimelight limelight = null;

        Pose start = new Pose(72, 72, Math.toRadians(90)); //?

        revolver.setPosition(0.0);

        isRed = true;

        Follower follower;
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(start);

        Indexer indexer = new Indexer(this);
        Intake intakeSequence = new Intake(this, indexer);
        Outtake outtake = new Outtake(this);
        LaunchSequence launch = new LaunchSequence(this, indexer);
        try {
            limelight = new BetterLimelight();
        } catch (Exception e) {
            canLimelight = false;
        }

        ElapsedTime aimBotTimer = new ElapsedTime();

        while (opModeIsActive()) {
            follower.update();
            robotX = follower.getPose().getX();
            robotY = follower.getPose().getY();
            robotOrientation = follower.getHeading();
            voltageMultiplier = 12.57/voltageSensor.getVoltage();

            turn = gamepad1.right_stick_x;
            strafe = gamepad1.left_stick_x;
            forwards = -gamepad1.left_stick_y;
            intakeYes = gamepad1.right_bumper;

            motorFRPower = (forwards - strafe - turn) / speedDivider;
            motorFLPower = (forwards + strafe + turn) / speedDivider;
            motorBLPower = (forwards - strafe + turn) / speedDivider;
            motorBRPower = (forwards + strafe - turn) / speedDivider;

            fireGreen = gamepad2.aWasPressed();
            firePurple = gamepad2.xWasPressed();
            transferToggle = gamepad2.bWasPressed();
            aimLeft = gamepad2.dpadLeftWasPressed();
            aimRight = gamepad2.dpadRightWasPressed();

            if (canLimelight) {
                // If we failed, we probably need to reconnect :P
                canLimelight = limelight.update();
            } else {
                try {
                    if (limelight == null) {
                        limelight = new BetterLimelight();
                    } else {
                        limelight.connect();
                    }
                } catch (Exception e) {
                    canLimelight = false;
                }
            }
            //detectAprilTag();
            drive();
            launch.update();
            indexer.update();
            intakeSequence.update();
            outtake.update();

            //gamepad 1 intake
            if (intakeYes)
                intake.setPower(1);
            else
                intake.setPower(0);

            //switch between intake and outtake
            if (transferToggle) {
                if (intaking) {
                    launch.currentState = LaunchSequence.State.IDLE;
                } else {
                    indexer.rotateToColor(Indexer.BallColor.EMPTY);
                    intakeSequence.currentStateIntake = Intake.State.IDLE;

                }
                intaking = !intaking;
            }

            //gamepad 2 indexer go
            if (indexer.colorInArray(Indexer.BallColor.EMPTY) && !breakBeamSensor.getState() && intakeSequence.currentStateIntake == Intake.State.IDLE && intaking) {
                intakeSequence.start();
            }

            //gamepad 2 press launch
            if (fireGreen) {
                toGreen = true;
                toPurple = false;
                launch.startLaunch();
            } else if (firePurple) {
                toPurple = true;
                toGreen = false;
                launch.startLaunch();
            }

            if (aimBotTimer.milliseconds() >= 500) {
                outtake.setTarget(nicksLittleHelper());
                aimBotTimer.reset();
            }

            //gamepad 1 swap far and close
            if (gamepad1.aWasPressed()) {

                if (!aAlreadyPressed) {
                    daHood.setPosition(0.15); //max 0
                    outPower = 0.6;
                } else {
                    daHood.setPosition(0.7); //max 0.88
                    outPower = 0.45;
                }
                aAlreadyPressed = !aAlreadyPressed;

            }

            outtakeFlywheel.setPower(outPower * voltageMultiplier);

            //gamepad 1 speed clutch
            if (gamepad1.right_trigger >= 0.5) {
                speedDivider = 2F;
            } else if (gamepad1.left_trigger >= 0.5) {
                speedDivider = 1F;
            } else {
                speedDivider = 1.2F;
            }

            //gamepad 2 outtake YAW
            if (gamepad2.left_bumper) {
                if (launcherYawRotation > 0) {
                    launcherYawRotation -= 0.03;
                }
            }

            if (gamepad2.right_bumper) {
                if (launcherYawRotation < 1) {
                    launcherYawRotation += 0.03;
                }
            }

            if (aimRight) {
                launcherYawRotation = 0.81;
            } else if (aimLeft) {
                launcherYawRotation = 0.2;
            }

            //outtake.setRotationPosition(launcherYawRotation);

                //outtake.setRotationPosition(launcherYawRotation);

                //gamepad 2 manual cycle (intake/outtake)
//                if (cycleLeft) {
//                    currentIndexIntake += 1;
//                    if (currentIndexIntake > 3) {
//                        currentIndexIntake = 0;
//                    }
//                    isIntake = true;
//                } else if (cycleRight) {
//                    currentIndexOuttake += 1;
//                    if (currentIndexOuttake > 3) {
//                        currentIndexOuttake = 0;
//                    }
//                    isIntake = false;
//                }

                telemetry.addData("is saarang retarded?", nicksLittleHelper());
                telemetry.addData("robot x", robotX);
                telemetry.addData("robot y", robotY);
                telemetry.addData("robot orientation", robotOrientation);
                telemetry.addData("Hood position" , daHood.getPosition());
                telemetry.addData("outtake encoder", outtakeEncoder);
                telemetry.addData("launch sequence state", launch.currentState);
                telemetry.addData("intake sequence state", intakeSequence.currentStateIntake);
//                telemetry.addData("revolver position", revolver.getPosition());
//                telemetry.addData("revolver expected position", revolverExpectedPosition);
//                telemetry.addData("findColor(EMPTY) returns", indexer.findColor(Indexer.BallColor.EMPTY));
//            telemetry.addData("launch debounce", launchDebounce);
//            telemetry.addData("velocity", outtake.getVelocity());
            telemetry.update();


            //gamepad 2 launcher positions
//                if (isFar) {
//                    targetRPM = (int) (0.575 * maxRPM * 13.1 / voltageSensor.getVoltage());
//                    measuredRPM = (int) (outtake.getVelocity() / 28 * 60);
//                    power = 0.575 + (targetRPM - measuredRPM) * P_FAR;
//                    hoodPosition = 0.3;
//                    outtake.setPower(power);
//                } else {
//                    targetRPM = (int) (0.5 * maxRPM * 12.5 / voltageSensor.getVoltage());
//                    measuredRPM = (int) (outtake.getVelocity() / 28 * 60);
//                    power = 0.5 + (targetRPM - measuredRPM) * P_CLOSE;
//                    outtake.setPower(power);
//                    hoodPosition = 0.7;
//                }

            //gamepad 2 button press to toggle between launch positions
//                if (gamepad2.bWasPressed()) {
//                    isFar = !isFar;
//                }

        }
    }
}
