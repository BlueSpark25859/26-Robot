package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "Intake test by Vane")
public class Intaketest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Started");
        telemetry.update();

        DcMotor Intake = hardwareMap.get(DcMotor.class, "intake1");

        Intake.setDirection(DcMotor.Direction.FORWARD);

        Intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while (opModeIsActive()) {
            double Ipower = 0;

            //Give initial power to motors
            if (gamepad1.b) {
                Ipower = 0.8;
            } else {
                Ipower = 0;
            }

            if (gamepad1.a) {
                Ipower = -0.8;
            }


            Intake.setPower(Ipower);


            telemetry.addData("Velocidad Intake: ", Intake.getPower());
            telemetry.update();

        }
    }
}
