package org.firstinspires.ftc.teamcode.everything.testPrograms;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.everything.Methods;

@TeleOp(name = "Launcher Demo")
public class LauncherDemo extends Methods {

    double speed = 8, hoodPos = 0.3;
    @Override
    public void runOpMode() {
        VoltageSensor voltageSensor;
        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");
        DcMotorEx flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        Servo hood = hardwareMap.get(Servo.class, "hood");
        Servo flicker = hardwareMap.get(Servo.class, "flicker");

        double constant = 0;
        double lowestRPM = 999999;
        double distance = 148; //placeholder
        int maxRPM = 5800;
        double targetRPM = 0;
        double measuredRPM = 0;
        double P = 0, D = 0;
        double power = 0;
        //double measuredAngularAcceleration = 0;

        waitForStart();
        while (opModeIsActive()) {
            targetRPM = constant * distance;
            measuredRPM = (flywheel.getVelocity()/28) * 60;
            constant = 23.716/12.7 * voltageSensor.getVoltage();
            //measuredAngularAcceleration = measuredRPM/60;

            power = targetRPM/maxRPM + P * (targetRPM - measuredRPM)/maxRPM + D;

            if (gamepad1.dpadUpWasPressed()) {
                constant += 0.1;
            } else if (gamepad1.dpadDownWasPressed()) {
                constant -= 0.1;
            }

            if (gamepad1.bWasPressed()) {
                P += 0.01;
            } else if (gamepad1.xWasPressed()) {
                P -= 0.01;
            }

            if (gamepad1.rightBumperWasPressed()) {
                D += 0.01;
            } else if (gamepad1.leftBumperWasPressed()) {
                D -= 0.01;
            }

            if (gamepad1.yWasPressed()) {
                hoodPos += 0.01;
            } else if (gamepad1.aWasPressed()) {
                hoodPos -= 0.01;
            }

            if (gamepad1.leftStickButtonWasPressed()) {
                lowestRPM = 999999;
            }

            if (measuredRPM < lowestRPM) {
                lowestRPM = measuredRPM;
            }

            hood.setPosition(hoodPos);
            flywheel.setPower(power);
            telemetry.addData("flywheel Speed", power);
            telemetry.addData("constant", constant);
            telemetry.addData("measured RPM", measuredRPM);
            telemetry.addData("Lowest RPM", lowestRPM);
            telemetry.addData("P", P);
            telemetry.addData("D", D);
            telemetry.addData("hood position", hoodPos);
            telemetry.update();
        }
    }
}