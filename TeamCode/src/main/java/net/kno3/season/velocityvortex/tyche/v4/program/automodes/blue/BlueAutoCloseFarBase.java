package net.kno3.season.velocityvortex.tyche.v4.program.automodes.blue;

import net.kno3.season.velocityvortex.tyche.v4.program.TycheAuto;
import net.kno3.season.velocityvortex.tyche.v4.robot.ShooterSystem;
import net.kno3.util.SimpleColor;

/**
 * @author Jaxon A Brown
 */
public abstract class BlueAutoCloseFarBase extends TycheAuto {
    public BlueAutoCloseFarBase() {
        super(SimpleColor.BLUE);
    }

    double initialHeading = 45;

    @Override
    public void postInit() {
        telemetry.addData("Initial Heading", () -> initialHeading);
        telemetry.update();
    }

    @Override
    public void main() {
        drive.modeSpeed();

        drive.driveWithCorrectionFastFloating(1, 39, initialHeading, /*Removed*/, true, () -> false);
        drive.turnPID(0.4, /*Removed*/, /*Removed*/, /*Removed*/, 0, /*Removed*/, /*Removed*/);
        waitFor(0.2);

        drive.driveWithCorrection(0.2, /*Removed*/, 100, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, 0, /*Removed*/, /*Removed*/, false, () -> {
            telemetry.update();
            return sensors.getLeftLightPercent() > 0.4 || sensors.getRightLightPercent() > 0.4;
        });

        if (sensors.isRightBlue()) {
            drive.driveForStop(0.25, 2);
        } else {
            drive.driveForStop(0.25, -3.5);
        }
        pushBeacon();

        drive.driveWithCorrectionFastFloating(1, 25, 0, true);
        drive.driveWithCorrection(0.25, /*Removed*/, 100, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, 0, /*Removed*/, /*Removed*/, false, () -> {
            telemetry.update();
            return sensors.getLeftLightPercent() > 0.3 || sensors.getRightLightPercent() > 0.3;
        });

        if(sensors.isRightBlue()) {
            drive.driveForStop(0.25, 2.5);
            pushBeacon();
        } else {
            drive.driveForStop(0.25, -2.5);
            pushBeacon();
            drive.driveForStop(0.25, 2.5);
        }

        drive.driveWithCorrection(0.3, /*Removed*/, -100, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, 0, /*Removed*/, /*Removed*/, false, () -> {
            telemetry.update();
            return sensors.getLeftLightPercent() > 0.4 || sensors.getRightLightPercent() > 0.4;
        });

        drive.turnPIDfast(49);

        waitFor(0.5);

        shooter.tryUpdateVoltage();
        shooter.setShooterState(ShooterSystem.ShooterState.SHORT);
        drive.driveWithCorrectionFast(-36, 49, true);

        waitFor(0.5);
        shooter.tryUpdateVoltage();
        shooter.setShooterState(ShooterSystem.ShooterState.SHORT);
        waitFor(0.5);
        shooter.feedBallSync();
        waitFor(1);
        shooter.feedBallQuickSync();
        shooter.setShooterState(ShooterSystem.ShooterState.STOPPED);
    }

    private void pushBeacon() {
        beaconButtons.extendRight();
        waitFor(2);
        beaconButtons.retractRight();
        waitFor(0.5);
    }
}