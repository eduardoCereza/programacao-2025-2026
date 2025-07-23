package org.firstinspires.ftc.teamcode.pedroPathing.Comp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class TeleOp extends OpMode {
    IntakeSubsystem intake;
    OuttakeSubsystem outtake;
    MecanumDrivetrain chassis;
    @Override
    public void init() {
        intake = new IntakeSubsystem(hardwareMap);
        outtake = new OuttakeSubsystem();
    }

    @Override
    public void loop() {
        outtake.updateSlides();
        chassis.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

    }
}
