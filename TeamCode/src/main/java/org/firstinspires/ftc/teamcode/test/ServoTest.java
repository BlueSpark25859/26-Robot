package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ServoTest by Raúl")
public class ServoTest extends LinearOpMode {
    public void runOpMode() throws InterruptedException{

        //Initialization of our Servo
        Servo servoOne;
        servoOne = hardwareMap.get(Servo.class,"servo_one");
        //servoOne.setPosition(0.5);
        servoOne.scaleRange(0.2,0.8); //limit the servo movement to a certain range

        //CRServo servoTwo; //servo that moves continuously
        //servoTwo = hardwareMap.get(CRServo.class, "servo_two");

        waitForStart();

        while(opModeIsActive()){
            servoOne.setPosition(0.5); //value from 0 to 1
            //servoTwo.setPower(0.5); //send power to the continous servo

            //Controlling servo with gamepad
            if(gamepad1.circle){
                servoOne.setPosition(1);
            }else if(gamepad1.triangle){
                servoOne.setPosition(0);
            }

            //Controlling servo with trigger
            boolean isPressed = gamepad1.right_trigger > 0.5;
            if(isPressed) {
                servoOne.setPosition(0.55);
            }

            //Testing the servo smoothly with dpad
            if(gamepad1.dpad_up){
                servoOne.setPosition(servoOne.getPosition() + 0.002);
            }else if(gamepad1.dpad_down){
                servoOne.setPosition(servoOne.getPosition() - 0.002);
            }

            //Telemetry to see the position of our servo
            telemetry.addData("Servo Position: ",servoOne.getPosition());
            telemetry.update();
        }
    }
}