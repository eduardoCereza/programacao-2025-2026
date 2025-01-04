package OpMode.Autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import OpMode.Subsystems.BucketServos;
import OpMode.Subsystems.ClawServo;
import OpMode.Subsystems.ExtendoServos;
import OpMode.Subsystems.IntakeServos;
import OpMode.Subsystems.ViperSlides;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@Config
@Autonomous(name = "AutonomousBaseCode", group = "Examples")
public class AutonomousBaseCode extends OpMode {

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private ViperSlides viperSlides;

    // Servo Subsystems
    private BucketServos bucketServos;
    private ClawServo clawServo;
    private ExtendoServos extendoServos;
    private IntakeServos intakeServos;

    private int pathState;



    // Define Poses and Paths
    private final Pose startPose = new Pose(8, 80, Math.toRadians(270));
    private final Pose scorePose = new Pose(14, 129, Math.toRadians(315));


    private Path scorePreload;

    public void buildPaths() {

        /* There are two major types of paths components: BezierCurves and BezierLines.
         *    * BezierCurves are curved, and require >= 3 points. There are the start and end points, and the control points.
         *    - Control points manipulate the curve between the start and end points.
         *    - A good visualizer for this is [this](https://pedro-path-generator.vercel.app/).
         *    * BezierLines are straight, and require 2 points. There are the start and end points.
         * Paths have can have heading interpolation: Constant, Linear, or Tangential
         *    * Linear heading interpolation:
         *    - Pedro will slowly change the heading of the robot from the startHeading to the endHeading over the course of the entire path.
         *    * Constant Heading Interpolation:
         *    - Pedro will maintain one heading throughout the entire path.
         *    * Tangential Heading Interpolation:
         *    - Pedro will follows the angle of the path such that the robot is always driving forward when it follows the path.
         * PathChains hold Path(s) within it and are able to hold their end point, meaning that they will holdPoint until another path is followed.
         * Here is a explanation of the difference between Paths and PathChains <https://pedropathing.com/commonissues/pathtopathchain.html> */

        /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */
        scorePreload = new Path(new BezierLine(new Point(startPose), new Point(scorePose)));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());


    }


    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);
        buildPaths();

        // Initialize ViperSlides
        DcMotorEx slideMotorLeft = hardwareMap.get(DcMotorEx.class, "slideMotorLeft");
        DcMotorEx slideMotorRight = hardwareMap.get(DcMotorEx.class, "slideMotorRight");
        TouchSensor limitSwitch = hardwareMap.get(TouchSensor.class, "limitSwitch");
        viperSlides = new ViperSlides(slideMotorLeft, slideMotorRight, limitSwitch, 0.01, 0.0, 0.005);

        // Initialize Bucket Servos
        bucketServos = new BucketServos(
                hardwareMap.get(Servo.class, "bucketServoRight"),
                hardwareMap.get(Servo.class, "bucketServoLeft")
        );

        // Initialize Claw Servo
        clawServo = new ClawServo(
                hardwareMap.get(Servo.class, "clawServo")
        );

        // Initialize Extendo Servos
        extendoServos = new ExtendoServos(
                hardwareMap.get(Servo.class, "extendoServoRight"),
                hardwareMap.get(Servo.class, "extendoServoLeft")
        );

        // Initialize Intake Servos
        intakeServos = new IntakeServos(
                hardwareMap.get(Servo.class, "intakeServoRight"),
                hardwareMap.get(Servo.class, "intakeServoLeft")
        );

    }



    @Override
    public void loop() {
        // Update follower and path state
        follower.update();
        autonomousPathUpdate();

        // Update subsystems
        viperSlides.update();

        // Telemetry feedback
        telemetry.addData("Path State", pathState);
        telemetry.addData("Slide Target", viperSlides.getTarget());
        telemetry.addData("Slide Position", viperSlides.getSlidePosition());
        telemetry.addData("Extendo State", extendoServos.isExtended() ? "Extended" : "Retracted");
        telemetry.update();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                // Follow path to score
                follower.followPath(scorePreload);
                viperSlides.setTarget(ViperSlides.Target.HIGH); // Set slides to high position
                bucketServos.transferPosition(); // Prepare bucket in transfer position
                clawServo.ClosedPosition(); // Close claw for scoring
                extendoServos.extend(); // Extend servos for scoring
                intakeServos.transferPosition(); // Set intake to transfer position
                setPathState(1);
                break;
            case 1:
                // Check if reached scoring pose
                if (Math.abs(follower.getPose().getX() - scorePose.getX()) < 1 &&
                        Math.abs(follower.getPose().getY() - scorePose.getY()) < 1) {
                    bucketServos.depositPosition(); // Score by tipping bucket
                    clawServo.OpenPosition(); // Open claw after scoring
                    intakeServos.intakePosition(); // Prepare intake for next cycle
                    setPathState(2);
                }
                break;
            case 2:
                // Maintain state to ensure scoring is completed
                if (pathTimer.getElapsedTimeSeconds() > 1.0) {
                    bucketServos.transferPosition(); // Reset bucket to transfer position
                    clawServo.OpenPosition(); // Open claw to prepare for next cycle
                    extendoServos.retract(); // Retract extendo servos
                    setPathState(-1); // End auto
                }
                break;
        }
    }

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    public void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }
}
