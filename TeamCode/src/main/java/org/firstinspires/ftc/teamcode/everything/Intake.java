package org.firstinspires.ftc.teamcode.everything;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Intake {
    public enum State {
        SET_COLOR,
        ROTATE_TO_EMPTY,
        IDLE;
    }

    public State currentState = State.IDLE;
    private final Indexer indexer;
    private long startTime;
    private final Methods methods;
    boolean broken;
    public Intake(Methods methods) {
        this.methods = methods;
        this.indexer = methods.indexer;
    }

    public void start() {
        currentState = State.SET_COLOR;
    }

    public void update() {
        methods.telemetry.addData("Current Launch State", currentState);
        switch (currentState) {
            case SET_COLOR:
                if (System.currentTimeMillis() - startTime > 300) {
                    startTime = System.currentTimeMillis();
                    indexer.setIndexerColor();
                    currentState = State.ROTATE_TO_EMPTY;
                }
                break;
            case ROTATE_TO_EMPTY:
                if (indexer.colorInArray(Indexer.BallColor.EMPTY) && methods.colorSensor.getDistance(DistanceUnit.INCH) < 1.167) {
                    indexer.rotateToColor(Indexer.BallColor.EMPTY);
                    currentState = State.IDLE;
                }
                break;
            case IDLE:
                break;
        }
    }
}
