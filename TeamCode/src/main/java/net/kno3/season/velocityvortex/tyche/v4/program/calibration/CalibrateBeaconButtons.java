package net.kno3.season.velocityvortex.tyche.v4.program.calibration;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import net.kno3.season.velocityvortex.tyche.v4.robot.Tyche;
import net.kno3.season.velocityvortex.tyche.v4.robot.TycheSettings;

/**
 * @author Jaxon A Brown
 */
@TeleOp(group = "calibration", name = "Calibrate Beacon Buttons")
public class CalibrateBeaconButtons extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("1", "Press start");
        telemetry.update();
        waitForStart();

        TycheSettings settings = new TycheSettings();

        ServoDefaults.resetAllServos(hardwareMap, settings);

        while(opModeIsActive()) {
            Servo rightBeaconPusher = hardwareMap.servo.get(Tyche.RIGHT_PUSHER_KEY);
            double rightSetpoint = settings.getBeaconRightBase();
            while(opModeIsActive() && !gamepad1.a) {
                if(Math.abs(gamepad1.left_stick_x) > 0.05) {
                    rightSetpoint += gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x) / 3000;
                }
                if(Math.abs(gamepad1.right_stick_x) > 0.05) {
                    rightSetpoint += gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x) / 15000;
                }
                rightBeaconPusher.setPosition(rightSetpoint);
                telemetry.addData("Right Setpoint: ", rightSetpoint);
                telemetry.addData("2", "Press A to set this side");
                telemetry.update();
            }

            telemetry.addData("2", "Release A");
            telemetry.update();
            while(opModeIsActive() && gamepad1.a);

            Servo leftBeaconPusher = hardwareMap.servo.get(Tyche.LEFT_PUSHER_KEY);
            double leftSetpoint = settings.getBeaconLeftBase();
            while(opModeIsActive() && !gamepad1.a) {
                if(Math.abs(gamepad1.left_stick_x) > 0.05) {
                    leftSetpoint += gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x) / 3000;
                }
                if(Math.abs(gamepad1.right_stick_x) > 0.05) {
                    leftSetpoint += gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x) / 15000;
                }
                leftBeaconPusher.setPosition(leftSetpoint);
                telemetry.addData("Left Setpoint: ", leftSetpoint);
                telemetry.addData("2", "Press A to set this side");
                telemetry.update();
            }

            telemetry.addData("2", "Release A");
            telemetry.update();
            while(opModeIsActive() && gamepad1.a);

            double extent = settings.getBeaconExtent();
            while(opModeIsActive() && !gamepad1.a) {
                if(Math.abs(gamepad1.left_stick_x) > 0.05) {
                    extent += gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x) / 3000;
                }
                if(Math.abs(gamepad1.right_stick_x) > 0.05) {
                    extent += gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x) / 15000;
                }
                leftBeaconPusher.setPosition(leftSetpoint + extent);
                rightBeaconPusher.setPosition(rightSetpoint - extent);
                telemetry.addData("Extent Setpoint: ", leftSetpoint);
                telemetry.addData("2", "Press A to set this side");
                telemetry.update();
            }

            telemetry.addData("3", "press B to test");
            telemetry.update();
            while(opModeIsActive() && !gamepad1.b);

            rightBeaconPusher.setPosition(rightSetpoint - extent);
            leftBeaconPusher.setPosition(leftSetpoint + extent);
            Thread.sleep(4000);
            rightBeaconPusher.setPosition(rightSetpoint);
            leftBeaconPusher.setPosition(leftSetpoint);
            Thread.sleep(4000);

            settings.setBeaconRightBase(rightSetpoint);
            settings.setBeaconLeftBase(leftSetpoint);
            settings.setBeaconExtent(extent);
            settings.save();

            telemetry.addData("5", "Saved! Press Y to try again.");
            telemetry.update();
            while(opModeIsActive() && !gamepad1.y);
        }
    }
}
