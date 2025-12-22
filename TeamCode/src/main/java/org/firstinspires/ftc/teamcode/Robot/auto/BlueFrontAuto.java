package org.firstinspires.ftc.teamcode.Robot.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "BFA3")
public class BlueFrontAuto {
    Pose start = new Pose(32.614, 134.376, Math.toRadians(90));
    Pose launch = new Pose(60.000, 84.000, Math.toRadians(135));
    Pose park = new Pose(36, 134, Math.toRadians(90));
    Follower follower;
    PathChain startToLaunch, launchToPark;

    int state = -1;
    int launchCount = 0;
    long launchDelayTimer = 0;
    int LAUNCH_DELAY_MS = 2500;
}
