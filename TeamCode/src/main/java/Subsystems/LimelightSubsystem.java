package Subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;


import java.util.List;

public class LimelightSubsystem extends SubsystemBase {
    public Limelight3A limelight;

    public LimelightSubsystem(HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
    }

    //returns list of april tags seen at a given time
    public List<LLResultTypes.FiducialResult> getAprilTag(){
        //Get detection info
       return limelight.getLatestResult().getFiducialResults();
    }

    public List<LLResultTypes.DetectorResult>  getDetections(){
        return limelight.getLatestResult().getDetectorResults();
    }

    public void getDetections(double numDetections){
        //Get detection info
        LLResult result = limelight.getLatestResult();

        //array list of result time


    }
}