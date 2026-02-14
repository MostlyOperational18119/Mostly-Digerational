package org.firstinspires.ftc.teamcode.Robot.subsystems.limelight;

import java.nio.ByteBuffer;
import java.util.Arrays;

// TODO: USE A FUCKING PROTOBUF YOU ABSOLUTE MUPPET (FROM: DAMIEN, TO: FUTURE DAMIEN)
public class AprilTagResult {
    // 56 is the size :P
    public static int APRIL_TAG_SIZE = 56;

    public int tagID;
    public double[] tagPos;
    public double[] tagRot;

    AprilTagResult(byte[] src) {
        // Nobody cares about the tag family pointer
        // could chop it off before sending to save 4 bytes, but I'm lazy
        int pos = 0;

        tagID = intFromBytesRange(src, pos);

        pos += 4; // done with tag ID

        // tag position
        tagPos = new double[]{
                doubleFromBytesRange(src, pos),
                doubleFromBytesRange(src, pos + 8),
                doubleFromBytesRange(src, pos + 16),
        };

        pos += 24; // done with tag position

        // tag rotation
        tagRot = new double[]{
                doubleFromBytesRange(src, pos),
                doubleFromBytesRange(src, pos + 8),
                doubleFromBytesRange(src, pos + 16),
        };

        pos += 24; // done with tag rotation

        pos += 4; // Remainder for alignment?

        // Hopefully I didn't mess up, otherwise we will be really sad :(
        assert pos == APRIL_TAG_SIZE;
    }


    static int intFromBytes(byte[] src) {
        assert src.length <= 4; // should be == maybe? idk
        // TODO: MAYBE APPLY THIS TO ALL OF THEM?
        return Integer.reverseBytes(ByteBuffer.wrap(src).getInt());
    }

    static int intFromBytesRange(byte[] src, int start) {
        return intFromBytes(Arrays.copyOfRange(src, start, start + 4));
    }

    static float floatFromBytes(byte[] src) {
        assert src.length <= 4; // should be == maybe? idk
        return ByteBuffer.wrap(src).getFloat();
    }

    static float floatFromBytesRange(byte[] src, int start) {
        return floatFromBytes(Arrays.copyOfRange(src, start, start + 4));
    }

    static double doubleFromBytes(byte[] src) {
        assert src.length <= 8; // should be == maybe? idk
        return ByteBuffer.wrap(src).getDouble();
    }

    static double doubleFromBytesRange(byte[] src, int start) {
        return doubleFromBytes(Arrays.copyOfRange(src, start, start + 8));
    }
}
