package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Limelight;

import java.io.IOException;
import java.util.Optional;

@TeleOp(name = "Limelight")
public class LimelightTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();

        Limelight limelight;
        try {
            limelight = new Limelight();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        while (opModeIsActive()) {
            Optional<Integer> ballCount = limelight.getBallCount();
            Optional<Integer[]> pattern = limelight.getPattern();

            if (ballCount.isPresent()) telemetry.addData("Ball count:", ballCount.get());
            else telemetry.addLine("Ball count: unknown");

            if (pattern.isPresent()) telemetry.addData("Pattern:", pattern.get());
            else telemetry.addLine("Pattern: unknown");

            telemetry.update();
        }
    }
}
