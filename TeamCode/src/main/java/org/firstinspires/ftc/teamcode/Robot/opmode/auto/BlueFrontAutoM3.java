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

@Autonomous(name = "BlueFrontM3")
public class BlueFrontAutoM3 extends LinearOpMode {
    Pose start = new Pose(32, 134.5, Math.toRadians(180));
    Pose readObelisk = new Pose(48, 120, Math.toRadians(45));
    Pose launch = new Pose(48, 96, Math.toRadians(180));
    Pose intakePrep1 = new Pose(40, 83, Math.toRadians(180));
    //    Pose intakePrep2 = new Pose(18, 83, Math.toRadians(180));
//    Pose intakePrep3 = new Pose(43, 84, Math.toRadians(180));
    Pose intakeEnd1 = new Pose(22, 83, Math.toRadians(180));
    //    Pose intakeEnd2 = new Pose(12, 58, Math.toRadians(180));
//    Pose intakeEnd3 = new Pose(14, 84, Math.toRadians(180));
    Pose park = new Pose(18, 100, Math.toRadians(180));
    Follower follower;
    PathChain startToObelisk, obeliskToLaunch, toIntakePrep1, intake1, intakeToLaunch1, toIntakePrep2, intake2, intakeToLaunch2, toIntakePrep3, intake3, intakeToLaunch3, launchToPark;
    int state = -2;
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
            return Indexer.startLaunch(numBalls);
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
            Log.e("RedBackAuto6BallM3", String.format("No limelight, error was: %s", e.getLocalizedMessage()));
        }

        startToObelisk = follower.pathBuilder()
                .addPath(new BezierLine(start, readObelisk))
                .setLinearHeadingInterpolation(start.getHeading(), readObelisk.getHeading())
                .build();

        obeliskToLaunch = follower.pathBuilder()
                .addPath(new BezierLine(readObelisk, launch))
                .setLinearHeadingInterpolation(readObelisk.getHeading(), launch.getHeading())
                .build();

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
//        toIntakePrep2 = follower.pathBuilder()
//                .addPath(new BezierLine(launch, intakePrep2))
//                .setLinearHeadingInterpolation(launch.getHeading(), intakePrep2.getHeading())
//                .build();
//
//        intake2 = follower.pathBuilder()
//                .addPath(new BezierLine(intakePrep2, intakeEnd2))
//                .setLinearHeadingInterpolation(intakePrep2.getHeading(), intakeEnd2.getHeading())
//                .build();
//
//        intakeToLaunch2 = follower.pathBuilder()
//                .addPath(new BezierLine(intakeEnd2, launch))
//                .setLinearHeadingInterpolation(intakeEnd2.getHeading(), launch.getHeading())
//                .build();
//
//        toIntakePrep3 = follower.pathBuilder()
//                .addPath(new BezierLine(launch, intakePrep3))
//                .setLinearHeadingInterpolation(launch.getHeading(), intakePrep3.getHeading())
//                .build();
//
//        intake3 = follower.pathBuilder()
//                .addPath(new BezierLine(intakePrep3, intakeEnd3))
//                .setLinearHeadingInterpolation(intakePrep3.getHeading(), intakeEnd3.getHeading())
//                .build();
//
//        intakeToLaunch3 = follower.pathBuilder()
//                .addPath(new BezierLine(intakeEnd3, launch))
//                .setLinearHeadingInterpolation(intakeEnd3.getHeading(), launch.getHeading())
//                .build();

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

            if (state != 13) {
                Outtake.outtakeSpeed();
                Outtake.outtakeUpdate(-1, false, 0);
            }
            Outtake.StaticVars.endPose = follower.getPose();
            Outtake.StaticVars.outtakePos = Drivetrain.outtakePosition();


            telemetry.addData("clicks", Drivetrain.outtakePosition());
            telemetry.addData("time delta", System.currentTimeMillis() - delayTimer);
            telemetry.addData("slot 1 state", Indexer.currentState1);
            telemetry.addData("robot x follower", follower.getPose().getX());
            telemetry.addData("flywheel speed target", Outtake.speed);
            telemetry.addData("flywheel speed", Outtake.outtakeMotorLeft.getVelocity());
            telemetry.update();

            if (!follower.isBusy()) {
                switch (state) {
                    case -2:
                        if (System.currentTimeMillis() - delayTimer > 500) {
                            follower.followPath(startToObelisk, 1.0, true);
                            state = -1;
                        }
                        break;
                    case -1:
                        if (!limelightAvailable || limelight.getPattern().isPresent()) state = 0;
//                        Outtake.SPEED_CONST_CLOSE = Outtake.SPEED_CONST_CLOSE / 1.1;
                        break;
                    case 0 :
                        follower.followPath(obeliskToLaunch, 0.8, true);
                        state = 1;
                        break;
                    case 1:
                        if (launch()) state = 3;
                        break;
                    case 3:
                        if (System.currentTimeMillis() - delayTimer > 1000) {
                            state = 4;
                        }
                        break;
                    case 4:
                        //targetClicks = Outtake.setTarget(Outtake.setRotationPosition(0.42));
                        follower.followPath(toIntakePrep1, 0.7, true);
                        state = 5;
                        Intake.intakeGo();
                        break;
                    case 5:
                        follower.followPath(intake1, 0.6, true);
                        state = 6;
                        break;
                    case 6:
                        follower.followPath(intakeToLaunch1, 1, true);
                        state = 7;
                        delayTimer = System.currentTimeMillis();
                        break;
                    case 7:
                        if (System.currentTimeMillis() - delayTimer > 1000) {
                            state = 8;
                            delayTimer = System.currentTimeMillis();
//                            Outtake.SPEED_CONST_CLOSE = Outtake.SPEED_CONST_CLOSE / 1.1;
                        }
                        break;
                    case 8:
                        if (launch()) state = 10;
                        break;
                    case 10:
                        if (System.currentTimeMillis() - delayTimer > 1000) {
                            state = 11;
                        }
                        break;
                    case 11:
                        follower.followPath(launchToPark, 0.6, true);
                        state = 12;
                        Intake.intakeGo();
                        break;
                    case 12:
                        Outtake.StaticVars.isBlue = true;
//                        Outtake.SPEED_CONST_CLOSE = Outtake.SPEED_CONST_CLOSE * 1.1;
                        state = 13;
                        break;
                    case 13:
                        Outtake.update(0, false);
                        break;
//                    case 12:
//                        follower.followPath(intake2, 0.8, true);
//                        state = 13;
//                        break;
//                    case 13:
//                        follower.followPath(intakeToLaunch2, 1, true);
//                        Intake.intakeStop();
//                        state = 14;
//                        launchDelayTimer = System.currentTimeMillis();
//                        break;
//                    case 14:
//                        if (System.currentTimeMillis() - launchDelayTimer > 700 && Outtake.outtakeMotorLeft.getVelocity() >= Outtake.speed - 100) {
//                            switch (launchCount) {
//                                case 0:
//                                    launchDelayTimer = Indexer.launch0();
//                                    launchCount = 1;
//                                    break;
//                                case 1:
//                                    launchDelayTimer = Indexer.launch1();
//                                    launchCount = 2;
//                                    break;
//                                case 2:
//                                    launchDelayTimer = Indexer.launch2();
//                                    state = 14;
//                                    launchCount = 0;
//                                    break;
//                            }
//                        }
//                        break;
//                    case 15:
//                        if (System.currentTimeMillis() - launchDelayTimer > 700 && Outtake.outtakeMotorLeft.getVelocity() >= Outtake.speed - 50) {
//                            for (int i = 0; i < 3; i++) {
//                                switch (i) {
//                                    case 0:
//                                        if (Indexer.slotColors()[i] != 0) {
//                                            launchDelayTimer = Indexer.launch0();
//                                        }
//                                        break;
//                                    case 1:
//                                        if (Indexer.slotColors()[i] != 0) {
//                                            launchDelayTimer = Indexer.launch1();
//                                        }
//                                        break;
//                                    case 2:
//                                        if (Indexer.slotColors()[i] != 0) {
//                                            launchDelayTimer = Indexer.launch2();
//                                        }
//                                        state = 16;
//                                        break;
//                                }
//                            }
//                        }
//                        break;
//                    case 16:
//                        if (System.currentTimeMillis() - launchDelayTimer > 1000) {
//                            state = 17;
//                        }
//                        break;
//                    case 17:
//                        follower.followPath(toIntakePrep3, 1, true);
//                        state = 18;
//                        Intake.intakeGo();
//                        break;
//                    case 18:
//                        follower.followPath(intake3, 0.8, true);
//                        state = 19;
//                        break;
//                    case 19:
//                        follower.followPath(intakeToLaunch3, 1, true);
//                        Intake.intakeStop();
//                        state = 20;
//                        launchDelayTimer = System.currentTimeMillis();
//                        break;
//                    case 20:
//                        if (System.currentTimeMillis() - launchDelayTimer > 1000 && Outtake.outtakeMotorLeft.getVelocity() >= Outtake.speed - 100) {
//                            switch (launchCount) {
//                                case 0:
//                                    launchDelayTimer = Indexer.launch0();
//                                    launchCount = 1;
//                                    break;
//                                case 1:
//                                    launchDelayTimer = Indexer.launch1();
//                                    launchCount = 2;
//                                    break;
//                                case 2:
//                                    launchDelayTimer = Indexer.launch2();
//                                    state = 21;
//                                    launchCount = 0;
//                                    break;
//                            }
//                        }
//                        break;
//                    case 21:
//                        if (System.currentTimeMillis() - launchDelayTimer > 700 && Outtake.outtakeMotorLeft.getVelocity() >= Outtake.speed - 50) {
//                            for (int i = 0; i < 3; i++) {
//                                switch (i) {
//                                    case 0:
//                                        if (Indexer.slotColors()[i] != 0) {
//                                            launchDelayTimer = Indexer.launch0();
//                                        }
//                                        break;
//                                    case 1:
//                                        if (Indexer.slotColors()[i] != 0) {
//                                            launchDelayTimer = Indexer.launch1();
//                                        }
//                                        break;
//                                    case 2:
//                                        if (Indexer.slotColors()[i] != 0) {
//                                            launchDelayTimer = Indexer.launch2();
//                                        }
//                                        state = 22;
//                                        break;
//                                }
//                            }
//                        }
//                        break;
//                    case 22:
//                        follower.followPath(launchToPark, 1, true);
//                        state = 23;
//                        break;
//                    case 23:
//                        Outtake.StaticVars.isBlue = true;
//                        Outtake.StaticVars.endPose = follower.getPose();
//                        state = 24;
//                        break;
                }
            }
        }
    }
}
