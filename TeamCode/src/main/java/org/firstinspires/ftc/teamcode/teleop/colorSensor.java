package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "color sensor")
public class colorSensor extends Methods {
    @Override
    public void runOpMode() {
        initialize();
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("red: ", colorSensor.red());
            telemetry.addData("green: ", colorSensor.green());
            telemetry.addData("blue", colorSensor.blue());
            telemetry.update();
        }
    }
}
