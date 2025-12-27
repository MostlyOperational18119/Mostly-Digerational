package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

public class Indexer {
    public static Servo slot0, slot1, slot2;
    public static double UP_POS_0 = 0, DOWN_POS_0 = 0, UP_POS_1 = 0, DOWN_POS_1 = 0, UP_POS_2 = 0, DOWN_POS_2 = 0;
    public enum States {
        LAUNCH,
        IDLE
    }
    public static int[] pattern = new int[] {1, 2, 2}; // change to new int[3];
    public static States currentState;
    static int currentBall; //ball being launched (0, 1, or 2)
    private static NormalizedColorSensor slot0Sensor, slot1Sensor, slot2Sensor;

    public static void init (HardwareMap hwMap) {
        slot0Sensor = hwMap.get(NormalizedColorSensor.class, "slot0Sensor");
        slot1Sensor = hwMap.get(NormalizedColorSensor.class, "slot1Sensor");
        slot2Sensor = hwMap.get(NormalizedColorSensor.class, "slot2Sensor");
        slot0 = hwMap.get(Servo.class, "slot0Servo");
        slot1 = hwMap.get(Servo.class, "slot1Servo");
        slot2 = hwMap.get(Servo.class, "slot2Servo");

        //initialize to start position
        slot0.setPosition(DOWN_POS_0);
        slot1.setPosition(DOWN_POS_1);
        slot2.setPosition(DOWN_POS_2);
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

        int[]  slots = new int[3];

        //0 slot
        if (149 >= hue0 && hue0 >= 90) {
            slots[0] = 2;
        } else if (300 >= hue0 && hue0 >= 270) {
            slots[0] = 1;
        } else {
            slots[0] = 0;
        }

        //1 slot
        if (149 >= hue1 && hue1 >= 90) {
            slots[1] = 2;
        } else if (300 >= hue1 && hue1 >= 270) {
            slots[1] = 1;
        } else {
            slots[1] = 0;
        }

        //2 slot
        if (149 >= hue2 && hue2 >= 90) {
            slots[2] = 2;
        } else if (300 >= hue2 && hue2 >= 270) {
            slots[2] = 1;
        } else {
            slots[2] = 0;
        }

        return slots;
    }

    //Transfer Part Duex import
    public static void startLaunch () {
        currentState = States.LAUNCH;
    }

    public static void update (int[] slots) {
        switch(currentState) {
            case LAUNCH:
                currentBall = nextBall(slots, 6, pattern); //placeholder chamberNum
                if (currentBall != -1) {
                    switch (currentBall) {
                        case 0:
                            slot0.setPosition(UP_POS_0);
                            break;
                        case 1:
                            slot1.setPosition(UP_POS_1);
                            break;
                        case 2:
                            slot2.setPosition(UP_POS_2);
                            break;
                        default:
                            break;
                    }
                }
                currentState = States.IDLE;
            case IDLE:
                switch (currentBall) {
                    case 0:
                        slot0.setPosition(DOWN_POS_0);
                        break;
                    case 1:
                        slot1.setPosition(DOWN_POS_1);
                        break;
                    case 2:
                        slot2.setPosition(DOWN_POS_2);
                        break;
                    default:
                        break;
                }
                break;
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
            if (slots[i] == 0) {
                emptyCount++;
            }
        }

        if (emptyCount < 3) {
            for (int i = 0; i < slots.length; i++) {
                if (slots[i] != 0) {
                    return i;
                }
            }
        }


        return -1;
    }
}
