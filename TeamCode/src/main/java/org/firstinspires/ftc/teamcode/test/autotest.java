/*package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous(name = "auto test")
public class autotest extends LinearOpMode {

    DcMotor leftBack = hardwareMap.get(DcMotor.class, "leftBack");
    DcMotor leftFront = hardwareMap.get(DcMotor.class, "leftFront");
    DcMotor rightFront = hardwareMap.get(DcMotor.class, "rightFront");
    DcMotor rightBack = hardwareMap.get(DcMotor.class, "rightBack");

    DcMotor Intake, Regulator;

    DcMotorEx Shooter;


    public IMU imu;

    @Override
    public void runOpMode() throws InterruptedException {

        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        imu = hardwareMap.get(IMU.class, "imu");

        //Motor variable creation
        Shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        Intake = hardwareMap.get(DcMotor.class, "intake1");
        Regulator = hardwareMap.get(DcMotor.class, "Regulator");


        Shooter.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);


        Intake.setDirection(DcMotorSimple.Direction.FORWARD);
        Shooter.setDirection(DcMotorSimple.Direction.REVERSE);
        Regulator.setDirection(DcMotorSimple.Direction.REVERSE);


        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        imu.initialize(
                new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.LEFT))
        );


        waitForStart();

        while (opModeIsActive()) {

            telemetry.addData("Heading1", obtenerAngulo());
            telemetry.update();
        }
    }

    public double obtenerAngulo () {
        return -(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
    }

    public double obtenerError (double anguloObjetivo) {
        //Calcular el error (distancia al ángulo objetivo)
        double error = anguloObjetivo - obtenerAngulo();
        if (error > 180) error -= 360;
        if (error < -180) error += 360;
        return error;
    }
    public void esperarAuto(double seconds) {
        double currentTime = getRuntime();
        while (getRuntime() < currentTime + seconds) {
            sleep(0);
            telemetry.addData("HEADING", obtenerAngulo());
            telemetry.update();
        }
    }


    public void giroAngulo(double angulo) {
        double kp = 0.008;       // Ganancia proporcional (ajustable)
        double minPower = 0.07;  // Potencia mínima para vencer fricción estática
        double tolerancia = 0.3; // Error aceptable en grados

        while (opModeIsActive()) {
            double error = obtenerError(angulo);


            if (Math.abs(error) <= tolerancia) break;


            double power = kp * error;

            if (Math.abs(power) < minPower) {
                power = minPower * Math.signum(error);
            }

            leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
            leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
            rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
            rightBack.setDirection(DcMotorSimple.Direction.REVERSE);


            leftFront.setPower(power);
            leftBack.setPower(power);
            rightFront.setPower(-power);
            rightBack.setPower(-power);


            telemetry.addData("Ángulo actual", obtenerAngulo());
            telemetry.addData("Error", error);
            telemetry.addData("Potencia", power);
            telemetry.update();
        }

        power(0);
    }


    public void avanzarRecto(double poder, double distanciaCM) {
        double targetAngle = obtenerAngulo();
        double kp = 0.015;
        double ticksPorVuelta = 280;
        double diametroRuedaCM = 9.2;
        double ticksPorCM = ticksPorVuelta / (Math.PI * diametroRuedaCM); //28.88
        int ticksObjetivo = (int) (distanciaCM * ticksPorCM);

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setTargetPosition(ticksObjetivo);
        leftBack.setTargetPosition(ticksObjetivo);
        rightFront.setTargetPosition(ticksObjetivo);
        rightBack.setTargetPosition(ticksObjetivo);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(poder);
        leftBack.setPower(poder);
        rightFront.setPower(poder);
        rightBack.setPower(poder);

        leftFront.getCurrentPosition();
        leftFront.getDirection();
        rightFront.getDirection();

        while (opModeIsActive() &&
                (leftFront.isBusy() ||  rightFront.isBusy() ||
                        leftBack.isBusy()  ||  rightBack.isBusy())) {

            double error = obtenerError(targetAngle);
            double correction = error * kp;

            correction = Math.max(Math.min(correction, 0.3), -0.3);


            leftFront.setPower(poder + correction);
            leftBack.setPower(poder + correction);
            rightFront.setPower(poder - correction);
            rightBack.setPower(poder - correction);

            telemetry.addData("Angulo", leftFront.getCurrentPosition());
            telemetry.addData("DireccionMotorIzquierdo", leftFront.getDirection());
            telemetry.addData("DireccionMotorDerecho", rightFront.getDirection());
            telemetry.addData("Error", error);
            telemetry.addData("Corrección", correction);
            telemetry.addData("Heading: ", obtenerAngulo());
            telemetry.addData("LeftFront", leftFront.getCurrentPosition());
            telemetry.addData("LeftBack", leftBack.getCurrentPosition());
            telemetry.addData("R", rightFront.getCurrentPosition());
            telemetry.update();
        }

    }

    public void enfrente (double velocidad){
        double minPower = 100;
        double maxPower = 2500;
    }
}*/


