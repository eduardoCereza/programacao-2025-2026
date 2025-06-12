package Testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class MotorTest extends LinearOpMode{

    DcMotor bob;

    @Override
    public void runOpMode() throws InterruptedException {

        bob = hardwareMap.get(DcMotor.class, "bob");


        waitForStart();

            while (opModeIsActive()){

                bob.setPower(1);
            }

    }
}
