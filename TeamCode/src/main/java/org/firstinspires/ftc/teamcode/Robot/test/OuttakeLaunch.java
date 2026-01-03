package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;

@TeleOp(name = "test launch")
public class OuttakeLaunch extends LinearOpMode {
    @Override
    public void runOpMode() {

        double targetClicks = 0;

        Outtake.init(hardwareMap);

        while (opModeIsActive()) {
            targetClicks = Outtake.pointAtGoal();
            Outtake.update(Outtake.setTarget(targetClicks));
            Outtake.outtakeSpeed();
        }

    }
}
