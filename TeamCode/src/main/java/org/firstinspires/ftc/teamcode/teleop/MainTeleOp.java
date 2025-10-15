package org.firstinspires.ftc.teamcode.teleop;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

@TeleOp(name = "TeleOp")
public class MainTeleOp extends Methods {
    @Override
    public void runOpMode() {
        initialize();
        waitForStart();
        Launch launch = new Launch(this);
        Intake intakeState = new Intake(this);
        while (opModeIsActive()) {
            turn = gamepad1.right_stick_x;
            strafe = gamepad1.left_stick_x;
            forwards = -gamepad1.left_stick_y;

            fire = gamepad2.aWasPressed();
            transferToggle = gamepad2.bWasPressed();
            cycleLeft = gamepad2.dpadLeftWasPressed();
            cycleRight = gamepad2.dpadRightWasPressed();
            intakeBall = gamepad2.right_trigger;

            detectAprilTag();
            drive();
            launch.update();
            intakeState.update();

            if (fire) {
                launch.startLaunch();}
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

            if (intakeBall != 0){
                intakeState.startIntake();
            }

            telemetry.update();
        }
    }
}
