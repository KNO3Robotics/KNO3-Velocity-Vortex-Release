package net.kno3.season.velocityvortex.tyche.v4.program.automodes.red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by jaxon on 12/2/2016.
 */
@Autonomous(name = "redauto3 shoot ramp")
public class RedAuto3 extends RedAutoShootBase {

    @Override
    public void main() {
        super.main();

        drive.driveForStop(0.3, 10);

        drive.turnPIDfast(80);
        drive.driveWithCorrectionFast(68, 80);
    }
}
