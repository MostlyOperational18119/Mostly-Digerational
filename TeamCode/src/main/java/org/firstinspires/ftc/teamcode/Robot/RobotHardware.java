package org.firstinspires.ftc.teamcode.Robot;

import org.firstinspires.ftc.teamcode.Robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Lift;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.Robot.subsystems.Transfer;

public class RobotHardware {
    Drivetrain drivetrain = new Drivetrain();
    Indexer indexer = new Indexer();
    Transfer transfer = new Transfer();
    Outtake outtake = new Outtake();
    Lift lift = new Lift();
    Intake intake = new Intake();

    public void fullRobotInit() {

    }
}
