package net.kno3.season.velocityvortex.tyche.v4.program.calibration;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import net.kno3.season.velocityvortex.tyche.v4.robot.TycheSettings;

import net.kno3.season.velocityvortex.tyche.v4.robot.Tyche;
import net.kno3.util.DoubleRateTrackerPlusTime;
import net.kno3.util.MotorPair;
import net.kno3.util.ValuesAdjuster;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jaxon A Brown
 */
@TeleOp(group = "calibration", name = "Calibrate Shooter (short)")
public class CalibrateShooter extends LinearOpMode {
    private double speed;
    private double waitCount;

    private boolean isFeeding;
    private long nextAction;

    private Set<VoltageSensor> voltageSensors;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("1", "Press start");
        telemetry.update();

        voltageSensors = new HashSet<>();
        for(VoltageSensor vsens : hardwareMap.voltageSensor) {
            voltageSensors.add(vsens);
        }

        waitForStart();
        TycheSettings settings = new TycheSettings();
        DcMotor shooter1 = hardwareMap.dcMotor.get("shooter");
        DcMotor shooter2 = hardwareMap.dcMotor.get("shooter2");
        shooter2.setDirection(DcMotorSimple.Direction.REVERSE);
        MotorPair shooter = new MotorPair(shooter1, shooter2);
        shooter.floatMode();
        Servo feeder = hardwareMap.servo.get("feeder");
        ServoDefaults.resetAllServos(hardwareMap, settings);
        hardwareMap.dcMotor.get(Tyche.COLLECTOR_KEY).setPower(0);
        hardwareMap.dcMotor.get(Tyche.LIFT_KEY).setPower(0);
        hardwareMap.dcMotor.get(Tyche.DRIVE_FL_KEY).setPower(0);
        hardwareMap.dcMotor.get(Tyche.DRIVE_FR_KEY).setPower(0);
        hardwareMap.dcMotor.get(Tyche.DRIVE_RL_KEY).setPower(0);
        hardwareMap.dcMotor.get(Tyche.DRIVE_RR_KEY).setPower(0);

        ValuesAdjuster adjuster = new ValuesAdjuster(this, telemetry);
        adjuster.addValue("speed", "Speed", -1, 1);
        adjuster.addValue("waitCount", "Feeding Delay", 1, 10000);

        while(opModeIsActive()) {
            speed = settings.getShooterShortBase();
            waitCount = settings.getShooterFeedDelay();

            while(opModeIsActive() && !gamepad1.a) {
                telemetry.addData("1", "Adjust shooter speed and feeding delay with gamepad2");
                telemetry.addData("2", "Press A to test");
                adjuster.update(gamepad2);
                telemetry.update();
            }

            telemetry.addData("3", "Release A");
            while(opModeIsActive() && gamepad1.a);

            DoubleRateTrackerPlusTime timeRateTracker = new DoubleRateTrackerPlusTime(0, shooter.getEncoder(), 50);

            shooter.setPower(Tyche.extrapolateShooterPower(getVoltage(), speed));
            long last = System.currentTimeMillis();
            while(opModeIsActive() && !gamepad1.b) {
                timeRateTracker.update(shooter.getEncoder());
                telemetry.addData("Speed", speed);
                telemetry.addData("Rate", timeRateTracker.getRate());
                telemetry.addData("4", "Press B to stop");

                if (gamepad1.a) {
                    if (isFeeding && (nextAction == 0 || System.currentTimeMillis() > nextAction)) {
                        feeder.setPosition(settings.getFeederFeedingPosition());
                        nextAction = System.currentTimeMillis() + 500;
                        isFeeding = false;
                    } else if (!isFeeding && (nextAction == 0 || System.currentTimeMillis() > nextAction)) {
                        feeder.setPosition(settings.getFeederLoadingPosition());
                        nextAction = System.currentTimeMillis() + (long) waitCount;
                        isFeeding = true;
                    }
                } else {
                    feeder.setPosition(settings.getFeederLoadingPosition());
                    isFeeding = false;
                    nextAction = 0;
                }

                telemetry.update();
                long sleep = last + 50 - System.currentTimeMillis();
                if(sleep > 0) {
                    Thread.sleep(sleep);
                }
            }
            shooter.setPower(0);


            settings.setShooterShortBase(speed);
            settings.setShooterFeedDelay((int) waitCount);
            settings.save();

            telemetry.addData("5", "Saved! Press Y to try again.");
            telemetry.update();
            while(opModeIsActive() && !gamepad1.y);
        }
    }

    private double getVoltage() {
        double voltage = 0;
        for(VoltageSensor vSensor : voltageSensors) {
            voltage += vSensor.getVoltage();
        }
        return voltage / voltageSensors.size();
    }
}