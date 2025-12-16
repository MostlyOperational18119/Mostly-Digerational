package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class Indexer {

    public ColorRangeSensor slot1Sensor, slot2Sensor, slot3Sensor;

    public void init (HardwareMap hwMap) {
        slot1Sensor = hwMap.get(ColorRangeSensor.class, "slot_1_sensor");
        slot2Sensor = hwMap.get(ColorRangeSensor.class, "slot_2_sensor");
        slot3Sensor = hwMap.get(ColorRangeSensor.class, "slot_3_sensor");
    }

//    public int[] slotColors () {
//        NormalizedRGBA slot1Colors = slot1Sensor.getNormalizedColors();
//        NormalizedRGBA slot2Colors = slot2Sensor.getNormalizedColors();
//        NormalizedRGBA slot3Colors = slot3Sensor.getNormalizedColors();
//
//        float red1, green1, blue1, red2, green2, blue2, red3, green3, blue3;
//
//        red1 = slot1Colors.red / slot1Colors.alpha;
//        blue1 = slot1Colors.blue / slot1Colors.alpha;
//        green1 = slot1Colors.green / slot1Colors.alpha;
//
//        red2 = slot2Colors.red / slot2Colors.alpha;
//        blue2 = slot2Colors.blue / slot2Colors.alpha;
//        green2 = slot2Colors.green / slot2Colors.alpha;
//
//        red3 = slot3Colors.red / slot3Colors.alpha;
//        blue3 = slot3Colors.blue / slot3Colors.alpha;
//        green3 = slot3Colors.green / slot3Colors.alpha;
//
//
//    }



}
