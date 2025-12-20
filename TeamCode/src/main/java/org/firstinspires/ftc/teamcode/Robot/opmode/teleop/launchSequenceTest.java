package org.firstinspires.ftc.teamcode.Robot.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Transfer;

@TeleOp(name="launch sequence test")
public class launchSequenceTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();

        Transfer.init(hardwareMap);
        Indexer.init(hardwareMap);
        boolean launch;

        while (opModeIsActive()) {
            launch = gamepad1.aWasPressed();
            if (launch) {
                int[] slots = Indexer.slotColors();
                Transfer.launch(slots, new int[]{1, 0, 0, 1, 0, 0, 1, 0, 0}, 0);
            }
        }

    }
}
