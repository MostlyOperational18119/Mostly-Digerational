package org.firstinspires.ftc.teamcode.everything;

import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Indexer {
    private final Methods methods;
    public Positions rotation = Positions.zeroIn;
    public Positions nextRotation = Positions.zeroIn;
    public BallColor[] slots = {BallColor.EMPTY, BallColor.EMPTY, BallColor.EMPTY};

    public Indexer(Methods methods) {
        this.methods = methods;
    }

    public void update() {
        if (rotation != nextRotation) {
            switch (nextRotation) {
                case zeroIn:
                    methods.revolver.setPosition(0.0);
                    rotation = nextRotation;
                    methods.revolverExpectedPosition = 0.0;
                    break;
                case zeroOut:
                    methods.revolver.setPosition(0.55);
                    rotation = nextRotation;
                    methods.revolverExpectedPosition = 0.55;
                    break;
                case oneIn:
                    methods.revolver.setPosition(0.74);
                    rotation = nextRotation;
                    methods.revolverExpectedPosition = 0.74;
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
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == color) {
                return i;
            }
        }
        return 0;
    }

    //for auto: set all of indexer to one color
    public void oneColor(BallColor color) {
        for (int i = 0; i < 3; i++) {
            slots[i] = color;
        }
    }

    public void redoColors() {
        methods.revolver.setPosition(0.0);
        setIndexerColor();
        methods.revolver.setPosition(0.74);
        setIndexerColor();
        methods.revolver.setPosition(0.37);
        setIndexerColor();
    }

    public void badColorWorkaround() {
        slots[0] = BallColor.GREEN;
        slots[1] = BallColor.PURPLE;
        slots[2] = BallColor.PURPLE;
    }

    //when beam is broken: check ball color
    public boolean setIndexerColor() {
        NormalizedRGBA normalizedRGBA = methods.colorSensor.getNormalizedColors();
        float blue = normalizedRGBA.blue;
        float green = normalizedRGBA.green;
        int currentIndex = currentIntakeIndex();
        BallColor ballIn = BallColor.EMPTY;

        if (methods.colorSensor.getDistance(DistanceUnit.MM) < 50) {
            if (blue > green) {
                ballIn = BallColor.PURPLE;
            } else if (green > blue) {
                ballIn = BallColor.GREEN;
            }
        }

        if (currentIndex > -1) {
            slots[currentIndex] = ballIn;
            return true;
        }
        return false;
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

    ;

    public int currentLaunchIndex() {
        int index = -1;
        switch (rotation) {
            case zeroOut:
                index = 0;
                break;
            case oneOut:
                index = 1;
                break;
            case twoOut:
                index = 2;
                break;
        }
        return index;
    }

    public int rotateToColorAndGetIndex(BallColor color) {
        int index;
        if (!colorInArray(color)) {
            return -1;
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
        return index; // Return the index that was found
    }

    public void rotateToSlotOuttake(int slot) {
        switch (slot) {
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
    }

    public enum Positions {zeroIn, zeroOut, oneIn, oneOut, twoIn, twoOut;}

    public enum BallColor {
        GREEN,
        EMPTY,
        PURPLE;
    }
}
