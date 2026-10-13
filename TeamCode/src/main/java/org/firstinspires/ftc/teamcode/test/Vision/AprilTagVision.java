package org.firstinspires.ftc.teamcode.test.Vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp (name = "AprilTag vision")
public class AprilTagVision extends LinearOpMode {

    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();

    @Override
    public void runOpMode(){
        aprilTagWebcam.init(hardwareMap, telemetry);

        waitForStart();
        while (opModeIsActive()){
            aprilTagWebcam.update();
            AprilTagDetection id38 = aprilTagWebcam.getTagBySpecificID(38);
            aprilTagWebcam.displayDetectionTelemetry(id38);
        }
    }
}
