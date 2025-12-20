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
        Transfer.init(hardwareMap);
        int[] idealChamber =  {1,1,2,1,1,2,1,1,2};

        while (opModeIsActive()) {
            Transfer.startLaunch();
            Transfer.update(Indexer.slotColors(), idealChamber, 0);

            telemetry.addData("index", Indexer.slotColors()[0]);
            telemetry.addData("index", Indexer.slotColors()[1]);
            telemetry.addData("index", Indexer.slotColors()[2]);
            telemetry.addData("ideal chamber 0", idealChamber[0]);
            telemetry.addData("ideal chamber 1", idealChamber[1]);
            telemetry.addData("ideal chamber 2", idealChamber[2]);
            telemetry.addData("slot0value", Indexer.slot0Values());
            telemetry.addData("slot1value", Indexer.slot1Values());
            telemetry.addData("slot2value", Indexer.slot2Values());
            telemetry.addData("slot 0 position", Transfer.slot0Position());
            telemetry.addData("slot 1 position", Transfer.slot1Position());
            telemetry.addData("slot 2 position", Transfer.slot2Position());
            telemetry.update();
        }
    }
}
