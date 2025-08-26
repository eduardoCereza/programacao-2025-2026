package org.firstinspires.ftc.teamcode.pedroPathing.Autonomous;

import com.pedropathing.localization.Encoder;
import com.pedropathing.localization.constants.DriveEncoderConstants;

public class LConstants {
    static {
        DriveEncoderConstants.forwardTicksToInches = 0.0098;
        DriveEncoderConstants.strafeTicksToInches = 0.0147;
        DriveEncoderConstants.turnTicksToInches = 0.0225;

        DriveEncoderConstants.robot_Width = 17;
        DriveEncoderConstants.robot_Length = 17;

        DriveEncoderConstants.leftFrontEncoderDirection = Encoder.FORWARD;
        DriveEncoderConstants.rightFrontEncoderDirection = Encoder.REVERSE;
        DriveEncoderConstants.leftRearEncoderDirection = Encoder.FORWARD;
        DriveEncoderConstants.rightRearEncoderDirection = Encoder.REVERSE;
    }
}




