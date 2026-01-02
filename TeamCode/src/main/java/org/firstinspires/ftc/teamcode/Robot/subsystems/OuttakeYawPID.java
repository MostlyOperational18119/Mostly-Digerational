package org.firstinspires.ftc.teamcode.Robot.subsystems;

public class OuttakeYawPID {
    private static double kP = 0.0;  // Proportional gain
    private static double kI = 0.00; // Integral gain
    private static double kD = 0.0;  // Derivative gain

    private static double integral = 0;
    private static double previousError = 0;
    private static double tolerance = 5;

    public static double calculate(double target, double current, double dt) {
        double error = target - current;
        integral += error * dt;

        if (Math.abs(error) < tolerance) {
            return 0;
        }
        double derivative = (error - previousError) / dt;
        previousError = error;

        double output = kP * error + kI * integral + kD * derivative;
        return Math.max(-1.0, Math.min(1.0, output));

    }

    public static void reset() {
        integral = 0;
        previousError = 0;
    }
}
