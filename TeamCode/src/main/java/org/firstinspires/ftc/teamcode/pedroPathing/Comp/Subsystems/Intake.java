package org.firstinspires.ftc.teamcode.pedroPathing.Comp.Subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pedroPathing.Comp.Config;

public class Intake {
        private Config config;
        private Servo wrist;
        private Servo claw;

    public Intake(){
        config = new Config();
        claw = hardwareMap.get(Servo.class, config.clawServoName);
        wrist = hardwareMap.get(Servo.class, config.wristServoName);
    }
    //---------------------------------------------
    //THIS IS ALL THE WRIST CONTROLLING CODE
    //---------------------------------------------
    public enum WristState{
        TRANSFER, PICKUP, INIT, TRAVEL
    }
    public WristState wristState;

    public void transferPose(){
        wrist.setPosition(config.wristTransferPose);
        setWristState(WristState.TRANSFER);
    }
    public void travelPose(){
        wrist.setPosition(config.wristTravelPose);
        setWristState(WristState.TRAVEL);
    }
    public void pickupPose(){
        wrist.setPosition(config.wristPickupPose);
        setWristState(WristState.PICKUP);
    }
    public void init(){
        wrist.resetDeviceConfigurationForOpMode();
        wrist.setPosition(config.wristInitPose);
        setWristState(WristState.INIT);
    }
    public void setWristState(WristState state){
        wristState = state;
    }

    //---------------------------------------------
    //THIS IS ALL THE CLAW CONTROLLING CODE\\
    //---------------------------------------------
    public enum ClawState{
        CLOSE, OPEN
    }
    public ClawState clawState;
    public void openClaw() {
        claw.setPosition(config.clawOpenPose);
        setClawState(ClawState.OPEN);
    }
    public void closeClaw(){
        claw.setPosition(config.clawClosePose);
        setClawState(ClawState.CLOSE);
    }
    public void initClaw(){
        claw.resetDeviceConfigurationForOpMode();
        closeClaw();
    }
    public void setClawState(ClawState state){
        clawState = state;
    }

}
