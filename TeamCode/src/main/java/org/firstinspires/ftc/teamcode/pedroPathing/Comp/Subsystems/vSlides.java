package org.firstinspires.ftc.teamcode.pedroPathing.Comp.Subsystems;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Comp.Config;

public class vSlides {

    enum slideState{
        FLOOR, SKY, CUSTOM
    }
    //declerations
    private Config config;
    private double targetPosition;
    private double integralSum;
    private DcMotor slides;
    private DcMotor slides2;
    private double Kp;
    private double Ki;
    private double Kd;
    private double lastError;
    private ElapsedTime timer = new ElapsedTime();
    //constructor
    public vSlides() {
            config = new Config();
            slides = hardwareMap.get(DcMotor.class, config.StarboardSlideName);
            slides2 = hardwareMap.get(DcMotor.class, config.PortSlideName);
            integralSum = 0;
            Kp = 0;
            Ki = 0;
            Kd = 0;
            lastError = 0;
            slides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            targetPosition = 0;

    }
        /**
         *PID
         *
         */
        public void updateSlides(){
            double error = targetPosition - slides.getCurrentPosition();
            integralSum += error * timer.seconds();
            double derivitave = (error - lastError)/ timer.seconds();
            lastError = error;

            timer.reset();
            double output = (error*Kp)+(derivitave*Kd)+(integralSum*Ki);
            slides.setPower(output);
            slides2.setPower(output);
        }
        public void setSlidesTargetPosition(double pose){
            targetPosition = pose;
        }
    }


