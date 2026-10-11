package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

@Autonomous(name = "autopractice")
public class autopractice extends LinearOpMode {
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

        waitForStart();

        while(opModeIsActive()){
            moveMotor(0.2);
            sleep(2000);
            moveMotor2(0.2);
            sleep(2000);
            moveMotor(0);

            break;
        }


    }

    public void moveMotor (double power){
        JMotor.setPower(power);
    }
    public void moveMotor2 (double power){
        JMotor.setPower(-power);
    }
}
