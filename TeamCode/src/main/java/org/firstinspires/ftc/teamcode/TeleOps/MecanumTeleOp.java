package org.firstinspires.ftc.teamcode.TeleOps;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "UltimateBazookaTest")

public class MecanumTeleOp extends LinearOpMode {
    public int servoCount = 0;
    private double lastAngle = 0;
    private double totalAngle = 0;
    private double target = 0;
    boolean isCorrecting = true;

    DcMotorEx odometryFront, odometrySide;

    private static final double TICKS_PER_REV = 2000.0;
    private static final double WHEEL_DIAMETER_MM = 32.0;

    DcMotorEx shooter, intake;

    public void runOpMode() throws InterruptedException {

        telemetry.addData("Status", "Started");
        telemetry.update();


        DcMotorEx leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        DcMotorEx leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        DcMotorEx rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        DcMotorEx rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");

        CRServo servoOne;
        servoOne = hardwareMap.get(CRServo.class, "servo_one");
        servoOne.getController().pwmEnable();


        leftFront.setDirection(DcMotorEx.Direction.FORWARD);
        leftBack.setDirection(DcMotorEx.Direction.FORWARD);
        rightFront.setDirection(DcMotorEx.Direction.REVERSE);
        rightBack.setDirection(DcMotorEx.Direction.REVERSE);

        leftFront.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        leftFront.setVelocityPIDFCoefficients(15, 3, 0, 12);
        leftBack.setVelocityPIDFCoefficients(15, 3, 0, 12);
        rightFront.setVelocityPIDFCoefficients(15, 3, 0, 12);
        rightBack.setVelocityPIDFCoefficients(15, 3, 0, 12);


        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        odometryFront = hardwareMap.get(DcMotorEx.class, "intake");
        odometrySide = hardwareMap.get(DcMotorEx.class, "podSide");


        shooter.setDirection(DcMotorEx.Direction.FORWARD);
        shooter.setVelocityPIDFCoefficients(21, 0, 0, 12.6);
        shooter.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        intake.setDirection(DcMotorEx.Direction.REVERSE);


        IMU IMU = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot RevOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
        );
        IMU.initialize(new IMU.Parameters(RevOrientation));

        int initialFrontTicks = odometryFront.getCurrentPosition();
        int initialSideTicks = odometrySide.getCurrentPosition();

        waitForStart();

        while (opModeIsActive()) {

            //IMU ==========================================================================================================================

            double orientation = IMU.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            double deltaAngle = orientation - lastAngle;

            if (deltaAngle < -180) {
                deltaAngle += 360;
            } else if (deltaAngle > 180) {
                deltaAngle -= 360;
            }

            totalAngle += deltaAngle;
            lastAngle = orientation;

            int ticksFront = odometryFront.getCurrentPosition() - initialFrontTicks;
            int ticksSide = odometrySide.getCurrentPosition() - initialSideTicks;

            double distanceMMFront = (ticksFront / TICKS_PER_REV) * Math.PI * WHEEL_DIAMETER_MM;
            double distanceMMSide = (ticksSide / TICKS_PER_REV) * Math.PI * WHEEL_DIAMETER_MM;

            //DRIVER CODE ==================================================================================================================

            double drive = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_trigger - gamepad1.left_trigger;

            boolean slowmode = gamepad1.a;
            double maxPower = slowmode ? 500 : 1500;

            double frontLeftInputVel = Range.clip((drive + strafe + turn), -1, 1);
            double backLeftInputVel = Range.clip((drive - strafe + turn), -1, 1);
            double frontRightInputVel = Range.clip((drive - strafe - turn), -1, 1);
            double backRightInputVel = Range.clip((drive + strafe - turn), -1, 1);

            frontLeftInputVel = frontLeftInputVel * maxPower;
            frontRightInputVel = frontRightInputVel * maxPower;
            backLeftInputVel = backLeftInputVel * maxPower;
            backRightInputVel = backRightInputVel * maxPower;

            double error = (totalAngle - target) * 15;
            double lFPower = frontLeftInputVel + error;
            double rFPower = frontRightInputVel - error;
            double lBPower = backLeftInputVel + error;
            double rBPower = backRightInputVel - error;

            if(gamepad1.b && isCorrecting){
                target = totalAngle;
                isCorrecting = false;
            }
            if(gamepad1.bWasReleased()){
                target = totalAngle;
                isCorrecting = true;
            }

            if (gamepad1.b){
                leftFront.setVelocity(lFPower);
                leftBack.setVelocity(lBPower);
                rightFront.setVelocity(rFPower);
                rightBack.setVelocity(rBPower);
            } else{
                leftFront.setVelocity(frontLeftInputVel);
                leftBack.setVelocity(backLeftInputVel);
                rightFront.setVelocity(frontRightInputVel);
                rightBack.setVelocity(backRightInputVel);
            }



            //OPERATOR CODE=================================================================================================================

            int shooterSpeed = 1550;
            boolean isOpen = shooter.getVelocity() >= shooterSpeed - 250;

            if (gamepad2.a) {
                intake.setPower(0.85);

            } else {
                intake.setPower(0);
            }

            if (isOpen){
                intake.setPower(0.5);
            }


            if (gamepad2.b) {
                shooter.setVelocity(shooterSpeed);
            } else {
                shooter.setVelocity(0);
            }


            if (servoCount <= 20 && isOpen) {
                servoOne.setPower(-1);
                servoCount++;
            }
            if (servoCount >= 1 && (!isOpen)) {
                servoOne.setPower(1);
                servoCount--;
            }
            if (servoCount < 1 || servoCount > 20) {
                servoOne.setPower(0);
            }

            //Telemetry ====================================================================================================================

            telemetry.addData("Servo: ", isOpen);

            telemetry.addLine("DRIVER TELEMETRY");
            telemetry.addData("leftFront", leftFront.getVelocity());
            telemetry.addData("leftBack", leftBack.getVelocity());
            telemetry.addData("rightFront", rightFront.getVelocity());
            telemetry.addData("rightBack", rightBack.getVelocity());

            telemetry.addLine("OPERATOR TELEMETRY");
            telemetry.addData("shooter velocity: ", shooter.getVelocity());
            telemetry.addData("intake power", intake.getPower());
            telemetry.addData("Servo count", servoCount);

            telemetry.addLine("Odometry");
            telemetry.addData("Total Angle", totalAngle);
            telemetry.addData("Encoder Front ticks", ticksFront);
            telemetry.addData("Encoder Side ticks", ticksSide);
            telemetry.addData("Distance Front (mm)", distanceMMFront);
            telemetry.addLine();
            telemetry.addData("Distance Front (cm)", distanceMMFront / 10.0);
            telemetry.addData("Distance Side (cm)", distanceMMSide / 10.0);

            telemetry.update();
        }
    }
}
