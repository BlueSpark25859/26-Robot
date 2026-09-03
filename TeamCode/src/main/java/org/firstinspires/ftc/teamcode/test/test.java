package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
// Importacion de lo que vamos a usar


@TeleOp (name = "Test by Dany and Vane") // Este programa es un teleoperado y su nombre es (Aparece en la DriverStation)...
public class test extends LinearOpMode { // Nombre de la clase
    //El programa hereda cosas de LinearOpMode, por eso hay que poner la extension
    private ElapsedTime runtime = new ElapsedTime();
    // Cronometro para mostrar en la driver station cuanto ha pasado desde que el programa inicio
    // Es private porque solo se puede usar en esta clase

    DcMotor leftdrive, rightdrive;
    ; // Declaramos los motores que vamos a utilizar

    @Override //estamos utilizando y modificando un metodo que existe en LinearMode
    //Aqui inicia el programa del robot
    // Void es que no devuelve nada 
    public void runOpMode() throws InterruptedException { // dice que el programa puede ser interrumpido
        telemetry.addData("Status", "Started"); // muestra en la pantalla que el programa inicio
        telemetry.update(); // Actualizamos la pantalla para que aparezca el mensaje

        //PRACTICA DE VANE
                // Conectamos los motores con los nombres de la configuracion
                // Busca un motor llamado ____drive y lo guarda como nuestro motor derecho/izquierdo
                leftdrive = hardwareMap.get(DcMotor.class, "leftdrive");
        rightdrive = hardwareMap.get(DcMotor.class, "rightdrive");

        // Cambiamos la direccion de los motores
        leftdrive.setDirection(DcMotor.Direction.REVERSE); //Hace funcione en direccion contraria
        rightdrive.setDirection(DcMotor.Direction.FORWARD); // El motor derecho funciona normal

        // Manera de quitar poder
        leftdrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightdrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Es un pequeño delay para que el boton responda
        Deadline gamepadRateLimit = new Deadline(500, TimeUnit.MILLISECONDS);
        double lowPower = 0.2;
        double highPower = 0.8;
        double currentPower = lowPower;

        // Esperamos a que se presione start
        waitForStart();
        runtime.reset(); //Se resetea el runtime para que se muestre el la Driver

        while (opModeIsActive()) { //Mientras el modo este activo
            double drive = gamepad1.left_stick_y; // El left es para adelante y atras
            // double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x; // El right es para izq y derecha
            // rightTrigger - (leftTrigger);


            //Cambiamos la velocidad usando a
            if (gamepadRateLimit.hasExpired() && gamepad1.a) { // Si el milisegundo de espera expiro y el boton "a" esa presionado
                if (currentPower == lowPower) { // si el poder de ahora es bajo
                    currentPower = highPower; // entonces se le aumentara
                } else {
                    currentPower = lowPower; // si no, se queda en low
                }
                gamepadRateLimit.reset(); // el tiempo se resetea
            }
            double leftPower = Range.clip(drive - turn, -currentPower, currentPower); //se ponen los limites del motor
            double rightPower = Range.clip(drive + turn, -currentPower, currentPower);
            // double porque se guardan los numeros

            leftdrive.setPower(leftPower);
            rightdrive.setPower(rightPower);

            telemetry.addData("leftPower", leftdrive.getPower());
            telemetry.update();
        }
    }
}