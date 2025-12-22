package org.firstinspires.ftc.teamcode.Robot.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Lift;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;

@TeleOp(name="drivetrain testing")
public class competitionOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();

        Intake.init(hardwareMap);
        Drivetrain.init(hardwareMap);

        boolean intake = gamepad1.rightBumperWasPressed();

        while (opModeIsActive()) {

            //drivetrain controller input variable declarations
            float y = gamepad1.left_stick_y; // y stick is not reversed
            float x = -gamepad1.left_stick_x;
            float rx = gamepad1.right_stick_x;
            
            Drivetrain.drive(y, x, rx);

            if (intake) {
                Intake.intakeGo();
            } else {
                Intake.intakeIdle();
            }

        }

    }
}
