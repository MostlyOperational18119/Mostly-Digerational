package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;

@TeleOp(name = "color sensor")
public class ColorSensorTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        Indexer.init(hardwareMap);
        RevColorSensorV3 slot0 = hardwareMap.get(RevColorSensorV3.class, "index0");
        RevColorSensorV3 slot1 = hardwareMap.get(RevColorSensorV3.class, "index1");
        RevColorSensorV3 slot2 = hardwareMap.get(RevColorSensorV3.class, "index2");
        double hue0, hue1, hue2;

        waitForStart();

        while (opModeIsActive()) {


            NormalizedRGBA slot0Colors = slot0.getNormalizedColors();
            hue0 = JavaUtil.colorToHue(slot0Colors.toColor());
            NormalizedRGBA slot1Colors = slot1.getNormalizedColors();
            hue1 = JavaUtil.colorToHue(slot1Colors.toColor());
            NormalizedRGBA slot2Colors = slot2.getNormalizedColors();
            hue2 = JavaUtil.colorToHue(slot2Colors.toColor());

            telemetry.addData("slot0 hue", hue0);
            telemetry.addData("slot1 hue", hue1);
            telemetry.addData("slot2 hue", hue2);
            telemetry.update();

        }
    }
}
