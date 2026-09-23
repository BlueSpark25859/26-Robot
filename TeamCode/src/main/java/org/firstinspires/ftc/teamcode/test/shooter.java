package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class shooter extends LinearOpMode {
    public void runOpMode() {
        DcMotor ShooterR = hardwareMap.get(DcMotor.class, "shooterR");
        DcMotor ShooterL = hardwareMap.get(DcMotor.class, "shooterL");

        ShooterR.setDirection(DcMotorSimple.Direction.REVERSE);
        ShooterL.setDirection(DcMotorSimple.Direction.FORWARD);

        double powerS;

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.right_bumper) {
                powerS = 0.8;
            } else {
                powerS = 0;
            }

            ShooterR.setPower(powerS);
            ShooterL.setPower(powerS);

            telemetry.addData("VelocidadShooter", powerS);
            telemetry.update();

        }
    }
}
