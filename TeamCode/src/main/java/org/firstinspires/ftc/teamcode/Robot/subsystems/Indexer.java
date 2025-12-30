package org.firstinspires.ftc.teamcode.Robot.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.util.Log;

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
        currentState0 = States.IDLE;
        currentState1 = States.IDLE;
        currentState2 = States.IDLE;

        updateSlot0();
        updateSlot1();
        updateSlot2();
    }

    public static double slotColor0Red () {
        return slot0Sensor.red();
    }
    public static double slotColor0Green () {
        return slot0Sensor.green();
    }
    public static double slotColor0Blue () {
        return slot0Sensor.blue();
    }
    public static double slotColor1Red () {
        return slot1Sensor.red();
    }
    public static double slotColor1Green () {
        return slot1Sensor.green();
    }
    public static double slotColor1Blue () {
        return slot1Sensor.blue();
    }
    public static double slotColor2Red () {
        return slot2Sensor.red();
    }
    public static double slotColor2Green () {
        return slot2Sensor.green();
    }
    public static double slotColor2Blue () {
        return slot2Sensor.blue();
    }

    public static String slot0Test () {
        if (getColorSlot0(slot0Sensor.red(), slot0Sensor.green(), slot0Sensor.blue()) == 2) {
            return "green";
        } else if (getColorSlot0(slot0Sensor.red(), slot0Sensor.green(), slot0Sensor.blue()) ==1 ) {
            return "purple";
        } else {
            return "empty";
        }
    }

    public static String slot1Test () {
        if (getColorSlot1(slot1Sensor.red(), slot1Sensor.green(), slot1Sensor.blue()) == 2) {
            return "green";
        } else if (getColorSlot1(slot1Sensor.red(), slot1Sensor.green(), slot1Sensor.blue()) ==1 ) {
            return "purple";
        } else {
            return "empty";
        }
    }

    public static String slot2Test () {
        if (getColorSlot2(slot2Sensor.red(), slot2Sensor.green(), slot2Sensor.blue()) == 2) {
            return "green";
        } else if (getColorSlot2(slot2Sensor.red(), slot2Sensor.green(), slot2Sensor.blue()) ==1 ) {
            return "purple";
        } else {
            return "empty";
        }
    }
    private static int getColorSlot0 (double red, double green, double blue) {
        if (green/blue > 1.3 && green/blue < 1.5) {
            return 2;
        } else if (red/green > .9 && red/green < 1.2 && red > 85) {
            return 1;
        } else {
            return 0;
        }
    }

    private static int getColorSlot1 (double red, double green, double blue) {
        if (green/blue > 1.25 && green/blue < 1.5) {
            return 2;
        } else if (red/green > .9 && red/green < 1.2 && red > 85) {
            return 1;
        } else {
            return 0;
        }
    }

    private static int getColorSlot2 (double red, double green, double blue) {
        if (green/blue > 1.3 && green/blue < 1.5) {
            return 2;
        } else if (red/green > .9 && red/green < 1.2 && red > 85) {
            return 1;
        } else {
            return 0;
        }
    }


//    public static int[] slotColors() {
//        int[] slots = new int[3];
//
//        int
//
//
//
//        return slots;
//    }





    public static int[] slotColors() {

        //green = 2, purple = 1, empty = 0;
        double hue0, hue1, hue2;

        NormalizedRGBA slot0Colors = slot0Sensor.getNormalizedColors();
        hue0 = JavaUtil.colorToHue(slot0Colors.toColor());
        NormalizedRGBA slot1Colors = slot1Sensor.getNormalizedColors();
        hue1 = JavaUtil.colorToHue(slot1Colors.toColor());
        NormalizedRGBA slot2Colors = slot2Sensor.getNormalizedColors();
        hue2 = JavaUtil.colorToHue(slot2Colors.toColor());

//        Log.i("Indexer", String.format("Hue 0: %d", hue0));
//        Log.i("Indexer", String.format("Hue 1: %d", hue1));
//        Log.i("Indexer", String.format("Hue 2: %d", hue2));

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
        if (180 >= hue2 && hue2 >= 150) {
            slots[2] = 2;
        } else if (300 >= hue2 && hue2 >= 240) {
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
    public static void updateSlot2 () {
        switch (currentState2) {
            case LAUNCH:
                slot2.setPosition(UP_POS_2);
                break;
            case IDLE:
                slot2.setPosition(DOWN_POS_2);
                break;
            case EMPTY:
                slot2.setPosition(MID_POS_2);
                break;
        }
    }

    public static void updateSlot1 () {
        switch (currentState1) {
            case LAUNCH:
                slot1.setPosition(UP_POS_1);
                break;
            case IDLE:
                slot1.setPosition(DOWN_POS_1);
                break;
            case EMPTY:
                slot1.setPosition(MID_POS_1);
                break;
        }
    }


    //Transfer Part Duex import

    public static long startLaunch(int chamberNum) {
        int[] slots = slotColors();
        long startTime = System.currentTimeMillis();
        if (slots[0] == nextBall(slots, chamberNum, pattern)) {
            currentState0 = States.LAUNCH;
        } else if (slots[1] == nextBall(slots, chamberNum, pattern)) {
            currentState1 = States.LAUNCH;
        } else if (slots[2] == nextBall(slots, chamberNum, pattern)) {
            currentState2 = States.LAUNCH;
        }
        return startTime;
    }

    public static void update (boolean launch){
        int[] slots = slotColors();
        if (currentState0 == States.LAUNCH) {
            if (launch == true) {
                currentState0 = States.EMPTY;
            }
            currentState0 = States.IDLE;
        }
        if (currentState1 == States.LAUNCH) {
            if (launch == true) {
                currentState1 = States.EMPTY;
            }
            currentState1 = States.IDLE;
        }
        if (currentState2 == States.LAUNCH) {
            if (launch == true) {
                currentState2 = States.EMPTY;
            }
            currentState2 = States.IDLE;
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