package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Transfer {

    private static final long WAIT = 300, FLICK = 100;
    private static final double UP_POS_0 = 0,DOWN_POS_0 = 0, UP_POS_1 = 0,DOWN_POS_1 = 0, UP_POS_2 = 0,DOWN_POS_2 = 0;
    public static Servo slot0, slot1, slot2;
    static long startTime;
    static int[] ideal;
    static int launchNum = 0;
    public static enum State {
        LAUNCH,
        IDLE
    }

    public static State currentState;

    public static void startLaunch () {
        if ((System.currentTimeMillis() - startTime) > WAIT) {
            currentState = State.LAUNCH;
        }
    }
    public static double slot0Position() {
        return slot0.getPosition();
    }
    public static double slot1Position() {
        return slot1.getPosition();
    }
    public static double slot2Position() {
        return slot2.getPosition();
    }


    public static void init (HardwareMap hwMap) {

        slot0 = hwMap.get(Servo.class, "slot0Servo");
        slot1 = hwMap.get(Servo.class, "slot1Servo");
        slot2 = hwMap.get(Servo.class, "slot2Servo");

        currentState = State.IDLE;

        //initialize to start position
        slot0.setPosition(0);
        slot1.setPosition(0);
        slot2.setPosition(0);
    }

    public static void update (int[] slots, int[] idealChamber, int chamberNum) {
        int upServo = -1;

        switch(currentState) {
            case LAUNCH:

                startTime = System.currentTimeMillis();

                if (launchServo(ideal, slots) == 0) {
                    slot0.setPosition(UP_POS_0);
                    upServo = 0;
                } else if (launchServo(ideal, slots) == 1) {
                    slot1.setPosition(UP_POS_1);
                    upServo = 1;
                } else if (launchServo(ideal, slots) == 2) {
                    slot2.setPosition(UP_POS_2);
                    upServo = 2;
                }

                launchNum++;
                currentState = State.IDLE;
                break;

            case IDLE:
                int emptySlotNum = 0;

                if (upServo == 0) {
                    slot0.setPosition(DOWN_POS_0);
                } else if (upServo == 1) {
                    slot1.setPosition(DOWN_POS_1);
                } else if (upServo == 2) {
                    slot2.setPosition(DOWN_POS_2);
                }

                for (int i = 0; i < slots.length - 1; i++) {
                    if (slots[i] == 0) {
                        emptySlotNum++;
                    }
                }
                if (emptySlotNum == slots.length) {
                    launchNum = 0;
                }

                chamberCheck(idealChamber, chamberNum);
                break;
        }

    }

    private static int launchServo (int[] ideal, int[] slots) {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == ideal[launchNum]) {
                return i;
            }
        }
        return -1;
    }

    public static void chamberCheck (int[] idealChamber, int chamberNum) {
        if (chamberNum > 6) {
            ideal = new int[9 - chamberNum];
        } else {
            ideal = new int[3];
        }

        for (int  i = 0; i < ideal.length; i++) {
            ideal[i] = idealChamber[i+chamberNum];
        }

    }
}