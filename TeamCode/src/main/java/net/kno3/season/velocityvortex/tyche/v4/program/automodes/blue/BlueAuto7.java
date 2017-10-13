package net.kno3.season.velocityvortex.tyche.v4.program.automodes.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * @author Jaxon A Brown
 */
@Autonomous(name = "blueauto7 far close shoot ramp")
public class BlueAuto7 extends BlueAutoFarCloseBase {

    @Override
    public void main() {
        super.main();

        drive.turnPIDfast(140);
        drive.driveWithCorrectionFast(43, 140);
    }
}