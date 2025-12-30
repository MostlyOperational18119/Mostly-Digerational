package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

public class Indexer {
    public static Servo slot0, slot1, slot2;
    public static double UP_POS_0 = 0.12, DOWN_POS_0 = 0.48, MID_POS_0 = 0.34;
    public static double UP_POS_1 = 0.12, DOWN_POS_1 = 0.48, MID_POS_1 = 0.34;
    public static double UP_POS_2 = 0.88, DOWN_POS_2 = 0.48, MID_POS_2 = 0.66;

    public enum States {
        LAUNCH,
        IDLE,
        EMPTY
    }

    public static int[] pattern = new int[]{1, 2, 2}; // change to new int[3];
    public static States currentState0, currentState1, currentState2;
    static int currentBall; //ball being launched (0, 1, or 2)
    private static RevColorSensorV3 slot0Sensor, slot1Sensor, slot2Sensor;

    public static void init(HardwareMap hwMap) {
        slot0Sensor = hwMap.get(RevColorSensorV3.class, "index0");
        slot1Sensor = hwMap.get(RevColorSensorV3.class, "index1");
        slot2Sensor = hwMap.get(RevColorSensorV3.class, "index2");
        slot0 = hwMap.get(Servo.class, "transfer0");
        slot1 = hwMap.get(Servo.class, "transfer1");
        slot2 = hwMap.get(Servo.class, "transfer2");

        //initialize to start position
        slot0.setPosition(DOWN_POS_0);
        slot1.setPosition(DOWN_POS_1);
        slot2.setPosition(DOWN_POS_2);
    }

    public static int[] slotColors() {

        //green = 2, purple = 1, empty = 0;
        double hue0, hue1, hue2;

        NormalizedRGBA slot0Colors = slot0Sensor.getNormalizedColors();
        hue0 = JavaUtil.colorToHue(slot0Colors.toColor());
        NormalizedRGBA slot1Colors = slot1Sensor.getNormalizedColors();
        hue1 = JavaUtil.colorToHue(slot1Colors.toColor());
        NormalizedRGBA slot2Colors = slot2Sensor.getNormalizedColors();
        hue2 = JavaUtil.colorToHue(slot2Colors.toColor());

        int[] slots = new int[3];

        //0 slot
        if (180 >= hue0 && hue0 >= 150) {
            slots[0] = 2;
        } else if (300 >= hue0 && hue0 >= 240) {
            slots[0] = 1;
        } else {
            slots[0] = 0;
        }

        //1 slot
        if (180 >= hue1 && hue1 >= 150) {
            slots[1] = 2;
        } else if (300 >= hue1 && hue1 >= 240) {
            slots[1] = 1;
        } else {
            slots[1] = 0;
        }

        //2 slot
        if (180 >= hue1 && hue1 >= 150) {
            slots[2] = 2;
        } else if (300 >= hue1 && hue1 >= 240) {
            slots[2] = 1;
        } else {
            slots[2] = 0;
        }

        return slots;
    }

    public static void updateSlot0 () {
        switch (currentState0) {
            case LAUNCH:
                slot0.setPosition(UP_POS_0);
                break;
            case IDLE:
                slot0.setPosition(DOWN_POS_0);
                break;
            case EMPTY:
                slot0.setPosition(MID_POS_0);
                break;
        }
    }

    //Transfer Part Duex import
    public static void startLaunch0() {
        currentState0 = States.LAUNCH;
    }
    public static void startLaunch1() {
        currentState1 = States.LAUNCH;
    }
    public static void startLaunch2() {
        currentState2 = States.LAUNCH;
    }

    public static void update(int chamberNum, boolean launch) {
        int[] slots = slotColors();
        long startTime = System.currentTimeMillis();
        if (slots[0] == nextBall(slots, chamberNum, pattern)) {
            currentState0 = States.LAUNCH;
        } else if (slots[1] == nextBall(slots, chamberNum, pattern)) {
            currentState1 = States.LAUNCH;
        } else if (slots[2] == nextBall(slots, chamberNum, pattern)) {
            currentState2 = States.LAUNCH;
        }
    }

    public static int nextBall(int[] slots, int chamberNum, int[] pattern) {
        int patternIndex = chamberNum % 3;
        int desiredColor = pattern[patternIndex];
        int emptyCount = 0;

        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == desiredColor) {
                return i;
            }
        }
        return -1;
    }
}