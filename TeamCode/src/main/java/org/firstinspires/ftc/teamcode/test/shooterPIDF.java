package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp (name = "shooter")
public class shooterPIDF extends LinearOpMode {
    public void runOpMode() {
        DcMotorEx ShooterR = hardwareMap.get(DcMotorEx.class, "shooterR");

        ShooterR.setDirection(DcMotorSimple.Direction.FORWARD);

        ShooterR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double velocityS = 1600;

        waitForStart();

        while (opModeIsActive()) {

            PIDFCoefficients pidfCoefficients = new PIDFCoefficients(0.93,0,0,14.49);

            ShooterR.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

            if (gamepad1.right_bumper) {
                ShooterR.setVelocity(velocityS);
            } else {
                ShooterR.setVelocity(0);
            }

            if (gamepad1.left_bumper) {
                velocityS = 1700;
            }



            telemetry.addData("VelocidadShooter", ShooterR.getVelocity());
            telemetry.update();

        }
    }
}
