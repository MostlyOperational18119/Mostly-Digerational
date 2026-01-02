package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;

@TeleOp(name="Autoaim test")
public class AutoAimTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        Outtake.init(hardwareMap);

        double x = 72;
        double y = 72;
        int height = 50;

        int buttonPressed = 0;

        while (opModeIsActive()) {
            boolean X = gamepad1.xWasPressed();
            boolean Y = gamepad1.yWasPressed();
            boolean A = gamepad1.aWasPressed();
            boolean upPad = gamepad1.dpad_up;
            boolean downPad = gamepad1.dpad_down;


            if (A) {
                buttonPressed = 1;
            } else if (X) {
                buttonPressed = 2;
            } else if (Y) {
                buttonPressed = 3;
            } else {
                buttonPressed = 0;
            }

            if (buttonPressed == 1) {
                if (upPad) {
                    height++;
                } else if (downPad) {
                    height--;
                }
            } else if (buttonPressed == 2) {
                if (upPad) {
                    x++;
                } else if (downPad) {
                    x--;
                }
            } else if (buttonPressed == 3) {
                if (upPad) {
                    y++;
                } else if (downPad) {
                    y--;
                }
            }

            telemetry.addData("Height:", height);
            telemetry.addData("x:", x);
            telemetry.addData("y:", y);
            Outtake.autoAimHoodPlusVelo(x, y);

            telemetry.update();
        }
    }

}
