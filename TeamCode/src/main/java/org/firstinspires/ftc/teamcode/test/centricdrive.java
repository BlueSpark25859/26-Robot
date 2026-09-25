package org.firstinspires.ftc.teamcode.test;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class centricdrive {
    public IMU IMU;
    public void driveFieldRelative (double forward, double strafe, double rotate){
        IMU = hardwareMap.get(IMU.class, "imu");
        double theta = Math.atan2 (forward, strafe);
        double r = Math.hypot (strafe, forward);

        theta = AngleUnit.normalizeRadians (theta - IMU.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));

        double newForward = r * Math.sin(theta);
        double newStrafe = r * Math.cos(theta);


    }

}
