package net.kno3.season.velocityvortex.tyche.v4.program.teleop.blue;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import net.kno3.opMode.DriverControlledProgram;
import net.kno3.robot.Robot;
import net.kno3.season.velocityvortex.tyche.v4.robot.Tyche;
import net.kno3.util.SimpleColor;

/**
 * @author Jaxon A Brown
 */
@TeleOp(name = "Prac Teleop Blue")
public class TychePracTeleopBlue extends DriverControlledProgram {

    public TychePracTeleopBlue() {
        disableTimer();
    }

    @Override
    protected Robot buildRobot() {
        return new Tyche(this, SimpleColor.BLUE);
    }
}
