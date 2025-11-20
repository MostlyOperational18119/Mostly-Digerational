package org.firstinspires.ftc.teamcode.everything;

import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LaunchSequence {
    public enum State {
        PREP_LAUNCH,
        LAUNCH,
        IDLE;
    }

    public State currentState = State.IDLE;
    private long startTime;
    private final Methods methods;
    private final Indexer indexer;
    public LaunchSequence(Methods methods, Indexer indexer) {
        this.methods = methods;
        this.indexer = indexer;
    }

    public void startLaunch() {
        startTime = System.currentTimeMillis();
        currentState = State.PREP_LAUNCH;
    }

    public void update() {
        methods.telemetry.addData("Current Launch State", currentState);
        switch (currentState) {
            case PREP_LAUNCH:
                methods.launchIdle = false;
                if (methods.toGreen) {
                    methods.telemetry.addLine("green");
                    methods.telemetry.update();
                    int launchIndex = indexer.rotateToColorAndGetIndex(Indexer.BallColor.GREEN);
                    startTime = System.currentTimeMillis();
                    currentState = State.LAUNCH;
                    if (launchIndex >= 0 && launchIndex < indexer.slots.length) {
                        indexer.slots[launchIndex] = Indexer.BallColor.EMPTY;
                    }
                } else if (methods.toPurple) {
                    int launchIndex = indexer.rotateToColorAndGetIndex(Indexer.BallColor.PURPLE);
                    startTime = System.currentTimeMillis();
                    currentState = State.LAUNCH;
                    if (launchIndex >= 0 && launchIndex < indexer.slots.length) {
                        indexer.slots[launchIndex] = Indexer.BallColor.EMPTY;
                    }
                }
                break;
            case LAUNCH:
                methods.launchIdle = false;
               if (System.currentTimeMillis() - startTime > 350) {
                    methods.transferServo.setPosition(methods.transferServoUp);
                }
               if (System.currentTimeMillis() - startTime > 500) {
                   methods.transferServo.setPosition(0.27);
               }
                if (System.currentTimeMillis() - startTime > 650) {
                    currentState = State.IDLE;
                }
                break;
            case IDLE:
                methods.launchIdle = true;
                methods.transferServo.setPosition(0.27);
                break;
        }
    }
}
