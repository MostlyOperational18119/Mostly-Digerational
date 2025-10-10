package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "alex")
public class whatever_the_hell_you_want extends Methods {
    @Override
    public void runOpMode(){
        float turn, strafe, forwards;
        DcMotor motorFL = hardwareMap.get(DcMotor.class, "motorFL");
        DcMotor motorFR = hardwareMap.get(DcMotor.class, "motorFR");
        DcMotor motorBL = hardwareMap.get(DcMotor.class, "motorBL");
        DcMotor motorBR = hardwareMap.get(DcMotor.class, "motorBR");

        waitForStart();
        while(opModeIsActive()) {
            turn = gamepad1.right_stick_x;
            strafe = gamepad1.left_stick_x;
            forwards = -gamepad1.left_stick_y;

            motorFL.setPower(forwards + strafe + turn);
            motorFR.setPower(forwards - strafe - turn);
            motorBL.setPower(forwards - strafe + turn);
            motorBR.setPower(forwards + strafe - turn);
        }
    }
}
