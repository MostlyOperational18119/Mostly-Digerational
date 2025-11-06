package org.firstinspires.ftc.teamcode.everything;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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
        float speedDivider = 1.2F;
        double hoodPosition = 0.3;
        boolean isIntake = true;
        boolean isFar = true;
        LaunchSequence launch = new LaunchSequence(this);
        while (opModeIsActive()) {
            turn = -gamepad1.right_stick_x;
            strafe = gamepad1.left_stick_x;
            forwards = -gamepad1.left_stick_y;
            motorFRPower = -(forwards - strafe - turn)/speedDivider;
            motorFLPower = -(forwards + strafe + turn)/speedDivider;
            motorBLPower = -(forwards - strafe + turn)/speedDivider;
            motorBRPower = -(forwards + strafe - turn)/speedDivider;

            fire = gamepad2.aWasPressed();
            transferToggle = gamepad2.bWasPressed();
            cycleLeft = gamepad2.dpadLeftWasPressed();
            cycleRight = gamepad2.dpadRightWasPressed();
            toGreen = gamepad2.rightBumperWasPressed();
            toPurple = gamepad2.leftBumperWasPressed();

            //detectAprilTag();
            drive();
            //launch.update();
            //indexer.update();

            if (gamepad1.right_trigger >= 0.5) {
                speedDivider = 2F;
            } else if (gamepad1.left_trigger >= 0.5) {
                speedDivider = 1F;
            } else {
                speedDivider = 1.2F;
            }

            daHood.setPosition(hoodPosition);

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

            if (cycleLeft) {
                currentIndexIntake += 1;
                if (currentIndexIntake > 3) {
                    currentIndexIntake = 0;
                }
                isIntake = true;
            } else if (cycleRight) {
                currentIndexOuttake += 1;
                if (currentIndexOuttake > 3) {
                    currentIndexOuttake = 0;
                }
                isIntake = false;
            }

            if (fire) {
                transferServo.setPosition(0);
                launchDebounce = 50;
            }

//            telemetry.addData("red: ", colorSensor.red());
//            telemetry.addData("green: ", colorSensor.green());
//            telemetry.addData("blue: ", colorSensor.blue());
            telemetry.addData("launch debounce", launchDebounce);
            telemetry.addData("velocity", outtake.getVelocity());
            telemetry.addData("hood position", hoodPosition);
            telemetry.addData("is far", isFar);
            telemetry.update();

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

            if (gamepad2.bWasPressed()) {
                isFar = !isFar;
            }

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
            intake.setPower(gamepad2.right_trigger);

//            switch (ballPosition) {
//                case ONE:
//                    revolver.setPosition(0);
//                    break;
//                case TWO:
//                    revolver.setPosition(0.1);
//                    break;
//                case THREE:
//                    revolver.setPosition(0.2);
//                    break;
//            }

            telemetry.update();
        }
    }
}
