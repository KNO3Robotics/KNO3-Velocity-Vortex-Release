package net.kno3.season.velocityvortex.tyche.v4.program.automodes.blue;

import net.kno3.season.velocityvortex.tyche.v4.program.TycheAuto;
import net.kno3.season.velocityvortex.tyche.v4.robot.ShooterSystem;
import net.kno3.util.SimpleColor;

/**
 * @author Jaxon A Brown
 */
public abstract class BlueAutoFarCloseBase extends TycheAuto {
    public BlueAutoFarCloseBase() {
        super(SimpleColor.BLUE);
    }

    double initialHeading = 25;

    @Override
    public void postInit() {
        telemetry.addData("Initial Heading", () -> initialHeading);
        telemetry.update();
    }

    @Override
    public void main() {
        drive.modeSpeed();

        drive.driveWithCorrectionFastFloating(1, 90, initialHeading, 0.01, true, () -> false);
        drive.driveForStop(0.25, -0.5);
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

        drive.driveWithCorrectionFastFloating(0.3, -25, 4, true);
        drive.driveWithCorrection(0.25, /*Removed*/, -100, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, 2, /*Removed*/, /*Removed*/, false, () -> {
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

        drive.driveWithCorrection(0.2, /*Removed*/, -100, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, 0, /*Removed*/, /*Removed*/, false, () -> {
            telemetry.update();
            return sensors.getLeftLightPercent() > 0.4 || sensors.getRightLightPercent() > 0.4;
        });

        drive.turnPIDfast(90);

        waitFor(1);

        shooter.tryUpdateVoltage();
        shooter.setShooterState(ShooterSystem.ShooterState.SHORT);
        drive.driveWithCorrectionFast(-17, 92, true);


        waitFor(0.5);
        //shooter.tryUpdateVoltage();
        //shooter.setShooterState(ShooterSystem.ShooterState.SHORT);
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