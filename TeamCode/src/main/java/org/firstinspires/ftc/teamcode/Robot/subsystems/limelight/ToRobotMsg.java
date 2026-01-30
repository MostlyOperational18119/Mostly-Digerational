package org.firstinspires.ftc.teamcode.Robot.subsystems.limelight;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

// TODO: USE A FUCKING PROTOBUF YOU ABSOLUTE MUPPET (FROM: DAMIEN, TO: FUTURE DAMIEN)
public class ToRobotMsg {
    public MessageType type;
    public HashMap<ResultType, Object> results;

    ToRobotMsg(byte[] data) {
        assert new String(Arrays.copyOf(data, 4)).equals("LSRV");

        Log.i("ToRobotMsg", Arrays.toString(data));

        byte typeByte = data[0x5];
        type = MessageType.values()[typeByte];

        switch (type) {
            case CurrentData:
                // Please be correct
                // Length of other data message stuff

                if (this.results == null) this.results = new HashMap<>();
                this.results.clear();

                byte resultType = data[0x7];

                int resultsPos = 0x8;

                // BallLine
                if ((resultType & 0x1) != 0x0) {
                    // pretend this doesn't exist for my sake :D
                    resultsPos++;

                    // Get the number of balls
                    int ballCount = data[resultsPos];
                    resultsPos++;

                    this.results.put(ResultType.BallCount, ballCount);
                }

                // Basalt
                if ((resultType & 0x2) != 0x0) {
                    this.results.put(ResultType.Basalt, null);
                }

                // PnP
                if ((resultType & 0x4) != 0x0) {
                    this.results.put(ResultType.PnP, null);
                }

                // AprilTag
                if ((resultType & 0x8) != 0x0) {
                    ArrayList<AprilTagResult> tagResults = new ArrayList<>();

                    byte aprilTagResultLength = data[resultsPos];

                    int posOrig = resultsPos;

                    while (resultsPos < (posOrig + aprilTagResultLength)) {
                        tagResults.add(new AprilTagResult(Arrays.copyOfRange(data, resultsPos, resultsPos+AprilTagResult.APRIL_TAG_SIZE)));

                        resultsPos += AprilTagResult.APRIL_TAG_SIZE;
                    }

                    this.results.put(ResultType.AprilTag, tagResults);
                }
        }

    }

    public enum ResultType {
        None,
        BallCount,
        Basalt,
        PnP,
        AprilTag
    }

    public enum MessageType {
        Connected(0x0),
        CurrentData(0x1);

        private Byte type;
        private Byte[] otherData;

        MessageType(int type) {
            this.type = (byte) type;
        }

        MessageType(byte type) {
            this.type = type;
        }

        public byte getTypeByte() {
            return type;
        }
    }

    public enum BallColor {
        GREEN,
        EMPTY,
        PURPLE;
    }
}
