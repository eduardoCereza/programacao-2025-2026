package Subsystems;


import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import Subsystems.LMECSubsystem;


public abstract class Robot extends LinearOpMode {

//    public SlideSubsystem slides;
//    public WristSubsystem wrist;
//    public IntakeSubsystem intake;
//    public SparkFunOTOSSubSystem drive;
    public LMECSubsystem lmec;

    public CommandScheduler cs = CommandScheduler.getInstance();

    public enum FSMStates {
        INTAKE,
        HANG,
        OUTTAKE,
        SPECIMEN,
        FOLD,
        NONE
    }
    public FSMStates robotState = FSMStates.NONE;

//    public void initialize(Pose2d pose) {
//
//        drive = new SparkFunOTOSSubSystem(hardwareMap, pose);
//        intake = new IntakeSubsystem(hardwareMap, telemetry);
//        wrist = new WristSubsystem(hardwareMap, telemetry);
//        slides = new SlideSubsystem(hardwareMap, telemetry);
//        lmec = new LMECSubsystem(hardwareMap);

//        CommandScheduler.getInstance().registerSubsystem(slides, wrist, intake, lmec);
//    }


    //TODO we probably dont need to but we could add in pos commands to get the pose of stuff just in case its used later
//    public Command intakePos() {
//        return new IntakePosCommand(slides, wrist, intake).alongWith(new InstantCommand(() -> setState(FSMStates.INTAKE)));
//    }

    public void update() {
        CommandScheduler.getInstance().run();
        telemetry.update();
    }
    public void end() {
        cs.reset();
    }


    public void setState(FSMStates state) {
        robotState = state;
    }

    public FSMStates getState() {
        return robotState;
    }



}