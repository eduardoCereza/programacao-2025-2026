package Testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import Subsystems.DriveSubsystem;
import Subsystems.RobotSubsystem;

@Autonomous
public class MotorTest extends RobotSubsystem {

    DcMotor bob;

    @Override
    public void runOpMode() throws InterruptedException {
        initialize(hardwareMap);


        waitForStart();

            while (opModeIsActive()){
                drive.setMotorPower(1);
            }

    }
}
