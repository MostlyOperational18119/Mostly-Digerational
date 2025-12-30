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
            telemetry.update();
        }
    }
}
