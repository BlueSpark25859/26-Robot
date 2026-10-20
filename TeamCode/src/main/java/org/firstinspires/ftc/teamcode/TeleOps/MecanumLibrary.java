package org.firstinspires.ftc.teamcode.TeleOps;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.lib.MecanumChassis;
import org.firstinspires.lib.MecanumChassisIO;

@TeleOp (name = "Mecanum Library by Emiliano")
public class MecanumLibrary extends LinearOpMode {

    @Override
    public void runOpMode(){
        telemetry.addData("Status", "Started");
        telemetry.update();

        DcMotorEx leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        DcMotorEx leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        DcMotorEx rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        DcMotorEx rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");

        MecanumChassisIO chassis = new MecanumChassis(leftFront,rightFront,leftBack,rightBack);

        chassis.initialize();

        chassis.setMaxSpeed(0.8);
        chassis.setSlowSpeed(0.2);

        waitForStart();

        while (opModeIsActive()){
            chassis.setSlowMode(gamepad1.circle);

            chassis.drive(gamepad1.left_stick_y,
                    gamepad1.left_stick_x,
                    gamepad1.right_trigger - gamepad1.left_trigger);

            telemetry.addLine("DRIVER TELEMETRY");
            chassis.getWheelPowers();
            telemetry.update();
        }

    }
}
