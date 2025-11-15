package org.firstinspires.ftc.teamcode.everything.limelight;

import android.util.Log;

import java.util.Arrays;

public class ToRobotMsg {
    public MessageType type;

    public byte[] otherData;

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
    };

    ToRobotMsg(byte[] data) {
        assert new String(Arrays.copyOf(data, 4)).equals("LSRV");

        Log.i("ToRobotMsg", Arrays.toString(data));

        byte typeByte = data[0x5];
        type = MessageType.values()[typeByte];

        switch (type) {
            case CurrentData:
                // Please be correct
                // Length of other data message stuff
                assert data[0x6] == 0xA;

                otherData = Arrays.copyOfRange(data, 0x7, 0x11);

                Log.i("ToRobotMsg", String.format("Length: %d", otherData.length));

                assert otherData.length == 0xA;
        }

    }
}
