package org.firstinspires.ftc.teamcode.pedroPathing.Comp;

public class Config {
    public final int HorizontalSlidesLength = 32; //cm
    public final String SlidesServoName = "SlideServo";
    public final int SlideServoMotionRange = 90;
    //Vertical SLides
    public final String StarboardSlideName = "StarboardSlide";
    public final String PortSlideName = "PortSlide";
    //drivetrain
    public final String backLeft = "backLeft";
    public final String frontLeft = "frontLeft";
    public final String backRight = "backRight";
    public final String frontRight = "frontRight";
    //Intake
    public final String clawServoName = "clawServo";
    public final String wristServoName = "wristServo";
    //intake wrist states
    public final double wristPickupPose = 0;
    public final double wristTransferPose = 0;
    public final double wristInitPose = 0;
    public final double wristTravelPose = 0;
    //intake claw states
    public final double clawClosePose = 0;
    public final double clawOpenPose = 1;
    //Outtake
    public final String starboardServoName = "starboardServo";
    public final String portServoName = "portServo";
    //outtake states
    public final double transferPose = 0.0;
    public final double scorePose = 0.0;
    public final double travelPose = 0.0;
    public final double initPose = 0.0;
}
