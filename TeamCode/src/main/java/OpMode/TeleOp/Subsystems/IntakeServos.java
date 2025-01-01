package OpMode.TeleOp.Subsystems;

import com.qualcomm.robotcore.hardware.Servo;

public class IntakeServos {

    private Servo IntakeServoRight;
    private Servo IntakeServoLeft;

    // Constructor to initialize servos
    public IntakeServos(Servo rightServo, Servo leftServo) {
        this.IntakeServoRight = rightServo;
        this.IntakeServoLeft = leftServo;

        // Scale the servos' range during initialization
        IntakeServoRight.scaleRange(0, 1);  // Scale for ExtendoServoRight (reversed)
        IntakeServoLeft.scaleRange(0, 1);   // Scale for ExtendoServoLeft (non-reversed)
    }

    // Method to move both servos to the Transfer Position
    public void intakePosition() {
        IntakeServoRight.setPosition(0.1);  // Set to maximum after scaling
        IntakeServoLeft.setPosition(0.9);   // Set to maximum after scaling
    }

    // Method to move both servos to the Intake Position
    public void transferPosition() {
        IntakeServoRight.setPosition(1);  // Set to minimum after scaling
        IntakeServoLeft.setPosition(0.0);   // Set to minimum after scaling
    }
}
