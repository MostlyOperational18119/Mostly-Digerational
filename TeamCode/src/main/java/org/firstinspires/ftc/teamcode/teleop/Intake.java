package org.firstinspires.ftc.teamcode.teleop;

public class Intake {

    private enum State {
        REVERSE,
        INTAKE,
        IDLE;
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
        methods.telemetry.addData("Current Launch State", currentState);
        switch (currentState) {
            case INTAKE:
                methods.intake.setPower(1);
                break;
            case REVERSE:
                methods.intake.setPower(-1);
                break;
            case IDLE:
                break;
        }
    }
}
