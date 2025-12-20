package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Transfer {

    private static final long WAIT = 300, FLICK = 100;
    private static final double UP_POS_0 = 0,DOWN_POS_0 = 0, UP_POS_1 = 0,DOWN_POS_1 = 0, UP_POS_2 = 0,DOWN_POS_2 = 0;
    public static Servo slot0, slot1, slot2;
    static int launchNum = 0;
    static int[] ideal = new int[3];
    static long startTime;


    public static enum State {
        LAUNCH,
        IDLE
    }

    public static State currentState;

    public static void startLaunch () {
        if ((System.currentTimeMillis() - startTime) < WAIT) {
            currentState = State.IDLE;
        }
    }
    
    public static void testSlot0Green() {
        slot0.setPosition(1);
    }
    public static void testSlot0Purple() {
        slot0.setPosition(0.5);
    }
    public static void saarang() {
        slot0.setPosition(0);
    }
    public static double getServoPosition() {
        return slot0.getPosition();
    }

    public static void init (HardwareMap hwMap) {

        slot0 = hwMap.get(Servo.class, "slot0servo");
        slot1 = hwMap.get(Servo.class, "slot1servo");
        slot2 = hwMap.get(Servo.class, "slot2servo");

        currentState = State.IDLE;

        //initialize to start position
        slot0.setPosition(0);
        slot1.setPosition(0);
        slot2.setPosition(0);
    }

    public static void update (int[] index, int[] idealChamber, int chamberNum) {
        int upServo = -1;

        switch(currentState) {
            case LAUNCH:

                startTime = System.currentTimeMillis();

                if (launchServo(ideal, index, idealChamber, chamberNum) == 0) {
                    slot0.setPosition(UP_POS_0);
                    upServo = 0;
                } else if (launchServo(ideal, index, idealChamber, chamberNum) == 1) {
                    slot1.setPosition(UP_POS_1);
                    upServo = 1;
                } else if (launchServo(ideal, index, idealChamber, chamberNum) == 2) {
                    slot2.setPosition(UP_POS_2);
                    upServo = 2;
                } else {

                }

                launchNum++;
                currentState = State.IDLE;
                break;

            case IDLE:
                if (upServo == 0) {
                    slot0.setPosition(DOWN_POS_0);
                } else if (upServo == 1) {
                    slot1.setPosition(DOWN_POS_1);
                } else if (upServo == 2) {
                    slot2.setPosition(DOWN_POS_2);
                }

                chamberCheck(idealChamber, chamberNum);
                break;
        }

    }



    private static int launchServo (int[] ideal, int[] index, int[] idealChamber, int chamberNum) {

        if (index[0] == ideal[launchNum]) {
            index[0] = 0;
            return 0;
        } else if (index[1] == ideal[launchNum]) {
            index[2] = 0;
            return 2;
        } else if (index[2] == ideal[launchNum]) {
            index[2] = 0;
            return 2;
        }
        return -1;
    }

    public static void chamberCheck (int[] idealChamber, int chamberNum) {
        System.arraycopy(idealChamber, chamberNum, ideal, 0,  3);
    }
}