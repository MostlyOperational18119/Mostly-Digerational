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

            telemetry.addData("Slot0: ", Indexer.slot0Test());
            telemetry.update();
        }
    }
}
