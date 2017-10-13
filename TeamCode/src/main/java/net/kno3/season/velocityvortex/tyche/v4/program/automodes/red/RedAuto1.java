package net.kno3.season.velocityvortex.tyche.v4.program.automodes.red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import net.kno3.season.velocityvortex.tyche.v4.program.TycheAuto;
import net.kno3.season.velocityvortex.tyche.v4.robot.ShooterSystem;
import net.kno3.util.SimpleColor;

/**
 * @author Jaxon A Brown
 */
@Autonomous(name = "redauto1 shoot beacons ramp")
public class RedAuto1 extends TycheAuto {
    public RedAuto1() {
        super(SimpleColor.RED);
    }

    @Override
    public void main() {
        drive.modeSpeed();
        sensors.getIMU().zeroHeading();

        shooter.setShooterState(ShooterSystem.ShooterState.SHORT);
        drive.driveForStop(0.4, -26);
        waitFor(1);
        shooter.feedBallSync();
        shooter.feedBallQuickSync();
        shooter.setShooterState(ShooterSystem.ShooterState.STOPPED);
        drive.driveForStop(0.4, -3);

        waitFor(0.5);
        drive.turnPIDfast(312);
        waitFor(0.5);
        drive.driveWithCorrectionFast(-66, 312, true);
        drive.stop();
        waitFor(0.5);

        drive.turnPIDslow(0);
        drive.stop();
        waitFor(0.5);

        long stopTime = System.currentTimeMillis() + 3500;
        drive.driveWithCorrection(0.3, /*Removed*/, -100, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, 0, /*Removed*/, /*Removed*/, false, () -> {
            telemetry.update();
            return sensors.getLeftLightPercent() > 0.4 || sensors.getRightLightPercent() > 0.4 || System.currentTimeMillis() > stopTime;
        });
        if(System.currentTimeMillis() < stopTime) {
            if (sensors.isRightRed()) {
                drive.driveForStop(0.25, 2.5);
            } else {
                drive.driveForStop(0.25, -2);
            }
            pushBeacon();

            drive.driveWithCorrectionFast(20, 0, true);
        } else {
            drive.driveForStop(0.4, 10);
            waitFor(0.5);
            drive.turnPIDslow(0);
        }
        drive.driveWithCorrection(0.3, /*Removed*/, 100, /*Removed*/, /*Removed*/, /*Removed*/, /*Removed*/, 0, /*Removed*/, /*Removed*/, false, () -> sensors.getLeftLightPercent() > 0.4 || sensors.getRightLightPercent() > 0.4);
        drive.stop();

        boolean doesNeedToCorrect;
        if(!(doesNeedToCorrect = !sensors.isRightRed())) {
            drive.driveForStop(0.25, 1.5);
        } else {
            drive.driveForStop(0.25, -2.5);
        }
        pushBeacon();

        if(doesNeedToCorrect) {
            drive.driveForStop(0.5, 5);
        }

        drive.turnPIDfast(350);
        drive.stop();
        drive.driveForStop(1, 36, true);
    }

    private void pushBeacon() {
        beaconButtons.extendRight();
        waitFor(2.5);
        beaconButtons.retractRight();
        waitFor(1);
    }
}