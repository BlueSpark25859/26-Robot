

package org.firstinspires.ftc.teamcode.TeleOps;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;

import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.List;
import java.util.concurrent.TimeUnit;

@TeleOp(name = "AprilTagTestbyVane", group = "TeleOp")
public class AprilTagTestbyVane extends LinearOpMode {

    // Adjust these numbers to suit your robot.
    final double TURN_GAIN = 0.01;
    final double MAX_AUTO_TURN = 0.25;
    final double ANGLE_TOLERANCE = 2.0;

    // Drive motors
    private DcMotorEx leftFront;
    private DcMotorEx rightFront;
    private DcMotorEx leftBack;
    private DcMotorEx rightBack;

    // Camera settings
    private final boolean USE_WEBCAM = true;

    // -1 means ANY single AprilTag
    private final int DESIRED_TAG_ID = -1;

    // null means ANY AprilTag Cluster
    private final String DESIRED_CLUSTER_NAME = null;

    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;

    // Target information
    private boolean targetFound = false;
    private String targetName = "none";
    private int targetID = 0;

    private double targetRange = 0;
    private double targetBearing = 0;
    private double targetYaw = 0;


    @Override
    public void runOpMode() {

        double drive = 0;
        double strafe = 0;
        double turn = 0;

        // Initialize AprilTag detection
        initAprilTag();

        // Initialize hardware
        leftBack = hardwareMap.get(
                DcMotorEx.class, "leftBack");

        leftFront = hardwareMap.get(
                DcMotorEx.class, "leftFront");

        rightFront = hardwareMap.get(
                DcMotorEx.class, "rightFront");

        rightBack = hardwareMap.get(
                DcMotorEx.class, "rightBack");


        leftFront.setDirection(DcMotorEx.Direction.REVERSE);
        leftBack.setDirection(DcMotorEx.Direction.REVERSE);
        rightFront.setDirection(DcMotorEx.Direction.FORWARD);
        rightBack.setDirection(DcMotorEx.Direction.FORWARD);

        // Brake when power is zero
        leftFront.setZeroPowerBehavior(
                DcMotorEx.ZeroPowerBehavior.BRAKE);

        leftBack.setZeroPowerBehavior(
                DcMotorEx.ZeroPowerBehavior.BRAKE);

        rightFront.setZeroPowerBehavior(
                DcMotorEx.ZeroPowerBehavior.BRAKE);

        rightBack.setZeroPowerBehavior(
                DcMotorEx.ZeroPowerBehavior.BRAKE);

        // Reduce motion blur
        /*
        if (USE_WEBCAM) {
            setManualExposure(6, 250);
        }
         */

        telemetry.addData(
                "Camera preview on/off",
                "3 dots, Camera Stream");

        telemetry.addData(
                ">",
                "Touch START to start OpMode");

        //telemetry.addData("Exposure: ", visionPortal.get);


        telemetry.update();

        setManualExposure(200,100);

        waitForStart();

        try {

            while (opModeIsActive()) {

                targetFound = false;

                // Look for a matching AprilTag or Cluster
                List<AprilTagDetection> currentDetections =
                        aprilTag.getDetections();


                for (AprilTagDetection detection : currentDetections) {

                    if (detection instanceof AprilTagSingleDetection) {

                        AprilTagSingleDetection singleDetection =
                                (AprilTagSingleDetection) detection;

                        if (singleDetection.metadata != null &&
                                singleDetection.ftcPose != null) {

                            if ((DESIRED_TAG_ID < 0) ||
                                    (singleDetection.id == DESIRED_TAG_ID)) {

                                targetName =
                                        singleDetection.metadata.name;

                                targetID = singleDetection.id;

                                targetRange =
                                        singleDetection.ftcPose.range;

                                targetBearing =
                                        singleDetection.ftcPose.bearing;

                                targetYaw =
                                        singleDetection.ftcPose.yaw;

                                targetFound = true;
                                break;

                            } else {

                                telemetry.addData(
                                        "Skipping",
                                        "Tag ID %d is not desired",
                                        singleDetection.id);
                            }

                        } else {

                            telemetry.addData(
                                    "Unknown",
                                    "Tag ID %d has no pose data",
                                    singleDetection.id);
                        }

                    } else if (detection instanceof AprilTagClusterDetection) {

                        AprilTagClusterDetection clusterDet =
                                (AprilTagClusterDetection) detection;

                        if (clusterDet.metadata != null &&
                                clusterDet.ftcPose != null) {

                            if (DESIRED_CLUSTER_NAME == null ||
                                    clusterDet.metadata.shortName.equals(
                                            DESIRED_CLUSTER_NAME)) {

                                targetName =
                                        clusterDet.metadata.shortName;

                                targetID = -1;

                                targetRange =
                                        clusterDet.ftcPose.range;

                                targetBearing =
                                        clusterDet.ftcPose.bearing;

                                targetYaw =
                                        clusterDet.ftcPose.yaw;

                                targetFound = true;
                                break;
                            }
                        }
                    }
                }

                // Display target information
                if (targetFound) {

                    telemetry.addData(
                            "\n>",
                            "HOLD Left-Bumper to Aim\n");

                    telemetry.addData(
                            "Found",
                            "ID %d (%s)",
                            targetID,
                            targetName);

                    telemetry.addData(
                            "Range",
                            "%5.1f inches",
                            targetRange);

                    telemetry.addData(
                            "Bearing",
                            "%3.0f degrees",
                            targetBearing);

                    telemetry.addData(
                            "Yaw",
                            "%3.0f degrees",
                            targetYaw);

                } else {

                    telemetry.addData(
                            "\n>",
                            "Drive using joysticks to find valid target\n");
                }

                // AUTOMATIC AIM
                // Rotate only: no forward or strafe movement
                if (gamepad1.left_bumper) {

                    drive = 0;
                    strafe = 0;
                    turn = 0;

                    if (targetFound) {

                        // Angular error between camera and target
                        double headingError = targetBearing;

                        // Stop turning inside tolerance
                        if (Math.abs(headingError) > ANGLE_TOLERANCE) {
                            turn = Range.clip(
                                    headingError * TURN_GAIN,
                                    -MAX_AUTO_TURN,
                                    MAX_AUTO_TURN);

                            telemetry.addData(
                                    "Auto",
                                    "AIMING - Turn %5.2f",
                                    turn);

                        } else {

                            turn = 0;

                            telemetry.addData(
                                    "Auto",
                                    "READY TO SHOOT");
                        }

                    } else {

                        // No target: remain stationary
                        turn = 0;

                        telemetry.addData(
                                "Auto",
                                "No AprilTag found");
                    }

                } else {

                    // MANUAL POV JOYSTICK MODE
                    drive = -gamepad1.left_stick_y / 2.0;

                    strafe = -gamepad1.left_stick_x / 2.0;

                    turn = -gamepad1.right_stick_x / 3.0;

                    telemetry.addData(
                            "Manual",
                            "Drive %5.2f, Strafe %5.2f, Turn %5.2f",
                            drive,
                            strafe,
                            turn);
                }

                telemetry.update();

                // Apply motion to mecanum drivetrain
                moveRobot(drive, strafe, turn);

                sleep(10);
            }

        } finally {

            // Stop motors when OpMode ends
            moveRobot(0, 0, 0);

            if (visionPortal != null) {
                visionPortal.close();
            }
        }
    }

    /*
     * Move robot according to desired axes motions.
     * Positive X is forward.
     * Positive Y is strafe left.
     * Positive Yaw is counter-clockwise.
     */
    public void moveRobot(double x, double y, double yaw) {

        // Calculate wheel powers
        double frontLeftPower = x - y - yaw;
        double frontRightPower = x + y + yaw;
        double backLeftPower = x + y - yaw;
        double backRightPower = x - y + yaw;

        // Normalize wheel powers to be <= 1.0
        double max = Math.max(
                Math.abs(frontLeftPower),
                Math.abs(frontRightPower));

        max = Math.max(
                max,
                Math.abs(backLeftPower));

        max = Math.max(
                max,
                Math.abs(backRightPower));

        if (max > 1.0) {

            frontLeftPower /= max;
            frontRightPower /= max;
            backLeftPower /= max;
            backRightPower /= max;
        }

        // Send powers to wheels
        leftFront.setPower(frontLeftPower);
        rightFront.setPower(frontRightPower);
        leftBack.setPower(backLeftPower);
        rightBack.setPower(backRightPower);
    }

    /*
     * Initialize AprilTag processor.
     */
    private void initAprilTag() {

        // Create AprilTag processor
        aprilTag = new AprilTagProcessor.Builder().build();

        // Detection speed / range balance
        aprilTag.setDecimation(2);
        setManualExposure(2000000,1000000);

        // Create VisionPortal
        if (USE_WEBCAM) {

            visionPortal = new VisionPortal.Builder()
                    .setCamera(
                            hardwareMap.get(
                                    WebcamName.class,
                                    "Webcam 1"))
                    .addProcessor(aprilTag)
                    .build();

        } else {

            visionPortal = new VisionPortal.Builder()
                    .setCamera(
                            BuiltinCameraDirection.BACK)
                    .addProcessor(aprilTag)
                    .build();
        }
    }

    /*
     * Manually set camera exposure and gain.
     * Call after initAprilTag().
     */
    private void setManualExposure(
            int exposureMS,
            int gain) {

        if (visionPortal == null) {
            return;
        }

        // Wait until camera is streaming
        if (visionPortal.getCameraState() !=
                VisionPortal.CameraState.STREAMING) {

            telemetry.addData(
                    "Camera",
                    "Waiting");

            //telemetry.update();

            while (!isStopRequested() &&
                    visionPortal.getCameraState() !=
                            VisionPortal.CameraState.STREAMING) {

                sleep(20);
            }

            telemetry.addData(
                    "Camera",
                    "Ready");


            //telemetry.update();
        }

        // Set controls unless stopping
        if (!isStopRequested()) {

            ExposureControl exposureControl =
                    visionPortal.getCameraControl(
                            ExposureControl.class);

            if (exposureControl != null) {

                if (exposureControl.getMode() !=
                        ExposureControl.Mode.Manual) {

                    exposureControl.setMode(
                            ExposureControl.Mode.Manual);

                    sleep(50);
                }

                exposureControl.setExposure(
                        (long) exposureMS,
                        TimeUnit.MILLISECONDS);

                sleep(20);
            }

            GainControl gainControl =
                    visionPortal.getCameraControl(
                            GainControl.class);

            if (gainControl != null) {

                gainControl.setGain(gain);
                sleep(20);
            }
        }
    }
}
