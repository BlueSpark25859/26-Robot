package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp (name = "shooter")
public class shooter extends LinearOpMode {
    public void runOpMode() {
        DcMotorEx ShooterR = hardwareMap.get(DcMotorEx.class, "shooterR");

        ShooterR.setDirection(DcMotorSimple.Direction.FORWARD);

        ShooterR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double velocityS;

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.right_bumper) {
                velocityS = 2000;
            } else {
                velocityS = 0;
            }

            ShooterR.setVelocity(velocityS);

            telemetry.addData("VelocidadShooter", ShooterR.getVelocity());
            telemetry.update();

        }
    }
}
