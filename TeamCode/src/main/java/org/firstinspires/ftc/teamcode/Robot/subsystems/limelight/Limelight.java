package org.firstinspires.ftc.teamcode.Robot.subsystems.limelight;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class Limelight {
    DataInputStream in = null;
    DataOutputStream out = null;
    Socket socket;
    int chosenGoal = 0;
    public HashMap<ToRobotMsg.ResultType, Object> results;

    public Limelight() throws IOException {
        connect();
    }

    public boolean update() {
        try {
            Log.i("Limelight", "hello 3");

            if (in.available() > 0) {
                Log.i("Limelight", String.format("Limelight gave us %d of data", in.available()));

                byte[] data = new byte[in.available()];
                in.readFully(data);

                if (new String(Arrays.copyOf(data, 5)).equals("Hello")) { // new byte[] { 0x48,0x65,0x6C,0x6C,0x6F }
                    Log.i("Limelight", "Limelight says hi :D");
                } else {
                    Log.i("Limelight", "Limelight actually sent us some data");

                    ToRobotMsg message = new ToRobotMsg(data);

                    switch (message.type) {
                        case Connected:
                            Log.i("Limelight", "LimeLight says it's connected");
                            break;
                        case CurrentData:

//                                    ballGoalColor = message.otherData[0];
                            results = message.results;
                            break;
                        default:
                            Log.i("Limelight", "Limelight sent us a message of unknown type. Huh");
                    }
                }

                Log.i("Limelight", "hello 3 done loop");

            } else {
                Log.i("Limelight", "Limelight sent us no message");

                ToLimelightMsg message = new ToLimelightMsg((byte) chosenGoal);

                Log.i("Limelight", Arrays.toString(message.getData()));

                out.write(message.getData());
            }
            return true;
        } catch (IOException e) {
            Log.e("Limelight", e.getLocalizedMessage());

            return false;
        }
    }

    // Doesn't need to be public, probably just going to cause problems
    private void connect() throws IOException {
        socket = new Socket("10.12.194.1", 8888);

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void setChosenGoal(int goal) {
        chosenGoal = goal;
    }

    public Optional<Object> getResult(ToRobotMsg.ResultType type) {
        if (results == null || !results.containsKey(type)) return Optional.empty();

        return Optional.ofNullable(results.get(type));
    }

    public Optional<Integer> getBallCount() {
        if (results == null || !results.containsKey(ToRobotMsg.ResultType.BallCount))
            return Optional.empty();

        return Optional.ofNullable((Integer) results.get(ToRobotMsg.ResultType.BallCount));
    }

    public Optional<Integer[]> getPattern() {
        if (results != null && results.containsKey(ToRobotMsg.ResultType.AprilTag)) {
            ArrayList<AprilTagResult> tagResults = (ArrayList<AprilTagResult>) results.get(ToRobotMsg.ResultType.AprilTag);

            for (AprilTagResult tagResult : tagResults) {
                switch (tagResult.tagID) {
                    case 21:
                        return Optional.of(new Integer[]{2, 1, 1});
                    case 22:
                        return Optional.of(new Integer[]{1, 2, 1});
                    case 23:
                        return Optional.of(new Integer[]{1, 1, 2});
                }
            }
        }

        return Optional.empty();
    }

    public Optional<AprilTagResult> getGoalTag() {
        if (results != null && results.containsKey(ToRobotMsg.ResultType.AprilTag)) {
            ArrayList<AprilTagResult> tagResults = (ArrayList<AprilTagResult>) results.get(ToRobotMsg.ResultType.AprilTag);

            for (AprilTagResult tagResult : tagResults) {
                // Is BLUE (and we chose blue) or RED (and we chose red)
                if ((chosenGoal == 0 && tagResult.tagID == 20) || (chosenGoal == 1 && tagResult.tagID == 23))
                    return Optional.of(tagResult);
            }
        }

        return Optional.empty();
    }

    public Optional<double[]> getGoalTagPosition() {
        Optional<AprilTagResult> goalTag = getGoalTag();

        // Get the tagPos if present, otherwise just don't
        return goalTag.map(aprilTagResult -> aprilTagResult.tagPos);
    }
}
