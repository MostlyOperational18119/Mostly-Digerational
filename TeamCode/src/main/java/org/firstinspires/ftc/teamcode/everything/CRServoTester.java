package org.firstinspires.ftc.teamcode.everything;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "cr servo tester")
public class CRServoTester extends Methods {
    @Override
    public void runOpMode() {
        double servoPosition = 0.5;
        initialize();
        Outtake outtake = new Outtake(this);
        //CRServo testServo = hardwareMap.get(CRServo.class, "daHood");

        waitForStart();

        while (opModeIsActive()) {
            outtake.update();
            if (gamepad1.aWasPressed()) {
                servoPosition += 0.01;
            } else if (gamepad1.bWasPressed()) {
                servoPosition -= 0.01;
            }

            outtake.setRotationPosition(servoPosition);

            //testServo.setPower(servoPosition);

            telemetry.addData("servo position", servoPosition);
            telemetry.update();

        }
    }


}