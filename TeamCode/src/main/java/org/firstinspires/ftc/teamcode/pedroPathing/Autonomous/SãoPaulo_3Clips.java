package org.firstinspires.ftc.teamcode.pedroPathing.Autonomous;

//importações referentes ao pedro pathing

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.PIDFController;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Clips - off season oficial")
public class SãoPaulo_3Clips extends OpMode {

    public void clipPos(){
        garra.setPosition(0.95);
        clippos = 1;
        pickpos = 0;
        specimenpickpos = 0;
    }
    public void pickPos(){
        garra.setPosition(0.0);
        clippos = 0;
        pickpos = 1;
        specimenpickpos = 0;

    }
    public void specimenPickpos(){
        garra.setPosition(0.2);
        clippos = 0;
        pickpos= 0;
        specimenpickpos = 1;
    }
    public void closed(){
        ponta.setPosition(0.0);
        isopen = 0;
    }
    public void open(){
        ponta.setPosition(0.6);
        isopen = 1;
    }
    public void subir(int target){

        while (Left.getCurrentPosition() <= target){

            Left.setTargetPosition(target);
            Right.setTargetPosition(-target);

            Left.setPower(0.8);
            Right.setPower(0.8);
            holdArm = 0;

            if(explode == 1){
                break;
            }
        }
        Left.setPower(0.0);
        Right.setPower(0.0);
        holdArm =1;
    }
    public void descer(int target){

        while (Left.getCurrentPosition() >= target){

            Left.setTargetPosition(-target);
            Right.setTargetPosition(target);

            Left.setPower(-0.3);
            Right.setPower(-0.3);
            holdArm = 0;
            if(explode == 1){
                break;
            }
        }
        Left.setPower(0.0);
        Right.setPower(0.0);
        holdArm = 1;
    }
    public void hold(){
        PID controller;

        double minPower = 0.7;
        double maxPower = 1.0;
        controller = new PID(12, 4, 5, 13);
        controller.setInputRange(-4000, 4000);
        controller.setOutputRange(minPower, maxPower);

        double powerM = maxPower + controller.getComputedOutput(Left.getCurrentPosition());
        double powerM1 = maxPower + controller.getComputedOutput(Right.getCurrentPosition());

        Left.setTargetPosition(Left.getCurrentPosition());
        Right.setTargetPosition(Right.getCurrentPosition());

        Left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Left.setPower(powerM);
        Right.setPower(powerM1);

    }
    public void extender( int target){

        while (slide.getCurrentPosition() >= target){
            slide.setTargetPosition(target);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slide.setPower(-1.0);
            holdSlide = 0;
            if(explode == 1){
                break;
            }
        }
        slide.setPower(0.0);
        holdSlide = 1;
    }
    public void recuar(int target){

        while(slide.getCurrentPosition() <= target){
            slide.setTargetPosition(target);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slide.setPower(1.0);
            holdSlide = 0;
            if(explode == 1){
                break;
            }
        }
        slide.setPower(0.0);
        holdSlide = 1;
    }
    public void stay(){
        int currentPosition = slide.getCurrentPosition();

        slide.setTargetPosition(currentPosition); // Define a posição atual como alvo
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION); // Mantém o motor na posição
        slide.setPower(0.1); // Aplica uma pequena potência para segurar a posição
    }
    int isopen;
    int num, explode;
    int  specimenpickpos, clippos, pickpos;
    int holdSlide;
    int holdArm;
    Pose pose;
    private DcMotorEx slide, Left, Right;
    private Servo ponta; //servo1 da garra/ponta
    private Servo garra;
    private Follower follower; //sla tbm
    private Timer pathTimer, opmodeTimer; //sla ja veio no código
    private int pathState; //variável de controle das trajetórias e ações
    // y = lados (se for maior vai para a direita)
    // x = frente e tras (se for maior vai para frente)
    private final Pose startPose = new Pose(-74, -12, Math.toRadians(-180)); //posição inicial do robô
    private final Pose ClipPose = new Pose(-48, -12, Math.toRadians(-180.0)); //clipa
                                                                //mudei esse valor de 70
    private final Pose Control1   = new Pose(-80  , -75.0 , Math.toRadians(-180.00));
    private final Pose move2      = new Pose(-20  , -75.0 , Math.toRadians(-180.00)); //perto do sample
    private final Pose move3      = new Pose(-10.0  , -90 , Math.toRadians(-180.00)); //frente do primero sample
    private final Pose move4      = new Pose(-76.5  , -100.0 , Math.toRadians(-180.00)); //empurra o primeiro sample
    private final Pose clip2      = new Pose(-47  ,  -10 , Math.toRadians(-180.00));
    private final Pose moveX      = new Pose(-25  ,  -10, Math.toRadians(-190.00));
    private final Pose move7      = new Pose(-70  , -100 , Math.toRadians(-205.00));
    private PathChain traj1, traj2, traj4, traj5; //conjunto de trajetórias


    public void buildPaths() {

        traj1 = follower.pathBuilder()
                //vai para frente para clipar
                .addPath(new BezierLine(new Point(startPose), new Point(ClipPose)))
                .setConstantHeadingInterpolation(Math.toRadians(-180))
                .build();

        traj2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(ClipPose), new Point(Control1), new Point(move2), new Point(move3)))
                .setConstantHeadingInterpolation(Math.toRadians(-180.00))
                .addPath(new BezierLine(new Point(move3), new Point(move4)))
                .setConstantHeadingInterpolation(Math.toRadians(-180.00))
                //drift
                .build();


        traj4 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(move4), new Point(clip2)))
                .setConstantHeadingInterpolation(Math.toRadians(-180.00))
                .addPath(new BezierLine(new Point(clip2), new Point(moveX)))
                .setConstantHeadingInterpolation(Math.toRadians(-190))
                .build();

        traj5 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(moveX), new Point(move7)))
                .setConstantHeadingInterpolation(Math.toRadians(-205.00))
                .build();

        //off
    }

    //dependendo de como funcionar a movimentação do atuador, esses cases vão precisar ser dividos e dividir as trajetórias neles, testar antes
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(traj1, 1.0, true);
                closed();
                subir(900);
                extender(-1390);
                setPathState(1);
                break;

            case 1:
                if (!follower.isBusy() && pathState == 1){
                    extender(-2000);
                    open();
                    num = 1;
                }
                if (num == 1){
                    //mudar
                    recuar(-100);
                    descer(10);
                    specimenPickpos();
                    //0.75
                    follower.followPath(traj2, 0.9, true);
                    setPathState(2);
                }
                break;
            //arrasta os dois
            case 2:
                if (!follower.isBusy() && pathState ==2){
                    //0.6
                    closed();
                    num = 2;
                    setPathState(101);//
                }
                break;
            //pega o specimen
            case 3:
                if(!follower.isBusy() && pathState == 3){
                    if(num == 2 && ponta.getPosition() == 0.0)
                        subir(900);
                    follower.followPath(traj4, 0.8, true);
                    clipPos();
                    slide.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
                    extender(-1400);
                    setPathState(5);
                }
                break;
            case 4:
                if(num == 2 && ponta.getPosition() == 0.0){
                    subir(900);
                    //0.6
                    follower.followPath(traj4, 0.8, true);
                    clipPos();
                    slide.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
                    extender(-1370);
                    setPathState(5);
                }
                break;
            //clipa
            case 5:
                if (!follower.isBusy() && pathState == 5){
                    extender(-1800);
                    open();
                    num = 4;
                    setPathState(6);
                }
                break;
            case 6:
                if(!follower.isBusy() && num == 4 && pathState == 6){
                    recuar(-100);
                    descer(10);
                    specimenPickpos();
                    setPathState(7);
                }
                break;
            case 7:
                follower.followPath(traj5, 0.8, true);
                specimenPickpos();
                setPathState(10);
                break;
            case 8:
                if(!follower.isBusy() && pathState == 8){
                    closed();
                    pathTimer.resetTimer();
                    setPathState(104);}
                break;
            case 9:
                if (!follower.isBusy() && pathState == 9){
                    slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    subir(900);
                    extender(-1200);
                    clipPos();
                    setPathState(10);}
                break;
            case 10:
                if(!follower.isBusy() && pathState == 10){
                    setPathState(11);
                }
                break;
            case 11:

                break;
            //estaciona

            case 101:
                if(pathTimer.getElapsedTimeSeconds() > 0.5){
                    setPathState(3);
                }
            case 102:
                if(pathTimer.getElapsedTimeSeconds() > 0.5){
                    setPathState(8);
                }
            case 103:
                if(pathTimer.getElapsedTimeSeconds() > 0.5){
                    setPathState(6);
                }
            case 104:
                if(pathTimer.getElapsedTimeSeconds() > 0.5){
                    setPathState(9);
                }
            case 105:
                if(pathTimer.getElapsedTimeSeconds() > 0.5){
                    setPathState(2);
                }
        }
    }

    //controle das trajetórias
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
        telemetry.addData("braço left", Left.getCurrentPosition());
        telemetry.addData("slide position", slide.getCurrentPosition());
        telemetry.update();

        pose = follower.getPose();

        //talvez precise mudar
        if (holdSlide == 1){
            stay();
        }

        if (holdArm == 1){
            hold();
        }

        if (isopen == 0){
            ponta.setPosition(0);
        }
        if (clippos == 1){
            garra.setPosition(0.95);
        }
        if (pickpos == 1){
            garra.setPosition(0.0);
        }
        if (specimenpickpos == 1){
            garra.setPosition(0.7);
        }

        follower.update();
        autonomousPathUpdate();

    }

    //se precisar fazer alguma ação no init tem que por aq
    @Override
    public void init() {

        holdSlide = 0;

        holdArm = 1;

        isopen = 0;

        clippos = 1;
        pickpos = 0;
        specimenpickpos = 0;

        explode = 0;

        slide = hardwareMap.get(DcMotorEx.class, "slide");
        ponta = hardwareMap.get(Servo.class, "ponta");
        Left = hardwareMap.get(DcMotorEx.class, "Esq");
        Right = hardwareMap.get(DcMotorEx.class, "Dir");
        garra = hardwareMap.get(Servo.class, "garra");

        garra.setDirection(Servo.Direction.REVERSE);

        Left.setDirection(DcMotorEx.Direction.REVERSE);
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        Left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        ponta.setPosition(0);

        garra.setPosition(0.95);

        Constants.setConstants(FConstants.class, LConstants.class);
        follower =  new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);
        buildPaths();
    }

    //só um loop pra quando dar init
    @Override
    public void init_loop() {

    }

    //quando começar ele define a variável de controle como 0 e ja começa as ações
    @Override
    public void start() {
        Constants.setConstants(FConstants.class, LConstants.class);
        follower =  new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);
        buildPaths();
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    //quando mandar parar ele fará oque está aq
    @Override
    public void stop() {
        holdArm = 0;
        holdSlide = 0;
        isopen = 1;
        clippos = 0;
        pickpos = 0;
        specimenpickpos = 0;
        explode = 1;

    }
}