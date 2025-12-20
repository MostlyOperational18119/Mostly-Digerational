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
            Transfer.update(Indexer.slotColors(), idealChamber, 0);
        }
    }
}
