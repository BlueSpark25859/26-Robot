package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "shooterandintaketest")
public class shooterandintake extends LinearOpMode {
        public void runOpMode() {
            DcMotorEx ShooterR = hardwareMap.get(DcMotorEx.class, "shooterR");


            ShooterR.setDirection(DcMotorSimple.Direction.REVERSE);

            ShooterR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            DcMotor Intake = hardwareMap.get(DcMotor.class, "intake1");

            Intake.setDirection(DcMotor.Direction.FORWARD);

            Intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            double velocityS;

            waitForStart();

            double Ipower = 0;

            while (opModeIsActive()) {

            //Give initial power to motors
            if (gamepad1.b) {
                Ipower = 0.8;
            } else {
                Ipower = 0;
            }

            if (gamepad1.a) {
                Ipower = -0.8;
            }

                if (gamepad1.right_bumper) {
                    velocityS = 1400;
                } else {
                    velocityS = 0;
                }

                if (gamepad1.left_bumper) {
                    velocityS = 1700;
                }

                ShooterR.setVelocity(-velocityS);

                telemetry.addData("VelocidadShooter", ShooterR.getVelocity());
                telemetry.update();
                telemetry.addData("Velocidad Intake: ", Intake.getPower());
                telemetry.update();

            }
        }
    }
