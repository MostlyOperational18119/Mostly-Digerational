package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;

@TeleOp(name = "clutch")
public class ClutchTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        Outtake.init(hardwareMap);
        //Outtake.startOuttake();
//        Outtake.setMotorPower();

        while (opModeIsActive()) {
//            if (gamepad1.right_trigger > .5) {
//                //Outtake.clutchUpdate();
//                Outtake.setMotorPower();
//            }
//            if (gamepad1.bWasPressed()) {
//                Outtake.offOuttake();
//            }
//            telemetry.addData("motor 1 velocity", Outtake.testTelemetryMotor1());
//            telemetry.addData("motor 2 velocity", Outtake.testTelemetryMotor2());
            telemetry.update();
        }
    }
}
