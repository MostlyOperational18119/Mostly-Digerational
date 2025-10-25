package org.firstinspires.ftc.teamcode.teleop.outreach;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.teleop.Methods;

@TeleOp(name = "BigBot")
public class BigBot extends Methods {
    @Override
    public void runOpMode(){
        float turn, strafe, forwards;
        int launchAngle = 0;
        DcMotor motorFL = hardwareMap.get(DcMotor.class, "motorFL");
        DcMotor motorFR = hardwareMap.get(DcMotor.class, "motorFR");
        motorFR.setDirection(DcMotorSimple.Direction.REVERSE);
        DcMotor motorBL = hardwareMap.get(DcMotor.class, "motorBL");
        motorBL.setDirection(DcMotorSimple.Direction.REVERSE);
        DcMotor motorBR = hardwareMap.get(DcMotor.class, "motorBR");
        motorBR.setDirection(DcMotorSimple.Direction.REVERSE);

        DcMotor motorLauncher = hardwareMap.get(DcMotor.class, "motorLauncher");
        motorLauncher.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLauncher.setTargetPosition(0);
        motorLauncher.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Servo littleArm = hardwareMap.get(Servo.class, "littleArm");

        waitForStart();
        motorLauncher.setPower(0.5);
        while (opModeIsActive()) {
            turn = gamepad1.right_stick_x;
            strafe = gamepad1.left_stick_x;
            forwards = -gamepad1.left_stick_y;
            if(gamepad1.bWasPressed()){
                littleArm.setPosition(0.12);
            } else if (gamepad1.aWasPressed()) {
                littleArm.setPosition(0.07);
            }

            if(gamepad1.dpad_down) {
                launchAngle -= 5;
            } else if (gamepad1.dpad_up) {
                launchAngle += 5;
            }

            motorLauncher.setTargetPosition(launchAngle);

            motorFL.setPower(forwards + strafe + turn);
            motorFR.setPower(forwards - strafe - turn);
            motorBL.setPower(forwards - strafe + turn);
            motorBR.setPower(forwards + strafe - turn);
        }
    }
}