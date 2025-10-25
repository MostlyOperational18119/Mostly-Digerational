package org.firstinspires.ftc.teamcode.teleop;

public class Indexer {
    public Positions rotation = Positions.zeroIn;
    public enum Positions {zeroIn, zeroOut, oneIn, oneOut, twoIn, twoOut;}
    Methods methods;
    public enum BallColor {
        GREEN,
        EMPTY,
        PURPLE;
    }
    public Indexer(Methods methods) {
        this.methods = methods;
    }
    public BallColor[] slots = new BallColor[3];
    public void update() {
        switch (rotation) {
            case zeroIn:
                methods.revolver.setPosition(0); // find servo positions
                break;
            case zeroOut:
                methods.revolver.setPosition(0.1);
                break;
            case oneIn:
                methods.revolver.setPosition(0.2);
                break;
            case oneOut:
                methods.revolver.setPosition(0.3);
                break;
            case twoIn:
                methods.revolver.setPosition(0.4);
                break;
            case twoOut:
                methods.revolver.setPosition(0.5);
                break;
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
                    rotation = Positions.zeroOut;
                    break;
                case 1:
                    rotation = Positions.oneOut;
                    break;
                case 2:
                    rotation = Positions.twoOut;
                    break;
            }
        } else {
            switch (index) {
                case 0:
                    rotation = Positions.zeroIn;
                    break;
                case 1:
                    rotation = Positions.oneIn;
                    break;
                case 2:
                    rotation = Positions.twoIn;
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
        for (BallColor pos:
             slots) {
            if (pos == color) {
                return index;
            }
            index++;
        }
        return 0;
    }
    public void setSlots(int index, BallColor state) {
        slots[index] = state;
    }
}
