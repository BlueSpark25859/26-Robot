package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name= "Field Relative test")

public class fieldrelative extends LinearOpMode {
    public IMU IMU;

    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Started");
        telemetry.update();

        DcMotor leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        DcMotor leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        DcMotor rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        DcMotor rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        IMU = hardwareMap.get(IMU.class, "imu");
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        IMU.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT)));

        waitForStart();

        while(opModeIsActive()){
            double drive =  -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_trigger - gamepad1.left_trigger;

            boolean slowmode = gamepad1.b;

            double maxPower = slowmode ? 0.2: 0.8;

            // Angulo
            double heading = IMU.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // Field Relative


            double lFPower = Range.clip((drive + turn + strafe), -maxPower, maxPower);
            double rFPower = Range.clip((drive - turn + strafe), -maxPower, maxPower);
            double lBPower = Range.clip((drive + turn-strafe), -maxPower, maxPower);
            double rBPower = Range.clip((drive - turn -strafe), -maxPower, maxPower);

            leftFront.setPower(lFPower);
            leftBack.setPower(lBPower);
            rightFront.setPower(rFPower);
            rightBack.setPower(rBPower);

            if (gamepad1.y) {
                IMU.resetYaw();
            }

            telemetry.addData("leftFront", leftFront.getPower());
            telemetry.addData("leftBack", leftBack.getPower());
            telemetry.addData("rightFront", rightFront.getPower());
            telemetry.addData("rightBack", rightBack.getPower());
            telemetry.update();
        }
    }
}
