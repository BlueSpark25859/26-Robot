package org.firstinspires.lib;

public interface MecanumChassisIO {

    void initialize();

    void drive(double drive, double strafe, double turn);

    void setMaxSpeed(double speed);
    void setSlowSpeed(double speed);
    void setSlowMode(boolean enabled);

    void getWheelPowers();

    void stop();

}
