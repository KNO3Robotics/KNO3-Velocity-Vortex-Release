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
@TeleOp(group = "calibration", name = "Calibrate Lift Hook")
public class CalibrateLiftHook extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("1", "Press start");
        telemetry.update();
        waitForStart();

        TycheSettings settings = new TycheSettings();

        ServoDefaults.resetAllServos(hardwareMap, settings);


        while(opModeIsActive()) {
            Servo hook = hardwareMap.servo.get(Tyche.LIFT_HOOK_LEFT_KEY);
            double hookedSetpoint = settings.getLiftHookClampedPosition();
            while(opModeIsActive() && !gamepad1.a) {
                if(Math.abs(gamepad1.left_stick_x) > 0.05) {
                    hookedSetpoint += gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x) / 3000;
                }
                if(Math.abs(gamepad1.right_stick_x) > 0.05) {
                    hookedSetpoint += gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x) / 15000;
                }
                hook.setPosition(hookedSetpoint);
                telemetry.addData("Clamped Setpoint: ", hookedSetpoint);
                telemetry.addData("2", "Press A to continue");
                telemetry.update();
            }

            telemetry.addData("2", "Release A");
            telemetry.update();
            while(opModeIsActive() && gamepad1.a);

            double releasedSetpoint = settings.getLiftHookReleasedPosition();
            while(opModeIsActive() && !gamepad1.a) {
                if(Math.abs(gamepad1.left_stick_x) > 0.05) {
                    releasedSetpoint += gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x) / 3000;
                }
                if(Math.abs(gamepad1.right_stick_x) > 0.05) {
                    releasedSetpoint += gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x) / 15000;
                }
                hook.setPosition(releasedSetpoint);
                telemetry.addData("Released Setpoint: ", releasedSetpoint);
                telemetry.addData("2", "Press A to continue");
                telemetry.update();
            }

            telemetry.addData("2", "Release A");
            telemetry.update();
            while(opModeIsActive() && gamepad1.a);

            telemetry.addData("3", "press B to test");
            telemetry.update();
            while(opModeIsActive() && !gamepad1.b);

            hook.setPosition(hookedSetpoint);
            Threading.delay(2);
            hook.setPosition(releasedSetpoint);
            Threading.delay(2);

            settings.setLiftHookClampedPosition(hookedSetpoint);
            settings.setLiftHookReleasedPosition(releasedSetpoint);
            settings.save();

            telemetry.addData("5", "Saved! Press Y to try again.");
            telemetry.update();
            while(opModeIsActive() && !gamepad1.y);
        }
    }
}
