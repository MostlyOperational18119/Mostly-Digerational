package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public abstract class Methods extends LinearOpMode {
    DcMotor motorFR, motorFL, motorBR, motorBL, intake, outtake, liftR, liftL;
    Servo revolver, launcherYaw, launcherPitch, transferServo, limelightServo, intakeGate;
    Limelight3A limelight;
    
    float turn, strafe, forwards;
    boolean fire, transferToggle, cycleLeft, cycleRight;
    public VisionPortal visionPortal;
    public AprilTagProcessor aprilTag;
    List<AprilTagDetection> currentApriltagDetections;
    public void initialize() {
        motorFR = hardwareMap.dcMotor.get("motorFR");
        motorFL = hardwareMap.dcMotor.get("motorFL");
        motorBR = hardwareMap.dcMotor.get("motorBR");
        motorBL = hardwareMap.dcMotor.get("motorBL");
        intake = hardwareMap.dcMotor.get("intake");
        outtake = hardwareMap.dcMotor.get("outtake");
        liftR = hardwareMap.dcMotor.get("liftR");
        liftL = hardwareMap.dcMotor.get("liftL");

        launcherYaw = hardwareMap.servo.get("launcherYaw");
        launcherPitch = hardwareMap.servo.get("launcherPitch");
        revolver = hardwareMap.servo.get("revolver");
        transferServo = hardwareMap.servo.get("transferServo");
        limelightServo = hardwareMap.servo.get("limelightServo");
        intakeGate = hardwareMap.servo.get("intakeGate");

        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        aprilTag = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagOutline(true)
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();
        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam"));
        builder.enableLiveView(true);
        builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);
        builder.setAutoStopLiveView(false);
        builder.addProcessor(aprilTag);
        visionPortal = builder.build();

    }
    public void drive() {
        //drive code with funny curve
        motorFR.setPower(Math.pow((forwards + strafe - turn), 1.67));
        motorFL.setPower(-Math.pow((forwards + strafe + turn), 1.67));
        motorBR.setPower(Math.pow((forwards - strafe - turn), 1.67));
        motorBL.setPower(-Math.pow((forwards - strafe + turn), 1.67));
    }

    public void launch() {
        //no outtake :(
    }
    public void detectAprilTag() {
        currentApriltagDetections = aprilTag.getDetections();
    }
}
