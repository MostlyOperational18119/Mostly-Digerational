package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public abstract class Methods extends LinearOpMode {
    //defines all hardware
    DcMotor motorFR, motorFL, motorBR, motorBL, intake, outtake, liftR, liftL;
    Servo revolver, launcherYaw, launcherPitch, transferServo, limelightServo, intakeRamp;
    Limelight3A limelight;
    RevColorSensorV3 colorSensor;
    DigitalChannel breakBeamSensor;
    Indexer indexer = new Indexer(this);
    float turn, strafe, forwards, motorFRPower, motorBRPower, motorFLPower, motorBLPower; //driver controls
    //    double currentRevolver, currentintakeRamp, currentTransferServo;
    double transferServoUp = 0.0;

    public enum BallColor {
        GREEN,
        EMPTY,
        PURPLE;
    }

    //public BallColor[] ballcolor = new BallColor[3];
    boolean fire, transferToggle, cycleLeft, cycleRight, toGreen, toPurple;

    //apriltag detection stuff (ALEX ADD COMMENTS PLEASE)
    public VisionPortal visionPortal;
    public AprilTagProcessor aprilTag;
    List<AprilTagDetection> currentApriltagDetections;

    //initializes all the hardware and the apriltag detection
    public void initialize() {
        motorFR = hardwareMap.dcMotor.get("motorFR");
        motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFL = hardwareMap.dcMotor.get("motorFL");
        motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBR = hardwareMap.dcMotor.get("motorBR");
        motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBL = hardwareMap.dcMotor.get("motorBL");
        motorBL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake = hardwareMap.dcMotor.get("intake");
        outtake = hardwareMap.dcMotor.get("outtake");
        liftR = hardwareMap.dcMotor.get("liftR");
        liftL = hardwareMap.dcMotor.get("liftL");

        launcherYaw = hardwareMap.servo.get("launcherYaw");
        launcherPitch = hardwareMap.servo.get("launcherPitch");
        revolver = hardwareMap.servo.get("revolver");
        transferServo = hardwareMap.servo.get("transferServo");
        limelightServo = hardwareMap.servo.get("limelightServo");
        intakeRamp = hardwareMap.servo.get("intakeRamp");

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        breakBeamSensor= hardwareMap.get(DigitalChannel.class, "beam_sensor");
        breakBeamSensor.setMode(DigitalChannel.Mode.INPUT);
        colorSensor = hardwareMap.get(RevColorSensorV3.class, "colorSensor");


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

        motorFRPower = (float) Range.clip(motorFRPower, -1.0, 1.0);
        motorFLPower = (float) Range.clip(motorFLPower, -1.0, 1.0);
        motorBRPower = (float) Range.clip(motorBRPower, -1.0, 1.0);
        motorBLPower = (float) Range.clip(motorBLPower, -1.0, 1.0);

        motorFL.setPower(motorFLPower);
        motorFR.setPower(motorFRPower);
        motorBL.setPower(motorBLPower);
        motorBR.setPower(motorBRPower);

    }

    public void detectAprilTag() {
        currentApriltagDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentApriltagDetections) {
            telemetry.addData("ID: ", detection.id);

        }
    }

    public void saarangHateLoveButton() {
        if (Math.random() >= 0.5) {
            telemetry.addLine("fuck you saarang");
        } else {
            telemetry.addLine("kiss me saarang");
        }
    }
}

