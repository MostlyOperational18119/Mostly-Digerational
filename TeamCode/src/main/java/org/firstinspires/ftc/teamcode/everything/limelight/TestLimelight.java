package org.firstinspires.ftc.teamcode.everything.limelight;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.everything.Indexer;
import org.firstinspires.ftc.teamcode.everything.Methods;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

@TeleOp(name = "Test LimeLight")
public class TestLimelight extends Methods {
    @Override
    public void runOpMode() {
        DataInputStream in = null;
        DataOutputStream out = null;
        Socket socket;
        String error = "none?";
        boolean connected = true;

        try {
           socket = new Socket("172.29.0.1", 8888); // used to be

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            error = e.getLocalizedMessage();

            connected = false;
        }

        waitForStart();
        Indexer.BallColor[] balls = new Indexer.BallColor[9];
        int ballGoalColor = -1;

        int chosenGoal = 0;


        while (opModeIsActive()) {
            Log.i("TestLimelight", "hello 1");


            if (connected) {
                telemetry.addLine("Connected");

                Log.i("TestLimelight", "hello 2");

                try {
                    Log.i("TestLimelight", "hello 3");

                    if (in.available() > 0) {
                        Log.i("TestLimelight", String.format("hello 3 yes input %d", in.available()));
                        telemetry.addLine("Wow look, input");

                        byte[] data = new byte[in.available()];
                        in.readFully(data);

                        Log.i("TestLimelight", Arrays.toString(Arrays.copyOf(data, 5)));

                        if (new String(Arrays.copyOf(data, 5)).equals("Hello")) { // new byte[] { 0x48,0x65,0x6C,0x6C,0x6F }
                            Log.i("TestLimelight", "LL says hi :D");
                        } else {

                            Log.i("TestLimelight", "hello 3 read data");

                            ToRobotMsg message = new ToRobotMsg(data);

                            switch (message.type) {
                                case Connected:
                                    telemetry.addLine("LimeLight says hi");
                                    break;
                                case CurrentData:
                                    telemetry.addLine("LimeLight gave us some results :D");

                                    ballGoalColor = message.otherData[0];
                                    for (int i = 1; i <= balls.length; i++) {
//                                        telemetry.addData("The number thing: ", message.otherData[i]);
                                        Log.i("TestLimelight", String.format("Number thing: %d", message.otherData[i]));
                                        balls[i] = Indexer.BallColor.values()[message.otherData[i]];
                                    }
                                    break;
                                default:
                                    Log.i("TestLimelight", "what");
                                    telemetry.addLine("Huh?");
                            }
                        }

                        Log.i("TestLimelight", "hello 3 done loop");

                    } else {
                        Log.i("TestLimelight", "hello 4 no");
                        telemetry.addLine("No input D:");

                        ToLimelightMsg message = new ToLimelightMsg((byte) chosenGoal);

                        Log.i("TestLimelight", Arrays.toString(message.getData()));

                        out.write(message.getData());
                    }
                } catch (IOException e) {
                    connected = false;

                    telemetry.addLine(e.getLocalizedMessage());
                }
            } else {
                Log.i("TestLimelight", "hello not connected");
                telemetry.addLine("Not connected");

                telemetry.addLine(error);

                try {
                    socket = new Socket("172.29.0.1", 8888);

                    in = new DataInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());

                    connected = true;
                } catch (Exception e) {
                    error = e.getLocalizedMessage();
                }
            }

            telemetry.addLine((ballGoalColor == 0) ? "Blue goal:" : "Red Goal:");
            telemetry.addData("Ball colors:", Arrays.toString(balls));

            if (gamepad1.aWasPressed()) {
                if (chosenGoal == 0) chosenGoal++;
                else chosenGoal--;
            }

            telemetry.update();
            sleep(200);
        }
    }
}
