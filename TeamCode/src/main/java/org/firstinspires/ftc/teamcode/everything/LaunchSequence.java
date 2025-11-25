package org.firstinspires.ftc.teamcode.everything;

import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LaunchSequence {
    public enum State {
        PREP_LAUNCH,
        LAUNCH,
        IDLE;
    }

    boolean transferServoReset = false;

    int launchIndex = -1;

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
        methods.telemetry.addData("current index", launchIndex);
        methods.telemetry.update();
        //methods.telemetry.addData("Current Launch State", currentState);
        switch (currentState) {
            case PREP_LAUNCH:
                methods.launchIdle = false;
                if (methods.toGreen) {
                    launchIndex = indexer.rotateToColorAndGetIndex(Indexer.BallColor.GREEN);
                    startTime = System.currentTimeMillis();
                    currentState = State.LAUNCH;
                } else if (methods.toPurple) {
                    launchIndex = indexer.rotateToColorAndGetIndex(Indexer.BallColor.PURPLE);
                    startTime = System.currentTimeMillis();
                    currentState = State.LAUNCH;
                }
                break;
            case LAUNCH:
                methods.launchIdle = false;
               if (System.currentTimeMillis() - startTime > 1000) {
                    methods.transferServo.setPosition(methods.transferServoUp);
                    transferServoReset = false;
                }
               if (System.currentTimeMillis() - startTime > 1050) {
                   if (!transferServoReset) {
                       transferServoReset = true;
                       methods.transferServo.setPosition(0.27);
                   }
                   if (launchIndex >= 0 && launchIndex < indexer.slots.length) {
                       indexer.slots[launchIndex] = Indexer.BallColor.EMPTY;
                   }
               }
                if (System.currentTimeMillis() - startTime > 1450) {
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
