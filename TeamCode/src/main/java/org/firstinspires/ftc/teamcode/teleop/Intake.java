package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class Intake {

    private enum State {
        REVERSE,
        INTAKE,
        IDLE;
    }
    public enum Color {
        GREEN,
        MAGENTA;
    }
    private static final int MAX_BALLS = 3;
    private int count = 0;
    private boolean isTripped;
    private Color[] balls = new Color[MAX_BALLS];
    public void addBalls() { //use beam break sensor to increase number of balls
        if (isTripped) {
            methods.breakBeamSensor.getState();
            Color c = detectColor();
            balls[count++] = c;
        }
    }

    public void removeBalls(){ //call when we launch?
        if (count == 0) return;
        System.arraycopy(balls, 1, balls, 0, count -1);
        balls[--count] = null;
    }

    private Color detectColor() { //detects color of ball in slot --> need to check color of other slots after rotation
        NormalizedRGBA rgba = methods.colorSensor.getNormalizedColors();
        if (rgba.green > rgba.red && rgba.green > rgba.blue) {
            return Color.GREEN;
        } else {
            return Color.MAGENTA;
        }
    }
    private Intake.State currentState = Intake.State.IDLE;
    private long startTime;
    private final Methods methods;
    public Intake(Methods methods) {
        this.methods = methods;
    }
    public void startLaunch() {
        currentState = Intake.State.IDLE;
    }
    public void update() {
        methods.telemetry.addData("Current Intake State", currentState);
        switch (currentState) {
            case INTAKE:
                methods.revolver.setPosition(1); //placeholder value for servo position (ramp down)
                methods.intake.setPower(1); //placeholder value for motor power
                if (System.currentTimeMillis() - startTime > 200) {
                    startTime = System.currentTimeMillis();
                }
                methods.revolver.setPosition(0); //placeholder value for servo position (ramp up)
                addBalls();
                detectColor();
                break;
            case REVERSE: //do we even need this?
                methods.revolver.setPosition(1); //placeholder value for servo position (ramp down)
                methods.intake.setPower(-1); //placeholder value for motor power
                if (System.currentTimeMillis() - startTime > 200) {
                    startTime = System.currentTimeMillis();
                }
                methods.revolver.setPosition(0);
                break;
            case IDLE:
                break;
        }
    }
}
