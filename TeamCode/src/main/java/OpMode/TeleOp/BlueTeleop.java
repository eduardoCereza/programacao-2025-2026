package OpMode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import OpMode.TeleOp.Subsystems.GamePieceDetection;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;


@Config
@TeleOp(name = "BlueTeleop", group = "Active")
public class BlueTeleop extends OpMode {

    // Viper Slide Variables
    private PIDController controller;
    public static double p = 0.01, i = 0, d = 0.0;
    public static double f = 0.1;
    public static int target = 0;
    private final double ticks_in_degree = 537.7 / 360;
    private DcMotorEx slidemotorright;
    private DcMotorEx slidemotorleft;

    private Follower follower;
    private final Pose startPose = new Pose(0,0,0);
    private FtcDashboard dashboard;

    // REV Touch Sensor (Limit Switch)
    private TouchSensor limitSwitch;  // Declare the touch sensor
    private boolean limitSwitchPreviouslyPressed = false; // if the limit switch was already pressed

    //Intake
    private DcMotor intakemotor; // intake motor
    private ColorSensor colorSensor; // intake color sensor
    private GamePieceDetection gamePieceDetection; // code to detect color
    private boolean hasRumbled = false; // Flag to track if rumble has been

    // Loop Time
    private ElapsedTime loopTimer;




    @Override
    public void init() {
        // Initialize GamePieceDetection
        gamePieceDetection = new GamePieceDetection(hardwareMap.get(ColorSensor.class, "colorSensor"));

        // Initialize the loop timer
        loopTimer = new ElapsedTime();

        // Initialize Viper Slide
        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        // Pedro TeleOp
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);

        slidemotorleft = hardwareMap.get(DcMotorEx.class, "slidemotorleft");
        slidemotorleft.setDirection(DcMotorSimple.Direction.REVERSE);
        slidemotorleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slidemotorleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        slidemotorright = hardwareMap.get(DcMotorEx.class, "slidemotorright");
        slidemotorright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slidemotorright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Initialize Dashboard
        dashboard = FtcDashboard.getInstance();

        // Initialize REV Touch Sensor (Limit Switch)
        limitSwitch = hardwareMap.get(TouchSensor.class, "limitSwitch"); // Assign the touch sensor

        // Initialize motors and sensors
        intakemotor = hardwareMap.get(DcMotor.class, "intakemotor");
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

        // Initialize GamePieceDetection
        gamePieceDetection = new GamePieceDetection(colorSensor);
    }
    @Override
    public void start() {
        follower.startTeleopDrive();
    }
    @Override
    public void loop() {
        /* Update Pedro to move the robot based on:
        - Forward/Backward Movement: -gamepad1.left_stick_y
        - Left/Right Movement: -gamepad1.left_stick_x
        - Turn Left/Right Movement: -gamepad1.right_stick_x
        - Robot-Centric Mode: false
        */

        follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, false);
        follower.update();



        // Update the game piece color detection
        gamePieceDetection.detectColor();
        String detectedColor = gamePieceDetection.getDetectedColor();

        // Trigger rumble when a correct color (blue or yellow) is detected and intaked
        if ((detectedColor.equals("Blue") || detectedColor.equals("Yellow")) && !hasRumbled) {
            gamepad1.rumble(1000);  // Rumble for 1 second
            hasRumbled = true;     // Set flag to prevent repeated rumble
        }
        // Reset flag if the color changes to an invalid or "None"
        if (!detectedColor.equals("Blue") && !detectedColor.equals("Yellow")) {
            hasRumbled = false; // Reset the flag
        }



        // Check if the detected color is the opponent's color (assuming the opponent's color is Red)
        if (detectedColor.equals("Red")) {
            // Opponent's color detected, outake immediately at 0.3 power
            intakemotor.setPower(0.5);
        } else {
            // Check if left or right bumper is pressed for intake/outtake control
            if (gamepad1.left_bumper) {
                // Full intake power when left bumper is pressed
                intakemotor.setPower(-1.0);
            } else if (gamepad1.right_bumper) {
                // Outtake at 0.3 power when right bumper is pressed
                intakemotor.setPower(
                        0.5);
            } else {
                // Stop motor if no bumpers are pressed
                intakemotor.setPower(0);
            }

            loopTimer.reset();
        }

        // Viper Slide Control (PID)
        controller.setPID(p, i, d);
        int slidePosLeft = slidemotorleft.getCurrentPosition();
        int slidePosRight = slidemotorright.getCurrentPosition();
        double pidLeft = controller.calculate(slidePosLeft, target);
        double pidRight = controller.calculate(slidePosRight, target);
        double ff = Math.cos(Math.toRadians(target / ticks_in_degree)) * f;
        double powerLeft = pidLeft + ff;
        double powerRight = pidRight + ff;

        slidemotorleft.setPower(powerLeft);
        slidemotorright.setPower(powerRight);

        // Button presses to change target for the Viper slide
        if (gamepad1.y) {
            target = 2950;
        }
        if (gamepad1.a) {
            target = 0;
        }
        if (gamepad1.x) {
            target = 900;
        }
        if (gamepad1.b) {
            target = 1400;
        }

        // **Limit Switch Functionality (Debounce)**
        boolean isLimitSwitchPressed = limitSwitch.isPressed();
        if (isLimitSwitchPressed && !limitSwitchPreviouslyPressed) {
            // Reset encoders to zero
            slidemotorleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slidemotorright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // Set target position to 0
            slidemotorleft.setTargetPosition(0);
            slidemotorright.setTargetPosition(0);

            // Re-enable running without encoders after reset
            slidemotorleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slidemotorright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            telemetry.addData("Limit Switch", "Resetting encoders");
        }

        // Update the state variable
        limitSwitchPreviouslyPressed = isLimitSwitchPressed;

        // Measure and report loop time
        double loopTimeMs = loopTimer.milliseconds();
        telemetry.addData("Loop Time (ms)", loopTimeMs);

        // Telemetry for debugging and visualization
        telemetry.addData("posLeft", slidePosLeft);
        telemetry.addData("posRight", slidePosRight);
        telemetry.addData("target", target);
        telemetry.addData("Detected Color", detectedColor);
        /* Telemetry Outputs of our Follower */
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading in Degrees", Math.toDegrees(follower.getPose().getHeading()));

        telemetry.update();
    }


}
