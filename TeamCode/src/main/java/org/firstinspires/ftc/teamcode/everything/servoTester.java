package org.firstinspires.ftc.teamcode.everything;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "servo tester")
public class servoTester extends Methods {
    @Override
    public void runOpMode() {
        double servoPosition = 0;
        //CRServo testServo = hardwareMap.get(CRServo.class, "daHood");
        Servo testServo = hardwareMap.servo.get("daHood");

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
