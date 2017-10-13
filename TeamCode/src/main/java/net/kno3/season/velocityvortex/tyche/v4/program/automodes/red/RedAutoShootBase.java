package net.kno3.season.velocityvortex.tyche.v4.program.automodes.red;

import net.kno3.season.velocityvortex.tyche.v4.program.TycheAuto;
import net.kno3.season.velocityvortex.tyche.v4.robot.ShooterSystem;
import net.kno3.util.SimpleColor;

/**
 * Created by jaxon on 12/2/2016.
 */
public abstract class RedAutoShootBase extends TycheAuto {
    public RedAutoShootBase() {
        super(SimpleColor.RED);
    }

    @Override
    public void main() {
        drive.modeSpeed();
        sensors.getIMU().zeroHeading();
        shooter.tryUpdateVoltage();

        drive.driveForStop(0.4, -4);

        drive.set(-0.1, 0.1);
        double heading;
        while (opModeIsActive() && ((heading = sensors.getIMU().getHeading()) > 317 || heading < 180)) {
            telemetry.update();
        }
        drive.stop();
        drive.driveWithCorrectionSlow(-36, 317, true);

        waitFor(1);
        shooter.tryUpdateVoltage();

        shooter.setShooterState(ShooterSystem.ShooterState.SHORT);
        waitFor(1);
        shooter.feedBallSync();
        shooter.feedBallQuickSync();
        shooter.setShooterState(ShooterSystem.ShooterState.STOPPED);
    }
}
