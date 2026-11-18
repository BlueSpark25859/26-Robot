/*package org.firstinspires.ftc.teamcode.pedro.auton;


import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static com.pedropathing.api.Paths.*;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class SimpleAuto extends OpMode {
    private Follower follower;
    private final PoseFactory p = PoseFactory.degrees();

    private final Pose startPose = p.of(58.75, 10.75, 90);
    private final Pose park = p.of(58.75, 34.75, 90);

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
    }
    @Override
    public void start() {
        schedule(follow(follower, park()));
    }
    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();
    }

    private Path park() {
        return line(startPose, park).linear(startPose, park);
    }
}
*/