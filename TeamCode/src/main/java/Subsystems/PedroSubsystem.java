package Subsystems;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class PedroSubsystem extends DriveSubsystem {
    Pose2d pose;

    public PedroSubsystem(HardwareMap hardwareMap, Pose2d pose) {
        super(hardwareMap);
        this.pose = pose;
    }
}
