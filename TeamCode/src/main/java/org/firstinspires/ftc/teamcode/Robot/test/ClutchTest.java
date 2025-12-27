package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;

@TeleOp(name="clutch")
public class ClutchTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        Outtake.init(hardwareMap);

        while (opModeIsActive()) {
            if (gamepad1.right_trigger > .5) {
                Outtake.run();
            }
        }
    }
}
