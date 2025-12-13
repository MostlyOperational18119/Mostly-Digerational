package org.firstinspires.ftc.teamcode.everything.teleop;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.everything.Methods;

public class Intake {
    private final Indexer indexer;
    private final Methods methods;
    public State currentStateIntake = State.IDLE;
    private long startTime;
    boolean idleSwap = true;
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

    public void updateAuto() {
        methods.telemetry.addData("Current Intake State", currentStateIntake);
        switch (currentStateIntake) {
            case SET_COLOR:
                if (System.currentTimeMillis() - startTime > 700) {
                    idleSwap = true;
                    if (indexer.setIndexerColor()) {
                        startTime = System.currentTimeMillis();
                        currentStateIntake = State.ROTATE_TO_EMPTY;
                    }
                }
                break;
            case ROTATE_TO_EMPTY:
                //&& methods.colorSensor.getDistance(DistanceUnit.MM) < 50
                if (indexer.colorInArray(Indexer.BallColor.EMPTY)) {
                    if (idleSwap) {
                        indexer.rotateToColor(Indexer.BallColor.EMPTY);
                        idleSwap = false;
                        startTime = System.currentTimeMillis(); // Reset timer when rotation starts
                    }
                    // Wait longer to ensure physical rotation completes
                    if (!idleSwap && System.currentTimeMillis() - startTime > 500) { // Increase delay
                        startTime = System.currentTimeMillis();
                        currentStateIntake = State.SET_COLOR;
                    }
                } else if (idleSwap) {
                    currentStateIntake = State.IDLE;
                }
                break;
            case IDLE:
                break;
        }
    }

    public enum State {
        SET_COLOR,
        ROTATE_TO_EMPTY,
        IDLE;
    }
}
