package org.firstinspires.ftc.teamcode.everything;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.camera.delegating.DelegatingCaptureSequence;

//flicker down 0.21
//up 0

@TeleOp(name = "TeleOp")
public class MainTeleOp extends Methods {
    @Override
    public void runOpMode() {
        teamHateLoveButton();
        initialize();
        waitForStart();
        //launcherYaw.setPosition(0.5);
        double launcherYawRotation = 0.5;
        double voltageMultiplier = 1;
        int launchDebounce = 0;
        float speedDivider = 1.2F;
        boolean aAlreadyPressed = false;
        double hoodPosition = 0.3;
        boolean isFar = true;
        boolean intaking = true;

        revolver.setPosition(0.0);


        Indexer indexer = new Indexer(this);
        Intake intakeSequence = new Intake(this, indexer);
        Outtake outtake = new Outtake(this);
        LaunchSequence launch = new LaunchSequence(this, indexer);

        while (opModeIsActive()) {
            voltageMultiplier = 12.57/voltageSensor.getVoltage();

            turn = gamepad1.right_stick_x;
            strafe = gamepad1.left_stick_x;
            forwards = -gamepad1.left_stick_y;
            double outPower = 0;
            motorFRPower = (forwards - strafe - turn) / speedDivider;
            motorFLPower = (forwards + strafe + turn) / speedDivider;
            motorBLPower = (forwards - strafe + turn) / speedDivider;
            motorBRPower = (forwards + strafe - turn) / speedDivider;

            fireGreen = gamepad2.aWasPressed();
            firePurple = gamepad2.xWasPressed();
            transferToggle = gamepad2.bWasPressed();
            aimLeft = gamepad2.dpadLeftWasPressed();
            aimRight = gamepad2.dpadRightWasPressed();
//            toGreen = gamepad2.rightBumperWasPressed();
//            toPurple = gamepad2.leftBumperWasPressed();

            //detectAprilTag();

            drive();
            launch.update();
            indexer.update();
            intakeSequence.update();
            outtake.update();

            //gamepad 2 intake
            intake.setPower(gamepad2.right_trigger);

            //switch between intake and outtake
            if (transferToggle) {
                if (intaking) {
                    launch.currentState = LaunchSequence.State.IDLE;
                } else {
                    indexer.rotateToColor(Indexer.BallColor.EMPTY);
                    intakeSequence.currentStateIntake = Intake.State.IDLE;

                }
                intaking = !intaking;
//                transferServo.setPosition(0);
//                launchDebounce = 50;
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

            //gamepad 1 swap far and close
            if (gamepad1.aWasPressed()) {

                if (!aAlreadyPressed) {
                    daHood.setPosition(0.15); //max 0
                    outPower = 0.6;
                }
                else {
                    daHood.setPosition(0.28); //max 0.88
                    outPower = 0.45;
                }
                aAlreadyPressed = !aAlreadyPressed;

            }

            outtakeFlywheel.setPower(outPower*voltageMultiplier);

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
                    launcherYawRotation = 0.15;
                }

                outtake.setRotationPosition(launcherYawRotation);

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



                telemetry.addData("intaking yes or no", intaking);
                telemetry.addData("A was pressed" , aAlreadyPressed);
                telemetry.addData("Hood position" , daHood.getPosition());
                telemetry.addData("outtake encoder", outtakeEncoder);
                telemetry.addData("toGreen", toGreen);
                telemetry.addData("toGreen", toPurple);
                telemetry.addData("firePurple", firePurple);
                telemetry.addData("fireGreen", fireGreen);
                telemetry.addData("launch sequence state", launch.currentState);
                telemetry.addData("intake sequence state", intakeSequence.currentStateIntake);
                telemetry.addData("outtake power", outtakePower);
                telemetry.addData("distance color sensor", colorSensor.getDistance(DistanceUnit.MM));
//                telemetry.addData("revolver position", revolver.getPosition());
                telemetry.addData("beam break", !breakBeamSensor.getState());
                telemetry.addData("indexer position", indexer.rotation);
                telemetry.addData("indexer next pos", indexer.nextRotation);
                telemetry.addData("red", colorSensor.red());
                telemetry.addData("green", colorSensor.green());
                telemetry.addData("blue", colorSensor.blue());
                telemetry.addData("slot 0", indexer.slots[0]);
                telemetry.addData("slot 1", indexer.slots[1]);
                telemetry.addData("slot 2", indexer.slots[2]);
//                telemetry.addData("revolver expected position", revolverExpectedPosition);
//                telemetry.addData("findColor(EMPTY) returns", indexer.findColor(Indexer.BallColor.EMPTY));
//            telemetry.addData("launch debounce", launchDebounce);
//            telemetry.addData("velocity", outtake.getVelocity());
//            telemetry.addData("hood position", hoodPosition);
//            telemetry.addData("is far", isFar);
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

                //debounce for transfer flicker
            }
        }
    }
