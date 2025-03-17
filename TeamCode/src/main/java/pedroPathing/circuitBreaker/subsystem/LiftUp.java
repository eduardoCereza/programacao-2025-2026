package pedroPathing.circuitBreaker.subsystem;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class LiftUp {
    Telemetry telemetryA;
    DcMotor Lift;
    DcMotor Lift2;
    TouchSensor LiftHomeTouchSensor;
    AnalogInput potentiometer;
    int LiftEncoderVal_Required;
    boolean hasInitialized;
    boolean isLiftBusy;
    double power;
    Telemetry telemetry;


    public LiftUp( Telemetry telemetry, HardwareMap hardwareMap){
        this.Lift = (DcMotor)hardwareMap.get(DcMotor.class, "Lift");
        this.Lift2 = (DcMotor)hardwareMap.get(DcMotor.class, "Lift2");

        this.Lift.setDirection(DcMotor.Direction.FORWARD);
        this.Lift2.setDirection(DcMotor.Direction.FORWARD);

        this.Lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.Lift2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.LiftHomeTouchSensor = hardwareMap.get(TouchSensor.class, "LiftHome");

        this.LiftEncoderVal_Required = 0;
        this.hasInitialized = false;
        this.power = 0.0;
        this.telemetry = telemetry;
        this.isLiftBusy = false;


        telemetryA = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

    }

    public void setLiftPosition(int encoderVal){
        this.LiftEncoderVal_Required = encoderVal;
    }
    public boolean run(){

        if(hasInitialized == false){
            power = 1.0;
            Lift.setPower(power);
            Lift2.setPower(power);

            hasInitialized = true;
        }

        if(LiftEncoderVal_Required > Lift.getCurrentPosition()){

            telemetryA.addData("Lift  :  Current position", Lift.getCurrentPosition());
            telemetryA.addData("Lift :  Desired position", LiftEncoderVal_Required);
            telemetryA.update();

           // telemetry.addData("Lift  :  Current position", Lift.getCurrentPosition());
            //telemetry.addData("Lift :  Desired position", LiftEncoderVal_Required);
            isLiftBusy = true;
            return true;
        }

        Lift.setPower(0.0);
        Lift2.setPower(0.0);
        Lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Lift2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hasInitialized = false; //reset the variable
        isLiftBusy = false;
        return false;


    }

    public boolean isLiftBusy(){
        return isLiftBusy;
    }

}
