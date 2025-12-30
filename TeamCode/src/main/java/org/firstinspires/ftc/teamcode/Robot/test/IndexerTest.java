package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;

@TeleOp(name="indexer/transfer")
public class IndexerTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Indexer.init(hardwareMap);
        Indexer.startLaunch();

        waitForStart();
        while (opModeIsActive()) {

            Indexer.update(Indexer.slotColors());

            telemetry.addData("current index state", Indexer.currentState);
            telemetry.addData("colorSensor0 color", Indexer.slotColors()[0]);
            telemetry.addData("colorSensor1 color", Indexer.slotColors()[1]);
            telemetry.addData("colorSensor2 color", Indexer.slotColors()[2]);
            telemetry.addData("slot0 position", Indexer.slot0.getPosition());
            telemetry.addData("slot1 position", Indexer.slot1.getPosition());
            telemetry.addData("slot2 position", Indexer.slot2.getPosition());
            telemetry.update();

        }
    }

}
