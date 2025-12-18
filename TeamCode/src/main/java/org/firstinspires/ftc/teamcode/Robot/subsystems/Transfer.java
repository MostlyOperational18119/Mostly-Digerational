package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.Timer;
import java.util.TimerTask;

public class Transfer {

    private static final long WAIT = 300;
    private static final long FLICK = 100;
    private static final double UP_POS = 1;
    private static final double DOWN_POS = 0;
    public Servo slot0, slot1, slot2;



    public void init (HardwareMap hwMap) {

        slot0 = hwMap.get(Servo.class, "slot_1_servo");
        slot1 = hwMap.get(Servo.class, "slot_2_servo");
        slot2 = hwMap.get(Servo.class, "slot_3_servo");

    }

    public void launch (int[] index, int[] idealChamber, int chamberNum) {
        int[] ideal = new int[3];

        System.arraycopy(idealChamber, chamberNum, ideal, 0,  3);

        for (int i = 0; i < 3; i++) {
            long startTime, currentTime;

            startTime = System.currentTimeMillis();

            if (index[0] == ideal[i]) {
                slot0.setPosition(UP_POS);
                do {
                    currentTime = System.currentTimeMillis();
                } while ((currentTime-startTime) < FLICK);
                slot0.setPosition(DOWN_POS);


                index[0] = 0;
            } else if (index[1] == ideal[i]) {
                slot1.setPosition(UP_POS);
                do {
                    currentTime = System.currentTimeMillis();
                } while ((currentTime-startTime) < FLICK);
                slot1.setPosition(DOWN_POS);

                index[1] = 0;
            } else if (index[2] == ideal[i]) {
                slot2.setPosition(UP_POS);
                do {
                    currentTime = System.currentTimeMillis();
                } while ((currentTime-startTime) < FLICK);
                slot2.setPosition(DOWN_POS);

                index[2] = 0;
            }

            do {
                currentTime = System.currentTimeMillis();
            } while ((currentTime-startTime) < WAIT);
        }

    }
}
