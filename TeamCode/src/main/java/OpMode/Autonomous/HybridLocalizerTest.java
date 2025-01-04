package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import OpMode.Autonomous.Localizers.HybridLocalizer;
import com.pedropathing.localization.Pose;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name = "HybridLocalizer Test", group = "Test")
public class HybridLocalizerTest extends LinearOpMode {

    private HybridLocalizer hybridLocalizer;
    private Follower follower;

    @Override
    public void runOpMode() {
        // Initialize the follower and hybrid localizer
        follower = new Follower(hardwareMap);
        hybridLocalizer = new HybridLocalizer(hardwareMap);

        telemetry.addLine("HybridLocalizer Initialized");
        telemetry.update();

        // Wait for the game to start
        waitForStart();

        while (opModeIsActive()) {
            hybridLocalizer.updateLocalization(follower.getPose());

            Pose temp = hybridLocalizer.getFusedPose();

            // Set the updated pose back to the follower
            follower.setPose(temp);

            // Display the updated fused pose
            telemetry.addData("Fused X", temp.getX());
            telemetry.addData("Fused Y", temp.getY());
            telemetry.addData("Fused Heading (deg)", Math.toDegrees(temp.getHeading()));
            telemetry.update();


        }

        // Stop the localizer when done
        hybridLocalizer.stop();
    }
}
