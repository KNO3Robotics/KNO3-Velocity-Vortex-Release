package net.kno3.season.velocityvortex.tyche.v4.program.automodes.red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * @author Jaxon A Brown
 */
@Autonomous(name = "redauto5 close far shoot ramp")
public class RedAuto5 extends RedAutoCloseFarBase {

    @Override
    public void main() {
        super.main();

        drive.turnPIDfast(200);
        drive.driveWithCorrectionFast(68, 200, true);
    }
}