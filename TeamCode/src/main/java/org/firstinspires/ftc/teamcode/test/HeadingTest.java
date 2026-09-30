package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

@TeleOp(name = "HeadingTest")
public class HeadingTest extends LinearOpMode {
    DcMotorEx JMotor;
    IMU imu;

    @Override
    public void runOpMode() throws InterruptedException {
        JMotor = hardwareMap.get(DcMotorEx.class, "JMotor");

        JMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        imu = hardwareMap.get(IMU.class, "imu");

        JMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        JMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT)));

        imu.resetYaw();

        int degrees = 270;

        waitForStart();

        while (opModeIsActive()) {
            JMotor.setTargetPosition(degrees);
            JMotor.setTargetPositionTolerance(10);
            JMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            JMotor.setPower(0.2);

            if (gamepad1.a){
                degrees = 100;
            }
            else{
                degrees = 270;
            }

            if (gamepad1.b){
                degrees = 200;
            }
        }
    }
}
