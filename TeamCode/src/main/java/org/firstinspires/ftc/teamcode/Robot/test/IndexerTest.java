package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;

@TeleOp(name="indexerTest")
public class IndexerTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Indexer.init(hardwareMap);
//        Indexer.startLaunch();

        waitForStart();
        while (opModeIsActive()) {

            telemetry.addData("slot0red: ", Indexer.slotColor0Red());
            telemetry.addData("slot0green: ", Indexer.slotColor0Green());
            telemetry.addData("slot0blue: ", Indexer.slotColor0Blue());
            telemetry.addData("Slot0: ", Indexer.slot0Test());

            telemetry.addData("slot1red: ", Indexer.slotColor1Red());
            telemetry.addData("slot1green: ", Indexer.slotColor1Green());
            telemetry.addData("slot1blue: ", Indexer.slotColor1Blue());
            telemetry.addData("Slot1: ", Indexer.slot1Test());

            telemetry.addData("slot2red: ", Indexer.slotColor2Red());
            telemetry.addData("slot2green: ", Indexer.slotColor2Green());
            telemetry.addData("slot2blue: ", Indexer.slotColor2Blue());
            telemetry.addData("Slot2: ", Indexer.slot2Test());

            telemetry.update();
        }
    }
}
