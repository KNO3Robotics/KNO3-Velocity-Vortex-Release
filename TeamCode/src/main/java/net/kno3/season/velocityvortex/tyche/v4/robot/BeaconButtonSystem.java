package net.kno3.season.velocityvortex.tyche.v4.robot;

import com.qualcomm.robotcore.hardware.Servo;
import net.kno3.robot.Robot;
import net.kno3.robot.SubSystem;

/**
 * @author Jaxon A Brown
 */
public class BeaconButtonSystem extends SubSystem {
    private Servo leftPusher, rightPusher;

    private double rightbase, leftbase, extent;

    public BeaconButtonSystem(Robot robot) {
        super(robot);

        this.rightbase = robot.settings.getBeaconRightBase();
        this.leftbase = robot.settings.getBeaconLeftBase();
        this.extent = robot.settings.getBeaconExtent();
    }

    @Override
    public void init() {
        leftPusher = hardwareMap().servo.get(Tyche.LEFT_PUSHER_KEY);
        rightPusher = hardwareMap().servo.get(Tyche.RIGHT_PUSHER_KEY);
        retractRight();
        retractLeft();
    }

    @Override
    public void handle() {
        if(gamepad1().left_bumper || gamepad2().left_bumper) {
            extendLeft();
        } else {
            retractLeft();
        }
        if(gamepad1().right_bumper || gamepad2().right_bumper) {
            extendRight();
        } else {
            retractRight();
        }
    }

    public void extendRight() {
        rightPusher.setPosition(rightbase - extent);
    }

    public void retractRight() {
        rightPusher.setPosition(rightbase);
    }

    public void extendLeft() {
        leftPusher.setPosition(rightbase + extent);
    }

    public void retractLeft() {
        leftPusher.setPosition(leftbase);
    }

    @Override
    public void stop() {

    }
}
