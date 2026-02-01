package org.firstinspires.ftc.teamcode.Robot.opmode.auto;

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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Autonomous(name = "9BallBlueBackM3")
public class BlueBackAutoM3 extends LinearOpMode {
    Pose start = new Pose(56, 8, Math.toRadians(180));
    Pose launch = new Pose(56, 10, Math.toRadians(180));
    Pose intakePrep1 = new Pose(46, 34, Math.toRadians(180));
    Pose intakePrep2 = new Pose(46, 58, Math.toRadians(180));
    Pose intakePrep3 = new Pose(46, 84, Math.toRadians(180));
    Pose intakeEnd1 = new Pose(15, 34, Math.toRadians(180));
    Pose intakeEnd2 = new Pose(15, 58, Math.toRadians(180));
    Pose intakeEnd3 = new Pose(15, 84, Math.toRadians(180));
    Pose park = new Pose(36, 8, Math.toRadians(180));
    Follower follower;
    PathChain toIntakePrep1, intake1, intakeToLaunch1, toIntakePrep2, intake2, intakeToLaunch2, toIntakePrep3, intake3, intakeToLaunch3, launchToPark;
    int state = -1;
    int targetClicks = 0;
    long launchDelayTimer = 0;
    int launchCount = 0;

    @Override
    public void runOpMode() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(start);
        Outtake.StaticVars.isBlue = true;
        Outtake.init(hardwareMap);
        Intake.init(hardwareMap);
        Indexer.init(hardwareMap);
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
        launchDelayTimer= System.currentTimeMillis();


        while (opModeIsActive()) {
            follower.update();
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

            telemetry.addData("clicks", Drivetrain.outtakePosition());
            telemetry.addData("time delta", System.currentTimeMillis() - launchDelayTimer);
            telemetry.addData("slot 1 state", Indexer.currentState1);
            telemetry.addData("robot x follower", follower.getPose().getX());
            telemetry.update();

            if (!follower.isBusy()) {
                switch (state) {
                    case -1:
                        if (System.currentTimeMillis() - launchDelayTimer > 2000) {
                            state = 0;
                            launchDelayTimer = System.currentTimeMillis();
                        }
                        break;
                    case 0:
                        if (System.currentTimeMillis() - launchDelayTimer > 700 && Outtake.outtakeMotorLeft.getVelocity() >= Outtake.speed - 20) {
                            switch (launchCount) {
                                case 0:
                                    launchDelayTimer = Indexer.launch0();
                                    launchCount = 1;
                                    break;
                                case 1:
                                    launchDelayTimer = Indexer.launch2();
                                    launchCount = 2;
                                    break;
                                case 2:
                                    launchDelayTimer = Indexer.launch1();
                                    state = 1;
                                    launchCount = 0;
                                    break;
                            }
                        }
                        break;
                    case 1:
                        if (System.currentTimeMillis() - launchDelayTimer > 700 && Outtake.outtakeMotorLeft.getVelocity() >= Outtake.speed - 20) {
                            // Check and launch any remaining balls in the indexer
                            if (Indexer.slotColors()[0] != 0) {
                                launchDelayTimer = Indexer.launch0();
                                state = 1; // Stay in this state to check again
                            } else if (Indexer.slotColors()[2] != 0) {
                                launchDelayTimer = Indexer.launch2();
                                state = 1; // Stay in this state to check again
                            } else if (Indexer.slotColors()[1] != 0) {
                                launchDelayTimer = Indexer.launch1();
                                state = 1; // Stay in this state to check again
                            } else {
                                // All slots empty, move to next state
                                state = 2;
                            }
                        }
                        break;
                    case 2:
                        if (System.currentTimeMillis() - launchDelayTimer > 500) {
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
                        follower.followPath(intakeToLaunch1, 1, true);
                        state = 6;
                        launchDelayTimer = System.currentTimeMillis();
                        break;
                    case 6:
                        if (System.currentTimeMillis() - launchDelayTimer > 700) {
                            Intake.intakeStop();
                            state = 7;
                        }
                        break;
                    case 7:
                        if (System.currentTimeMillis() - launchDelayTimer > 700 && Outtake.outtakeMotorLeft.getVelocity() >= Outtake.speed - 20) {
                            switch (launchCount) {
                                case 0:
                                    launchDelayTimer = Indexer.launch0();
                                    launchCount = 1;
                                    break;
                                case 1:
                                    launchDelayTimer = Indexer.launch2();
                                    launchCount = 2;
                                    break;
                                case 2:
                                    launchDelayTimer = Indexer.launch1();
                                    state = 8;
                                    launchCount = 0;
                                    break;
                            }
                        }
                        break;
                    case 8:
                        if (System.currentTimeMillis() - launchDelayTimer > 700 && Outtake.outtakeMotorLeft.getVelocity() >= Outtake.speed - 20) {
                            // Check and launch any remaining balls in the indexer
                            if (Indexer.slotColors()[0] != 0) {
                                launchDelayTimer = Indexer.launch0();
                                state = 8; // Stay in this state to check again
                            } else if (Indexer.slotColors()[2] != 0) {
                                launchDelayTimer = Indexer.launch2();
                                state = 8; // Stay in this state to check again
                            } else if (Indexer.slotColors()[1] != 0) {
                                launchDelayTimer = Indexer.launch1();
                                state = 8; // Stay in this state to check again
                            } else {
                                // All slots empty, move to next state
                                state = 9;
                            }
                        }
                        break;
                    case 9:
                        if (System.currentTimeMillis() - launchDelayTimer > 500) {
                            state = 10;
                        }
                        break;
                    case 10:
                        follower.followPath(toIntakePrep2, 1, true);
                        state = 11;
                        Intake.intakeGo();
                        break;
                    case 11:
                        follower.followPath(intake2, 0.8, true);
                        state = 12;
                        break;
                    case 12:
                        follower.followPath(intakeToLaunch2, 0.9, true);
                        state = 13;
                        launchDelayTimer = System.currentTimeMillis();
                        break;
                    case 13:
                        if (System.currentTimeMillis() - launchDelayTimer > 500) {
                            Intake.intakeStop();
                            state = 14;
                        }
                        break;
                    case 14:
                        if (System.currentTimeMillis() - launchDelayTimer > 700 && Outtake.outtakeMotorLeft.getVelocity() >= Outtake.speed - 20) {
                            switch (launchCount) {
                                case 0:
                                    launchDelayTimer = Indexer.launch0();
                                    launchCount = 1;
                                    break;
                                case 1:
                                    launchDelayTimer = Indexer.launch2();
                                    launchCount = 2;
                                    break;
                                case 2:
                                    launchDelayTimer = Indexer.launch1();
                                    state = 15;
                                    launchCount = 0;
                                    break;
                            }
                        }
                        break;
                    case 15:
                        if (System.currentTimeMillis() - launchDelayTimer > 700 && Outtake.outtakeMotorLeft.getVelocity() >= Outtake.speed - 20) {
                            // Check and launch any remaining balls in the indexer
                            if (Indexer.slotColors()[0] != 0) {
                                launchDelayTimer = Indexer.launch0();
                                state = 15; // Stay in this state to check again
                            } else if (Indexer.slotColors()[2] != 0) {
                                launchDelayTimer = Indexer.launch2();
                                state = 15; // Stay in this state to check again
                            } else if (Indexer.slotColors()[1] != 0) {
                                launchDelayTimer = Indexer.launch1();
                                state = 15; // Stay in this state to check again
                            } else {
                                // All slots empty, move to next state
                                state = 16;
                            }
                        }
                        break;
                    case 16:
                        if (System.currentTimeMillis() - launchDelayTimer > 500) {
                            state = 17;
                        }
                        break;
                    case 17:
//                        follower.followPath(launchToPark, 0.6, true);
                        Intake.intakeStop();
                        state = 23;
                        break;
//                    case 18:
//                        Outtake.StaticVars.isBlue = true;
//                        Outtake.StaticVars.endPose = follower.getPose();
//                        state = 19;
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
//                        state = 20;
//                        launchDelayTimer = System.currentTimeMillis();
//                        break;
//                    case 20:
//                        if (System.currentTimeMillis() - launchDelayTimer > 500) {
//                            Intake.intakeStop();
//                            state = 21;
//                        }
//                        break;
//                    case 21:
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
//                                    state = 19;
//                                    launchCount = 0;
//                                    break;
//                            }
//                        }
//                        break;
//                    case 22:
//                        if (System.currentTimeMillis() - launchDelayTimer > 1000 && Outtake.outtakeMotorLeft.getVelocity() >= Outtake.speed - 50) {
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
//                                        state = 23;
//                                        break;
//                                }
//                            }
//                        }
//                        break;
//                    case 23:
//                        follower.followPath(launchToPark, 1, true);
//                        state = 60;
//                        break;
                    case 23:
                        follower.followPath(launchToPark, 1, true);
                        state = 24;
                        break;
                    case 24:
                        Outtake.update(0, false);
                        Outtake.StaticVars.isBlue = true;
                        break;
                }
            }
        }
    }
}
