package org.firstinspires.ftc.teamcode.everything.limelight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

public class ToLimelightMsg {
    byte[] magic = new byte[] { 0x43, 0x4C, 0x49, 0x45, 0x4E, 0x54, 0x00 };
    byte[] data = new byte[0xFF];

    public enum MessageType {
        Exit(0x0),
        GetResult(0x1),
        RunCommand(0x2),
        WriteFile(0x3),
        ReadFile(0x3);

        private Byte type;
        MessageType(int type) {
            this.type = (byte) type;
        }

        public byte getTypeByte() {
            return type;
        }
    }

    ToLimelightMsg(MessageType type) {
        byte typeByte = type.getTypeByte();

        IntStream.range(0, magic.length)
                .forEach((int i) -> {
                    data[i] = magic[i];
                });

        data[7] = typeByte;
    }

    public byte[] getData() {
        return data;
    }

}
