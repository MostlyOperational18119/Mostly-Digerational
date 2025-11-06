package org.firstinspires.ftc.teamcode.everything;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@TeleOp(name = "sensor test")
public class sensorTester extends Methods {
    @Override
    public void runOpMode(){
        boolean beamBreakTripped = false;
        DigitalChannel beamBreak = hardwareMap.get(DigitalChannel.class, "beam_sensor");
        //RevColorSensorV3 colorSensor = hardwareMap.get(RevColorSensorV3.class, "colorSensor");

        waitForStart();

        while (opModeIsActive()) {

            telemetry.addData("beam break tripped", beamBreakTripped);
            telemetry.update();

        }
    }
}
