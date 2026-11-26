package org.firstinspires.ftc.teamcode.Constants;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Constants {

    //Shooter
    public static final class Shooter{
        public static final DcMotorSimple.Direction kShooterDirection= DcMotorSimple.Direction.REVERSE;

        public static final int kShooterSpeed = 1350;
    }

    public static final class Intake {
        public static final DcMotorSimple.Direction kIntakeDirection = DcMotorSimple.Direction.REVERSE;

        public static final double kIntakePower = 0.8;
        public static final double kLowIntakePower = 0.5;
    }
}
