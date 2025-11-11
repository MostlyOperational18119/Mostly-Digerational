package org.firstinspires.ftc.teamcode.everything.limelight;

import org.firstinspires.ftc.teamcode.everything.Indexer;
import org.firstinspires.ftc.teamcode.everything.Methods;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TestLimelight extends Methods {
    @Override
    public void runOpMode() throws InterruptedException {
        DataInputStream in = null;
        DataOutputStream out = null;
        Socket socket;
        boolean connected = true;

        try {
           socket = new Socket("172.29.0.21", 8888);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            connected = false;
        }

        waitForStart();
        Indexer.BallColor[] balls = new Indexer.BallColor[9];


        while (opModeIsActive()) {
            if (connected) {
                telemetry.addLine("Connected");

                try {
                    if (in.available() > 0) {
                        telemetry.addLine("Wow look, input");

                        byte[] data = new byte[256];
                        in.readFully(data);

                        ToRobotMsg message = new ToRobotMsg(data);

                        switch (message.type) {
                            case Connected:
                                telemetry.addLine("LimeLight says hi");
                            case CurrentData:
                                telemetry.addLine("LimeLight gave us some results :D");

                                for (int i = 0; i < balls.length; i++) {
                                    balls[i] = Indexer.BallColor.values()[message.otherData[i]];
                                }
                        }

                    } else {
                        telemetry.addLine("No input D:");

                        ToLimelightMsg message = new ToLimelightMsg(ToLimelightMsg.MessageType.GetResult);

                        out.write(message.getData());
                    }
                } catch (IOException e) {
                    telemetry.addLine(e.getLocalizedMessage());
                }
            } else {
                telemetry.addLine("Not connected");
            }

            telemetry.addData("Ball colors:", balls);

            telemetry.update();
            sleep(20);
        }
    }
}
