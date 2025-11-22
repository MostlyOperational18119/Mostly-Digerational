package org.firstinspires.ftc.teamcode.everything;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Intake {
    public enum State {
        SET_COLOR,
        ROTATE_TO_EMPTY,
        IDLE;
    }

    public State currentStateIntake = State.IDLE;
    private final Indexer indexer;
    private final Methods methods;
    private long startTime;
    public Intake(Methods methods, Indexer indexer) {
        this.methods = methods;
        this.indexer = indexer;
    }

    public void start() {
        startTime = System.currentTimeMillis();
        currentStateIntake = State.SET_COLOR;
    }

    public void update() {
        methods.telemetry.addData("Current Intake State", currentStateIntake);
        switch (currentStateIntake) {
            case SET_COLOR:
                if (System.currentTimeMillis() - startTime > 800) {
                    indexer.setIndexerColor();
                    currentStateIntake = State.ROTATE_TO_EMPTY;
                }
                break;
            case ROTATE_TO_EMPTY:
                if (indexer.colorInArray(Indexer.BallColor.EMPTY) && methods.colorSensor.getDistance(DistanceUnit.MM) < 50) {
                    indexer.rotateToColor(Indexer.BallColor.EMPTY);
                }
                if (System.currentTimeMillis() - startTime > 500)
                    currentStateIntake = State.IDLE;
                break;
            case IDLE:
                break;
        }
    }
}
