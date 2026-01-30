package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;

@TeleOp(name = "new autoaim")
public class AutoAimTest2 extends LinearOpMode {
    @Override
    public void runOpMode(){

        double targetClicks = 0;

        Drivetrain.init(hardwareMap);
        Outtake.init(hardwareMap);


        //double launcherYawRotation = 0;


        waitForStart();

        while (opModeIsActive()) {
//            targetClicks = Outtake.pointAtGoal();
//            Outtake.update(Outtake.setTarget(targetClicks));

            telemetry.addData("current position", Drivetrain.outtakePosition());
            //telemetry.addData("target position", Outtake.setTarget(Outtake.pointAtGoal()));
            //telemetry.addData("target setRotation", Outtake.setRotationPosition(Outtake.pointAtGoal()));
            telemetry.addData("target angle/clicks", Outtake.pointAtGoal());
            telemetry.addData("target angle", (targetClicks - 2816)/68.555);

            telemetry.update();
        }
    }
}
