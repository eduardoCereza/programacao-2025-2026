package TeleOp;

import Subsystems.Robot;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;




public class FieldCentricTest extends Robot {

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
