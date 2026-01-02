package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;

@TeleOp(name = "new autoaim")
public class AutoAimTest2 extends LinearOpMode {
    @Override
    public void runOpMode(){

        Drivetrain.init(hardwareMap);
        Outtake.init(hardwareMap);
        OuttakeDemo.init(hardwareMap);

        //double launcherYawRotation = 0;


        waitForStart();

        while (opModeIsActive()) {
            Outtake.update(OuttakeDemo.setTarget(OuttakeDemo.nicksLittleHelper()));

            telemetry.addData("current position", Drivetrain.outtakePosition());
            telemetry.addData("target position", OuttakeDemo.setTarget(OuttakeDemo.nicksLittleHelper()));
            telemetry.addData("target setRotation", OuttakeDemo.setRotationPosition(OuttakeDemo.nicksLittleHelper()));
            telemetry.addData("target angle/clicks", OuttakeDemo.nicksLittleHelper());
            telemetry.addData("target angle", OuttakeDemo.nicksLittleTelemetry1());

            telemetry.update();

            //Outtake.update(Outtake.setRotationPosition(OuttakeDemo.nicksLittleHelper()));
        }
    }
}
