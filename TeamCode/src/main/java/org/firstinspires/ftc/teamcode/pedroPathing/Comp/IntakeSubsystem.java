package org.firstinspires.ftc.teamcode.pedroPathing.Comp;


import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class IntakeSubsystem {
    Servo slides;
    Config config;

    public IntakeSubsystem(@NonNull HardwareMap hardwareMap){
        config = new Config();
        slides = hardwareMap.get(Servo.class, config.SlidesServoName);
    }

    /**
     * takes cm
     * @param customPose is position in centimeters
     */
    public void setPosition(int customPose){
        double ratio = 1.0/config.HorizontalSlidesLength;
        slides.setPosition(customPose*ratio);
    }






}
