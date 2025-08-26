package org.firstinspires.ftc.teamcode.pedroPathing.Autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Modo Teleoperado", group="Teleoperado")
public class Modo_Teleoperado extends OpMode {
    private Follower follower;
    private final Pose startPose = new Pose(0, 0, Math.toRadians(180));
    DcMotor slide, left, right;
    int estadoMove, estadoServo;

    Servo ponta, garra;

    boolean holdSlide = false, holdArm = false;

    @Override
    public void init(){

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);

        slide = hardwareMap.get(DcMotor.class, "slide");
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        left = hardwareMap.get(DcMotorEx.class, "Esq");
        right = hardwareMap.get(DcMotorEx.class, "Dir");

        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        garra = hardwareMap.get(Servo.class, "garra");
        ponta = hardwareMap.get(Servo.class, "ponta");


    }

    public void start(){
        follower.startTeleopDrive();
        estadoMove = 1;
    }

    @Override
    public void loop(){

        if (gamepad1.a){
            estadoMove = 1;
        } else if (gamepad1.b) {
            estadoMove = 2;
        }
        double y = -gamepad1.left_stick_y;
        double x = -gamepad1.left_stick_x;
        double turn = -gamepad1.right_stick_x;

        if (estadoMove == 1) {
            follower.setTeleOpMovementVectors(y, x, turn);
            follower.update();
            telemetry.addLine("Chassi normal");

        } else if (estadoMove == 2) {
            follower.setTeleOpMovementVectors(-y, -x, -turn);
            follower.update();
            telemetry.addLine("Chassi inverso");
        }

        slide(0.5);
        servoMove();
        servoClip();
        moveActuator();


        telemetry.update();

    }

    public void slide(double power){
        int position = slide.getCurrentPosition();
        double control = gamepad2.left_stick_y;
        if (control > 0){
            slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            slide.setPower(power);
            holdSlide = false;
        } else if (control < 0) {
            slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            slide.setPower(-power);
            holdSlide = false;
        }else if (!holdSlide){
            slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            slide.setTargetPosition(position);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slide.setPower(1);
            holdSlide = true;
        }

        telemetry.addData("Posição slide: ", position);
    }

    public void servoMove(){
        if (gamepad2.cross){
            //pegar sample
            estadoServo = 1;
        }else if(gamepad2.circle){
            //pegar clip/ deixar na cesta
            estadoServo = 2;
        }else if(gamepad2.triangle){
            //clipar
            estadoServo = 3;
        }

        if (estadoServo == 1){
            //pegar sample
            garra.setPosition(1);

        } else if (estadoServo == 2) {
            //pegar clip/ deixar na cesta
            garra.setPosition(0.6);

        } else if (estadoServo == 3) {
            //clipar
            garra.setPosition(0);

        }
    }

    public void servoClip(){
        if (gamepad2.right_bumper){
            ponta.setPosition(1);
        }else{
            ponta.setPosition(0);
        }
    }

    public void moveActuator(){

        if (gamepad2.right_stick_y > 0) {
            left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                left.setPower(0.3);
                right.setPower(0.3);
                holdArm = false;
        } else if (gamepad2.right_stick_y < 0) {
            left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                left.setPower(-0.3);
                right.setPower(-0.3);
                holdArm = false;
        }
        else if (!holdArm){
                left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                left.setTargetPosition(left.getCurrentPosition());
                right.setTargetPosition(right.getCurrentPosition());

                left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                left.setPower(1);
                right.setPower(1);
                holdArm = true;
            }

    }
}
