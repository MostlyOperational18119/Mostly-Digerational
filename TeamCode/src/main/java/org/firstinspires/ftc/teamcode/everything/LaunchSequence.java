package org.firstinspires.ftc.teamcode.everything;

import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class LaunchSequence {
    private enum State {
        PREP_LAUNCH,
        LAUNCH,
        IDLE;
    }

    private State currentState = State.IDLE;
    private long startTime;
    private final Methods methods;
    public LaunchSequence(Methods methods) {
        this.methods = methods;
    }

    public void startLaunch() {
        currentState = State.PREP_LAUNCH;
    }

    public void update() {
        methods.telemetry.addData("Current Launch State", currentState);
        switch (currentState) {
            case PREP_LAUNCH:
                //methods.outtake.setPower(1);

                if (System.currentTimeMillis() - startTime > 2000) {
                    startTime = System.currentTimeMillis();
                    currentState = State.LAUNCH;
                }
                break;
            case LAUNCH:
               // if () {
                    methods.transferServo.setPosition(methods.transferServoUp);
                //}
                currentState = State.IDLE;
                break;
            case IDLE:
                break;
        }
    }
}
