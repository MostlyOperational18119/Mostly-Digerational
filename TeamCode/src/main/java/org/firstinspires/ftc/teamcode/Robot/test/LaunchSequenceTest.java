package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Transfer;

@TeleOp(name = "Launch Sequence Test")
public class LaunchSequenceTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        Indexer.init(hardwareMap);

        while (opModeIsActive()) {
            Indexer.startLaunch();
            Indexer.update(Indexer.slotColors());

            telemetry.addData("index", Indexer.slotColors()[0]);
            telemetry.addData("index", Indexer.slotColors()[1]);
            telemetry.addData("index", Indexer.slotColors()[2]);
            telemetry.addData("slot 0 position", Transfer.slot0Position());
            telemetry.addData("slot 1 position", Transfer.slot1Position());
            telemetry.addData("slot 2 position", Transfer.slot2Position());
            telemetry.update();
        }
    }
}
