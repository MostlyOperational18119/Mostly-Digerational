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
    public static Servo slot0, slot1, slot2;



    public static void init (HardwareMap hwMap) {

        slot0 = hwMap.get(Servo.class, "slot_1_servo");
        slot1 = hwMap.get(Servo.class, "slot_2_servo");
        slot2 = hwMap.get(Servo.class, "slot_3_servo");

    }

    public static void launch (int[] index, int[] idealChamber, int chamberNum) {
        //declare ideal array
        int[] ideal = new int[3];

        //create idea array by copying the chamber array past filled slots
        System.arraycopy(idealChamber, chamberNum, ideal, 0,  3);

        //iterate through the ideal array to launch the correct ball for pattern
        for (int i = 0; i < 3; i++) {
            long startTime, currentTime;

            //set the start time at the start of the loop
            startTime = System.currentTimeMillis();

            //check if the first slot matches what we want to shoot
            if (index[0] == ideal[i]) {
                slot0.setPosition(UP_POS);
                do {
                    currentTime = System.currentTimeMillis();
                } while (currentTime-startTime < WAIT);
                slot0.setPosition(DOWN_POS);

                index[0] = 0;
                do {
                    currentTime = System.currentTimeMillis();
                } while (currentTime-startTime < WAIT);

            //check if the second slot matches what we want to shoot
            } else if (index[1] == ideal[i]) {
                slot1.setPosition(UP_POS);
                do {
                    currentTime = System.currentTimeMillis();
                } while (currentTime-startTime < FLICK);
                slot1.setPosition(DOWN_POS);

                index[1] = 0;
                do {
                    currentTime = System.currentTimeMillis();
                } while (currentTime-startTime < WAIT);

            //check if the third slot matches what we want to shoot
            } else if (index[2] == ideal[i]) {
                slot2.setPosition(UP_POS);
                do {
                    currentTime = System.currentTimeMillis();
                } while (currentTime-startTime < FLICK);
                slot2.setPosition(DOWN_POS);

                index[2] = 0;
                do {
                    currentTime = System.currentTimeMillis();
                } while (currentTime-startTime < WAIT);
            }


            //wait until flicker is down before launching
        }

    }
}
