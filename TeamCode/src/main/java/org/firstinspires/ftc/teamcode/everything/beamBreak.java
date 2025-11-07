package org.firstinspires.ftc.teamcode.everything;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@TeleOp(name = "break beam test")
public class beamBreak extends Methods {
    @Override
    public void runOpMode(){
        DigitalChannel beamBreak = hardwareMap.get(DigitalChannel.class, "beamSensor");
        beamBreak.setMode(DigitalChannel.Mode.INPUT);

        waitForStart();

        while (opModeIsActive()) {
            boolean beamBreakTripped = !beamBreak.getState();
            telemetry.addData("beam break tripped", beamBreakTripped);
            telemetry.update();

        }
    }
}
