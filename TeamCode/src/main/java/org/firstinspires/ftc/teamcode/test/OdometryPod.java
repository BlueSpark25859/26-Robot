package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Odometry Pod Test")
public class OdometryPod extends LinearOpMode {

    private DcMotorEx odometry;

    private static final double TICKS_PER_REV = 2000.0;
    private static final double WHEEL_DIAMETER_MM = 32.0;

    @Override
    public void runOpMode() {

        // Encoder connected to port 0 (leftFront)
        odometry = hardwareMap.get(DcMotorEx.class, "intake");

        telemetry.addLine("Odometry initialized");
        telemetry.update();

        waitForStart();

        // Software zero without resetting the motor
        int initialTicks = odometry.getCurrentPosition();

        while (opModeIsActive()) {

            int ticks = odometry.getCurrentPosition() - initialTicks;

            double distanceMM = (ticks / TICKS_PER_REV) * Math.PI * WHEEL_DIAMETER_MM;

            telemetry.addData("Encoder ticks", ticks);
            telemetry.addData("Distance (mm)", distanceMM);
            telemetry.addData("Distance (cm)", distanceMM / 10.0);
            telemetry.update();
        }
    }
}
