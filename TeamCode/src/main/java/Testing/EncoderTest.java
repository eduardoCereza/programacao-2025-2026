package Testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import Drivers.GoBildaPinpointDriver;

@TeleOp
public class EncoderTest extends LinearOpMode {

    GoBildaPinpointDriver odo; // Declare OpMode member for the Odometry Computer

    @Override
    public void runOpMode() throws InterruptedException {
        // Find a motor in the hardware map named "Arm Motor"
        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo");


        waitForStart();

        while (opModeIsActive()) {
            // Get the current position of the motor
            int position = odo.getEncoderX();


            // Show the position of the motor on telemetry
            telemetry.addData("Encoder Position", position);
            telemetry.addData("Encoder Position", odo.getDeviceStatus());
            telemetry.addData("Encoder Position", odo.getPosX(DistanceUnit.CM));
            telemetry.update();
        }
    }



}
