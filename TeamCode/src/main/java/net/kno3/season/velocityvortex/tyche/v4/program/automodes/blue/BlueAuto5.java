package net.kno3.season.velocityvortex.tyche.v4.program.automodes.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * @author Jaxon A Brown
 */
@Autonomous(name = "blueauto5 close far shoot ramp")
public class BlueAuto5 extends BlueAutoCloseFarBase {

    @Override
    public void main() {
        super.main();

        drive.turnPIDfast(160);
        drive.driveWithCorrectionFast(65, 160, true);
    }
}