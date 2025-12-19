package org.firstinspires.ftc.teamcode.Robot.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Transfer;

@TeleOp(name="launchSequenceTest")
public class launchSequenceTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();
        boolean launch;

        Transfer.init(hardwareMap);
        Indexer.init(hardwareMap);

        while (opModeIsActive()) {
            launch = gamepad1.aWasPressed();



        }

    }
}
