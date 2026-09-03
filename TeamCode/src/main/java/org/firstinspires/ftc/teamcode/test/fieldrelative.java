package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name= "Field Relative test")

public class fieldrelative extends LinearOpMode {
    public IMU IMU = hardwareMap.get(IMU.class, "IMU");

    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Started");
        telemetry.update();

        DcMotor leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        DcMotor leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        DcMotor rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        DcMotor rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        IMU = hardwareMap.get(IMU.class, "IMU");
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        IMU.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT)));

        waitForStart();

        while(opModeIsActive()){
            double drive = gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_trigger - gamepad1.left_trigger;

            boolean slowmode = gamepad1.b;

            double maxpower = slowmode ? 0.2: 0.8;

            //fieldrelative

            double powerfl = Range.clip((drive + turn + strafe), -maxpower, maxpower);
            double powerfr = Range.clip((drive - turn - strafe), -maxpower, maxpower);
            double powerbl = Range.clip((drive + turn - strafe), -maxpower, maxpower);
            double powerbr = Range.clip((drive - turn + strafe), -maxpower, maxpower);

            leftFront.setPower(powerfl);
            leftBack.setPower(powerbl);
            rightFront.setPower(powerfr);
            rightBack.setPower(powerbr);

            telemetry.addData("leftFront", leftFront.getPower());
            telemetry.addData("leftBack", leftBack.getPower());
            telemetry.addData("rightFront", rightFront.getPower());
            telemetry.addData("leftBack", rightBack.getPower());
            telemetry.update();
        }
    }
}
