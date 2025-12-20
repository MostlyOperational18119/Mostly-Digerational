package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

public class Indexer {

    private static RevColorSensorV3 slot0Sensor, slot1Sensor, slot2Sensor;

    public static void init (HardwareMap hwMap) {
        slot0Sensor = hwMap.get(RevColorSensorV3.class, "slot0sensor");
        slot1Sensor = hwMap.get(RevColorSensorV3.class, "slot1sensor");
        slot2Sensor = hwMap.get(RevColorSensorV3.class, "slot2sensor");
    }

    public static NormalizedRGBA colorSensorTest() {
        return slot0Sensor.getNormalizedColors();
    }

    public static int[] slotColors () {

        //green = 2, purple = 1, empty = 0;
        double hue0, hue1, hue2;

        NormalizedRGBA slot0Colors = slot0Sensor.getNormalizedColors();
        hue0 = JavaUtil.colorToHue(slot0Colors.toColor());
        NormalizedRGBA slot1Colors = slot1Sensor.getNormalizedColors();
        hue1 = JavaUtil.colorToHue(slot1Colors.toColor());
        NormalizedRGBA slot2Colors = slot2Sensor.getNormalizedColors();
        hue2 = JavaUtil.colorToHue(slot2Colors.toColor());


        int[]  index = new int[3];


        //0 slot
        if (180 >= hue0 && hue0 >= 150) {
            index [0] = 2;
        } else if (240 >= hue0 && hue0 >= 225) {
            index[0] = 1;
        }

        //1 slot
        if (180 >= hue1 && hue1 >= 150) {
            index [1] = 2;
        } else if (240 >= hue1 && hue1 >= 225) {
            index[1] = 1;
        }

        //2 slot
        if (180 >= hue2 && hue2 >= 150) {
            index [2] = 2;
        } else if (240 >= hue2 && hue2 >= 225) {
            index[2] = 1;
        }

        return index;
    }


}
