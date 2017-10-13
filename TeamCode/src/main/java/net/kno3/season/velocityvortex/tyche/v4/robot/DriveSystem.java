package net.kno3.season.velocityvortex.tyche.v4.robot;

import com.google.common.base.Supplier;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import net.kno3.robot.Robot;
import net.kno3.robot.SubSystem;
import net.kno3.util.*;

/**
 * @author Jaxon A Brown
 */
public class DriveSystem extends SubSystem {
    private MotorPair left, right;
    private SensorSystem sensorSystem;
    private LiftSystem liftSystem;
    private boolean reverse = false, slow = false;
    private boolean slowReverse = false;

    public DriveSystem(Robot robot) {
        super(robot);
    }

    @Override
    public void init() {
        DcMotor frontLeft = hardwareMap().dcMotor.get(Tyche.DRIVE_FL_KEY);
        DcMotor rearLeft = hardwareMap().dcMotor.get(Tyche.DRIVE_RL_KEY);
        DcMotor frontRight = hardwareMap().dcMotor.get(Tyche.DRIVE_FR_KEY);
        DcMotor rearRight = hardwareMap().dcMotor.get(Tyche.DRIVE_RR_KEY);

        rearLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);

        this.left = new MotorPair(frontLeft, rearLeft);
        this.right = new MotorPair(frontRight, rearRight);

        this.sensorSystem = robot.getSubSystem(SensorSystem.class);
        this.liftSystem = robot.getSubSystem(LiftSystem.class);

        modeVoltage();
    }

    private boolean startLockout = false;
    private boolean backLockout = false;
    private boolean bLockout = false;

    @Override
    public void handle() {
        if(!startLockout && gamepad1().start) {
            reverse = !reverse;
            startLockout = true;
        }
        if(startLockout && !gamepad1().start) {
            startLockout = false;
        }
        if(!backLockout && gamepad1().back) {
            slow = !slow;
            backLockout = true;
        }
        if(backLockout && !gamepad1().back) {
            backLockout = false;
        }
        if(!bLockout && gamepad1().left_stick_button) {
            slowReverse = !slowReverse;
            slow = slowReverse;
            reverse = slowReverse;
            if(slowReverse) {
                floatMode();
            } else {
                brakeMode();
            }
            bLockout = true;
        }
        if(bLockout && !gamepad1().left_stick_button) {
            bLockout = false;
        }

        double speed = (gamepad1().right_trigger - gamepad1().left_trigger);
        speed *= Math.abs(speed);
        double dir = gamepad1().left_stick_x;
        if(reverse) {
            speed *= -1;
        }
        if(slow) {
            speed *= 0.6;
        }
        double left = ((1 - Math.abs(dir)) * speed + (1 - Math.abs(speed)) * dir + dir + speed) / 2;
        double right = ((1 - Math.abs(dir)) * speed - (1 - Math.abs(speed)) * dir - dir + speed) / 2;

        telemetry().addData("Drive", "Left: " + left + " | Right: " + right);
        telemetry().addData("Reverse", reverse);
        telemetry().addData("Slow", slow);
        telemetry().addData("Enc", this.left.getEncoder() + ", " + this.right.getEncoder());

        if(Math.abs(left) > 0.1 || Math.abs(right) > 0.1) {
            liftSystem.cancelJerking();
        }

        set(left, right);
    }

    public void floatMode() {
        this.left.floatMode();
        this.right.floatMode();
    }

    public void brakeMode() {
        this.left.brakeMode();
        this.right.brakeMode();
    }

    public void modeSpeed() {
        this.left.runMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.right.runMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void modeVoltage() {
        this.left.runMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.right.runMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setLeft(double power) {
        this.left.setPower(power);
    }

    public void setRight(double power) {
        this.right.setPower(power);
    }

    public void set(double left, double right) {
        setLeft(left);
        setRight(right);
    }

    public void drive(double speed) {
        set(speed, speed);
    }

    public void driveUntil(double speed, Supplier<Boolean> until) {
        drive(speed);
        Threading.waitFor(until);
        stop();
    }

    public void driveForNoPID(double maxSpeed, double inches) {
        left.resetEncoder();
        right.resetEncoder();
        int targetPulses = inchesToEncoderPulses(inches);
        set(maxSpeed, maxSpeed);

        Threading.waitFor(() -> {
            telemetry().update();
            return Math.abs(this.left.getEncoder() - targetPulses) < 25 || Math.abs(this.right.getEncoder() - targetPulses) < 25;
        });
    }

    public void driveFor(double maxSpeed, double p, double inches, boolean pass) {
        /*Removed*/
    }

    public void driveFor(double maxSpeed, double inches) {
        driveFor(maxSpeed, /*Removed*/, inches, false);
    }

    public void driveFor(double inches) {
        driveFor(0.5, inches);
    }

    public void driveForStop(double maxSpeed, double p, double inches) {
        driveFor(maxSpeed, p, inches, false);
        stop();
    }

    public void driveForStop(double maxSpeed, double inches) {
        driveFor(maxSpeed, inches);
        stop();
    }

    public void driveForStop(double inches) {
        driveFor(inches);
        stop();
    }

    public void driveFor(double maxSpeed, double inches, boolean pass) {
        driveFor(maxSpeed, /*Removed*/, inches, pass);
    }

    public void driveFor(double inches, boolean pass) {
        driveFor(0.5, inches);
    }

    public void driveForStop(double maxSpeed, double p, double inches, boolean pass) {
        driveFor(maxSpeed, p, inches, pass);
        stop();
    }

    public void driveForStop(double maxSpeed, double inches, boolean pass) {
        driveFor(maxSpeed, inches, pass);
        stop();
    }

    public void driveForStop(double inches, boolean pass) {
        driveFor(inches, pass);
        stop();
    }

    public void turnStopOld(double speed, int angle) {
        //sensorSystem.getIMU().zeroHeading();
        set(angle > 0 ? speed : -speed, angle > 0 ? -speed : speed);
        float initialAngle = sensorSystem.getIMU().getHeading();
        Threading.waitFor(() -> {
            telemetry().addData("initialAngle", initialAngle);
            float dirdif = AngleUtil.directionalDifference(initialAngle, sensorSystem.getIMU().getHeading(), angle > 0);
            telemetry().addData("dirdif", dirdif);
            telemetry().update();
            return dirdif > Math.abs(angle) && dirdif < 350;
        });
        stop();
    }

    public void turnPID(double maxTurn, double kp, double ki, double kd, double angle, double turnTolerance, double delay) {
        /*Removed*/
    }

    public void turnPIDslow(double angle) {
        turnPID(/*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, angle, /*Removed*/, /*Removed*/);
    }

    public void turnPIDfast(double angle) {
        turnPID(/*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, angle, /*Removed*/, /*Removed*/);
    }

    public void turnPIDsuperfast(double angle) {
        turnPID(/*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, angle, /*Removed*/, /*Removed*/);
    }

    public void driveWithCorrection(double maxSpeed, double p, double inches, double kp, double ki, double kd, double maxTurn, double angle, double turnDeadband, double delay, boolean pass, Supplier<Boolean> additionalStopParameter) {
        /*Removed*/
    }

    public void driveWithCorrectionFloating(double maxSpeed, double minSpeed, double p, double inches, double kp, double ki, double kd, double maxTurn, double angle, double turnDeadband, double delay, boolean pass, Supplier<Boolean> additionalStopParameter) {
        /*Removed*/
    }

    public void driveWithCorrectionFast(double inches, double angle) {
        driveWithCorrectionFast(inches, angle, /*Removed*/, null);
    }

    public void driveWithCorrectionSlow(double inches, double angle) {
        driveWithCorrectionSlow(inches, angle, /*Removed*/, null);
    }

    public void driveWithCorrectionFast(double inches, double angle, double delay, Supplier<Boolean> additionalStopParameter) {
        driveWithCorrection(/*Removed*/, /*Removed*/, inches, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, angle, /*Removed*/, delay, false, additionalStopParameter);
    }

    public void driveWithCorrectionSlow(double inches, double angle, double delay, Supplier<Boolean> additionalStopParameter) {
        driveWithCorrection(/*Removed*/, /*Removed*/, inches, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, angle, /*Removed*/, delay, false, additionalStopParameter);
    }

    public void driveWithCorrectionFastFloating(double minSpeed, double inches, double angle) {
        driveWithCorrectionFastFloating(minSpeed, inches, angle, /*Removed*/, null);
    }

    public void driveWithCorrectionSlowFloating(double minSpeed, double inches, double angle) {
        driveWithCorrectionSlowFloating(minSpeed, inches, angle, /*Removed*/, null);
    }

    public void driveWithCorrectionFastFloating(double minSpeed, double inches, double angle, double delay, Supplier<Boolean> additionalStopParameter) {
        driveWithCorrectionFloating(/*Removed*/, minSpeed, /*Removed*/, inches, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, angle, /*Removed*/, delay, false, additionalStopParameter);
    }

    public void driveWithCorrectionSlowFloating(double minSpeed, double inches, double angle, double delay, Supplier<Boolean> additionalStopParameter) {
        driveWithCorrectionFloating(/*Removed*/, minSpeed, /*Removed*/, inches, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, angle, /*Removed*/, delay, false, additionalStopParameter);
    }

    public void driveWithCorrectionFast(double inches, double angle, boolean pass) {
        driveWithCorrectionFast(inches, angle, /*Removed*/, pass, null);
    }

    public void driveWithCorrectionSlow(double inches, double angle, boolean pass) {
        driveWithCorrectionSlow(inches, angle, /*Removed*/, pass, null);
    }

    public void driveWithCorrectionFast(double inches, double angle, double delay, boolean pass, Supplier<Boolean> additionalStopParameter) {
        driveWithCorrection(/*Removed*/, /*Removed*/, inches, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, angle, /*Removed*/, delay, pass, additionalStopParameter);
    }

    public void driveWithCorrectionSlow(double inches, double angle, double delay, boolean pass, Supplier<Boolean> additionalStopParameter) {
        driveWithCorrection(/*Removed*/, /*Removed*/, inches, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, angle, /*Removed*/, delay, pass, additionalStopParameter);
    }

    public void driveWithCorrectionFastFloating(double minSpeed  , double inches, double angle, boolean pass) {
        driveWithCorrectionFastFloating(minSpeed, inches, angle, /*Removed*/, pass, null);
    }

    public void driveWithCorrectionSuperFastFloating(double minSpeed, double maxSpeed, double inches, double angle, boolean pass) {
        driveWithCorrectionFloating(maxSpeed, minSpeed, /*Removed*/, inches, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, angle, /*Removed*/, /*Removed*/, pass, null);
    }

    public void driveWithCorrectionSlowFloating(double minSpeed, double inches, double angle, boolean pass) {
        driveWithCorrectionSlowFloating(minSpeed, inches, angle, /*Removed*/, pass, null);
    }

    public void driveWithCorrectionFastFloating(double minSpeed, double inches, double angle, double delay, boolean pass, Supplier<Boolean> additionalStopParameter) {
        driveWithCorrectionFloating(/*Removed*/, minSpeed, /*Removed*/, inches, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, angle, /*Removed*/, delay, pass, additionalStopParameter);
    }

    public void driveWithCorrectionSlowFloating(double minSpeed, double inches, double angle, double delay, boolean pass, Supplier<Boolean> additionalStopParameter) {
        driveWithCorrectionFloating(/*Removed*/, minSpeed, /*Removed*/, inches, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, angle, /*Removed*/, delay, pass, additionalStopParameter);
    }


    @Override
    public void stop() {
        left.stop();
        right.stop();
    }

    public int inchesToEncoderPulses(double inches) {
        return (int) (57.1 * inches);
    }

    public double getLeftPower() {
        return left.getPower();
    }

    public double getRightPower() {
        return right.getPower();
    }

    public int getLeftEnc() {
        return left.getEncoder();
    }

    public int getRightEnc() {
        return right.getEncoder();
    }

    public void resetLeftEnc() {
        left.resetEncoder();
    }

    public void resetRightEnc() {
        right.resetEncoder();
    }
}
