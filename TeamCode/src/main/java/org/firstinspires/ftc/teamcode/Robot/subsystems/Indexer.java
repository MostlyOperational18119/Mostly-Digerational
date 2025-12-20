package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

public class Indexer {

    private NormalizedColorSensor slot0Sensor, slot1Sensor, slot2Sensor;

    public void init (HardwareMap hwMap) {
        slot0Sensor = hwMap.get(NormalizedColorSensor.class, "slot_0_sensor");
        slot1Sensor = hwMap.get(NormalizedColorSensor.class, "slot_1_sensor");
        slot2Sensor = hwMap.get(NormalizedColorSensor.class, "slot_2_sensor");
    }

    public int[] slotColors () {

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
        if (149 >= hue0 && hue0 >= 90) {
            index [0] = 2;
        } else if (300 >= hue0 && hue0 >= 270) {
            index[0] = 1;
        }

        //1 slot
        if (149 >= hue1 && hue1 >= 90) {
            index [1] = 2;
        } else if (300 >= hue1 && hue1 >= 270) {
            index[1] = 1;
        }

        //2 slot
        if (149 >= hue2 && hue2 >= 90) {
            index [2] = 2;
        } else if (300 >= hue2 && hue2 >= 270) {
            index[2] = 1;
        }

        return index;
    }


}
