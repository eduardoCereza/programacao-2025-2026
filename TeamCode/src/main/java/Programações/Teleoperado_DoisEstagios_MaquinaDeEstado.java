package Programações;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.util.Constants;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@TeleOp(name = "teleoperadodoisestagios", group = "Examples")
public class Teleoperado_DoisEstagios_MaquinaDeEstado extends OpMode {
    private ElapsedTime delayTimer = new ElapsedTime();
    private boolean delayAtivado = false;
    private DcMotorEx vertical, horizontal;
    private Servo garraVer, garraHor;
    private Servo garraVerY, garraHorY;
    String controlVer;
    String controlHor;
    String controlGarraV, controlGarraH;
    TouchSensor toqueVer, toqueHor;
    private Follower follower;
    private final Pose startPose = new Pose(0,0,0);

    @Override
    public void init() {
        vertical = hardwareMap.get(DcMotorEx.class, "vertical");
        vertical.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        vertical.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        vertical.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        horizontal = hardwareMap.get(DcMotorEx.class, "vertical");
        horizontal.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        horizontal.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        controlVer = "recuado";

        controlGarraV = "aberto";
        garraVer.setPosition(1.0);
        controlGarraH = "aberto";
        garraHor.setPosition(1.0);

        garraVerY.setPosition(0.0);
        garraHorY.setPosition(1.0);

        garraVer = hardwareMap.get(Servo.class, "garraVer");
        garraHor = hardwareMap.get(Servo.class, "garraHor");

        garraVerY = hardwareMap.get(Servo.class, "garraVerY");
        garraHorY = hardwareMap.get(Servo.class, "garraHorY");

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        follower.startTeleopDrive();
    }

    @Override
    public void loop(){

        if(controlVer == "recuado"){
            extender_horizontal();
            if (controlHor == "extendido"){
                fechar_garra_horizontal();
                recuar_horizontal();
                if (controlHor == "recuado"){
                    //transfere já que o slide vai tocar no sensor
                    //garra vertical vai fechar e a horizontal abrir
                   extender_vertical();
                }
            }
        }
        if (controlHor == "extendido"){
            abrir_garra_vertical();
            recuar_vertical();
        }
        //esse sensor tem q ser pressionado não segurado
        if (toqueHor.isPressed() && !delayAtivado) {
            garraHorY.setPosition(1.0);
            garraVerY.setPosition(0.0);
            delayTimer.reset();
            delayAtivado = true;
        }

        if (delayAtivado) {
            if (delayTimer.milliseconds() > 1000 && delayTimer.milliseconds() <= 2000) {
                garraHor.setPosition(1.0);
                garraVer.setPosition(0.0);
            } else if (delayTimer.milliseconds() > 2000) {
                garraVerY.setPosition(1.0);
                delayAtivado = false;
            }
        }
        if (toqueVer.isPressed()){
            garraHorY.setPosition(0.0);
            garraVerY.setPosition(0.0);
        }

        abortar_maquina();
        //maquina de estado

        follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        follower.update();

        /* Telemetry Outputs of our Follower */
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading in Degrees", Math.toDegrees(follower.getPose().getHeading()));

        /* Update Telemetry to the Driver Hub */
        telemetry.update();

    }

    @Override
    public void stop() {
    }
    public void extender_vertical(){
        if (gamepad2.a) {
            vertical.setTargetPosition(1000);
            vertical.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            vertical.setPower(1.0);

            controlVer = "extendido";
        }
    }
    public void recuar_vertical(){
        if (gamepad2.b){
        vertical.setTargetPosition(0);
        vertical.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        vertical.setPower(1.0);

        controlVer = "recuado";
        }
    }
    public void extender_horizontal(){
        if (gamepad2.a) {
            horizontal.setTargetPosition(1000);
            horizontal.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            horizontal.setPower(1.0);

            controlHor = "extendido";
        }
    }
    public void recuar_horizontal(){
        if (gamepad2.b) {
            horizontal.setTargetPosition(0);
            horizontal.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            horizontal.setPower(1.0);
            controlHor = "recuado";
        }
    }
    public void abrir_garra_vertical(){
        if(gamepad2.y){
            //abrir garra vertical
            garraVer.setPosition(1.0);
            controlGarraV = "aberto";
        }
    }

    public void fechar_garra_horizontal(){
        if(gamepad2.y){
            //fechar garra horintal
            garraHor.setPosition(0.0);
            controlGarraH = "fechado";
        }
    }
    public void abortar_maquina(){
        if(gamepad2.x){
            horizontal.setTargetPosition(0);
            horizontal.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            horizontal.setPower(1.0);
            controlHor = "recuado";

            vertical.setTargetPosition(0);
            vertical.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            vertical.setPower(1.0);

            controlVer = "recuado";

            controlGarraV = "aberto";
            garraVer.setPosition(1.0);
            controlGarraH = "aberto";
            garraHor.setPosition(1.0);

            garraVerY.setPosition(0.0);
            garraHorY.setPosition(1.0);
        }
    }
}