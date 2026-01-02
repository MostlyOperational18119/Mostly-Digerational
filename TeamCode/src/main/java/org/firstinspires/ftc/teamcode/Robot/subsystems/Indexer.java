package org.firstinspires.ftc.teamcode.Robot.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.util.Log;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import java.util.Arrays;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

public class Indexer {
    public static Servo slot0, slot1, slot2;
    public static double UP_POS_0 = 0.12, DOWN_POS_0 = 0.48, MID_POS_0 = 0.34;
    public static double UP_POS_1 = 0.12, DOWN_POS_1 = 0.48, MID_POS_1 = 0.34;
    public static double UP_POS_2 = 0.88, DOWN_POS_2 = 0.48, MID_POS_2 = 0.66;

    public enum States {
        LAUNCH,
        IDLE
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


    public static long launch0() {
        currentState0 = States.LAUNCH;
        long startTime = System.currentTimeMillis();


        return startTime;
    }

    public static long launch1() {
        currentState1 = States.LAUNCH;
        long startTime = System.currentTimeMillis();


        return startTime;
    }

    public static long launch2() {
        currentState2 = States.LAUNCH;
        long startTime = System.currentTimeMillis();


        return startTime;
    }

    public static void down0() {
        currentState0 = States.IDLE;
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
        if (getColorSlot(slot0Sensor.red(), slot0Sensor.green(), slot0Sensor.blue()) == 2) {
            return "green";
        } else if (getColorSlot(slot0Sensor.red(), slot0Sensor.green(), slot0Sensor.blue()) ==1 ) {
            return "purple";
        } else {
            return "empty";
        }
    }

    public static String slot1Test() {
        if (getColorSlot(slot1Sensor.red(), slot1Sensor.green(), slot1Sensor.blue()) == 2) {
            return "green";
        } else if (getColorSlot(slot1Sensor.red(), slot1Sensor.green(), slot1Sensor.blue()) ==1 ) {
            return "purple";
        } else {
            return "empty";
        }
    }

    public static String slot2Test() {
        if (getColorSlot(slot2Sensor.red(), slot2Sensor.green(), slot2Sensor.blue()) == 2) {
            return "green";
        } else if (getColorSlot(slot2Sensor.red(), slot2Sensor.green(), slot2Sensor.blue()) ==1 ) {
            return "purple";
        } else {
            return "empty";
        }
    }
    private static int getColorSlot (double red, double green, double blue) {
        if (green/blue > 1.3 && green/blue < 1.6 && green > 90) {
            return 2;
        } else if (red/green > .9 && red/green < 1.2 && red > 85) {
            return 1;
        } else {
            return 0;
        }
    }

//    private static int getColorSlot1 (double red, double green, double blue) {
//        if (green/blue > 1.3 && green/blue < 1.6 && green > 65) {
//            return 2;
//        } else if (red/green > .9 && red/green < 1.2 && blue > 45) {
//            return 1;
//        } else {
//            return 0;
//        }
//    }
//
//    private static int getColorSlot2 (double red, double green, double blue) {
//        if (green/blue > 1.3 && green/blue < 1.6) {
//            return 2;
//        } else if (red/green > .9 && red/green < 1.2 && red > 100) {
//            return 1;
//        } else {
//            return 0;
//        }
//    }


    public static int[] slotColors() {
        int[] slots = new int[3];

        slots[0] = getColorSlot(slot0Sensor.red(), slot0Sensor.green(), slot0Sensor.blue());
        slots[1] = getColorSlot(slot1Sensor.red(), slot1Sensor.green(), slot1Sensor.blue());
        slots[2] = getColorSlot(slot2Sensor.red(), slot2Sensor.green(), slot2Sensor.blue());



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
        }
    }

    //Transfer Part Duex import

    public static long startLaunch(int chamberNum) {
        int[] slots = Arrays.copyOf(slotColors(), 3);
        long startTime = System.currentTimeMillis();
        if (slots[0] == nextBall(chamberNum, pattern)) {
            currentState0 = States.LAUNCH;
        } else if (slots[1] == nextBall(chamberNum, pattern)) {
            currentState1 = States.LAUNCH;
        } else if (slots[2] == nextBall(chamberNum, pattern)) {
            currentState2 = States.LAUNCH;
        }
        return startTime;
    }

    public static void update (boolean launch){
        if (currentState0 == States.LAUNCH || getColorSlot(slot0Sensor.red(), slot0Sensor.green(), slot0Sensor.blue()) == 0) {
            if (launch) {
                slot0.setPosition(MID_POS_0);
            }
            currentState0 = States.IDLE;
        }
        if (currentState1 == States.LAUNCH || getColorSlot(slot1Sensor.red(), slot1Sensor.green(), slot1Sensor.blue()) == 0) {
            if (launch) {
                slot1.setPosition(MID_POS_1);
            }
            currentState1 = States.IDLE;
        }
        if (currentState2 == States.LAUNCH || getColorSlot(slot2Sensor.red(), slot2Sensor.green(), slot2Sensor.blue()) == 0) {
            if (launch) {
                slot2.setPosition(MID_POS_2);
            }
            currentState2 = States.IDLE;
        }

        updateSlot0();
        updateSlot1();
        updateSlot2();
    }

    public static int nextBall(int chamberNum, int[] pattern) {
        int patternIndex = chamberNum % 3;
        return pattern[patternIndex];
    }




}