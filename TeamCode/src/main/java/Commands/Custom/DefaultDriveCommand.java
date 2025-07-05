package Commands.Custom;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.function.DoubleSupplier;

import Subsystems.DriveSubsystem;

public class DefaultDriveCommand extends CommandBase {
    DriveSubsystem driveSubsystem;
    DoubleSupplier x, y, rx;
    double heading;
    Telemetry telemetry;

    public DefaultDriveCommand(DriveSubsystem driveSubsystem, DoubleSupplier inputX, DoubleSupplier inputY, DoubleSupplier inputRx, double robotHeading) {
        this.driveSubsystem = driveSubsystem;
        this.x = inputX;
        this.y = inputY;
        this.rx = inputRx;
        this.heading = robotHeading;
        addRequirements(driveSubsystem);

    }

    @Override
    public void execute() {
        driveSubsystem.driveFieldCentric(x.getAsDouble() + getXModPower(), y.getAsDouble() + getYModPower(), rx.getAsDouble() + getRModPower(), heading);
    }

    public double getXModPower() {
        return 0.0;
    }

    public double getYModPower() {
        return 0.0;
    }

    public double getRModPower() {
        return 0.0;
    }


}
