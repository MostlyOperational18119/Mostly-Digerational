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
        detectAprilTag();
        while (opModeIsActive()) {
            turn = gamepad1.right_stick_x;
            strafe = gamepad1.left_stick_x;
            forwards = -gamepad1.left_stick_y;

            fire = gamepad2.a;
            transferToggle = gamepad2.b;
            cycleLeft = gamepad2.dpad_left;
            cycleRight = gamepad2.dpad_right;


            drive();
            
            intake.setPower(gamepad2.right_trigger);
        }
    }
}
