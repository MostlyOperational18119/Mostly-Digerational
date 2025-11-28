package org.firstinspires.ftc.teamcode.everything;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public abstract class Methods extends LinearOpMode {
    //defines all hardware
    DcMotor motorFR, motorFL, motorBR, motorBL, intake, liftR, liftL;
    DcMotorEx outtakeFlywheel;
    Servo revolver, daHood, transferServo, limelightServo;
    CRServo launcherYaw;
    VoltageSensor voltageSensor;
    Limelight3A limelight;
    RevColorSensorV3 colorSensor;
    DigitalChannel breakBeamSensor;
    Indexer indexer = new Indexer(this);
    float turn, strafe, forwards, motorFRPower, motorBRPower, motorFLPower, motorBLPower; //driver controls
    float P_FAR = 0.0F, P_CLOSE = 0.0F;
    int outtakeEncoder = 0;
    double power;
    double transferServoUp = 0.09;
    double outtakePower = 0.0;
    int maxRPM = 5900, targetRPM, measuredRPM;

    boolean fireGreen, firePurple, transferToggle, aimLeft, aimRight, toGreen, toPurple, intakeYes;
    boolean launchIdle = false;
    double revolverExpectedPosition = -1.0;

    //apriltag detection stuff (ALEX ADD COMMENTS PLEASE)
    public VisionPortal visionPortal;
    public AprilTagProcessor aprilTag;
    List<AprilTagDetection> currentApriltagDetections;
    boolean isBlue;
    double robotX = 0;
    double robotY = 0;
    double goalX;
    double goalY = 144;

    //initializes all the hardware and the apriltag detection
    public void initialize() {
        motorFR = hardwareMap.dcMotor.get("motorFR"); //also contains encoder for outtake
        motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFL = hardwareMap.dcMotor.get("motorFL");
        motorFL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBR = hardwareMap.dcMotor.get("motorBR");
        motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBL = hardwareMap.dcMotor.get("motorBL");
        motorBL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake = hardwareMap.dcMotor.get("intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        outtakeFlywheel = hardwareMap.get(DcMotorEx.class, "outtake");
        //liftR = hardwareMap.dcMotor.get("liftR");
        //liftL = hardwareMap.dcMotor.get("liftL");

        launcherYaw = hardwareMap.get(CRServo.class, "launcherYaw");
        daHood = hardwareMap.servo.get("daHood");
        revolver = hardwareMap.servo.get("revolver");
        transferServo = hardwareMap.servo.get("transferServo");
        limelightServo = hardwareMap.servo.get("limelightServo");
        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");

//        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        breakBeamSensor= hardwareMap.get(DigitalChannel.class, "beamSensor");
        breakBeamSensor.setMode(DigitalChannel.Mode.INPUT);
        colorSensor = hardwareMap.get(RevColorSensorV3.class, "colorSensor");

        if (isBlue) {goalX = 0;} else {goalX = 144;}


//        aprilTag = new AprilTagProcessor.Builder()
//                .setDrawAxes(true)
//                .setDrawCubeProjection(true)
//                .setDrawTagOutline(true)
//                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
//                .setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
//                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
//                .build();
//        VisionPortal.Builder builder = new VisionPortal.Builder();
//        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam"));
//        builder.enableLiveView(true);
//        builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);
//        builder.setAutoStopLiveView(false);
//        builder.addProcessor(aprilTag);
//        visionPortal = builder.build();

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

//    public void setIndexerIntake(int index) { //placeholder
//        switch (index) {
//            case 0:
//                revolver.setPosition(0.0);
//                break;
//            case 1:
//                revolver.setPosition(0.74);
//                break;
//            case 2:
//                revolver.setPosition(0.37);
//                break;
//        }
//    }
//
//    public void setIndexerOuttake(int index) {
//        switch (index) {
//            case 0:
//                revolver.setPosition(0.55);
//                break;
//            case 1:
//                revolver.setPosition(0.2);
//                break;
//            case 2:
//                revolver.setPosition(0.92);
//                break;
//        }
//    }



    public void detectAprilTag() {
        currentApriltagDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentApriltagDetections) {
            telemetry.addData("ID: ", detection.id);

        }
    }

    public double nicksLittleHelper() {
        double targetAngle;
        double targetPos;
        targetAngle = Math.atan((robotY-goalY)/(robotX-goalX));
        targetPos = targetAngle*51.724137931;
        return targetPos;
    }

    public void teamHateLoveButton() {
        if (Math.random() >= 0.5) {
            telemetry.addLine("fuck you saarang");
        } else {
            telemetry.addLine("kiss me saarang");
        }
    }
}

