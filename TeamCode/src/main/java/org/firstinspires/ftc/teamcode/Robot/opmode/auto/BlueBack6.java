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

@Autonomous(name = "BlueBack6")
public class BlueBack6 extends LinearOpMode {
    Pose start = new Pose(56, 8, Math.toRadians(180));
    Pose launch = new Pose(56, 10, Math.toRadians(180));
    Pose intakePrep1 = new Pose(46, 34, Math.toRadians(180));
    Pose intakePrep2 = new Pose(46, 58, Math.toRadians(180));
    Pose intakePrep3 = new Pose(46, 84, Math.toRadians(180));
    Pose intakeEnd1 = new Pose(18, 34, Math.toRadians(180));
    Pose intakeEnd2 = new Pose(18, 58, Math.toRadians(180));
    Pose intakeEnd3 = new Pose(18, 84, Math.toRadians(180));
    Pose park = new Pose(36, 8, Math.toRadians(180));
    Follower follower;
    PathChain toIntakePrep1, intake1, intakeToLaunch1, toIntakePrep2, intake2, intakeToLaunch2, toIntakePrep3, intake3, intakeToLaunch3, launchToPark;
    int state = -1;
    int targetClicks = 0;
    long delayTimer = 0;
    int launchCount = 0;
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
        Outtake.StaticVars.isBlue = true;
        Outtake.init(hardwareMap);
        Intake.init(hardwareMap);
        Indexer.init(hardwareMap);

        try {
            limelight = new Limelight();
        } catch (IOException e) {
            limelightAvailable = false;
            Log.e("BlueBackAuto6BallM3", String.format("No limelight, error was: %s", e.getLocalizedMessage()));
        }

        // Sanity check
        // Either we have no limelight, or the limelight is NOT null
        assert !limelightAvailable || limelight != null;

//        Outtake.SPEED_CONST_FAR = Outtake.SPEED_CONST_FAR / 1.1;

        toIntakePrep1 = follower.pathBuilder()
                .addPath(new BezierLine(launch, intakePrep1))
                .setLinearHeadingInterpolation(launch.getHeading(), intakePrep1.getHeading())
                .build();
        intake1 = follower.pathBuilder()
                .addPath(new BezierLine(intakePrep1, intakeEnd1))
                .setLinearHeadingInterpolation(intakePrep1.getHeading(), intakeEnd1.getHeading())
                .build();
        intakeToLaunch1 = follower.pathBuilder()
                .addPath(new BezierLine(intakeEnd1, launch))
                .setLinearHeadingInterpolation(intakeEnd1.getHeading(), launch.getHeading())
                .build();
        toIntakePrep2 = follower.pathBuilder()
                .addPath(new BezierLine(launch, intakePrep2))
                .setLinearHeadingInterpolation(launch.getHeading(), intakePrep2.getHeading())
                .build();

        intake2 = follower.pathBuilder()
                .addPath(new BezierLine(intakePrep2, intakeEnd2))
                .setLinearHeadingInterpolation(intakePrep2.getHeading(), intakeEnd2.getHeading())
                .build();

        intakeToLaunch2 = follower.pathBuilder()
                .addPath(new BezierLine(intakeEnd2, launch))
                .setLinearHeadingInterpolation(intakeEnd2.getHeading(), launch.getHeading())
                .build();

        toIntakePrep3 = follower.pathBuilder()
                .addPath(new BezierLine(launch, intakePrep3))
                .setLinearHeadingInterpolation(launch.getHeading(), intakePrep3.getHeading())
                .build();

        intake3 = follower.pathBuilder()
                .addPath(new BezierLine(intakePrep3, intakeEnd3))
                .setLinearHeadingInterpolation(intakePrep3.getHeading(), intakeEnd3.getHeading())
                .build();

        intakeToLaunch3 = follower.pathBuilder()
                .addPath(new BezierLine(intakeEnd3, launch))
                .setLinearHeadingInterpolation(intakeEnd3.getHeading(), launch.getHeading())
                .build();

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


            if (state != 24 && state != 25) {
                Outtake.outtakeSpeed();
                Outtake.outtakeUpdate(-1, false, 0);
            }
            Outtake.StaticVars.endPose = follower.getPose();
            Outtake.StaticVars.outtakePos = Drivetrain.outtakePosition();


            telemetry.addData("time delta", System.currentTimeMillis() - delayTimer);
            telemetry.addData("slot 1 state", Indexer.currentState1);
            telemetry.addData("robot x follower", follower.getPose().getX());
            telemetry.addData("outtake position", Outtake.StaticVars.outtakePos);
            telemetry.update();

            if (!follower.isBusy()) {
                switch (state) {
                    case -1:
                        if (System.currentTimeMillis() - delayTimer > 2000) {
                            state = 0;
                            delayTimer = System.currentTimeMillis();
                        }
                        break;
                    case 0:
                        // Move on if we're done :P
                        if (launch()) state = 2;

                        break;
                    case 2:
                        if (System.currentTimeMillis() - delayTimer > 500) {
                            state = 3;
                        }
                        break;
                    case 3:
                        //targetClicks = Outtake.setTarget(Outtake.setRotationPosition(0.42));
                        follower.followPath(toIntakePrep1, 1, true);
                        state = 4;
                        Intake.intakeGo();
                        break;
                    case 4:
                        follower.followPath(intake1, 0.8, true);
                        state = 5;
                        break;
                    case 5:
                        follower.followPath(intakeToLaunch1, 0.7, true);
                        state = 6;
                        delayTimer = System.currentTimeMillis();
                        break;
                    case 6:
                        if (System.currentTimeMillis() - delayTimer > 500) {
                            Intake.intakeStop();
                            state = 7;
                        }
                        break;
                    case 7:
                        if (launch()) state = 9;
                        break;
                    case 9:
                        if (System.currentTimeMillis() - delayTimer > 500) {
                            state = 10;
                        }
                        break;
                    case 10:
                        follower.followPath(launchToPark, 1, true);
                        state = 11;
                        break;
                    case 11:
                        Outtake.update(0, false);
                        Outtake.StaticVars.isBlue = true;
//                        Outtake.SPEED_CONST_FAR = 205;
                        break;
                }
            }
        }
    }
}
