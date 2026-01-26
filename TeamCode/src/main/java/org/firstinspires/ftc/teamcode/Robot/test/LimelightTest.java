package org.firstinspires.ftc.teamcode.Robot.test;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.subsystems.limelight.Limelight;

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
            Log.e("LimelightTest", String.format("Error getting limelight: %s", e.getLocalizedMessage()));
            throw new RuntimeException(e);
        }


        while (opModeIsActive()) {
            limelight.update();

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
