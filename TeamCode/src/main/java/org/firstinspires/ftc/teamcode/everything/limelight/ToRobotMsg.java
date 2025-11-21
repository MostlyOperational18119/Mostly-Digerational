package org.firstinspires.ftc.teamcode.everything.limelight;

import android.util.Log;

import org.firstinspires.ftc.teamcode.everything.Indexer;

import java.util.Arrays;
import java.util.HashMap;

public class ToRobotMsg {
    public MessageType type;

    public byte[] otherData;

    public enum ResultType {
        None,
        BallLine,
        Basalt,
        PnP,
        AprilTag
    }

    public HashMap<ResultType, Object> results;

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

    ToRobotMsg(byte[] data) {
        assert new String(Arrays.copyOf(data, 4)).equals("LSRV");

        Log.i("ToRobotMsg", Arrays.toString(data));

        byte typeByte = data[0x5];
        type = MessageType.values()[typeByte];

        switch (type) {
            case CurrentData:
                // Please be correct
                // Length of other data message stuff
//                assert data[0x6] == 0xA;

                if (this.results == null) this.results = new HashMap<>();
                this.results.clear();

                byte resultType = data[0x7];

                int resultsPos = 0x8;

                // BallLine
                if ((resultType & 0x1) != 0x0) {
                    // pretend this doesn't exist for my sake :D
                    resultsPos++;
                    Indexer.BallColor[] ballColors = new Indexer.BallColor[9];


                    int start = resultsPos;
                    for (; resultsPos < (start + 0x9); resultsPos++) {
                        int num = data[resultsPos] & 0xFF;

                        if (num >= 0 && num < 3) ballColors[resultsPos - start] = Indexer.BallColor.values()[num];
                        else ballColors[resultsPos - start] = Indexer.BallColor.EMPTY;
                    }

                    if (ballColors != null) Log.i("ToRobotMsg", Arrays.toString(ballColors));
                    else Log.i("ToRobotMsg", "Ball colors are null. Huh?");

                    this.results.put(ResultType.BallLine, ballColors);
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
                    this.results.put(ResultType.AprilTag, null);
                }



                otherData = Arrays.copyOfRange(data, 0x7, 0x11);

                Log.i("ToRobotMsg", String.format("Length: %d", otherData.length));

                assert otherData.length == 0xA;
        }

    }
}
