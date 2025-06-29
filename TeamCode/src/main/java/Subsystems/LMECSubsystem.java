package Subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class LMECSubsystem extends SubsystemBase {

    public Servo LMECFront;
    public Servo LMECBACK;

    public LMECSubsystem(HardwareMap hardwareMap) {
        LMECFront = hardwareMap.get(Servo.class, "LMECFront");
        LMECBACK = hardwareMap.get(Servo.class, "LMECBACK");
    }
}
