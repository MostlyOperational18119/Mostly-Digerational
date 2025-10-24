package org.firstinspires.ftc.teamcode.teleop;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "TeleOp")
public class MainTeleOp extends Methods {
    @Override
    public void runOpMode() {
        saarangHateLoveButton();
        initialize();
        waitForStart();
        LaunchSequence launch = new LaunchSequence();
        launch.InitLaunchSequence(this);
        while (opModeIsActive()) {
            turn = gamepad1.right_stick_x;
            strafe = gamepad1.left_stick_x;
            forwards = -gamepad1.left_stick_y;
            motorFRPower = (forwards - strafe - turn);
            motorFLPower = (forwards + strafe + turn);
            motorBLPower = (forwards - strafe + turn);
            motorBRPower = (forwards + strafe - turn);


            fire = gamepad2.aWasPressed();
            transferToggle = gamepad2.bWasPressed();
            cycleLeft = gamepad2.dpadLeftWasPressed();
            cycleRight = gamepad2.dpadRightWasPressed();
            toGreen = gamepad2.rightBumperWasPressed();
            toPurple = gamepad2.leftBumperWasPressed();

            //detectAprilTag();
            drive();
            launch.update();
            indexer.update();

            if (fire) {
                launch.startLaunch();
            }
            if (toGreen) {
                indexer.rotateToColor(Indexer.BallColor.GREEN);
            }
            if (toPurple) {
                indexer.rotateToColor(Indexer.BallColor.PURPLE);
            }
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
