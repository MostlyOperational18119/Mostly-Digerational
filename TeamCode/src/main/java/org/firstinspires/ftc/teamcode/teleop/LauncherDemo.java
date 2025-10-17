package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

@TeleOp(name = "Launcher Demo")
public class LauncherDemo extends Methods {

    double speed = 8, hoodPos = 0;
    @Override
    public void runOpMode() {
        VoltageSensor voltageSensor;
        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");
        DcMotorEx flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        Servo hood = hardwareMap.get(Servo.class, "hood");
        Servo flicker = hardwareMap.get(Servo.class, "flicker");

        double constant = 19.5;
        double distance = 180; //placeholder
        int maxRPM = 5800;
        double targetRPM = 0;
        double measuredRPM = 0;
        double P = 1, D = 1;
        double power = 0;
        //double measuredAngularAcceleration = 0;

        waitForStart();
        while (opModeIsActive()) {
            targetRPM = constant * distance;
            measuredRPM = flywheel.getVelocity();
            //measuredAngularAcceleration = measuredRPM/60;

            power = targetRPM/maxRPM + P * (targetRPM - measuredRPM)/maxRPM + D;

            /*if (gamepad1.dpadUpWasPressed()) {
                speed += 0.1;
            } else if (gamepad1.dpadDownWasPressed()) {
                speed -= 0.1;
            }*/

            if (gamepad1.yWasPressed()) {
                hoodPos += 0.01;
            } else if (gamepad1.aWasPressed()) {
                hoodPos -= 0.01;
            }

            hood.setPosition(hoodPos);
            flywheel.setPower(power);
            telemetry.addData("flywheel Speed", power);
            telemetry.addData("flywheel velocity", flywheel.getVelocity());
            telemetry.addData("hood position", hoodPos);
            telemetry.update();
        }
    }
}