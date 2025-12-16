package org.firstinspires.ftc.teamcode.Robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Robot.Robot;

public class Lift {

    public DcMotorEx lift;

    public void init (HardwareMap hwMap) {
        lift = hwMap.get(DcMotorEx.class, "lift");
    }

}
