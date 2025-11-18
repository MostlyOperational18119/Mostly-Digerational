package org.firstinspires.ftc.teamcode.everything;

import com.qualcomm.robotcore.hardware.NormalizedRGBA;

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
                //methods.outtake.setPower(1);
                if (methods.toGreen) {
                    indexer.rotateToColor(Indexer.BallColor.GREEN);
                    startTime = System.currentTimeMillis();
                    currentState = State.LAUNCH;
                    indexer.slots[indexer.currentIntakeIndex()] = Indexer.BallColor.EMPTY;
                } else if (methods.toPurple) {
                    indexer.rotateToColor(Indexer.BallColor.PURPLE);
                    startTime = System.currentTimeMillis();
                    currentState = State.LAUNCH;
                    indexer.slots[indexer.currentIntakeIndex()] = Indexer.BallColor.EMPTY;
                }
//                else {
//                   if (System.currentTimeMillis() - startTime > 300) {
//                       currentState = State.IDLE;
//                   }
//                }
                break;
            case LAUNCH:
               if (System.currentTimeMillis() - startTime > 150) {
                    methods.transferServo.setPosition(methods.transferServoUp);
                }
                if (System.currentTimeMillis() - startTime > 450) {
                    currentState = State.IDLE;
                }
                break;
            case IDLE:
                methods.transferServo.setPosition(0.21);
                break;
        }
    }
}
