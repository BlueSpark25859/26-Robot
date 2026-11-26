package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Constants.Constants;


@TeleOp(name = "Intake test by Vane")
public class Intaketest extends LinearOpMode {

    double Ipower;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Started");
        telemetry.update();

        DcMotor Intake = hardwareMap.get(DcMotor.class, "intake1");

        Intake.setDirection(Constants.Intake.kIntakeDirection);

        Intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while (opModeIsActive()) {

            //Give initial power to motors
            if (gamepad1.b) {
                Ipower = Constants.Intake.kIntakePower;
            } else {
                Ipower = 0;
            }

            if (gamepad1.a) {
                Ipower = -Constants.Intake.kIntakePower;
            }


            Intake.setPower(Ipower);


            telemetry.addData("Velocidad Intake: ", Intake.getPower());
            telemetry.update();

        }
    }
}
