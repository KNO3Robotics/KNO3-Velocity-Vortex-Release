package net.kno3.season.velocityvortex.tyche.v4.program.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import net.kno3.season.velocityvortex.tyche.v4.program.automodes.TycheAutoBlue;

/**
 * Created by jaxon on 3/24/2017.
 */
@Autonomous(name = "rfgen")
public class RFGenAuto extends TycheAutoBlue {
    @Override
    public void main() {
        while (opModeIsActive()) {
            drive.drive(1);
            waitFor(2);
            drive.drive(-1);
            waitFor(2);
            telemetry.update();
        }
    }
}
