package org.firstinspires.ftc.teamcode.Robot.subsystems;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class Limelight {
    DataInputStream in = null;
    DataOutputStream out = null;
    Socket socket;
    int chosenGoal = 0;
    //    Indexer.BallColor[] balls = new Indexer.BallColor[9];
    public HashMap<ToRobotMsg.ResultType, Object> results;

    public Limelight() throws IOException {
        connect();
    }

    public boolean update() {
        try {
            Log.i("BetterLimelight", "hello 3");

            if (in.available() > 0) {
                Log.i("BetterLimelight", String.format("hello 3 yes input %d", in.available()));

                byte[] data = new byte[in.available()];
                in.readFully(data);

                if (new String(Arrays.copyOf(data, 5)).equals("Hello")) { // new byte[] { 0x48,0x65,0x6C,0x6C,0x6F }
                    Log.i("BetterLimelight", "LL says hi :D");
                } else {
                    Log.i("BetterLimelight", "hello 3 read data");

                    ToRobotMsg message = new ToRobotMsg(data);

                    switch (message.type) {
                        case Connected:
                            Log.i("BetterLimelight", "LimeLight says hi");
                            break;
                        case CurrentData:

//                                    ballGoalColor = message.otherData[0];
                            results = message.results;
                            break;
                        default:
                            Log.i("BetterLimelight", "what");
                    }
                }

                Log.i("BetterLimelight", "hello 3 done loop");

            } else {
                Log.i("BetterLimelight", "hello 4 no");

                ToLimelightMsg message = new ToLimelightMsg((byte) chosenGoal);

                Log.i("BetterLimelight", Arrays.toString(message.getData()));

                out.write(message.getData());
            }
            return true;
        } catch (IOException e) {
            Log.e("BetterLimelight", e.getLocalizedMessage());

            return false;
        }
    }

    // Doesn't need to be public, probably just going to cause problems
    private void connect() throws IOException {
        socket = new Socket("172.29.0.1", 8888);

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public Optional<Object> getResult(ToRobotMsg.ResultType type) {
        if (results == null || !results.containsKey(type)) return Optional.empty();

        return Optional.ofNullable(results.get(type));
    }

    public Optional<Integer> getBallCount() {
        if (results == null || !results.containsKey(ToRobotMsg.ResultType.BallLine)) return Optional.empty();

        return Optional.ofNullable((Integer) results.get(ToRobotMsg.ResultType.BallLineCount));
    }

    public Optional<Integer[]> getPattern() {
        if (results != null && results.containsKey(ToRobotMsg.ResultType.BallLine)) {
            AprilTagResult result = (AprilTagResult) results.get(ToRobotMsg.ResultType.AprilTag);

            switch (result.tagID) {
                case 21:
                    return Optional.of(new Integer[]{0, 1, 1});
                case 22:
                    return Optional.of(new Integer[]{1, 0, 1});
                case 23:
                    return Optional.of(new Integer[]{1, 1, 0});
            }
        }

        return Optional.empty();
    }
}
