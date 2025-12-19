package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Transfer {


    public static Servo slot1;
    public static Servo slot2;
    public static Servo slot3;

    public static void init(HardwareMap hwMap) {

        slot1 = hwMap.get(Servo.class, "slot_1_servo");
        slot2 = hwMap.get(Servo.class, "slot_2_servo");
        slot3 = hwMap.get(Servo.class, "slot_3_servo");

    }

}
