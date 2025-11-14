package org.firstinspires.ftc.teamcode.everything;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

//flicker down 0.21
//up 0

@TeleOp(name = "TeleOp")
public class MainTeleOp extends Methods {
    @Override
    public void runOpMode() {
        teamHateLoveButton();
        initialize();
        waitForStart();
        launcherYaw.setPosition(0.5);
        double launcherYawRotation = 0.5;
        int launchDebounce = 0;
        int currentIndexIntake = 0;
        int currentIndexOuttake = 0;
        double debounceStart = 0;
        float speedDivider = 1.2F;
        double hoodPosition = 0.3;
        boolean isIntake = true;
        boolean isFar = true;
        boolean colorDebounce = false;

        long beamBreakStartTime;

        LaunchSequence launch = new LaunchSequence(this);
        Indexer indexer = new Indexer(this);

        while (opModeIsActive()) {
            turn = gamepad1.right_stick_x;
            strafe = gamepad1.left_stick_x;
            forwards = -gamepad1.left_stick_y;
            motorFRPower = (forwards - strafe - turn) / speedDivider;
            motorFLPower = (forwards + strafe + turn) / speedDivider;
            motorBLPower = (forwards - strafe + turn) / speedDivider;
            motorBRPower = (forwards + strafe - turn) / speedDivider;

            fire = gamepad2.aWasPressed();
            transferToggle = gamepad2.bWasPressed();
            cycleLeft = gamepad2.dpadLeftWasPressed();
            cycleRight = gamepad2.dpadRightWasPressed();
            toGreen = gamepad2.rightBumperWasPressed();
            toPurple = gamepad2.leftBumperWasPressed();

            //detectAprilTag();

            drive();
            //launch.update();
            indexer.update();

            intake.setPower(gamepad2.right_trigger);
//            if (gamepad2.right_trigger > 0) {
//                    if (!beamDebounce) {
//                        beamDebounce = true;
//                        debounceStart = getRuntime();
//                        if (indexer.colorInArray(Indexer.BallColor.EMPTY)) {
//                            int index = indexer.findColor(Indexer.BallColor.EMPTY);
//                            indexer.rotateToColor(Indexer.BallColor.EMPTY);
//                            indexer.onBeamBreak(index, colorSensor.green(), colorSensor.blue());
//                        } else {
//                            intake.setPower(0);
//                        }
//                    }
//            }
//
//            if ((getRuntime() - debounceStart) >= 1) {
//                beamDebounce = false;
//            }

            if (!breakBeamSensor.getState()) {
                if (!colorDebounce) {
                    colorDebounce = true;
                    debounceStart = getRuntime();
                    indexer.setIndexerColor();
                    indexer.rotateWithDistanceCheck();
                }
            }


            if ((getRuntime() - debounceStart) >= 0.3) {
                colorDebounce = false;
            }

                //gamepad 1 speed clutch
                if (gamepad1.right_trigger >= 0.5) {
                    speedDivider = 2F;
                } else if (gamepad1.left_trigger >= 0.5) {
                    speedDivider = 1F;
                } else {
                    speedDivider = 1.2F;
                }

                daHood.setPosition(hoodPosition);

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

                launcherYaw.setPosition(launcherYawRotation);

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

                //gamepad 2 press launch
                if (fire) {
                    transferServo.setPosition(0);
                    launchDebounce = 50;
                }

                telemetry.addData("findColorHappened", indexer.findColorHappened);
                telemetry.addData("distance color sensor", colorSensor.getDistance(DistanceUnit.MM));
                telemetry.addData("revolver position", revolver.getPosition());
                telemetry.addData("beam break", !breakBeamSensor.getState());
                telemetry.addData("indexer position", indexer.rotation);
                telemetry.addData("indexer next pos", indexer.nextRotation);
//                telemetry.addData("db: ", beamDebounce);
//                telemetry.addData("last db: ", debounceStart);
//                telemetry.addData("current time: ", getRuntime());
                telemetry.addData("red", colorSensor.red());
                telemetry.addData("green", colorSensor.green());
                telemetry.addData("blue", colorSensor.blue());
                telemetry.addData("slot 0", indexer.slots[0]);
                telemetry.addData("slot 1", indexer.slots[1]);
                telemetry.addData("slot 2", indexer.slots[2]);
//            telemetry.addData("launch debounce", launchDebounce);
//            telemetry.addData("velocity", outtake.getVelocity());
//            telemetry.addData("hood position", hoodPosition);
//            telemetry.addData("is far", isFar);
                telemetry.update();


                //gamepad 2 launcher positions
                if (isFar) {
                    targetRPM = (int) (0.575 * maxRPM * 13.1 / voltageSensor.getVoltage());
                    measuredRPM = (int) (outtake.getVelocity() / 28 * 60);
                    power = 0.575 + (targetRPM - measuredRPM) * P_FAR;
                    hoodPosition = 0.3;
                    outtake.setPower(power);
                } else {
                    targetRPM = (int) (0.5 * maxRPM * 12.5 / voltageSensor.getVoltage());
                    measuredRPM = (int) (outtake.getVelocity() / 28 * 60);
                    power = 0.5 + (targetRPM - measuredRPM) * P_CLOSE;
                    outtake.setPower(power);
                    hoodPosition = 0.7;
                }

                //gamepad 2 button press to toggle between launch positions
                if (gamepad2.bWasPressed()) {
                    isFar = !isFar;
                }

                //debounce for transfer flicker
                if (launchDebounce <= 0) {
                    transferServo.setPosition(0.21);
                } else {
                    launchDebounce -= 1;
                }

                if (isIntake) {
                    setIndexerIntake(currentIndexIntake);
                } else {
                    setIndexerOuttake(currentIndexOuttake);
                }

//            if (fire) {
//                launch.startLaunch();
//            }
//            if (toGreen) {
//                indexer.rotateToColor(Indexer.BallColor.GREEN);
//            }
//            if (toPurple) {
//                indexer.rotateToColor(Indexer.BallColor.PURPLE);
//            }
            }
        }
    }
