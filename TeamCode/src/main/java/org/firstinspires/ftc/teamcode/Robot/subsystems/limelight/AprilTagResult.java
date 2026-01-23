package org.firstinspires.ftc.teamcode.Robot.subsystems.limelight;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class AprilTagResult {
    // 112 is the base size (no homography matrix sadly)
    // add the size of the homography matrix ourselves (just the date itself)
    public static int APRIL_TAG_SIZE = 112 + 72;

    public int tagID;
    public int tagHamming; // error bits, not too useful (could be removed/ignored)
    public float tagDecisionMargin; // same
    public double[][] homographyMatrix; // evil
    public double[] centerPoint;
    public double[][] cornerPoints;

    AprilTagResult(byte[] src) {
        // Nobody cares about the pointer
        // could chop it off before sending to save 4 bytes, but I'm lazy
        int pos = 3;

        tagID = intFromBytesRange(src, pos);

        pos += 4;

        tagHamming = intFromBytesRange(src, pos);

        pos += 4;

        tagDecisionMargin = floatFromBytesRange(src, pos);

        pos += 4;

        // Ignore the homography matrix pointer (doesn't matter imo)
        pos += 8;

        pos += 72; // 9 doubles, and doubles are 8 bytes each so 72 (8 is the size stuff which is earlier)

        centerPoint = new double[]{
                doubleFromBytesRange(src, pos),
                doubleFromBytesRange(src, pos + 8),
        };

        pos += 16;

        cornerPoints = new double[][]{
                {
                        doubleFromBytesRange(src, pos),
                        doubleFromBytesRange(src, pos + 8),
                },
                {
                        doubleFromBytesRange(src, pos + 16),
                        doubleFromBytesRange(src, pos + 24),
                }
        };

        pos += 32;

        // homography matrix stub
        homographyMatrix = new double[][]{
                {
                        doubleFromBytesRange(src, pos),
                        doubleFromBytesRange(src, pos + 8),
                        doubleFromBytesRange(src, pos + 16)
                },
                {
                        doubleFromBytesRange(src, pos + 24),
                        doubleFromBytesRange(src, pos + 32),
                        doubleFromBytesRange(src, pos + 40)
                },
                {
                        doubleFromBytesRange(src, pos + 48),
                        doubleFromBytesRange(src, pos + 56),
                        doubleFromBytesRange(src, pos + 64)
                }
        };

        pos += 72;

        // If not, we fucked up (too short or long, oops D:)
        assert (pos + 1) == APRIL_TAG_SIZE;

    }


    static int intFromBytes(byte[] src) {
        assert src.length <= 4; // should be == maybe? idk
        return ByteBuffer.wrap(src).getInt();
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



/*
Based on this C/C++ code: (size of struct is 112 according to sizeof)

// Represents the detection of a tag. These are returned to the user
// and must be individually destroyed by the user.
typedef struct apriltag_detection apriltag_detection_t;
struct apriltag_detection
{
    // a pointer for convenience. not freed by apriltag_detection_destroy.
    apriltag_family_t *family;

    // The decoded ID of the tag
    int id;

    // How many error bits were corrected? Note: accepting large numbers of
    // corrected errors leads to greatly increased false positive rates.
    // NOTE: As of this implementation, the detector cannot detect tags with
    // a hamming distance greater than 2.
    int hamming;

    // A measure of the quality of the binary decoding process: the
    // average difference between the intensity of a data bit versus
    // the decision threshold. Higher numbers roughly indicate better
    // decodes. This is a reasonable measure of detection accuracy
    // only for very small tags-- not effective for larger tags (where
    // we could have sampled anywhere within a bit cell and still
    // gotten a good detection.)
    float decision_margin;

    // The 3x3 homography matrix describing the projection from an
    // "ideal" tag (with corners at (-1,1), (1,1), (1,-1), and (-1,
    // -1)) to pixels in the image. This matrix will be freed by
    // apriltag_detection_destroy.
    matd_t *H;

    // The center of the detection in image pixel coordinates.
    double c[2];

    // The corners of the tag in image pixel coordinates. These always
    // wrap counter-clock wise around the tag.
    double p[4][2];
};

Also a duct-taped on double[3][3] at the end (basically the homography matrix data)
 */