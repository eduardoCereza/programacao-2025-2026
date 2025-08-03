package org.firstinspires.ftc.teamcode.pedroPathing.Comp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Comp.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.pedroPathing.Comp.Subsystems.Outtake;
import org.firstinspires.ftc.teamcode.pedroPathing.Comp.Subsystems.hSlides;
import org.firstinspires.ftc.teamcode.pedroPathing.Comp.Subsystems.vSlides;

public class TeleOp extends OpMode {
    hSlides intakeSlides;
    vSlides Vslides;
    MecanumDrivetrain chassis;
    Intake intake;
    Outtake outtake;
    @Override
    public void init() {
        intakeSlides = new hSlides(hardwareMap);
        Vslides = new vSlides();
        intake = new Intake();
        outtake = new Outtake();
    }
    @Override
    public void loop() {
        Vslides.updateSlides();
        chassis.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

    }

}
