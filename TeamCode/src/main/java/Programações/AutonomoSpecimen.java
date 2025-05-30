package Programações;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import Robô.AtuadorDoisEstagios_Servos;
import Robô.AtuadorDoisEstagios_VerticalHorizontal;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@Autonomous(name = "Clip México Oficial")
public class AutonomoSpecimen extends OpMode {
    private Servo servo;
    Pose pose;
    AtuadorDoisEstagios_VerticalHorizontal atuador;
    AtuadorDoisEstagios_Servos servos;
    private Follower follower;
    private Timer pathTimer, opmodeTimer;
    private int pathState;
    private PathChain trajetória1, trajetória2;

    private final Pose startPose = new Pose(0, 70, Math.toRadians(180.00));
    private final Pose ClipPose = new Pose(23, 70, Math.toRadians(180.00));
    private final Pose Move1 = new Pose(23, 50, Math.toRadians(180.00));
    private final Pose Move2 = new Pose(40, 50, Math.toRadians(180.00));
    private final Pose Move3 = new Pose(40, 20, Math.toRadians(180.00));

    public void buildPaths() {
        trajetória1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(ClipPose)))
                .setConstantHeadingInterpolation(ClipPose.getHeading())

                .setPathEndTimeoutConstraint(0)

                .addPath(new BezierLine(new Point(ClipPose), new Point(Move1)))
                .setConstantHeadingInterpolation(Move1.getHeading())

                .setPathEndTimeoutConstraint(200)
                .addParametricCallback(0.5, () -> atuador.extender_horizontal())

                .build();

        trajetória2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(Move1), new Point(Move2)))
                .setConstantHeadingInterpolation(Move2.getHeading())

                .setPathEndTimeoutConstraint(0)

                .addPath(new BezierLine(new Point(Move2), new Point(Move3)))
                .setConstantHeadingInterpolation(Move3.getHeading())

                .setPathEndTimeoutConstraint(200)
                .addParametricCallback(0.5, () -> servos.abrir())

                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                if (!follower.isBusy()){
                    follower.followPath(trajetória1, true);
                }
                setPathState(1);
                break;
            case 1:
                if(!follower.isBusy()){
                    follower.followPath(trajetória2, true);
                }
                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    //loop
    @Override
    public void loop() {
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();

        pose = follower.getPose();

        follower.update();
        autonomousPathUpdate();
    }

    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        Constants.setConstants(FConstants.class, LConstants.class);
        follower =  new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);
        buildPaths();

        servo = hardwareMap.get(Servo.class, "servo1");
        servo.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void stop() {

    }
}