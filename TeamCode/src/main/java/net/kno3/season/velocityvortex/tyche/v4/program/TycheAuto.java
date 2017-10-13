package net.kno3.season.velocityvortex.tyche.v4.program;

import net.kno3.opMode.AutonomousProgram;
import net.kno3.robot.Robot;
import net.kno3.season.velocityvortex.tyche.v4.robot.*;
import net.kno3.util.SimpleColor;

/**
 * @author Jaxon A Brown
 */
public abstract class TycheAuto extends AutonomousProgram {
    public SensorSystem sensors;
    public DriveSystem drive;
    public CollectorSystem collector;
    public ShooterSystem shooter;
    public BeaconButtonSystem beaconButtons;
    public LiftSystem lift;

    private SimpleColor alliance;

    public TycheAuto(SimpleColor alliance) {
        this.alliance = alliance;
    }

    @Override
    protected Robot buildRobot() {
        Tyche tyche = new Tyche(this, alliance);
        sensors = tyche.getSubSystem(SensorSystem.class);
        drive = tyche.getSubSystem(DriveSystem.class);
        collector = tyche.getSubSystem(CollectorSystem.class);
        shooter = tyche.getSubSystem(ShooterSystem.class);
        beaconButtons = tyche.getSubSystem(BeaconButtonSystem.class);
        lift = tyche.getSubSystem(LiftSystem.class);
        return tyche;
    }
}
