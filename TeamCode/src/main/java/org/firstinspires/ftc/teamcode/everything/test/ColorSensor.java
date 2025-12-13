package org.firstinspires.ftc.teamcode.everything.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.everything.Methods;

@TeleOp(name = "color sensor")
public class ColorSensor extends Methods {
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
