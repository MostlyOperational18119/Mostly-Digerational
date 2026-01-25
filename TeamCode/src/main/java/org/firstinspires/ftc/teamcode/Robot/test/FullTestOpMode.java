package org.firstinspires.ftc.teamcode.Robot.test;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.Robot.pedroPathing.Constants;

@TeleOp(name = "full test")
public class FullTestOpMode extends LinearOpMode {
    @Override
    public void runOpMode() {

        int chamberNum = 0; //limelight sets number
        int[] pattern = new int[3]; //limelight sets pattern
        int launch = -1;
        long startTime = 0;
        final long WAIT = 300;

        Drivetrain.init(hardwareMap);
        Outtake.init(hardwareMap);
        Intake.init(hardwareMap);
        Indexer.init(hardwareMap);

        Pose start = new Pose(72,72, Math.toRadians(90));
        Follower follower;
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(start);

        waitForStart();

        while (opModeIsActive()) {
            //aimbot + field odometry
            follower.update();
            Outtake.robotY = follower.getPose().getY();
            Outtake.robotX = follower.getPose().getX();
            Outtake.robotOrientation = follower.getHeading();
            
            float y = -gamepad1.left_stick_y;
            float x = gamepad1.left_stick_x;
            float rx = gamepad1.right_stick_x;
            boolean X = gamepad1.xWasPressed();
            boolean A = gamepad1.aWasPressed();
            boolean intake = gamepad1.right_trigger > 0.5;

            //drivetrain
            //Drivetrain.drive(y, x, rx);

            //outtake
            Outtake.outtakeSpeed();
//            Outtake.outtakeUpdate(launch);

            //indexer
            Indexer.slotColors();

            //aim at goal/chamber
            if (X) {
                launch *= -1;
            }


            if ((A) && (launch < 0)) {
                long currentTime = System.currentTimeMillis();
                if (currentTime-startTime > WAIT) {
                    startTime = Indexer.startLaunch(0);
                    Indexer.updateSlot0();
                    Indexer.updateSlot1();
                    Indexer.updateSlot2();
                }
            }

            //intake balls
            if (intake) {
                Intake.intakeGo();
            } else {
                Intake.intakeStop();
            }


//            telemetry.addData("angle target: ", (Outtake.pointAtGoal() - 2816)/68.555);
            telemetry.addData("robot X", Outtake.robotX);
            telemetry.addData("robot Y", Outtake.robotY);
            telemetry.addData("robot heading", Outtake.robotOrientation);
            telemetry.addData("left motor velocity", Outtake.testTelemetryMotor1());
            telemetry.addData("right motor velocity", Outtake.testTelemetryMotor2());
            telemetry.addData("launch", launch);
            telemetry.addData("slot0 state", Indexer.currentState0);
            telemetry.addData("slot1 state", Indexer.currentState1);
            telemetry.addData("slot2 state", Indexer.currentState2);
            telemetry.addData("Slot0 (array): ", Indexer.slotColors()[0]);
            telemetry.addData("Slot1 (array): ", Indexer.slotColors()[1]);
            telemetry.addData("Slot2 (array): ", Indexer.slotColors()[2]);
            telemetry.addData("current outtake position", Drivetrain.outtakePosition());

            telemetry.update();
        }

    }
}
