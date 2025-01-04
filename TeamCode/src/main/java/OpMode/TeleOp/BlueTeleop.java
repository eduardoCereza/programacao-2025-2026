package OpMode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import OpMode.Subsystems.BucketServos;
import OpMode.Subsystems.GamePieceDetection;
import OpMode.Subsystems.ClawServo;
import OpMode.Subsystems.ExtendoServos;
import OpMode.Subsystems.IntakeMotor;
import OpMode.Subsystems.ViperSlides;
import OpMode.Subsystems.IntakeServos;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;

@Config
@TeleOp(name = "BlueTeleop", group = "Active")
public class BlueTeleop extends OpMode {

    // Viper Slide Variables
    public static double p = 0.01, i = 0, d = 0.0;
    public static double f = 0.1;
    private ViperSlides viperSlides;
    // PedroPathing Teleop
    private Follower follower;
    private final Pose startPose = new Pose(0, 0, 0);
    private FtcDashboard dashboard;

    // REV Touch Sensor (Limit Switch)
    private TouchSensor limitSwitch;

    // Servos
    private Servo intakeServoRight;
    private Servo intakeServoLeft;
    private IntakeServos intakeServos; // Intake subsystem instance
    private Servo extendoServoRight;
    private Servo extendoServoLeft;
    private ExtendoServos extendoServos;
    private ClawServo clawServo;
    private Servo bucketServoRight;
    private Servo bucketServoLeft;
    private BucketServos bucketServos;


    // Intake Motor and Color Sensor
    private DcMotor intakemotor;
    private IntakeMotor intakeMotor;
    private ColorSensor colorSensor;
    private GamePieceDetection gamePieceDetection;
    private boolean hasRumbled = false;

    // Loop Timer
    private ElapsedTime loopTimer;
    // Declare the timer for the extendo servo retraction
    private ElapsedTime retractTimer = new ElapsedTime();

    // Variables for Left Trigger Rising Edge Detection
    private boolean previousLeftTriggerState = false;
    private boolean currentLeftTriggerState = false;
    private boolean isClawOpen = false;

    @Override
    public void init() {
        // Initialize GamePieceDetection
        gamePieceDetection = new GamePieceDetection(hardwareMap.get(ColorSensor.class, "colorSensor"));

        // Initialize the loop timer
        loopTimer = new ElapsedTime();

        // Initialize Viper Slide
        viperSlides = new ViperSlides(
                hardwareMap.get(DcMotorEx.class, "slidemotorleft"),
                hardwareMap.get(DcMotorEx.class, "slidemotorright"),
                hardwareMap.get(TouchSensor.class, "limitSwitch"),
                p, i, d
        );

        // Initialize Pedro follower
        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);

        // Initialize Dashboard
        dashboard = FtcDashboard.getInstance();

        // Initialize REV Touch Sensor
        limitSwitch = hardwareMap.get(TouchSensor.class, "limitSwitch");

        // Initialize motors and sensors
        intakeMotor = new IntakeMotor(hardwareMap.get(DcMotor.class, "intakemotor"));
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

        // Initialize intake servos
        intakeServoRight = hardwareMap.get(Servo.class, "IntakeServoRight");
        intakeServoLeft = hardwareMap.get(Servo.class, "IntakeServoLeft");
        intakeServos = new IntakeServos(intakeServoRight , intakeServoLeft);

        // Initialize extendo servos
        extendoServoRight = hardwareMap.get(Servo.class, "ExtendoServoRight");
        extendoServoLeft = hardwareMap.get(Servo.class, "ExtendoServoLeft");
        extendoServos = new ExtendoServos(extendoServoRight, extendoServoLeft);

        // Initialize claw servo
        clawServo = new ClawServo(hardwareMap.get(Servo.class, "ClawServo"));

        // Set extendo servos to retracted position
        extendoServos.retract();

        // Set intake servos to transfer position
        intakeServos.transferPosition();

        // Initialize Bucket Servo
        bucketServoRight = hardwareMap.get(Servo.class, "BucketServoRight");
        bucketServoLeft = hardwareMap.get(Servo.class, "BucketServoLeft");
        bucketServos = new BucketServos(bucketServoRight, bucketServoLeft);


    }

    @Override
    public void start() {
        // Ensure the follower starts TeleOp drive
        follower.startTeleopDrive();
    }

    @Override
    public void loop() {
        // TeleOp movement
        follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, false);
        follower.update();

        // Game Piece Detection and Rumble Feedback
        gamePieceDetection.detectColor();
        String detectedColor = gamePieceDetection.getDetectedColor();
        if ((detectedColor.equals("Blue") || detectedColor.equals("Yellow")) && !hasRumbled) {
            gamepad1.rumble(1000);  // Rumble for 1 second
            hasRumbled = true;
        }
        if (!detectedColor.equals("Blue") && !detectedColor.equals("Yellow")) {
            hasRumbled = false;
        }

        // Opponent Color Detection (e.g., Red)
        if (detectedColor.equals("Red")) {
            intakeMotor.outtake();  // Outtake at low power
        } else if (gamepad1.left_bumper) {
            intakeMotor.intake();  // Intake
        } else if (gamepad1.right_bumper) {
            intakeMotor.outtake();  // Outtake
        } else {
            intakeMotor.stop();  // Stop intake motor
        }

        // Rising edge detection for left trigger to toggle claw
        currentLeftTriggerState = gamepad1.left_trigger > 0.3;  // Detect if the left trigger is pressed
        if (currentLeftTriggerState && !previousLeftTriggerState) {  // Rising edge
            // Toggle claw position on rising edge
            if (isClawOpen) {
                clawServo.closedPosition();  // Close the claw
            } else {
                clawServo.openPosition();  // Open the claw
            }
            // Flip the claw state
            isClawOpen = !isClawOpen;
        }
        previousLeftTriggerState = currentLeftTriggerState;  // Update the previous state

        // Bucket Servo Control Based on Slide Position and Right Trigger
        if (viperSlides.getSlidePositionRight() > 1950) {
            if (gamepad1.right_trigger > 0.1) {
                bucketServos.depositPosition(); // Move bucket to deposit position if right trigger is pressed and slides are up
            } else {
                bucketServos.transferPosition();   // Otherwise, set bucket transfer position
            }
        } else {
            bucketServos.transferPosition();       // If the slide position is not less than 1950, set bucket to 1
        }



        // Servo Control Logic
        if (gamepad1.dpad_right) {
            if (extendoServos.isExtended()) {
                intakeServos.intakePosition();
            } else {
                telemetry.addData("Warning", "Cannot move intake servos to intaking position while extendo servos are retracted!");
            }
        } else if (gamepad1.dpad_down) {
            if (intakeServos.isIntakePosition()) {
                // If intake servos are in intake position, move them to transfer position
                intakeServos.transferPosition();
            } else if (intakeServos.isTransferPosition()) {
                // If intake servos are already in transfer position, retract extendo servos
                extendoServos.retract();
            } else {
                telemetry.addData("Warning", "Intake servos must be in either intake or transfer position to proceed!");
            }
        } else if (gamepad1.dpad_up) {
            if (!extendoServos.isExtended()) {
                extendoServos.extend();
            }
        } else if (gamepad1.dpad_left) {
            // Move intake servos to transfer position
            intakeServos.transferPosition();
        }



        // Viper Slide Control (Predefined Targets)
        viperSlides.update();

        if (gamepad1.y) {
            viperSlides.setTarget(ViperSlides.Target.HIGH);
        }
        if (gamepad1.a) {
            viperSlides.setTarget(ViperSlides.Target.GROUND);
        }
        if (gamepad1.x) {
            viperSlides.setTarget(ViperSlides.Target.LOW);
        }
        if (gamepad1.b) {
            viperSlides.setTarget(ViperSlides.Target.MEDIUM);
        }

        // Telemetry for debugging and visualization
        telemetry.addData("Slide Position Left", viperSlides.getSlidePositionLeft());
        telemetry.addData("Slide Position Right", viperSlides.getSlidePositionRight());
        telemetry.addData("Slide Target", viperSlides.getTarget());
        telemetry.addData("Detected Color", detectedColor);
        /* Telemetry Outputs of our Follower */
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading in Degrees", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.update();
    }
}
