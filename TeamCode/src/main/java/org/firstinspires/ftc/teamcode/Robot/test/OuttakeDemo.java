package org.firstinspires.ftc.teamcode.Robot.test;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;

public class OuttakeDemo {

    private static CRServo rotateServo;
    private static DcMotor encoderMotor;
    private static int maxClicks =  22000;
    private static int tolerance = 50;

    public static void update(int targetClicks) {
        int clicks = Drivetrain.outtakePosition();

        if (Math.abs(targetClicks - clicks) <= tolerance) {
            rotateServo.setPower(0);
        } else {
            double outtakePower = (double) (targetClicks - clicks) / (maxClicks / 2);
            outtakePower = Math.max(-1, Math.min(1, outtakePower));
            rotateServo.setPower(outtakePower);
        }
    }
    public static int setRotationPosition(double target) {
        target = Math.max(0, Math.min(1, target));
        return (int) (maxClicks * target);
    }
    public static int setTarget(double target) {
        double num = Math.max(0, Math.min(22000, target));
        return (int) num;
    }

    public static void setPower(double num){
        rotateServo.setPower(num);
    }
    public static double servoPower(){
        return rotateServo.getPower();
    }


    static boolean isBlue = false;
    static double robotX = 72;
    static double robotY = 72;
    static double robotOrientation = 0;
    static double goalX;
    static double goalY = 144;

    public static void init(HardwareMap hwMap) {
        rotateServo = hwMap.get(CRServo.class, "rotate");

        encoderMotor = hwMap.get(DcMotor.class, "motorFR");
        encoderMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encoderMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if (isBlue) {goalX = 0;} else {goalX = 144;}
    }

    public static double nicksLittleHelper() {
        double dx = goalX - robotX;
        double dy = goalY - robotY;

        // Correct: accounts for all quadrants
        double absoluteAngleToGoal = Math.atan2(dy, dx);

        // Angle robot needs to turn
        double relativeAngle = absoluteAngleToGoal - Math.toRadians(robotOrientation);

        // Normalize to (-pi, pi)
        relativeAngle = Math.atan2(Math.sin(relativeAngle), Math.cos(relativeAngle));
        relativeAngle = Math.toDegrees(relativeAngle);

        // Convert to motor ticks (example factor)
        return (relativeAngle * 51.724137931) + 400;
    }
    public static double nicksLittleTelemetry1() {
        double dx = goalX - robotX;
        double dy = goalY - robotY;

        // Correct: accounts for all quadrants
        double absoluteAngleToGoal = Math.atan2(dy, dx);

        // Angle robot needs to turn
        double relativeAngle = absoluteAngleToGoal - robotOrientation;

        // Normalize to (-pi, pi)
        relativeAngle = Math.atan2(Math.sin(relativeAngle), Math.cos(relativeAngle));
        relativeAngle = Math.toDegrees(relativeAngle);

        //return angle (to check stuff in telemetry)
        return (relativeAngle);
    }

}
