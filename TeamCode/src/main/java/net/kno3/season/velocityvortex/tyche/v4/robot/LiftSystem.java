package net.kno3.season.velocityvortex.tyche.v4.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import net.kno3.robot.Robot;
import net.kno3.robot.SubSystem;
import net.kno3.util.Threading;

/**
 * @author Jaxon A Brown
 */
public class LiftSystem extends SubSystem {
    private DcMotor liftMotor;
    private Servo leftHook;
    private Servo capClamp;
    private Servo capKicker;

    private DriveSystem driveSystem;
    private Thread seatReleaseThread;
    private CapClampState currentClampState;
    private HookState hookState;
    private boolean jerkingCancelled = false;

    private int liftencstart, topLimit = 1231, loweringLimit = 400;
    private boolean hasMovedUp = false, wasYPressed = false, wasStartPressed = false;
    private YButtonProgression ystate = YButtonProgression.RELEASE_SEAT;

    public LiftSystem(Robot robot) {
        super(robot);

        topLimit = robot.settings.getLiftTopLimit();
        loweringLimit = robot.settings.getLiftLoweringLimit();
        CapClampState.STARTING.pos = robot.settings.getLiftClampStartingPosition();
        CapClampState.IDLE.pos = robot.settings.getLiftClampIdlePosition();
        CapClampState.CLAMPED.pos = robot.settings.getLiftClampClampedPosition();
        CapKickerState.IDLE.pos = robot.settings.getLiftKickerIdlePosition();
        CapKickerState.KICK.pos = robot.settings.getLiftKickerKickPosition();
    }

    @Override
    public void init() {
        this.driveSystem = robot.getSubSystem(DriveSystem.class);

        this.liftMotor = hardwareMap().dcMotor.get(Tyche.LIFT_KEY);
        this.liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.liftencstart = this.liftMotor.getCurrentPosition();
        this.leftHook = hardwareMap().servo.get(Tyche.LIFT_HOOK_LEFT_KEY);
        this.capClamp = hardwareMap().servo.get(Tyche.LIFT_CAP_CLAMP_KEY);
        this.capKicker = hardwareMap().servo.get(Tyche.LIFT_KICKER_KEY);

        setHookState(HookState.HOOKED);
        this.currentClampState = CapClampState.STARTING;
        capClamp.setPosition(CapClampState.STARTING.pos);
        setKickerState(CapKickerState.IDLE);
    }

    public void setHookState(HookState state) {
        this.leftHook.setPosition(state.left);
        this.hookState = state;
    }

    public void setCapClampState(CapClampState state) {
        this.capClamp.setPosition(state.pos);
        this.currentClampState = state;
    }

    public void setKickerState(CapKickerState state) {
        this.capKicker.setPosition(state.pos);
    }

    public void idlePos() {
        if(currentClampState == null || currentClampState == CapClampState.IDLE) {
            setCapClampState(CapClampState.IDLE);
        } else {
            setCapClampState(CapClampState.IDLE);
        }
    }

    public void clamp() {
        setCapClampState(CapClampState.CLAMPED);
    }

    public void setLiftPower(double power) {
        this.liftMotor.setPower(power);
    }

    public void syncReleaseSeat() {
        setLiftPower(1);
        Threading.delay(0.25);
        setLiftPower(0);
        setHookState(HookState.RELEASED);
        jerkingCancelled = false;
        Threading.delay(0.25);
        if(jerkingCancelled) {return;}
        driveSystem.drive(1);
        Threading.delay(0.25);
        if(jerkingCancelled) {return;}
        driveSystem.drive(-1);
        Threading.delay(0.25);
        if(jerkingCancelled) {return;}
        driveSystem.drive(1);
        Threading.delay(0.25);
        if(jerkingCancelled) {return;}
        driveSystem.drive(-1);
        Threading.delay(0.25);
        if(jerkingCancelled) {return;}
        driveSystem.stop();
    }

    public void asyncReleaseSeat() {
        this.seatReleaseThread = Threading.startThread(this::syncReleaseSeat);
    }

    public void cancelJerking() {
        jerkingCancelled = true;
    }

    @Override
    public void handle() {
        if(currentClampState == CapClampState.IDLE) {
            idlePos();
        }

        if(wasYPressed && !gamepad1().right_stick_button) {
            wasYPressed = false;
        }
        if(!wasYPressed && gamepad1().right_stick_button) {
            wasYPressed = true;
            switch(ystate) {
                case RELEASE_SEAT:
                    //asyncReleaseSeat();
                    setHookState(HookState.RELEASED);
                    break;
                case CLAMP:
                    clamp();
                    break;
                case RELEASE:
                    idlePos();
                    if(hasMovedUp) {
                        setKickerState(CapKickerState.KICK);
                    }
            }
            ystate = ystate.next();
        }

        if(wasStartPressed && !gamepad2().start) {
            wasStartPressed = false;
        }
        if(!wasStartPressed && gamepad2().start) {
            wasStartPressed = true;
            switch(ystate) {
                case RELEASE_SEAT:
                    //asyncReleaseSeat();
                    setHookState(HookState.RELEASED);
                    break;
                case CLAMP:
                    clamp();
                    break;
                case RELEASE:
                    idlePos();
                    if(hasMovedUp) {
                        setKickerState(CapKickerState.KICK);
                    }
            }
            ystate = ystate.next();
        }

        double liftJoystick = gamepad2().right_stick_y;
        double liftPower = 0;
        if(Math.abs(liftJoystick) > 0.03) {
            liftJoystick *= Math.abs(liftJoystick);
            liftPower = -liftJoystick;
        } else if(Math.abs(gamepad1().right_stick_y) > 0.1) {
            liftPower = -gamepad1().right_stick_y;
        }

        if(hasMovedUp && liftPower < 0 && (liftMotor.getCurrentPosition() - liftencstart) <= loweringLimit) {
            liftPower = 0;
        }
        if(liftPower > 0 && (liftMotor.getCurrentPosition() - liftencstart) >= topLimit) {
            liftPower = 0;
        }
        if((liftMotor.getCurrentPosition() - liftencstart) >= loweringLimit) {
            hasMovedUp = true;
        }

        setLiftPower(liftPower);

        setHookState(hookState);
        setCapClampState(currentClampState);

        telemetry().addData("capclamp", capClamp.getPosition());
        telemetry().addData("hook pos", leftHook.getPosition());
    }

    @Override
    public void stop() {

    }

    public double getLiftPower() {
        return liftMotor.getPower();
    }

    public enum HookState {
        HOOKED(0.554),
        RELEASED(0.458);

        double left;

        HookState(double left) {
            this.left = left;
        }
    }

    public enum CapClampState {
        STARTING(.394),
        IDLE(0.474),
        CLAMPED(.351);

        double pos;

        CapClampState(double pos) {
            this.pos = pos;
        }
    }

    public enum CapKickerState {
        IDLE(0),
        KICK(1);

        double pos;

        CapKickerState(double pos) {
            this.pos = pos;
        }
    }

    private enum YButtonProgression {
        RELEASE_SEAT,
        CLAMP,
        RELEASE;

        public YButtonProgression next() {
            switch(this) {
                case RELEASE_SEAT:
                    return CLAMP;
                case CLAMP:
                    return RELEASE;
                case RELEASE:
                    return CLAMP;
                default:
                    throw new RuntimeException("Java broke");
            }
        }
    }
}
