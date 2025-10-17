package org.firstinspires.ftc.teamcode.teleop;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TeleOp")
public class MainTeleOp extends Methods {
    @Override
    public void runOpMode() {
        initialize();
        waitForStart();
        //LaunchSequence launch = new LaunchSequence(this);
        while (opModeIsActive()) {
            turn = gamepad1.right_stick_x;
            strafe = gamepad1.left_stick_x;
            forwards = -gamepad1.left_stick_y;

            fire = gamepad2.aWasPressed();
            transferToggle = gamepad2.bWasPressed();
            cycleLeft = gamepad2.dpadLeftWasPressed();
            cycleRight = gamepad2.dpadRightWasPressed();

            detectAprilTag();
            drive();
            //launch.update();

            if (fire) {
                //launch.startLaunch();
            }
            intake.setPower(gamepad2.right_trigger);

            switch (ballPosition) {
                case ONE:
                    revolver.setPosition(0);
                    break;
                case TWO:
                    revolver.setPosition(0.1);
                    break;
                case THREE:
                    revolver.setPosition(0.2);
                    break;
            }

            telemetry.update();
        }
    }
}
