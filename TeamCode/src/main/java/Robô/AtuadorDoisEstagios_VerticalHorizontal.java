package Robô;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class AtuadorDoisEstagios_VerticalHorizontal {
    private DcMotorEx vertical, horizontal;
    String controlVer;
    String controlHor;
    public AtuadorDoisEstagios_VerticalHorizontal(HardwareMap hardwareMap){
        vertical = hardwareMap.get(DcMotorEx.class, "vertical");
        vertical.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        vertical.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        vertical.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        horizontal = hardwareMap.get(DcMotorEx.class, "vertical");
        horizontal.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        horizontal.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }
    public void extender_vertical(){
            vertical.setTargetPosition(1000);
            vertical.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            vertical.setPower(1.0);

            controlVer = "extendido";

    }
    public void recuar_vertical(){
        vertical.setTargetPosition(0);
        vertical.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        vertical.setPower(1.0);

        controlVer = "recuado";
    }
    public void extender_horizontal(){
        horizontal.setTargetPosition(1000);
        horizontal.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        horizontal.setPower(1.0);

        controlHor = "extendido";
    }
    public void recuar_horizontal(){
        horizontal.setTargetPosition(0);
        horizontal.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        horizontal.setPower(1.0);
        controlHor = "recuado";
    }

}
