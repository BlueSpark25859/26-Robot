package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Constants.Constants;

@TeleOp (name = "shooter")
public class shooter extends LinearOpMode {
    public void runOpMode() {
        DcMotorEx ShooterR = hardwareMap.get(DcMotorEx.class, "shooterR");

        ShooterR.setDirection(Constants.Shooter.kShooterDirection);

        ShooterR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double velocityS;

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.right_bumper) {
                velocityS = Constants.Shooter.kShooterSpeed;
            } else {
                velocityS = 0;
            }

            if (gamepad1.left_bumper) {
                velocityS = 1700;
            }

            ShooterR.setVelocity(velocityS);

            telemetry.addData("VelocidadShooter", ShooterR.getVelocity());
            telemetry.update();

        }
    }
}
