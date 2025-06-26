package TeleOp;

import Subsystems.DriveSubsystem;
import Subsystems.RobotSubsystem;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp
public class FieldCentricTest extends RobotSubsystem {

    protected GamepadEx driverPad;
    protected GamepadEx operatorPad;


    @Override
    public void runOpMode() {


        driverPad = new GamepadEx(gamepad1);
        operatorPad = new GamepadEx(gamepad2);


//      configureOperator();


        waitForStart();

        while (opModeIsActive()){




        }


//      public void configureOperator() {
//
//      }



    }
}
