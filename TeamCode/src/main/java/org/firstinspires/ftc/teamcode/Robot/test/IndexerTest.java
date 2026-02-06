package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;

@TeleOp(name = "indexerTest")
public class IndexerTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        int chamberNum = 0;
        int chamberIncrease = -1;
        Indexer.init(hardwareMap);

        waitForStart();
        while (opModeIsActive()) {

            Indexer.startLaunch(chamberNum, true);
            Indexer.updateSlot2();
            Indexer.updateSlot0();
            Indexer.updateSlot1();

            telemetry.addData("slot0 state", Indexer.currentState0);
            telemetry.addData("slot1 state", Indexer.currentState1);
            telemetry.addData("slot2 state", Indexer.currentState2);
            telemetry.addData("chamber increase", Indexer.chamberIncrease);

            telemetry.addData("slot0red: ", Indexer.slotColor0Red());
            telemetry.addData("slot0green: ", Indexer.slotColor0Green());
            telemetry.addData("slot0blue: ", Indexer.slotColor0Blue());
            telemetry.addData("Slot0 (array): ", Indexer.slotColors()[0]);

            telemetry.addData("slot1red: ", Indexer.slotColor1Red());
            telemetry.addData("slot1green: ", Indexer.slotColor1Green());
            telemetry.addData("slot1blue: ", Indexer.slotColor1Blue());
            telemetry.addData("Slot1 (array): ", Indexer.slotColors()[1]);

            telemetry.addData("slot2red: ", Indexer.slotColor2Red());
            telemetry.addData("slot2green: ", Indexer.slotColor2Green());
            telemetry.addData("slot2blue: ", Indexer.slotColor2Blue());
            telemetry.addData("Slot2 (array): ", Indexer.slotColors()[2]);

            telemetry.update();
        }
    }
}
