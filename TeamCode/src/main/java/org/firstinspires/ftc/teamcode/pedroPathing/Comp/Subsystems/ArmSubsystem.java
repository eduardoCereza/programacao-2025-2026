package org.firstinspires.ftc.teamcode.pedroPathing.Comp.Subsystems;

import com.qualcomm.robotcore.hardware.Servo;

public class ArmSubsystem {
    private Servo shoulder;
    //states
    public ArmState state;
    public enum ArmState{
        TRANSFER, INIT, SCORE
    }
    public void init(){
        state = ArmState.INIT;
    }
    public void transferPose(){

    }
}
