package org.firstinspires.ftc.teamcode.everything;

public class nicksLittleHelperTest {
    static double robotX = 0;
    static double robotY = 0;
    static double goalX = 0;
    static double goalY = 0;

    public static void main(String[] args) {
        System.out.println(nicksLittleHelp());
    }

    public static double nicksLittleHelp() {
        double targetAngle;
        int targetPos;
        targetAngle = Math.max(-58, Math.min(58, Math.atan((robotY - goalY) / (robotX - goalX))));
        targetPos = (int) (targetAngle * 51.724137931);
        return targetPos;
    }
}
