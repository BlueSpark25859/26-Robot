package org.firstinspires.ftc.teamcode.TeleOps;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name= "Bio Buzz Mecanum Chassis")

public class BioBuzzMecanum extends LinearOpMode {
    public IMU IMU;

    DcMotorEx shooter, intake;

    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Started");
        telemetry.update();

        DcMotorEx leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        DcMotorEx leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        DcMotorEx rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        DcMotorEx rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");

        IMU = hardwareMap.get(IMU.class, "imu");
        leftFront.setDirection(DcMotorEx.Direction.REVERSE);
        leftBack.setDirection(DcMotorEx.Direction.FORWARD);
        rightFront.setDirection(DcMotorEx.Direction.REVERSE);
        rightBack.setDirection(DcMotorEx.Direction.REVERSE);

        leftFront.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);


        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        intake = hardwareMap.get(DcMotorEx.class, "intake");

        shooter.setDirection(DcMotorEx.Direction.REVERSE);
        intake.setDirection(DcMotorEx.Direction.REVERSE);

        shooter.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        IMU.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT)));

        waitForStart();

        while(opModeIsActive()){
            //DRIVER CODE
            double drive =  -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_trigger - gamepad1.left_trigger;

            boolean slowmode = gamepad1.b;

            double maxPower = slowmode ? 0.2: 0.8;


            double lFPower = Range.clip((drive + turn + strafe), -maxPower, maxPower);
            double rFPower = Range.clip((drive - turn + strafe), -maxPower, maxPower);
            double lBPower = Range.clip((drive + turn-strafe), -maxPower, maxPower);
            double rBPower = Range.clip((drive - turn -strafe), -maxPower, maxPower);

            leftFront.setPower(lFPower);
            leftBack.setPower(lBPower);
            rightFront.setPower(rFPower);
            rightBack.setPower(rBPower);



            //OPERATOR CODE
            if (gamepad2.a){
                intake.setPower(0.8);
            } else {
                intake.setPower(0);
            }

            if (gamepad2.b){
                shooter.setVelocity(3000);
            } else {
                shooter.setVelocity(0);
            }

            telemetry.addLine("DRIVER TELEMETRY");
            telemetry.addData("leftFront", leftFront.getPower());
            telemetry.addData("leftBack", leftBack.getPower());
            telemetry.addData("rightFront", rightFront.getPower());
            telemetry.addData("rightBack", rightBack.getPower());

            telemetry.addLine("OPERATOR TELEMETRY");
            telemetry.addData("shooter velocity: ", shooter.getVelocity());
            telemetry.addData("intake power", intake.getPower());

            for (int i = 0; i<5; i++){
                telemetry.addLine("Verity");
            }

            telemetry.update();
        }
    }
}
