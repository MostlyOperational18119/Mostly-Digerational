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

@Autonomous(name = "BlueBack3")
public class BlueBack3 extends LinearOpMode {
    Pose start = new Pose(56, 8, Math.toRadians(180));
    Pose launchPos = new Pose(56, 10, Math.toRadians(180));
    Pose park = new Pose(36, 8, Math.toRadians(180));
    Follower follower;
    PathChain launchToPark, startToLaunch;
    int state = -2;
    long delayTimer = 0;
    int launchCount = 0;
    int numBalls = -1;
    boolean limelightAvailable = true;

    Limelight limelight = null;
    // Return value is true if we're done
    boolean normalLaunch() {
        if (System.currentTimeMillis() - delayTimer > 700 && Outtake.outtakeMotorLeft.getVelocity() >= Outtake.speed - 20) {
            switch (launchCount) {
                case 0:
                    delayTimer = Indexer.launch0();
                    launchCount = 1;
                    break;
                case 1:
                    delayTimer = Indexer.launch2();
                    launchCount = 2;
                    break;
                case 2:
                    delayTimer = Indexer.launch1();
                    state = 1;
                    launchCount = 0;
                    break;
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
        Outtake.StaticVars.isBlue = true;
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

        startToLaunch = follower.pathBuilder()
                .addPath(new BezierLine(start, launchPos))
                .setLinearHeadingInterpolation(start.getHeading(), launchPos.getHeading())
                .build();

        launchToPark = follower.pathBuilder()
                .addPath(new BezierLine(launchPos, park))
                .setLinearHeadingInterpolation(launchPos.getHeading(), park.getHeading())
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

            if (state == -2) {
                Outtake.outtakeUpdate(-2, false, 0);
                Outtake.outtakeSpeed();
            } else if (state != 4) {
                Outtake.outtakeUpdate(-1, false, 0);
                Outtake.outtakeSpeed();
            }

            Outtake.StaticVars.endPose = follower.getPose();
            Outtake.StaticVars.outtakePos = Drivetrain.outtakePosition();


            telemetry.addData("time delta", System.currentTimeMillis() - delayTimer);
            telemetry.addData("slot 1 state", Indexer.currentState1);
            telemetry.addData("robot x follower", follower.getPose().getX());
            telemetry.addData("outtake position", Outtake.StaticVars.outtakePos);
            telemetry.addData("0 state", Indexer.currentState0);
            telemetry.addData("1 state", Indexer.currentState1);
            telemetry.addData("2 state", Indexer.currentState2);
            telemetry.addData("follower busy", follower.isBusy());
            telemetry.update();

            if (!follower.isBusy()) {
                switch (state) {
                    case -2:
//                        Outtake.outtakeUpdate(-2, false, 0);
                        if (limelightAvailable && limelight.getPattern().isPresent()) {
                            Indexer.updatePattern(limelight.getPattern().get());
                        }
                        if (System.currentTimeMillis() - delayTimer > 3000) {
                            state = -1;
                            delayTimer = System.currentTimeMillis();
                        }
                        break;
                    case -1:
//                        Outtake.outtakeUpdate(-1, false, 0);
//                        Outtake.outtakeSpeed();
                        if (System.currentTimeMillis() - delayTimer > 2000) {
                            state = 0;
                            delayTimer = System.currentTimeMillis();
                        }
                        break;
                    case 0:
                        follower.followPath(startToLaunch, 1, true);
                        state = 1;
                        break;
                    case 1:
//                        Outtake.outtakeUpdate(-1, false, 0);
//                        Outtake.outtakeSpeed();
                        // Move on if we're done :P
                        if (launch()) state = 2;
                        break;
                    case 2:
                        Indexer.oneHasLaunched = false;
                        Indexer.zeroHasLaunched = false;
                        Indexer.twoHasLaunched = false;
//                        Outtake.outtakeUpdate(-1, false, 0);
//                        Outtake.outtakeSpeed();
                        if (System.currentTimeMillis() - delayTimer > 500) {
                            state = 3;
                        }
                        break;
                    case 3:
                        follower.followPath(launchToPark, 1, true);
                        state = 4;
                        break;
                    case 4:
                        Outtake.update(0, false);
                        Outtake.StaticVars.isBlue = true;
                        break;
                }
            }
        }
    }
}
