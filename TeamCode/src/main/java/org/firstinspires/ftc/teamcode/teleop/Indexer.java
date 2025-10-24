package org.firstinspires.ftc.teamcode.teleop;

public class Indexer {
    public double rotation = 0;

    public enum BallColor {
        GREEN,
        EMPTY,
        PURPLE;
    }

    public BallColor[] slots = new BallColor[3];

    public void setSlots(int index, BallColor State) {
        slots[index] = State;
    }

    public double moveToEmpty(Indexer.BallColor[] slots, double currentMotorPosition) {

        double futureRotation, futurePosition;
        double currentRotation = currentMotorPosition + 100; //math to change motor position to rotation

        for (int i = 0; i < 3; i++) {
            if (slots[i] == Indexer.BallColor.EMPTY) {
                //currentRotation
            }
        }

        return futurePosition;
    }
}
