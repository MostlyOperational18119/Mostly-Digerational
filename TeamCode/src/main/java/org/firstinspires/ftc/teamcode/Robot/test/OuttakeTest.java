package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;


@TeleOp(name = "outtake rotation test")
public class OuttakeTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        double launcherYawRotation = 0;

        Drivetrain.init(hardwareMap);
        OuttakeDemo.init(hardwareMap);
        waitForStart();

        while (opModeIsActive()) {

            int targetClicks = OuttakeDemo.setRotationPosition(launcherYawRotation);
            OuttakeDemo.update(targetClicks);

            if (gamepad1.leftBumperWasPressed()) {
                if (launcherYawRotation > 0) {
                    launcherYawRotation -= 0.07;
                }
            }

            if (gamepad1.rightBumperWasPressed()) {
                if (launcherYawRotation < 1) {
                    launcherYawRotation += 0.07;
                }
            }


            telemetry.addData("bumper value",launcherYawRotation);
            telemetry.addData("setRotationPosition", OuttakeDemo.setRotationPosition(launcherYawRotation));
            telemetry.addData("current position", Drivetrain.outtakePosition());
            telemetry.addData("error", targetClicks - Drivetrain.outtakePosition());
            telemetry.addData("servo power", OuttakeDemo.servoPower());
            telemetry.update();

        }
    }
}
