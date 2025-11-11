package org.firstinspires.ftc.teamcode.everything.limelight;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BetterLimelight {
    DataInputStream in = null;
    DataOutputStream out = null;
    Socket socket;

    BetterLimelight() throws IOException {
        socket = new Socket("172.29.0.21", 8888);

    }
}
