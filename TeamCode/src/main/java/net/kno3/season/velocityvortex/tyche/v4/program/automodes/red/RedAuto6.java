package net.kno3.season.velocityvortex.tyche.v4.program.automodes.red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * @author Jaxon A Brown
 */
@Autonomous(name = "redauto6 far close shoot cap")
public class RedAuto6 extends RedAutoFarCloseBase {

    @Override
    public void main() {
        super.main();

        drive.driveFor(0.5, -8);
        drive.turnPIDfast(290);
        drive.turnPIDfast(270);
        drive.driveForStop(0.5, -10);
    }
}