package pedroPathing.constants;

import com.pedropathing.localization.*;
import com.pedropathing.localization.constants.*;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class LConstants {
    static {
        ///  These values represent the distance of the odometry pods from the robot's center of rotation on the PEDRO robot coordinate grid.
        PinpointConstants.forwardY = 1;
        PinpointConstants.strafeX = -2.5;
        /// If the x value ticks down when the robot moves forward, invert the direction by changing the GoBildaPinpointDriver.EncoderDirection for the forwardEncoder.
        /// If the y value ticks down when the robot moves left, invert the direction by changing the GoBildaPinpointDriver.EncoderDirection for the strafeEncoder.

        PinpointConstants.distanceUnit = DistanceUnit.CM;
        PinpointConstants.hardwareMapName = "pinpoint";
        PinpointConstants.useYawScalar = false;
        PinpointConstants.yawScalar = 1.0;
        PinpointConstants.useCustomEncoderResolution = false;
        PinpointConstants.encoderResolution = GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD;
        PinpointConstants.customEncoderResolution = 13.26291192;
        PinpointConstants.forwardEncoderDirection = GoBildaPinpointDriver.EncoderDirection.REVERSED;
        PinpointConstants.strafeEncoderDirection = GoBildaPinpointDriver.EncoderDirection.FORWARD;
    }
}




