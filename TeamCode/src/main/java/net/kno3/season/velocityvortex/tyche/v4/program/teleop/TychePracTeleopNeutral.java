package net.kno3.season.velocityvortex.tyche.v4.program.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import net.kno3.opMode.DriverControlledProgram;
import net.kno3.robot.Robot;
import net.kno3.season.velocityvortex.tyche.v4.robot.Tyche;

/**
 * @author Jaxon A Brown
 */
@TeleOp(name = "Prac Teleop Neutral")
public class TychePracTeleopNeutral extends DriverControlledProgram {

    public TychePracTeleopNeutral() {
        disableTimer();
    }

    @Override
    protected Robot buildRobot() {
        return new Tyche(this, null);
    }
}
