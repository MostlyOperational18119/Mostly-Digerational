package org.firstinspires.ftc.teamcode.everything;

public class LaunchSequence {
    private final Methods methods;
    private final Indexer indexer;
    public State currentState = State.IDLE;
    boolean transferServoReset = false;
    int launchIndex = -1;
    private long startTime;
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
                if (System.currentTimeMillis() - startTime > 1150) {
                    if (!transferServoReset) {
                        transferServoReset = true;
                        methods.transferServo.setPosition(0.34);
                    }
                    if (launchIndex >= 0 && launchIndex < indexer.slots.length) {
                        indexer.slots[launchIndex] = Indexer.BallColor.EMPTY;
                    }
                }
                if (System.currentTimeMillis() - startTime > 1550) {
                    currentState = State.IDLE;
                }
                break;
            case IDLE:
                methods.launchIdle = true;
                methods.transferServo.setPosition(0.34);
                break;
        }
    }

    int slot = 0;

    public void updateAuto() {
        methods.telemetry.addData("current index", launchIndex);
        methods.telemetry.update();
        //methods.telemetry.addData("Current Launch State", currentState);
        switch (currentState) {
            case PREP_LAUNCH:
                methods.launchIdle = false;
                indexer.rotateToSlotOuttake(slot);
                slot++;
                if (slot >= 3) {
                    slot = 0;
                }
                startTime = System.currentTimeMillis();
                currentState = State.LAUNCH;
                break;
            case LAUNCH:
                methods.launchIdle = false;
                if (System.currentTimeMillis() - startTime > 1000) {
                    methods.transferServo.setPosition(methods.transferServoUp);
                    transferServoReset = false;
                }
                if (System.currentTimeMillis() - startTime > 1150) {
                    if (!transferServoReset) {
                        transferServoReset = true;
                        methods.transferServo.setPosition(0.34);
                    }
                    if (launchIndex >= 0 && launchIndex < indexer.slots.length) {
                        indexer.slots[launchIndex] = Indexer.BallColor.EMPTY;
                    }
                }
                if (System.currentTimeMillis() - startTime > 1550) {
                    currentState = State.IDLE;
                }
                break;
            case IDLE:
                methods.launchIdle = true;
                methods.transferServo.setPosition(0.34);
                break;
        }
    }

    public enum State {
        PREP_LAUNCH,
        LAUNCH,
        IDLE;
    }
}
