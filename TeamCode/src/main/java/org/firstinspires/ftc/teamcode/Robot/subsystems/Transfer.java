package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.Timer;
import java.util.TimerTask;

public class Transfer {

    private static final long WAIT = 300, FLICK = 100;
    private static final double UP_POS_0 = 0,DOWN_POS_0 = 0, UP_POS_1 = 0,DOWN_POS_1 = 0, UP_POS_2 = 0,DOWN_POS_2 = 0;
    public Servo slot0, slot1, slot2;
    int launchNum = 0;
    int[] ideal = new int[3];

    public enum State {
        LAUNCH,
        IDLE
    }

    public State currentState;

    public void init (HardwareMap hwMap) {

        slot0 = hwMap.get(Servo.class, "slot_1_servo");
        slot1 = hwMap.get(Servo.class, "slot_2_servo");
        slot2 = hwMap.get(Servo.class, "slot_3_servo");

        currentState = State.IDLE;
    }

    public void startLaunch (currentState ) {

    }
    public void update (int[] index, int[] idealChamber, int chamberNum) {
        int upServo = -1;
        long startTime;
        switch(currentState) {
            case LAUNCH:

                if ((System.currentTimeMillis() - startTime) < WAIT) {
                    currentState = State.IDLE;
                }

                if (launchServo(ideal, index, idealChamber, chamberNum) == 0) {
                    slot0.setPosition(UP_POS_0);
                    upServo = 0;
                } else if (launchServo(ideal, index, idealChamber, chamberNum) == 1) {
                    slot1.setPosition(UP_POS_1);
                    upServo = 1;
                } else if (launchServo(ideal, index, idealChamber, chamberNum) == 2) {
                    slot2.setPosition(UP_POS_2);
                    upServo = 2;
                }
                startTime = System.currentTimeMillis();
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



    private int launchServo (int[] ideal, int[] index, int[] idealChamber, int chamberNum) {

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

    public void chamberCheck (int[] idealChamber, int chamberNum) {
        System.arraycopy(idealChamber, chamberNum, ideal, 0,  3);
    }
}