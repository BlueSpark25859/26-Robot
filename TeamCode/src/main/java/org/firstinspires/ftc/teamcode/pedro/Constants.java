package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.Encoder;
import com.pedropathing.revhub.localizers.RevHubIMU;
import com.pedropathing.revhub.localizers.TwoWheelConfig;
import com.pedropathing.revhub.localizers.TwoWheelLocalizer;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    public static Follower create(HardwareMap h) {
        // return new Follower(Drivetrain, Localizer, Foresight);
        return new Follower(
                new TwoWheelLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("leftFront");
        c.frontRightName.set("rightFront");
        c.backLeftName.set("leftBack");
        c.backRightName.set("rightBack");
        c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.manualBrakeMode.set(true);
    });

    public static TwoWheelConfig localizerConfig = new TwoWheelConfig(c -> {
        c.xPodName.set("intake"); // Forward - Backward
        c.yPodName.set("podSide"); // Strafe
        c.imuName.set("imu");
        c.xPodOffset.set(-0.14083399778383887);
        c.yPodOffset.set(-4.186359006131262); //Check this manually or not
        c.forwardTicksToInches.set(0.0029759261946001557);  //Possible miscalculation of heading procedure (try now)
        c.strafeTicksToInches.set(0.0033689880552765857);
        c.xPodDirection.set(Encoder.REVERSE);
        c.yPodDirection.set(Encoder.REVERSE); // FIXED
        c.imu.set(new RevHubIMU(new RevHubOrientationOnRobot( //Orientado al intake
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
        )));
    });

    //Not the best
    //Maybe try it one more time with bigger space and more area to drive
    /*
    //FIRST TEST

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.2359038660705418);
                Controller secondaryTranslationalForward = Controller.proportional(0.08716017504567117);
                Controller primaryTranslationalLateral = Controller.proportional(1.6802616448230612);
                Controller secondaryTranslationalLateral = Controller.proportional(0.6208117803440839);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.013466740080080825));
                c.brake.set(Controller.proportionalFeedforward(0.011446729068068701));

                c.headingFeedback.set(Controller.proportional(18.01649702447726));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.06895241140382566, 0.005740975430129303));

                c.linearBrakeCoefficients.set(Matrix.diag(0.10860105454356218, 0.07830348655516559));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0010829963192627984, 0.0012446594757673118));

                c.maxAchievableForwardVelocity.set(73.85423872409262);
                c.maxAchievableStrafeVelocity.set(46.63900771383721);
                c.naturalForwardDeceleration.set(49.70684570045401);
                c.naturalStrafeDeceleration.set(100.70202914390929);
            }
    );
    */
    //SECOND TEST
    //Had trouble with going backwards
    //Need to fix mecanum wheel
    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.2887872847658797);
                Controller secondaryTranslationalForward = Controller.proportional(0.10669918518262608);
                Controller primaryTranslationalLateral = Controller.proportional(1.9429970872649984);
                Controller secondaryTranslationalLateral = Controller.proportional(0.7178855059060607);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.013593874506759697));
                c.brake.set(Controller.proportionalFeedforward(0.011554793330745742));

                c.headingFeedback.set(Controller.proportional(12.364097690585735));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.06327410598533449, 0.00802080678110564));

                c.linearBrakeCoefficients.set(Matrix.diag(0.02332791692231776, 0.044467909568443764));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0040604878481083455, 0.0022332637566985663));

                c.maxAchievableForwardVelocity.set(74.99220518449594);
                c.maxAchievableStrafeVelocity.set(49.748399822417724);
                c.naturalForwardDeceleration.set(42.88599136570789);
                c.naturalStrafeDeceleration.set(122.75705241935492);
            }
    );


}