package TeleOp;

import Commands.Custom.DefaultDriveCommand;
import Subsystems.DriveSubsystem;
import Subsystems.RobotSubsystem;

import com.arcrobotics.ftclib.command.CommandScheduler;
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


        initialize(hardwareMap);

        CommandScheduler.getInstance().setDefaultCommand(drive,
                new DefaultDriveCommand(
                        drive,
                        () -> driverPad.getLeftX(),
                        () -> driverPad.getLeftY(),
                        () -> driverPad.getRightX(),
                        drive.getHeadingRads()
                )
        );

        configureOperator();


        waitForStart();

        while (opModeIsActive()){

            telemetry.addData("Gamepad1 Ry", driverPad.getRightY());
            telemetry.addData("Gamepad1 Rx", driverPad.getRightX());
            telemetry.addData("Gamepad1 Ly", driverPad.getLeftY());
            telemetry.addData("Gamepad1 Lx", driverPad.getLeftX());
            telemetry.addData("Heading", drive.getHeadingRads());


            telemetry.update();


        }
        CommandScheduler.getInstance().reset();
    }


    public void configureOperator() {

    }
}
