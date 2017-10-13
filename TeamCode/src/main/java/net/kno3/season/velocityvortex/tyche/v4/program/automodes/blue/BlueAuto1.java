package net.kno3.season.velocityvortex.tyche.v4.program.automodes.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import net.kno3.season.velocityvortex.tyche.v4.program.TycheAuto;
import net.kno3.season.velocityvortex.tyche.v4.robot.ShooterSystem;
import net.kno3.util.SimpleColor;

/**
 * @author Jaxon A Brown
 */
@Autonomous(name = "blueauto1 shoot far close ramp")
public class BlueAuto1 extends TycheAuto {
    public BlueAuto1() {
        super(SimpleColor.BLUE);
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
        drive.turnPIDfast(48);
        waitFor(0.5);
        drive.driveWithCorrectionFast(-66, 48, true);
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
            if (sensors.isLeftBlue()) {
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
        if(!(doesNeedToCorrect = !sensors.isLeftBlue())) {
            drive.driveForStop(0.25, 1.5);
        } else {
            drive.driveForStop(0.25, -2.5);
        }
        pushBeacon();

        if(doesNeedToCorrect) {
            drive.driveForStop(0.5, 5);
        }

        drive.turnPIDfast(10);
        drive.stop();
        drive.driveForStop(1, 36, true);
    }

    private void pushBeacon() {
        beaconButtons.extendLeft();
        waitFor(2.5);
        beaconButtons.retractLeft();
        waitFor(1);
    }
}