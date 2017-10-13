package net.kno3.season.velocityvortex.tyche.v4.program.automodes.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import net.kno3.season.velocityvortex.tyche.v4.program.TycheAuto;
import net.kno3.season.velocityvortex.tyche.v4.robot.ShooterSystem;
import net.kno3.util.SimpleColor;

@Autonomous(name = "blueauto8 shoot far close shoot ramp")
public class BlueAuto8 extends TycheAuto {
    public BlueAuto8() {
        super(SimpleColor.BLUE);
    }

    double initialHeading = 4;

    @Override
    public void postInit() {
        telemetry.addData("Initial Heading", () -> initialHeading);
        telemetry.update();
    }

    @Override
    public void main() {
        drive.modeSpeed();

        shooter.setShooterState(ShooterSystem.ShooterState.SHORT);
        drive.driveWithCorrectionFast(-22, initialHeading, true);

        waitFor(1);
        shooter.tryUpdateVoltage();
        shooter.setShooterState(ShooterSystem.ShooterState.SHORT);
        waitFor(0.5);
        shooter.feedBallSync();
        shooter.feedBallQuickSync();
        shooter.setShooterState(ShooterSystem.ShooterState.STOPPED);

        drive.turnPIDfast(228);
        collector.collect(1);
        collector.enableTelemetryRejection();

        drive.driveWithCorrectionFastFloating(1, 60, 228, /*Removed*/, true, () -> false);
        drive.stop();
        waitFor(0.1);
        drive.driveForStop(0.2, -0.25);
        drive.turnPID(0.55, /*Removed*/, /*Removed*/, /*Removed*/, 180, /*Removed*/, /*Removed*/);
        waitFor(0.2);

        drive.driveWithCorrection(0.25, /*Removed*/, 100, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, 180, /*Removed*/, /*Removed*/, false, () -> {
            telemetry.update();
            return sensors.getLeftLightPercent() > 0.4 || sensors.getRightLightPercent() > 0.4;
        });

        if (sensors.isRightBlue()) {
            drive.driveForStop(0.25, 2);
        } else {
            drive.driveForStop(0.25, -3.5);
        }
        pushBeacon();

        drive.drive(0.5);
        waitFor(1.5);
        drive.stop();

        drive.driveWithCorrectionFastFloating(0.2, -50, 180, true);
        drive.driveWithCorrection(0.3, /*Removed*/, -100, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, 180, /*Removed*/, /*Removed*/, false, () -> {
            telemetry.update();
            return sensors.getLeftLightPercent() > 0.4 || sensors.getRightLightPercent() > 0.4;
        });

        if(sensors.isRightBlue()) {
            drive.driveForStop(0.25, 2.5);
            pushBeacon();
        } else {
            drive.driveForStop(0.25, -2.5);
            pushBeacon();
            drive.driveFor(0.25, 2.5);
        }
        drive.driveForStop(0.25, 5);

        if(sensors.getBallCount() > 0) {
            hasBall();
        } else {
            toRamp();
        }
    }

    private void hasBall() {
        drive.driveForStop(0.25, 5);

        drive.driveWithCorrection(0.2, /*Removed*/, -100, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, 180, /*Removed*/, /*Removed*/, false, () -> {
            telemetry.update();
            return sensors.getLeftLightPercent() > 0.4 || sensors.getRightLightPercent() > 0.4;
        });

        drive.turnPIDfast(270);

        waitFor(1);

        shooter.tryUpdateVoltage();
        shooter.setShooterState(ShooterSystem.ShooterState.SHORT);
        drive.driveWithCorrectionFast(-17, 272, true);


        waitFor(1);
        while(sensors.getBallCount() > 0) {
            shooter.feedBallSync();
        }
        shooter.setShooterState(ShooterSystem.ShooterState.STOPPED);
    }

    private void toRamp() {
        drive.turnPIDfast(190);
        drive.stop();
        drive.driveForStop(1, -36, true);
    }

    private void pushBeacon() {
        beaconButtons.extendRight();
        waitFor(2);
        beaconButtons.retractRight();
        waitFor(0.5);
    }
}
