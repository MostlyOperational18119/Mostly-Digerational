package org.firstinspires.ftc.teamcode.teleop;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "TeleOp")
public class MainTeleOp extends Methods {
    @Override
    public void runOpMode() {
        initialize();
        waitForStart();
        //LaunchSequence launch = new LaunchSequence(this);8
        while (opModeIsActive()) {
            turn = gamepad1.right_stick_x;
            strafe = gamepad1.left_stick_x;
            forwards = -gamepad1.left_stick_y;
            motorFRPower = (forwards - strafe - turn);
            motorFLPower = (forwards + strafe + turn);
            motorBLPower = (forwards - strafe + turn);
            motorBRPower = (forwards + strafe - turn);

            motorFRPower = (float) Range.clip(motorFRPower, -1.0, 1.0);
            motorFLPower = (float) Range.clip(motorFLPower, -1.0, 1.0);
            motorBRPower = (float) Range.clip(motorBRPower, -1.0, 1.0);
            motorBLPower = (float) Range.clip(motorBLPower, -1.0, 1.0);


            fire = gamepad2.aWasPressed();
            transferToggle = gamepad2.bWasPressed();
            cycleLeft = gamepad2.dpadLeftWasPressed();
            cycleRight = gamepad2.dpadRightWasPressed();

            //detectAprilTag();
            drive();
            //launch.update();

            if (Math.random() >= 0.5) {
                saarangHateButton();
            } else {
                saarangLoveButton();
            }

//            if (fire) {
//                //launch.startLaunch();
//            }
//            intake.setPower(gamepad2.right_trigger);
//
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
