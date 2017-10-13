package net.kno3.season.velocityvortex.tyche.v4.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import net.kno3.robot.Robot;
import net.kno3.robot.SubSystem;
import net.kno3.util.MotorPair;
import net.kno3.util.Threading;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jaxon A Brown
 */
public class ShooterSystem extends SubSystem {
    private MotorPair shooter;
    private Servo feeder;
    private Set<VoltageSensor> voltageSensors;

    private ShooterState currentShooterState;
    private FeederState currentFeederState;

    private Thread singleShootThread;

    private double voltage = 0;
    private double feedDelay;

    private SensorSystem sensorSystem;
    private CollectorSystem collectorSystem;
    private DriveSystem driveSystem;
    private LiftSystem liftSystem;
    private long lastVoltage = System.currentTimeMillis();

    public ShooterSystem(Robot robot) {
        super(robot);

        ShooterState.SHORT.setPower(robot.settings.getShooterShortBase());
        ShooterState.LONG.setPower(robot.settings.getShooterLongBase());
        this.feedDelay = robot.settings.getShooterFeedDelay() / 1000D;
        FeederState.LOADING.position = robot.settings.getFeederLoadingPosition();
        FeederState.SHOOTING.position = robot.settings.getFeederFeedingPosition();
    }

    @Override
    public void init() {
        DcMotor shooter1 = hardwareMap().dcMotor.get(Tyche.SHOOTER_KEY);
        DcMotor shooter2 = hardwareMap().dcMotor.get(Tyche.SHOOTER2_KEY);
        shooter2.setDirection(DcMotorSimple.Direction.REVERSE);
        this.shooter = new MotorPair(shooter1, shooter2);
        this.shooter.floatMode();
        this.feeder = hardwareMap().servo.get(Tyche.FEEDER_KEY);
        setShooterState(ShooterState.STOPPED);
        voltageSensors = new HashSet<>();
        for(VoltageSensor vsens : hardwareMap().voltageSensor) {
            voltageSensors.add(vsens);
        }

        sensorSystem = robot.getSubSystem(SensorSystem.class);
        collectorSystem = robot.getSubSystem(CollectorSystem.class);
        driveSystem = robot.getSubSystem(DriveSystem.class);
        liftSystem = robot.getSubSystem(LiftSystem.class);

        telemetry().addData("shooterspeed", () -> shooter.getPower());
        telemetry().addData("Last Voltage Update", () -> (System.currentTimeMillis() - lastVoltage) / 1000D);
    }

    @Override
    public void handle() {
        if(gamepad2().b) {
            double voltage = 0;
            for(VoltageSensor vSensor : voltageSensors) {
                voltage += vSensor.getVoltage();
            }
            voltage /= voltageSensors.size();
            this.voltage = voltage;
            this.lastVoltage = System.currentTimeMillis();
        } else {
            tryUpdateVoltage();
        }

        if(gamepad2().left_stick_y > 0.3) {
            if(currentShooterState == ShooterState.STOPPED) {
                setShooterState(ShooterState.SHORT);
            }
        } else if(gamepad2().left_stick_y < -0.3) {
            if(currentShooterState == ShooterState.STOPPED) {
                setShooterState(ShooterState.LONG);
            }
        } else if(gamepad2().y) {
            setShooterState(ShooterState.REVERSE);
        } else if(gamepad1().b) {
            if(currentShooterState == ShooterState.STOPPED) {
                setShooterState(ShooterState.SHORT);
            }
        } else {
            setShooterState(ShooterState.STOPPED);
        }

        if(getShooterState() != ShooterState.STOPPED && getShooterState() != ShooterState.REVERSE) {
            if(gamepad2().a || gamepad1().y) {
                feedBallAsync();
            }
        }

        if(!isFeedingBall()) {
            if(gamepad2().x) {
                setFeederState(FeederState.SHOOTING);
            } else {
                setFeederState(FeederState.LOADING);
            }
        }

        telemetry().addData("Shooter Power", shooter.getPower());
        telemetry().addData("SignleShoot", singleShootThread != null);
    }

    public void setShooterState(ShooterState state) {
        if(state == ShooterState.SHORT || state == ShooterState.LONG) {
            this.shooter.setPower(getShooterPower(state.power));//extrapolateShooterPower(state.power));
        } else {
            this.shooter.setPower(state.power);
        }
        this.currentShooterState = state;
    }

    public double getShooterPower(double base) {
        tryUpdateVoltage();
        return Tyche.extrapolateShooterPower(voltage, base);
    }

    public void tryUpdateVoltage() {
        if(isNoMotorRunning()) {
            double voltage = 0;
            for(VoltageSensor vSensor : voltageSensors) {
                voltage += vSensor.getVoltage();
            }
            voltage /= voltageSensors.size();
            this.voltage = voltage;
            this.lastVoltage = System.currentTimeMillis();
        }
    }

    public boolean isNoMotorRunning() {
        return shooter.getPower() < 0.05 &&
                driveSystem.getLeftPower() < 0.05 && driveSystem.getRightPower() < 0.05 &&
                collectorSystem.getCollectorPower() < 0.05 &&
                liftSystem.getLiftPower() < 0.05;
    }

    public ShooterState getShooterState() {
        return this.currentShooterState;
    }

    public void setFeederState(FeederState state) {
        this.feeder.setPosition(state.position);
        this.currentFeederState = state;
    }

    public FeederState getFeederState() {
        return this.currentFeederState;
    }

    public void feedBallSync() {
        setFeederState(FeederState.SHOOTING);
        Threading.delay(0.5, robot);
        setFeederState(FeederState.LOADING);
        Threading.delay(feedDelay, robot);
        sensorSystem.decrementBall();
    }

    public void feedBallQuickSync() {
        setFeederState(FeederState.SHOOTING);
        Threading.delay(0.5, robot);
        setFeederState(FeederState.LOADING);
        Threading.delay(0.25, robot);
        sensorSystem.decrementBall();
    }

    public void feedBallAsync() {
        synchronized (this) {
            if (singleShootThread != null) {
                return;
            }
            this.singleShootThread = Threading.async(() -> {
                feedBallSync();
                singleShootThread = null;
            });
        }
    }

    public boolean isFeedingBall() {
        return this.singleShootThread != null;
    }

    @Override
    public void stop() {
        if(singleShootThread != null) {
            singleShootThread.stop();
        }
    }



    public enum ShooterState {
        STOPPED(0),
        SHORT(),
        LONG(),
        REVERSE(-1);

        private double power;
        ShooterState(double power) {
            this.power = power;
        }

        ShooterState() {}

        public void setPower(double power) {
            this.power = power;
        }
    }

    public enum FeederState {
        LOADING(0.23),
        SHOOTING(0.67);

        double position;
        FeederState(double position) {
            this.position = position;
        }
    }
}
