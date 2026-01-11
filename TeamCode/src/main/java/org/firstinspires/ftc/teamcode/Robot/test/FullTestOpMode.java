package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;

@TeleOp(name = "test launch")
public class FullTestOpMode extends LinearOpMode {
    @Override
    public void runOpMode() {

        double targetClicks = 0;
        int launch = -1;
        long startTime = 0;
        final long WAIT = 300;

        Drivetrain.init(hardwareMap);
        Outtake.init(hardwareMap);
        Intake.init(hardwareMap);
        Indexer.init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            boolean X = gamepad1.xWasPressed();
            boolean A = gamepad1.aWasPressed();
            boolean intake = gamepad1.right_trigger > 0.5;

            Outtake.outtakeSpeed();
            Outtake.outtakeUpdate(launch);

            if (X) {
                launch *= -1;
            }

            //Indexer.update(launch > 0);


//            if ((A) && (launch < 0)) {
//                long currentTime = System.currentTimeMillis();
//                if (currentTime-startTime > WAIT) {
//                    startTime = Indexer.startLaunch(4);
//                }
//            }

            //intake balls
            if (intake) {
                Intake.intakeGo();
            } else {
                Intake.intakeStop();
            }

            telemetry.addData("left motor velocity", Outtake.testTelemetryMotor1());
            telemetry.addData("right motor velocity", Outtake.testTelemetryMotor2());
            telemetry.addData("launch", launch);
            telemetry.addData("right motor velocity", Outtake.testTelemetryMotor2());


            telemetry.update();
        }

    }
}
