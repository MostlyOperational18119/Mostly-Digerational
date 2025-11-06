package org.firstinspires.ftc.teamcode.everything;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "drive forward")
public class auto extends Methods {
    @Override
    public void runOpMode() {
        initialize();
        waitForStart();
        motorBL.setPower(0.5);
        motorBR.setPower(0.5);
        motorFL.setPower(0.5);
        motorFR.setPower(0.5);
        sleep(500);
        motorBL.setPower(0);
        motorBR.setPower(0);
        motorFL.setPower(0);
        motorFR.setPower(0);
    }
}
