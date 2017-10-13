package net.kno3.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import net.kno3.season.velocityvortex.tyche.v4.robot.Tyche;
import net.kno3.season.velocityvortex.tyche.v4.robot.TycheSettings;
import net.kno3.util.DoubleRateTracker;
import net.kno3.util.DoubleRateTrackerPlusTime;
import net.kno3.util.MotorPair;
import net.kno3.util.SynchronousPID;
import net.kno3.util.ValuesAdjuster;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jaxon on 12/21/2016.
 */
@TeleOp(name = "shooterpidtest")
public class ShooterPIDTest extends LinearOpMode {
    private double speed, speed2;
    private double waitCount;
    private double p, i, d, min, max, deadband;
    private double delay;

    private boolean isFeeding;
    private long nextAction;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("1", "Press start");
        telemetry.update();

        waitForStart();
        TycheSettings settings = new TycheSettings();
        DcMotor shooter1 = hardwareMap.dcMotor.get("shooter");
        DcMotor shooter2 = hardwareMap.dcMotor.get("shooter2");
        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter2.setDirection(DcMotorSimple.Direction.REVERSE);
        Servo feeder = hardwareMap.servo.get("feeder");
        hardwareMap.servo.get(Tyche.LEFT_PUSHER_KEY).setPosition(settings.getBeaconLeftBase());
        hardwareMap.servo.get(Tyche.RIGHT_PUSHER_KEY).setPosition(settings.getBeaconRightBase());
        hardwareMap.servo.get(Tyche.LIFT_CAP_CLAMP_KEY).setPosition(.460);
        hardwareMap.servo.get(Tyche.LIFT_HOOK_LEFT_KEY).setPosition(0.554);
        hardwareMap.dcMotor.get(Tyche.COLLECTOR_KEY).setPower(0);
        hardwareMap.dcMotor.get(Tyche.LIFT_KEY).setPower(0);
        hardwareMap.dcMotor.get(Tyche.DRIVE_FL_KEY).setPower(0);
        hardwareMap.dcMotor.get(Tyche.DRIVE_FR_KEY).setPower(0);
        hardwareMap.dcMotor.get(Tyche.DRIVE_RL_KEY).setPower(0);
        hardwareMap.dcMotor.get(Tyche.DRIVE_RR_KEY).setPower(0);

        ValuesAdjuster adjuster = new ValuesAdjuster(this, telemetry);
        adjuster.addValue("speed", "Speed", -1, 1);
        adjuster.addValue("speed2", "Speed 2", -1, 1);
        adjuster.addValue("waitCount", "Feeding Delay", 1, 10000);
        waitCount = settings.getShooterFeedDelay();

        while(opModeIsActive()) {

            while(opModeIsActive() && !gamepad1.a) {
                telemetry.addData("1", "Adjust shooter speed and feeding delay with gamepad2");
                telemetry.addData("2", "Press A to test");
                adjuster.update(gamepad2);
                telemetry.update();
            }

            telemetry.addData("3", "Release A");
            while(opModeIsActive() && gamepad1.a);

            shooter1.setPower(speed);
            shooter2.setPower(speed2);
            while(opModeIsActive() && !gamepad1.b) {
                telemetry.addData("Speed", speed);
                telemetry.addData("Speed2", speed2);
                telemetry.addData("4", "Press B to stop");

                if (gamepad1.a) {
                    if (isFeeding && (nextAction == 0 || System.currentTimeMillis() > nextAction)) {
                        feeder.setPosition(0.64);
                        nextAction = System.currentTimeMillis() + 500;
                        isFeeding = false;
                    } else if (!isFeeding && (nextAction == 0 || System.currentTimeMillis() > nextAction)) {
                        feeder.setPosition(0.23);
                        nextAction = System.currentTimeMillis() + (long) waitCount;
                        isFeeding = true;
                    }
                } else {
                    feeder.setPosition(0.23);
                    isFeeding = false;
                    nextAction = 0;
                }

                telemetry.update();
            }
            shooter1.setPower(0);
            shooter2.setPower(0);

            telemetry.addData("5", "Saved! Press Y to try again.");
            telemetry.update();
            while(opModeIsActive() && !gamepad1.y);
        }
    }
}
