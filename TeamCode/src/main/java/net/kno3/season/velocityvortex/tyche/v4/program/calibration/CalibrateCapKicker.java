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
@TeleOp(group = "calibration", name = "Calibrate Kicker")
public class CalibrateCapKicker extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("1", "Press start");
        telemetry.update();
        waitForStart();

        TycheSettings settings = new TycheSettings();

        ServoDefaults.resetAllServos(hardwareMap, settings);


        while(opModeIsActive()) {
            Servo kicker = hardwareMap.servo.get(Tyche.LIFT_KICKER_KEY);
            double idleSetpoint = settings.getLiftKickerIdlePosition();
            while(opModeIsActive() && !gamepad1.a) {
                if(Math.abs(gamepad1.left_stick_x) > 0.05) {
                    idleSetpoint += gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x) / 3000;
                }
                if(Math.abs(gamepad1.right_stick_x) > 0.05) {
                    idleSetpoint += gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x) / 15000;
                }
                kicker.setPosition(idleSetpoint);
                telemetry.addData("Idle Setpoint: ", idleSetpoint);
                telemetry.addData("2", "Press A to continue");
                telemetry.update();
            }

            telemetry.addData("2", "Release A");
            telemetry.update();
            while(opModeIsActive() && gamepad1.a);

            double kickingSetpoint = settings.getLiftKickerKickPosition();
            while(opModeIsActive() && !gamepad1.a) {
                if(Math.abs(gamepad1.left_stick_x) > 0.05) {
                    kickingSetpoint += gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x) / 3000;
                }
                if(Math.abs(gamepad1.right_stick_x) > 0.05) {
                    kickingSetpoint += gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x) / 15000;
                }
                kicker.setPosition(kickingSetpoint);
                telemetry.addData("Kicking Setpoint: ", kickingSetpoint);
                telemetry.addData("2", "Press A to continue");
                telemetry.update();
            }

            telemetry.addData("2", "Release A");
            telemetry.update();
            while(opModeIsActive() && gamepad1.a);

            telemetry.addData("3", "press B to test");
            telemetry.update();
            while(opModeIsActive() && !gamepad1.b);

            kicker.setPosition(idleSetpoint);
            Threading.delay(2);
            kicker.setPosition(kickingSetpoint);
            Threading.delay(2);

            settings.setLiftKickerIdlePosition(idleSetpoint);
            settings.setLiftKickerKickPosition(kickingSetpoint);
            settings.save();

            telemetry.addData("5", "Saved! Press Y to try again.");
            telemetry.update();
            while(opModeIsActive() && !gamepad1.y);
        }
    }
}
