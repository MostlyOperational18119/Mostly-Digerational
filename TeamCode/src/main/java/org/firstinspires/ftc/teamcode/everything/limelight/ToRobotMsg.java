package org.firstinspires.ftc.teamcode.everything.limelight;

import java.util.Arrays;

public class ToRobotMsg {
    byte[] magic = new byte[] { 0x4C, 0x53, 0x52, 0x56, 0x00 };
    public MessageType type;

    public byte[] otherData;

    public enum MessageType {
        Connected(0x0),
        CurrentData(0x1);

        private Byte type;
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
        assert magic == Arrays.copyOf(data, 5);

        byte typeByte = data[0x5];
        type = MessageType.values()[typeByte];

//        switch ()

    }
}
