package net.kno3.season.velocityvortex.tyche.v4.program.automodes.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * @author Jaxon A Brown
 */
@Autonomous(name = "blueauto4 close far shoot cap")
public class BlueAuto4 extends BlueAutoCloseFarBase {

    @Override
    public void main() {
        super.main();

        drive.driveFor(0.5, -20);
    }
}