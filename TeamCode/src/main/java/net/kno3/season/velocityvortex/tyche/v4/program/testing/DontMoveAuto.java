package net.kno3.season.velocityvortex.tyche.v4.program.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import net.kno3.season.velocityvortex.tyche.v4.program.automodes.TycheAutoBlue;

/**
 * Created by jaxon on 3/24/2017.
 */
@Autonomous(name = "nomoveauto")
public class DontMoveAuto extends TycheAutoBlue {
    @Override
    public void main() {
        while (opModeIsActive()) {
            telemetry.update();
        }
    }
}
