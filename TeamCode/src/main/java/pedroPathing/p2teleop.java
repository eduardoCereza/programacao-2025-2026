package pedroPathing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Two Player Teleop")
public class p2teleop extends LinearOpMode {

    // Declare hardware variables
    DcMotor slide_horizontalMotor, winch_rightMotor, winch_leftMotor, slide_verticalMotor;
    DcMotor topRight, topLeft, bottomRight, bottomLeft;  // Declare drivetrain motors
    Servo arm_clawServo, armServo, rdServo, ldServo;

    // Define State Machine Enum
    private enum RobotState {
        IDLE,
        INTAKE_SPECIMEN,  // Intake specimen logic
        INTAKE_SAMPLE_EXTEND,  // Extend horizontal slide
        INTAKE_SAMPLE_GRAB,    // Close claw
        INTAKE_SAMPLE_RETRACT, // Retract slide
        INTAKE_SAMPLE_ROTATE   // Rotate servos
    }

    private RobotState currentState = RobotState.IDLE; // Initial state

    @Override
    public void runOpMode() {
        // Initialize hardware
        slide_horizontalMotor = hardwareMap.dcMotor.get("slide_horizontalMotor");
        winch_rightMotor = hardwareMap.dcMotor.get("winch_rightMotor");
        winch_leftMotor = hardwareMap.dcMotor.get("winch_leftMotor");
        slide_verticalMotor = hardwareMap.dcMotor.get("slide_verticalMotor");

        // Initialize drivetrain motors
        topLeft = hardwareMap.dcMotor.get("topLeft");
        topRight = hardwareMap.dcMotor.get("topRight");
        bottomRight = hardwareMap.dcMotor.get("bottomRight");
        bottomLeft = hardwareMap.dcMotor.get("bottomLeft");

        arm_clawServo = hardwareMap.servo.get("arm_clawServo");
        armServo = hardwareMap.servo.get("armServo");
        rdServo = hardwareMap.servo.get("rdServo");
        ldServo = hardwareMap.servo.get("ldServo");

        // Reverse motors to ensure correct direction
        topLeft.setDirection(DcMotor.Direction.REVERSE);
        bottomLeft.setDirection(DcMotor.Direction.REVERSE);

        // Set zero power behavior for more controllable drivetrain
        topLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        topRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bottomLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bottomRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        while (opModeIsActive()) {
            // State Machine Logic
            switch (currentState) {
                case IDLE:
                    if (gamepad2.x) {
                        currentState = RobotState.INTAKE_SPECIMEN;
                    } else if (gamepad2.y) {
                        currentState = RobotState.INTAKE_SAMPLE_EXTEND;
                    }
                    break;

                case INTAKE_SPECIMEN:
                    // TODO: Add specimen intake logic
                    currentState = RobotState.IDLE;
                    break;

                case INTAKE_SAMPLE_EXTEND:
                    // TODO: Extend slide
                    currentState = RobotState.INTAKE_SAMPLE_GRAB;
                    break;

                case INTAKE_SAMPLE_GRAB:
                    // TODO: Close claw
                    currentState = RobotState.INTAKE_SAMPLE_RETRACT;
                    break;

                case INTAKE_SAMPLE_RETRACT:
                    // TODO: Retract slide
                    currentState = RobotState.INTAKE_SAMPLE_ROTATE;
                    break;

                case INTAKE_SAMPLE_ROTATE:
                    // TODO: Rotate diff servos
                    currentState = RobotState.IDLE;
                    break;
            }

            // Add drivetrain control to the IDLE state (you can modify this for another state if needed)
            driveTrainControl();

            telemetry.addData("State", currentState);
            telemetry.update();
        }
    }

    // Method to control the drivetrain using mecanum logic
    private void driveTrainControl() {
        double drive = -gamepad1.left_stick_y; // Forward/backward
        double strafe = gamepad1.left_stick_x; // Left/right
        double rotate = gamepad1.right_stick_x; // Rotation

        // Mecanum drive calculation
        double frontLeftPower = (drive + strafe + rotate);
        double backLeftPower = (drive - strafe + rotate);
        double frontRightPower = (drive - strafe - rotate);
        double backRightPower = (drive + strafe - rotate);

        // Normalize the values to ensure the power values are within the range [-1, 1]
        double max = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(backLeftPower), Math.max(Math.abs(frontRightPower), Math.abs(backRightPower))));
        if (max > 1.0) {
            frontLeftPower /= max;
            backLeftPower /= max;
            frontRightPower /= max;
            backRightPower /= max;
        }

        // Set motor powers
        topLeft.setPower(frontLeftPower);
        bottomLeft.setPower(backLeftPower);
        topRight.setPower(frontRightPower);
        bottomRight.setPower(backRightPower);
    }
}
