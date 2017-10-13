package net.kno3.season.velocityvortex.tyche.v4.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import net.kno3.robot.Robot;
import net.kno3.robot.SubSystem;
import net.kno3.util.SimpleColor;

/**
 * @author Jaxon A Brown
 */
public class CollectorSystem extends SubSystem {
    private DcMotor collector;
    private int collecting = 0;
    private boolean depressDelay = false;

    private long reverseUntil = -1;
    private boolean wasRegurgitating = false;

    private SensorSystem sensors;

    public CollectorSystem(Robot robot) {
        super(robot);
    }

    @Override
    public void init() {
        this.collector = hardwareMap().dcMotor.get(Tyche.COLLECTOR_KEY);
        this.sensors = robot.getSubSystem(SensorSystem.class);
        robot.getSubSystem(SensorSystem.class).turnOnCollectorLights();
    }

    @Override
    public void handle() {
        SimpleColor entering = sensors.collectorSees();
        if(entering != null && entering != Tyche.ALLIANCE_COLOR) {
            reverseUntil = System.currentTimeMillis() + 500;
        }

        if(reverseUntil > System.currentTimeMillis()) {
            if(reverseUntil / 50 % 2 == 0) {
                sensors.turnOffLights();
            } else {
                sensors.turnOnLights();
            }
            regurgitate(1);
            wasRegurgitating = true;
        } else {
            if(wasRegurgitating) {
                sensors.decrementBall();
                wasRegurgitating = false;
            }

            if(gamepad1().a && !depressDelay) {
                collecting = collecting == 1 ? 0 : 1;
                depressDelay = true;
            }
            if(gamepad1().x && !depressDelay) {
                collecting = collecting == 2 ? 0 : 2;
                depressDelay = true;
            }
            if(!gamepad1().a && !gamepad1().x) {
                depressDelay = false;
            }

            if(gamepad1().dpad_up) {
                collect(1);
                collecting = 0;
                sensors.turnOnLights();
            } else if(gamepad1().dpad_down) {
                collect(-1);
                collecting = 0;
                sensors.turnOnLights();
            } else if(Math.abs(gamepad2().right_trigger - gamepad2().left_trigger) > 0.1) {
                collect(gamepad2().right_trigger - gamepad2().left_trigger);
                collecting = 0;
                sensors.turnOnLights();
            } else if(collecting == 1) {
                collect(1);
                sensors.turnOnLights();
            } else if(collecting == 2) {
                collect(-1);
                sensors.turnOnLights();
            } else {
                collect(0);
                sensors.turnOffLights();
            }
        }
    }

    public void collect(double speed) {
        this.collector.setPower(speed);
    }

    public void regurgitate(double speed) {
        this.collector.setPower(-speed);
    }

    @Override
    public void stop() {
        this.collect(0);
    }

    public double getCollectorPower() {
        return collector.getPower();
    }

    void telemetryRejection(SimpleColor entering) {
        if(entering != null && entering != Tyche.ALLIANCE_COLOR) {
            regurgitate(1);
            sensors.decrementBall();
        }
    }

    public void enableTelemetryRejection() {
        sensors.telemetryRejection(this::telemetryRejection);
    }
}
