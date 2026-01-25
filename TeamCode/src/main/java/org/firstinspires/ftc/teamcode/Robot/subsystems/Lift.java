package org.firstinspires.ftc.teamcode.Robot.subsystems;


import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Lift {

    private static Servo lift1, lift2;
    private static double UP = 0.82;
    public static void init (HardwareMap hwMap) {
        lift1 = hwMap.get(Servo.class, "lift1");
        //lift2 = hwMap.get(Servo.class, "lift2");

        //initialize to start position
        lift1.setPosition(0.82);
        //lift2.setPosition(0.82);
    }

    public static void lift () {
        double LIFT = 1;
        lift1.setPosition(LIFT);
        //lift2.setPosition(LIFT);
    }

    public static void unlift(){
        double UP = 0.82;
        lift1.setPosition(UP);
    }

}
