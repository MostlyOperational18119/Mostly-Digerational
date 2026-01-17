package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;

@TeleOp(name = "teleop dead test")
public class outtakeDeadTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        waitForStart();

        Drivetrain.init(hardwareMap);
        Intake




                .init(hardwareMap);
        while (opModeIsActive()) {
            telemetry.addData("clicks", Drivetrain.outtakePosition());
            telemetry.update();
        }
    }
}
