package org.firstinspires.ftc.teamcode.Robot.test;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.subsystems.limelight.Limelight;

import java.io.IOException;
import java.util.Arrays;
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
            Optional<double[]> goalTagPosition = limelight.getGoalTagPosition();

            if (ballCount.isPresent()) telemetry.addData("Ball count:", ballCount.get());
            else telemetry.addLine("Ball count: unknown");

            if (pattern.isPresent()) telemetry.addData("Pattern:", Arrays.toString(pattern.get()));
            else telemetry.addLine("Pattern: unknown");

            if (goalTagPosition.isPresent()) telemetry.addData("Goal tag position:", Arrays.toString(goalTagPosition.get()));
            else telemetry.addLine("Goal tag position: unknown");

            telemetry.update();

            sleep(50);
        }
    }
}
