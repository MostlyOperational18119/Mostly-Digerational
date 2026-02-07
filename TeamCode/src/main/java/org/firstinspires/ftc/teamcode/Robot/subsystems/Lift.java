package org.firstinspires.ftc.teamcode.Robot.subsystems;


import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Lift {

    public static Servo lift1;
    private static Servo lift2;
    public static double UP1 = 0.82;
    private static double UP2 = 0.66;
    private static double DOWN1 = 1.0;
    private static double DOWN2 = 0.86;
    //UP is inside the robot, DOWN is lifted
    public static void init (HardwareMap hwMap) {
        lift1 = hwMap.get(Servo.class, "lift1");
        lift2 = hwMap.get(Servo.class, "lift2");

        //initialize to start position
        lift1.setPosition(UP1);
        lift2.setPosition(UP2);
    }

    public static void lift () {
        lift1.setPosition(DOWN1);
        lift2.setPosition(DOWN2);
    }

    public static void unlift(){
        lift1.setPosition(UP1);
        lift2.setPosition(UP2);
    }

}
