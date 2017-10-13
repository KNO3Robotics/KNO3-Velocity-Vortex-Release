package net.kno3.season.velocityvortex.tyche.v4.program.automodes.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by jaxon on 4/21/2017.
 */
@Autonomous(name = "bluedefense3 shoot capdef")
public class BlueDefense3 extends BlueAutoShootBase {

    @Override
    public void main() {
        super.main();

        //drive.turnPIDfast(170);
        drive.turnPID(0.6, /*Removed*/, /*Removed*/, /*Removed*/, 170, /*Removed*/, /*Removed*/);
        collector.regurgitate(1);
        drive.driveWithCorrectionFastFloating(.3, 60, 170);
    }
}
