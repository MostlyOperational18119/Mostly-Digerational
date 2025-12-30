package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;

@TeleOp(name="indexer/transfer")
public class IndexerTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Indexer.init(hardwareMap);
//        Indexer.startLaunch();

        waitForStart();
        while (opModeIsActive()) {

            telemetry.addData("Slot 0 Red: ", Indexer.slotColor0Red());
            telemetry.addData("Slot 0 Green: ", Indexer.slotColor0Green());
            telemetry.addData("Slot 0 Blue: ", Indexer.slotColor0Blue());

            telemetry.addData("Slot 1 Red: ", Indexer.slotColor1Red());
            telemetry.addData("Slot 1 Green: ", Indexer.slotColor1Green());
            telemetry.addData("Slot 1 Blue: ", Indexer.slotColor1Blue());

            telemetry.addData("Slot 2 Red: ", Indexer.slotColor2Red());
            telemetry.addData("Slot 2 Green: ", Indexer.slotColor2Green());
            telemetry.addData("Slot 2 Blue: ", Indexer.slotColor2Blue());
        }
    }

}
