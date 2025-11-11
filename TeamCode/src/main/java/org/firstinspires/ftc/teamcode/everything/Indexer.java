package org.firstinspires.ftc.teamcode.everything;

import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class Indexer {
    public Positions rotation = Positions.zeroIn;
    public Positions nextRotation = Positions.zeroIn;
    public enum Positions {zeroIn, zeroOut, oneIn, oneOut, twoIn, twoOut;}

    public String lastColor = "empty";
    Methods methods;
    public enum BallColor {
        GREEN,
        EMPTY,
        PURPLE;
    }
    public Indexer(Methods methods) {
        this.methods = methods;
    }
    public BallColor[] slots = {BallColor.EMPTY, BallColor.EMPTY, BallColor.EMPTY};

    public void update() {
        if (rotation != nextRotation) {
            switch (nextRotation) {
                case zeroIn:
                    methods.revolver.setPosition(0.0);
                    rotation = nextRotation;
                    break;
                case zeroOut:
                    methods.revolver.setPosition(0.55);
                    rotation = nextRotation;
                    break;
                case oneIn:
                    methods.revolver.setPosition(0.74);
                    rotation = nextRotation;
                    break;
                case oneOut:
                    methods.revolver.setPosition(0.2);
                    rotation = nextRotation;
                    break;
                case twoIn:
                    methods.revolver.setPosition(0.37);
                    rotation = nextRotation;
                    break;
                case twoOut:
                    methods.revolver.setPosition(0.92);
                    rotation = nextRotation;
                    break;
            }
        }
    }
    public void rotateToColor(BallColor color) {
        int index;
        if (!colorInArray(color)) {
            return;
        }
        index = findColor(color);
        if (color != BallColor.EMPTY) {
            switch (index) {
                case 0:
                    nextRotation = Positions.zeroOut;
                    break;
                case 1:
                    nextRotation = Positions.oneOut;
                    break;
                case 2:
                    nextRotation = Positions.twoOut;
                    break;
            }
        } else {
            switch (index) {
                case 0:
                    nextRotation = Positions.zeroIn;
                    break;
                case 1:
                    nextRotation = Positions.oneIn;
                    break;
                case 2:
                    nextRotation = Positions.twoIn;
                    break;
            }
        }
    }
    public boolean colorInArray(BallColor color) {
        for (BallColor ball:
                slots) {
            if (ball == color) {
                return true;
            }
        }
        return false;
    }
    public int findColor(BallColor color) {
        int index = 0;
        for (BallColor pos: slots) {
            if (pos == color) {
                return index;
            }
            index++;
        }
        return index;
    }
    public void setSlots(int index, BallColor state) {
        slots[index] = state;
    }
    //when beam is broken: check ball color
    public void onBeamBreak(int i, int green, int blue) {
        lastColor = "empty";
        if ((green > 75) && (green > blue)) {
            setSlots(i, BallColor.GREEN);
            lastColor = "green";
        } else if ((blue > 75) && (blue > green)) {
            setSlots(i, BallColor.PURPLE);
            lastColor = "purple";
        }
    }
}
