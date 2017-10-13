package net.kno3.season.velocityvortex.tyche.v4.program.automodes.red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by jaxon on 12/2/2016.
 */
@Autonomous(name = "redauto2 shoot cap")
public class RedAuto2 extends RedAutoShootBase {

    @Override
    public void main() {
        super.main();

        drive.set(-0.6, 0.6);
        double heading;
        while (opModeIsActive() && (heading = sensors.getIMU().getHeading()) > 140) {
            if(heading < 230) {
                drive.set(-0.1, 0.1);
            }
            telemetry.update();
        }
        drive.stop();

        collector.regurgitate(1);
        drive.driveWithCorrectionSlow(30, 140);
        drive.driveForStop(-4);
    }
}
