package net.kno3.season.velocityvortex.tyche.v4.program.calibration;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import net.kno3.season.velocityvortex.tyche.v4.robot.Tyche;
import net.kno3.season.velocityvortex.tyche.v4.robot.TycheSettings;
import net.kno3.util.Threading;

/**
 * @author Jaxon A Brown
 */
@TeleOp(group = "calibration", name = "Calibrate Feeder")
public class CalibrateFeeder extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("1", "Press start");
        telemetry.update();
        waitForStart();

        TycheSettings settings = new TycheSettings();

        ServoDefaults.resetAllServos(hardwareMap, settings);


        while(opModeIsActive()) {
            Servo feeder = hardwareMap.servo.get(Tyche.FEEDER_KEY);
            double loadingSetpoint = settings.getFeederLoadingPosition();
            while(opModeIsActive() && !gamepad1.a) {
                if(Math.abs(gamepad1.left_stick_x) > 0.05) {
                    loadingSetpoint += gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x) / 3000;
                }
                if(Math.abs(gamepad1.right_stick_x) > 0.05) {
                    loadingSetpoint += gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x) / 15000;
                }
                feeder.setPosition(loadingSetpoint);
                telemetry.addData("Loading Setpoint: ", loadingSetpoint);
                telemetry.addData("2", "Press A to continue");
                telemetry.update();
            }

            telemetry.addData("2", "Release A");
            telemetry.update();
            while(opModeIsActive() && gamepad1.a);

            double feedingSetpoint = settings.getFeederFeedingPosition();
            while(opModeIsActive() && !gamepad1.a) {
                if(Math.abs(gamepad1.left_stick_x) > 0.05) {
                    feedingSetpoint += gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x) / 3000;
                }
                if(Math.abs(gamepad1.right_stick_x) > 0.05) {
                    feedingSetpoint += gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x) / 15000;
                }
                feeder.setPosition(feedingSetpoint);
                telemetry.addData("Feeding Setpoint: ", feedingSetpoint);
                telemetry.addData("2", "Press A to continue");
                telemetry.update();
            }

            telemetry.addData("2", "Release A");
            telemetry.update();
            while(opModeIsActive() && gamepad1.a);

            telemetry.addData("3", "press B to test");
            telemetry.update();
            while(opModeIsActive() && !gamepad1.b);

            feeder.setPosition(loadingSetpoint);
            Threading.delay(2);
            feeder.setPosition(feedingSetpoint);

            settings.setFeederLoadingPosition(loadingSetpoint);
            settings.setFeederFeedingPosition(feedingSetpoint);
            settings.save();

            telemetry.addData("5", "Saved! Press Y to try again.");
            telemetry.update();
            while(opModeIsActive() && !gamepad1.y);
        }
    }
}
