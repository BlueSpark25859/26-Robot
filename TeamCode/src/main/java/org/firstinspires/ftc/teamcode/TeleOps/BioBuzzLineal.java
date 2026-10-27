package org.firstinspires.ftc.teamcode.TeleOps;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

public class BioBuzzLineal {
    @TeleOp (name = "PurpleSparkBioBuzz")

    public class PurpleSparkBioBuzz extends LinearOpMode {
        private ElapsedTime runtime= new ElapsedTime();

        public void runOpMode(){
            telemetry.addData("Status","Initialized");
            telemetry.update();

            DcMotor leftDrive = hardwareMap.get(DcMotor.class, "leftD");
            DcMotor rightDrive = hardwareMap.get(DcMotor.class, "rightD");

            leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
            rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);

            leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            Deadline gamepadRateLimit = new Deadline(500, TimeUnit.MILLISECONDS);
            double lowPower = 0.3;
            double highPower = 0.8;
            double currentPower = lowPower;

            DcMotor shooter = hardwareMap.get(DcMotor.class,"Shooter");
            shooter.setDirection(DcMotorSimple.Direction.FORWARD);

            DcMotor intake = hardwareMap.get(DcMotor.class,"Intake");
            intake.setDirection(DcMotorSimple.Direction.FORWARD);

            waitForStart();
            runtime.reset();

            while (opModeIsActive()){
                double drive = gamepad1.left_stick_y;
                double turn = gamepad1.right_stick_x;

                if(gamepadRateLimit.hasExpired() && gamepad1.dpad_right){
                    currentPower = (currentPower == lowPower) ? highPower : lowPower;
                    gamepadRateLimit.reset();
                }

                double leftPower = Range.clip(drive - turn, -currentPower, currentPower); //limits the range of the motor
                double rightPower = Range.clip(drive + turn, -currentPower, currentPower);

                leftDrive.setPower(leftPower);
                rightDrive.setPower(rightPower);

                if (gamepad2.right_bumper){
                    intake.setPower(0.8);
                } else {
                    intake.setPower(0);
                }

                if (gamepad2.left_bumper){
                    shooter.setPower(0.8);
                } else {
                    shooter.setPower(0);
                }

                leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
                rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

                leftDrive.setPower(leftPower);
                rightDrive.setPower(rightPower);
            }
        }
    }


}
