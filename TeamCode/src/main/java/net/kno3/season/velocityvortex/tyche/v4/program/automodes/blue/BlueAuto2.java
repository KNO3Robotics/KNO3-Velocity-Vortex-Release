package net.kno3.season.velocityvortex.tyche.v4.program.automodes.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by jaxon on 12/2/2016.
 */
@Autonomous(name = "blueauto2 shoot cap")
public class BlueAuto2 extends BlueAutoShootBase {
    @Override
    public void main() {
        super.main();

        drive.set(0.6, -0.6);
        double heading;
        while (opModeIsActive() && (heading = sensors.getIMU().getHeading()) < 220) {
            if(heading > 130) {
                drive.set(0.1, -0.1);
            }
            telemetry.update();
        }
        drive.stop();

        collector.regurgitate(1);
        drive.driveWithCorrectionSlow(30, 225);
        drive.driveForStop(-4);
    }
}
