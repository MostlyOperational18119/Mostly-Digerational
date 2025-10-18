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
}
