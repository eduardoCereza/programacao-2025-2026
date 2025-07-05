package Subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import lib.Drivers.GoBildaPinpointDriver;

public class DriveSubsystem extends SubsystemBase {
    GoBildaPinpointDriver odo; // Declare OpMode member for the Odometry Computer

    DcMotor leftFront;
    DcMotor leftBack;
    DcMotor rightBack;
    DcMotor rightFront;
    
    

    public DriveSubsystem(final HardwareMap hardwareMap) {
        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        leftFront = hardwareMap.get(DcMotorEx.class, "frontLeft");
        leftBack = hardwareMap.get(DcMotorEx.class, "backLeft");
        rightBack = hardwareMap.get(DcMotorEx.class, "backRight");
        rightFront = hardwareMap.get(DcMotorEx.class, "frontRight");
        odo = hardwareMap.get(GoBildaPinpointDriver.class, "odo");

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public double getHeadingRads() {
        return odo.getHeading(AngleUnit.RADIANS);
    }

    public double getHeadingDeg() {
        return odo.getHeading(AngleUnit.DEGREES);
    }

    public void driveFieldCentric(double gamepadX, double gamepadY, double gamepadRX, double heading) {
        double rotX = gamepadX * Math.cos(-heading) - gamepadY * Math.sin(-heading);
        double rotY = gamepadX * Math.sin(-heading) + gamepadY * Math.cos(-heading);

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(gamepadRX), 1);
        double frontLeftPower = (rotY + rotX + gamepadRX) / denominator;
        double backLeftPower = (rotY - rotX + gamepadRX) / denominator;
        double frontRightPower = (rotY - rotX - gamepadRX) / denominator;
        double backRightPower = (rotY + rotX - gamepadRX) / denominator;
        if (!(Double.valueOf(frontLeftPower).isNaN() ||
                Double.valueOf(backLeftPower).isNaN() ||
                Double.valueOf(frontRightPower).isNaN() ||
                Double.valueOf(backRightPower).isNaN())) {

            leftFront.setPower(frontLeftPower);
            leftBack.setPower(backLeftPower);
            rightFront.setPower(frontRightPower);
            rightBack.setPower(backRightPower);
        }
    }

    public void setMotorPower (double power){
        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(power);
        rightBack.setPower(power);
    }
}

