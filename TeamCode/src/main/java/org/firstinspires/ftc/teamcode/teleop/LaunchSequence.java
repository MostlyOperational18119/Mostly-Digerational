package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class LaunchSequence {
    private enum State {
        PREP_LAUNCH,
        LAUNCH,
        IDLE;
    }

    private State currentState = State.IDLE;
    private Indexer index;
    private long startTime;
    private final Methods methods;
    boolean broken;
    public LaunchSequence(Methods methods) {
        this.methods = methods;
        index = methods.indexer;
    }

    public void startLaunch() {
        currentState = State.PREP_LAUNCH;
    }

    public void update() {
        broken = methods.breakBeamSensor.getState();
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
                methods.transferServo.setPosition(methods.transferServoUp);
                break;
            case IDLE:
                break;
        }
    }

    //when beam is broken: check ball color
//    public void onBeamBreak() {
//        if (!broken) {
//            NormalizedRGBA rgba = methods.colorSensor.getNormalizedColors();
//
//            //change later --> variable that has empty position (indexer automatically rotates before intaking)
//            for (int i = 0; i < 3; i++) {
//                if (index.slots[i] == Indexer.BallColor.EMPTY) {
//                    if (rgba.green > rgba.red & rgba.green > rgba.blue) {
//                        index.setSlots(i, Indexer.BallColor.GREEN);
//                    } else {
//                        index.setSlots(i, Indexer.BallColor.PURPLE);
//                    }
//                } else {
//
//                }
//            }
//        }
//    }
}
