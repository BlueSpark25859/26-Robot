package org.firstinspires.lib;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

public class MecanumChassis implements MecanumChassisIO{

    private final DcMotorEx leftFront;
    private final DcMotorEx rightFront;
    private final DcMotorEx leftBack;
    private final DcMotorEx rightBack;

    private double maxSpeed = 0.8;
    private double slowSpeed = 0.2;

    private boolean slowMode = false;

    public MecanumChassis (DcMotorEx leftFront,DcMotorEx rightFront,DcMotorEx leftBack,DcMotorEx rightBack){
        this.leftFront = leftFront;
        this.rightFront = rightFront;
        this.leftBack = leftBack;
        this.rightBack = rightBack;

    }
    @Override
    public void initialize() {
        leftFront.setDirection(DcMotorEx.Direction.FORWARD);
        leftBack.setDirection(DcMotorEx.Direction.FORWARD);
        rightFront.setDirection(DcMotorEx.Direction.REVERSE);
        rightBack.setDirection(DcMotorEx.Direction.REVERSE);

        leftFront.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        leftFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        stop();
    }

    @Override
    public void drive(double drive, double strafe, double turn) {

        double speed = slowMode ? slowSpeed: maxSpeed;

        double lFPower = Range.clip((drive + turn + strafe), -speed, speed);
        double rFPower = Range.clip((drive - turn - strafe), -speed, speed);
        double lBPower = Range.clip((drive - turn + strafe), -speed, speed);
        double rBPower = Range.clip((drive + turn - strafe), -speed, speed);

        leftFront.setPower(lFPower);
        leftBack.setPower(lBPower);
        rightFront.setPower(rFPower);
        rightBack.setPower(rBPower);

    }

    @Override
    public void setMaxSpeed(double speed) {
        maxSpeed = Range.clip(speed,0.2,0.8);
    }

    @Override
    public void setSlowSpeed(double speed) {
        slowSpeed = Range.clip(speed,0.2,0.6);
    }

    @Override
    public void setSlowMode(boolean enabled) {
        slowMode = enabled;
    }

    @Override
    public void getWheelPowers() {
        telemetry.addData("leftFront", leftFront.getPower());
        telemetry.addData("leftBack", leftBack.getPower());
        telemetry.addData("rightFront", rightFront.getPower());
        telemetry.addData("rightBack", rightBack.getPower());
    }

    @Override
    public void stop() {
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }


}
