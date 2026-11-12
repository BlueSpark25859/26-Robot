
package org.firstinspires.ftc.teamcode.TeleOps;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@TeleOp(name = "MecanumWithAprilTags")

public class MecanumWithAprilTags extends LinearOpMode {

    public IMU IMU;

    DcMotorEx shooter, intake;

    // APRILTAG SETTINGS
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    final double TURN_GAIN = 0.01;
    final double MAX_AUTO_TURN = 0.25;
    final double ANGLE_TOLERANCE = 2.0;

    // Change to -1 if robot turns away from target
    final double AIM_DIRECTION = 1.0;

    // -1 means any individual tag
    final int DESIRED_TAG_ID = -1;

    // null means any cluster
    final String DESIRED_CLUSTER_NAME = null;

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addData("Status", "Initializing");
        telemetry.update();

        DcMotorEx leftBack =
                hardwareMap.get(DcMotorEx.class, "leftBack");

        DcMotorEx leftFront =
                hardwareMap.get(DcMotorEx.class, "leftFront");

        DcMotorEx rightFront =
                hardwareMap.get(DcMotorEx.class, "rightFront");

        DcMotorEx rightBack =
                hardwareMap.get(DcMotorEx.class, "rightBack");

        IMU = hardwareMap.get(IMU.class, "imu");


        leftFront.setDirection(DcMotorEx.Direction.REVERSE);
        leftBack.setDirection(DcMotorEx.Direction.REVERSE);
        rightFront.setDirection(DcMotorEx.Direction.FORWARD);
        rightBack.setDirection(DcMotorEx.Direction.FORWARD);


        leftFront.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);


        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        intake = hardwareMap.get(DcMotorEx.class, "intake");

        shooter.setDirection(DcMotorEx.Direction.FORWARD);
        intake.setDirection(DcMotorEx.Direction.REVERSE);

        shooter.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        // ORIGINAL IMU ORIENTATION
        IMU.initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
                )
        ));

        // INITIALIZE APRILTAG
        aprilTag = new AprilTagProcessor.Builder().build();
        aprilTag.setDecimation(2);

        visionPortal = new VisionPortal.Builder()
                .setCamera(
                        hardwareMap.get(
                                WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTag)
                .build();

        telemetry.addData("Status", "Ready");
        telemetry.addLine("Left Bumper = Auto Aim");
        telemetry.update();

        waitForStart();

        try {

            while (opModeIsActive()) {

                // --- APRILTAG DETECTION ---

                boolean targetFound = false;
                String targetName = "none";
                double targetBearing = 0;

                List<AprilTagDetection> detections =
                        aprilTag.getDetections();

                for (AprilTagDetection detection : detections) {

                    if (detection instanceof AprilTagSingleDetection) {

                        AprilTagSingleDetection single =
                                (AprilTagSingleDetection) detection;

                        if (single.metadata != null &&
                                single.ftcPose != null &&
                                (DESIRED_TAG_ID < 0 ||
                                        single.id == DESIRED_TAG_ID)) {

                            targetName = single.metadata.name;
                            targetBearing = single.ftcPose.bearing;
                            targetFound = true;
                            break;
                        }

                    } else if (detection instanceof AprilTagClusterDetection) {

                        AprilTagClusterDetection cluster =
                                (AprilTagClusterDetection) detection;

                        if (cluster.metadata != null &&
                                cluster.ftcPose != null &&
                                (DESIRED_CLUSTER_NAME == null ||
                                        cluster.metadata.shortName.equals(
                                                DESIRED_CLUSTER_NAME))) {

                            targetName = cluster.metadata.shortName;
                            targetBearing = cluster.ftcPose.bearing;
                            targetFound = true;
                            break;
                        }
                    }
                }

                // --- DRIVER CODE ---

                double drive = -gamepad1.left_stick_y;
                double strafe = gamepad1.left_stick_x;
                double turn = gamepad1.right_trigger
                        - gamepad1.left_trigger;

                boolean slowmode = gamepad1.b;

                double maxPower = slowmode ? 0.2 : 0.8;

                // AUTOMATIC APRILTAG AIM
                if (gamepad1.left_bumper) {

                    drive = 0;
                    strafe = 0;
                    turn = 0;

                    if (targetFound) {

                        double headingError = targetBearing;

                        if (Math.abs(headingError) >
                                ANGLE_TOLERANCE) {

                            turn = Range.clip(
                                    AIM_DIRECTION
                                            * headingError
                                            * TURN_GAIN,
                                    -MAX_AUTO_TURN,
                                    MAX_AUTO_TURN
                            );

                        } else {

                            turn = 0;
                        }
                    }
                }

                // ORIGINAL MECANUM CALCULATIONS
                double lFPower = Range.clip(
                        drive + turn + strafe,
                        -maxPower, maxPower);

                double rFPower = Range.clip(
                        drive - turn - strafe,
                        -maxPower, maxPower);

                double lBPower = Range.clip(
                        drive - turn + strafe,
                        -maxPower, maxPower);

                double rBPower = Range.clip(
                        drive + turn - strafe,
                        -maxPower, maxPower);

                leftFront.setPower(lFPower);
                leftBack.setPower(lBPower);
                rightFront.setPower(rFPower);
                rightBack.setPower(rBPower);

                // --- OPERATOR CODE ---

                if (gamepad2.a) {

                    intake.setPower(0.8);

                } else {

                    intake.setPower(0);
                }

                if (gamepad2.b) {

                    shooter.setVelocity(1400);

                } else {

                    shooter.setVelocity(0);
                }

                // --- TELEMETRY ---

                telemetry.addLine("APRILTAG TELEMETRY");
                telemetry.addData("Target Found", targetFound);
                telemetry.addData("Target", targetName);

                if (targetFound) {
                    telemetry.addData(
                            "Bearing", "%.2f", targetBearing);
                }

                if (gamepad1.left_bumper) {

                    if (!targetFound) {

                        telemetry.addLine("NO APRILTAG FOUND");

                    } else if (Math.abs(targetBearing)
                            <= ANGLE_TOLERANCE) {

                        telemetry.addLine("AIM ALIGNED");

                    } else {

                        telemetry.addLine("AIMING...");
                    }
                }

                telemetry.addLine("DRIVER TELEMETRY");

                telemetry.addData(
                        "leftFront", leftFront.getPower());

                telemetry.addData(
                        "leftBack", leftBack.getPower());

                telemetry.addData(
                        "rightFront", rightFront.getPower());

                telemetry.addData(
                        "rightBack", rightBack.getPower());

                telemetry.addLine("OPERATOR TELEMETRY");

                telemetry.addData(
                        "shooter velocity", shooter.getVelocity());

                telemetry.addData(
                        "intake power", intake.getPower());

                telemetry.update();

                sleep(10);
            }

        } finally {


            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);

            shooter.setVelocity(0);
            intake.setPower(0);

            if (visionPortal != null) {
                visionPortal.close();
            }
        }
    }
}

