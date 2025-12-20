package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;

@TeleOp(name="indexer/transfer")
public class IndexerTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        Indexer.init(hardwareMap);
        Transfer.init(hardwareMap);

        while (opModeIsActive()) {
////            double colorSensor0 = Indexer.slot0Values();
////            double hue0 = JavaUtil.colorToHue(colorSensor0.toColor());
//
////            if (180 >= hue0 && hue0 >= 150) {
////                Transfer.testSlot0Green();
////            } else if (240 >= hue0 && hue0 >= 225) {
////                Transfer.testSlot0Purple();
////            } else {
////                Transfer.saarang();
////            }
//            telemetry.addData("colorSensor0 hue", hue0);
//            telemetry.addData("colorSensor0 NormalizedRGBA", colorSensor0);
//            telemetry.addData("servo position", Transfer.slot0Position());
//            telemetry.update();

        }
    }

}
