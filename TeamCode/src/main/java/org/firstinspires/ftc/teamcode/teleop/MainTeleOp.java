package org.firstinspires.ftc.teamcode.teleop;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

//flicker down 0.97
//up 0.75

@TeleOp(name = "TeleOp")
public class MainTeleOp extends Methods {
    @Override
    public void runOpMode() {
        teamHateLoveButton();
        initialize();
        waitForStart();
        launcherYaw.setPosition(0.5);
        int launchDebounce = 0;
        int currentIndexIntake = 0;
        int currentIndexOuttake = 0;
        float speedDivider = 1.2F;
        boolean isIntake = true;
        boolean isUp = true;
        LaunchSequence launch = new LaunchSequence(this);
        while (opModeIsActive()) {
            turn = gamepad1.right_stick_x;
            strafe = gamepad1.left_stick_x;
            forwards = -gamepad1.left_stick_y;
            motorFRPower = (forwards - strafe - turn)/speedDivider;
            motorFLPower = (forwards + strafe + turn)/speedDivider;
            motorBLPower = (forwards - strafe + turn)/speedDivider;
            motorBRPower = (forwards + strafe - turn)/speedDivider;


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

            if (gamepad2.xWasPressed()) {
                if (isUp) {
                    daHood.setPosition(0.3);
                } else {
                    daHood.setPosition(0);
                }

                isUp = !isUp;
            }

            if (gamepad1.right_trigger >= 0.5) {
                speedDivider = 2F;
            } else if (gamepad1.left_trigger >= 0.5) {
                speedDivider = 1F;
            } else {
                speedDivider = 1.2F;
            }

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
                transferServo.setPosition(0.8);
                launchDebounce = 50

                ;
            }

            telemetry.addData("red: ", colorSensor.red());
            telemetry.addData("green: ", colorSensor.green());
            telemetry.addData("blue: ", colorSensor.blue());
            telemetry.addData("launch debounce", launchDebounce);
            telemetry.update();

            outtake.setPower(0.6);

            if (launchDebounce <= 0) {
                transferServo.setPosition(1);
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
