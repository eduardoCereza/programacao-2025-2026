package Robô;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class AtuadorDoisEstagios_Servos {
    private Servo garra;

    public AtuadorDoisEstagios_Servos(HardwareMap hardwareMap){
        garra = hardwareMap.get(Servo.class, "vertical");
        //outros servos
    }
    public void abrir(){
        garra.setPosition(1.0);
    }
    public void fechar(){
        garra.setPosition(0.0);
    }
    //outros movimentos dos outros servos
}
