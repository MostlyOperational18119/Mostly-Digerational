package org.firstinspires.ftc.teamcode.Robot.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(23)
            .forwardZeroPowerAcceleration(-41.1102)
            .lateralZeroPowerAcceleration(-65.114)
            .useSecondaryTranslationalPIDF(true)
            .useSecondaryHeadingPIDF(true)
            .useSecondaryDrivePIDF(true)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.06, 0, 0, 0))
            .secondaryTranslationalPIDFCoefficients(new PIDFCoefficients(0, 0, 0.01, 0.01))
            .headingPIDFCoefficients(new PIDFCoefficients(1, 0, 0.085, 0.01))
            .secondaryHeadingPIDFCoefficients(new PIDFCoefficients(0.15, 0, 0.01, 0.01))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.025, 0, 0.0001, 0.6, 0.01))
            .secondaryDrivePIDFCoefficients(new FilteredPIDFCoefficients(0.02, 0, 0.0005, 0.6, 0.01))
            .centripetalScaling(0.0002);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("frontRight")
            .rightRearMotorName("backRight")
            .leftRearMotorName("backLeft")
            .leftFrontMotorName("frontLeft")
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .xVelocity(75.6281)
            .yVelocity(58.1404);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);
    public static PinpointConstants localizerConstants = new PinpointConstants()
            //.yawScalar(1.05)
            .forwardPodY(3.837451)
            .strafePodX(0.152644)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pinpointLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .build();
    }

}
