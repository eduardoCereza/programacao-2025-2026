package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;

@TeleOp(name = "Modo Teleoperado", group="Teleoperado")
public class Modo_Teleoperado extends OpMode {
    private Follower follower;
    private final Pose startPose = new Pose(0, 0, Math.toRadians(180));
    DcMotor slide;
    int estadoMove, estadoServo;

    Servo servo, garra;

    @Override
    public void init(){

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);

        slide = hardwareMap.get(DcMotor.class, "slide");
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    public void start(){
        follower.startTeleopDrive();
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

        telemetry.update();

    }

    public void slide(double power){
        int position = slide.getCurrentPosition();
        double control = gamepad2.left_stick_y;
        if (control > 0){
            slide.setPower(power);
        } else if (control < 0) {
            slide.setPower(-power);
        }else {
            slide.setTargetPosition(position);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slide.setPower(1);
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
        }else if(gamepad1.triangle){
            //clipar
            estadoServo = 3;
        }

        if (estadoServo == 1){
            //pegar sample
            servo.setPosition(1);

        } else if (estadoServo == 2) {
            //pegar clip/ deixar na cesta

            servo.setPosition(0.5);
        } else if (estadoServo == 3) {
            //clipar
            estadoServo = 3;
        }
    }

    public void servoClip(){
        if (gamepad2.right_bumper){
            garra.setPosition(1);
        }else{
            garra.setPosition(0);
        }
    }

}
