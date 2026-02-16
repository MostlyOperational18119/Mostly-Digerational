package org.firstinspires.ftc.teamcode.Robot.opmode.auto;

import android.util.Log;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Robot.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.Robot.subsystems.limelight.Limelight;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Autonomous(name = "RedBack3NL")
public class RedBack3 extends LinearOpMode {
    Pose start = new Pose(88, 8, Math.toRadians(180));
    Pose launch = new Pose(88, 10, Math.toRadians(180));
    Pose park = new Pose(108, 8, Math.toRadians(180));
    Follower follower;
    PathChain launchToPark;
    int state = 0;
    long delayTimer = 0;
    int numBalls = -1;
    boolean limelightAvailable = true;

    Limelight limelight = null;
    // Return value is true if we're done
    boolean normalLaunch() {
        if (System.currentTimeMillis() - delayTimer > 700 && Outtake.outtakeMotorLeft.getVelocity() >= Outtake.speed - 20) {
            // Check and launch any remaining balls in the indexer
            if (Indexer.slotColors()[0] != 0) {
                delayTimer = Indexer.launch0();
            } else if (Indexer.slotColors()[2] != 0) {
                delayTimer = Indexer.launch2();
            } else if (Indexer.slotColors()[1] != 0) {
                delayTimer = Indexer.launch1();
            } else {
                // All slots empty, we're done, return true
                return true;
            }
        }
        // We wanna continue
        return false;
    }

    // Return value is true if we're done
    boolean launch() {
        if (limelightAvailable && numBalls != -1) {
            return Indexer.startLaunch(numBalls, true);
        } else {
            return normalLaunch();
        }
    }


    @Override
    public void runOpMode() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(start);
        Outtake.init(hardwareMap);
        Intake.init(hardwareMap);
        Indexer.init(hardwareMap);

        try {
            limelight = new Limelight();
        } catch (IOException e) {
            limelightAvailable = false;
            Log.e("BlueBackAuto3BallM3", String.format("No limelight, error was: %s", e.getLocalizedMessage()));
        }

        // Sanity check
        // Either we have no limelight, or the limelight is NOT null
        assert !limelightAvailable || limelight != null;

//        Outtake.SPEED_CONST_FAR = Outtake.SPEED_CONST_FAR / 1.1;

        launchToPark = follower.pathBuilder()
                .addPath(new BezierLine(launch, park))
                .setLinearHeadingInterpolation(launch.getHeading(), park.getHeading())
                .build();

        waitForStart();

        Outtake.outtakeSpeed();
        delayTimer = System.currentTimeMillis();


        while (opModeIsActive()) {
//            Outtake.update(targetClicks);
            follower.update();
            if (limelightAvailable) {
                limelight.update();
                // Set the numBalls to- wait for this:
                // The number of balls if we can get it (who would've guessed)
                limelight.getBallCount().ifPresent(integer -> numBalls = integer);
            }
            Indexer.updateSlot0();
            Indexer.updateSlot1();
            Indexer.updateSlot2();
            Outtake.robotOrientation = Math.toDegrees(follower.getHeading());
            Outtake.robotY = follower.getPose().getY();
            Outtake.robotX = follower.getPose().getX();

            String outtakePos = Integer.toString(Drivetrain.outtakePosition());
            String fileName = "OuttakeVars.txt";
            String filePath = "org.firstinspires.ftc.teamcode.Robot.opmode.OuttakeVars.txt";

            File myFile = AppUtil.getInstance().getSettingsFile(fileName);
            try (FileWriter fw = new FileWriter(filePath, false)) {
                // Opening with 'false' (or omitting it) overwrites the file
                fw.write("");
            } catch (IOException e) {
                e.printStackTrace();
            }
            ReadWriteFile.writeFile(myFile, outtakePos);

            Outtake.StaticVars.endPose = follower.getPose();
            Outtake.StaticVars.outtakePos = Drivetrain.outtakePosition();
            Outtake.StaticVars.isBlue = false;

            telemetry.addData("time delta", System.currentTimeMillis() - delayTimer);
            telemetry.addData("slot 1 state", Indexer.currentState1);
            telemetry.addData("robot x follower", follower.getPose().getX());
            telemetry.addData("outtake position", Outtake.StaticVars.outtakePos);
            telemetry.update();

            if (!follower.isBusy()) {
                switch (state) {
                    case 0:
                        Outtake.outtakeUpdate(-2, false, 0);
                        if (!limelightAvailable || limelight.getPattern().isPresent()) {
                            Indexer.updatePattern(limelight.getPattern().get());
                        }
                        if (System.currentTimeMillis() - delayTimer > 1000) {
                            state = 1;
                            delayTimer = System.currentTimeMillis();
                        }
                        break;
                    case 1:
                        Outtake.outtakeSpeed();
                        Outtake.outtakeUpdate(-1, false, 0);
                        if (System.currentTimeMillis() - delayTimer > 2000) {
                            state = 2;
                            delayTimer = System.currentTimeMillis();
                        }
                        break;
                    case 2:
                        Outtake.outtakeSpeed();
                        Outtake.outtakeUpdate(-1, false, 0);
                        if (launch()) state = 3;
                        break;
                    case 3:
                        Outtake.outtakeSpeed();
                        Outtake.outtakeUpdate(-1, false, 0);
                        if (System.currentTimeMillis() - delayTimer > 500) {
                            state = 4;
                        }
                        break;
                    case 4:
                        Outtake.outtakeSpeed();
                        Outtake.outtakeUpdate(-1, false, 0);
                        follower.followPath(launchToPark, 1, true);
                        state = 5;
                        break;
                    case 5:
                        Outtake.update(0, false);
                        break;
                }
            }
        }
    }
}
