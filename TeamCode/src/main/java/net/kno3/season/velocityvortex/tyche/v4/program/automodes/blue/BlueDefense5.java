package net.kno3.season.velocityvortex.tyche.v4.program.automodes.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import net.kno3.season.velocityvortex.tyche.v4.program.TycheAuto;
import net.kno3.season.velocityvortex.tyche.v4.robot.ShooterSystem;
import net.kno3.util.SimpleColor;

/**
 * Created by jaxon on 4/21/2017.
 */
@Autonomous(name = "bluedefense5 shoot gdef")
public class BlueDefense5 extends TycheAuto {

    public BlueDefense5() {
        super(SimpleColor.BLUE);
    }

    @Override
    public void main() {
        drive.modeSpeed();
        sensors.getIMU().zeroHeading();
        shooter.tryUpdateVoltage();

        drive.driveForStop(0.4, -4);

        drive.set(0.15, -0.15);
        double heading;
        while (opModeIsActive() && ((heading = sensors.getIMU().getHeading()) < 43 || heading > 180)) {
            telemetry.update();
        }
        drive.stop();
        drive.driveWithCorrectionSlow(-36, 43, true);

        waitFor(1);
        shooter.tryUpdateVoltage();

        shooter.setShooterState(ShooterSystem.ShooterState.SHORT);
        waitFor(1);
        shooter.feedBallSync();
        shooter.feedBallQuickSync();
        shooter.setShooterState(ShooterSystem.ShooterState.STOPPED);

        drive.turnPIDfast(225);
        collector.regurgitate(1);
        drive.driveWithCorrectionFastFloating(0.3, 55, 225);
    }
}
