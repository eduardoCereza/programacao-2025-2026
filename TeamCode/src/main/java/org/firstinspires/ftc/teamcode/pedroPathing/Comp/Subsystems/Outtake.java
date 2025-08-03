package org.firstinspires.ftc.teamcode.pedroPathing.Comp.Subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pedroPathing.Comp.Config;

public class Outtake {
        private Servo starboard;
        private Servo port;
        Config config;
    public Outtake(){
        config = new Config();
        port = hardwareMap.get(Servo.class, config.portServoName);
        starboard = hardwareMap.get(Servo.class, config.starboardServoName);
    }
    public enum armState{
        SCORE, TRANSFER, INIT, TRAVEL
    }
    public void init(){

    }

}
