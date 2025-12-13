package org.firstinspires.ftc.teamcode.everything.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.everything.Methods;

@TeleOp(name = "servo tester")
public class ServoTester extends Methods {
    @Override
    public void runOpMode() {
        double servoPosition = 0;
        //CRServo testServo = hardwareMap.get(CRServo.class, "daHood");
        Servo testServo = hardwareMap.servo.get("transferServo");

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.aWasPressed()) {
                servoPosition += 0.01;
            } else if (gamepad1.bWasPressed()) {
                servoPosition -= 0.01;
            }

            //testServo.setPower(servoPosition);
            testServo.setPosition(servoPosition);

            telemetry.addData("servo position", servoPosition);
            telemetry.update();

        }
    }


}
