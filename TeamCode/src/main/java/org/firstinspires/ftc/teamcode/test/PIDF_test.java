package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp
public class PIDF_test extends LinearOpMode {

    public DcMotorEx ShooterR;

    double highVelocity = 1400;
    double lowVelocity = 800;
    double currentTargetVelocity = highVelocity;

    //Values
    double F, P = 0;

    double[] incremental = {10.0, 1.0, 0.1, 0.01}; //Available sizes in which P,F is incremented

    int incrementIndex = 1; //Start incrementing by 1

    public void runOpMode() {
        ShooterR = hardwareMap.get(DcMotorEx.class, "shooterR");

        ShooterR.setDirection(DcMotorSimple.Direction.FORWARD);
        ShooterR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P,0,0,F);

        ShooterR.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        double currentVelocity = ShooterR.getVelocity();
        double error = currentTargetVelocity - currentVelocity;

        telemetry.addLine("Configuración Completa. Presiona PLAY para empezar");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.triangleWasPressed()){
                if (currentTargetVelocity == highVelocity) { currentTargetVelocity = lowVelocity;}
            } else { currentTargetVelocity = highVelocity;}

            if (gamepad1.circleWasPressed()){
                incrementIndex = (incrementIndex + 1) % incremental.length; //If reach end then go to beginning
            }

            //Increment out coeficients

            if (gamepad1.dpadLeftWasPressed()) {F -= incremental[incrementIndex];}
            if (gamepad1.dpadRightWasPressed()) {F += incremental[incrementIndex];}

            if (gamepad1.dpadDownWasPressed()) {P -= incremental[incrementIndex];}
            if (gamepad1.dpadUpWasPressed()) {P += incremental[incrementIndex];}


            ShooterR.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

            ShooterR.setVelocity(currentTargetVelocity);

            telemetry.addData("Target Velocity: ", currentTargetVelocity);
            telemetry.addData("Current Velocity: ", currentVelocity);
            telemetry.addData("Error: ", error);
            telemetry.addLine("--- TUNING CONFIGURATION ---");
            telemetry.addData("Tuning F: ", F);
            telemetry.addData("Tuning P: ", P);
            telemetry.addData("Incremental Size: ", incremental);
            telemetry.update();

        }
    }
}
