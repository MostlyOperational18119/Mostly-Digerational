package org.firstinspires.ftc.teamcode.everything;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Indexer {
    public Positions rotation = Positions.zeroIn;
    public Positions nextRotation = Positions.zeroIn;
    public boolean findColorHappened = false;

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
        for (BallColor ball :
                slots) {
            if (ball == color) {
                return true;
            }
        }
        return false;
    }

    public int findColor(BallColor color) {
        for (int i = 0; i<slots.length; i++) {
            if (slots[i] == color) {
                findColorHappened = true;
                return i;
            }
        }
        return 0;
    }

    public void setSlots(int index, BallColor state) {
        slots[index] = state;
    }

    //when beam is broken: check ball color
    public void setIndexerColor() {
        int blue = methods.colorSensor.blue();
        int green = methods.colorSensor.green();
        int currentIndex = currentIntakeIndex();
        BallColor ballIn = BallColor.EMPTY;

        if (blue > green) {
            ballIn = BallColor.PURPLE;
        } else if (green > blue) {
            ballIn = BallColor.GREEN;
        }

        if (currentIndex > -1) {
            setSlots(currentIndex, ballIn);
        }
    }

    public void rotateWithDistanceCheck() {
        double distance = methods.colorSensor.getDistance(DistanceUnit.MM);

        if (distance <= 78) {
            rotateToColor(BallColor.EMPTY);
        }
    }

    public int currentIntakeIndex() {
        int index = -1;
        switch (rotation) {
            case zeroIn:
                index = 0;
                break;
            case oneIn:
                index = 1;
                break;
            case twoIn:
                index = 2;
                break;
        }
        return index;
    }
}
