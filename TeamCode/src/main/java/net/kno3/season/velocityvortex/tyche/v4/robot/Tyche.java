package net.kno3.season.velocityvortex.tyche.v4.robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;
import net.kno3.robot.Robot;
import net.kno3.util.SimpleColor;

/**
 * @author Jaxon A Brown
 */
public class Tyche extends Robot {
    //SensorSystem
    public static final String IMU_KEY = "imu";
    public static final String LEFT_COLOR_KEY = "leftColor";
    public static final String RIGHT_COLOR_KEY = "rightColor";
    public static final String LEFT_ODS_KEY = "leftODS";
    public static final String RIGHT_ODS_KEY = "rightODS";

    public static final I2cAddr LEFT_COLOR_ADDRESS = I2cAddr.create8bit(0x4c);
    public static final I2cAddr RIGHT_COLOR_ADDRESS = I2cAddr.create8bit(0x3c);


    //DriveSystem
    public static final String DRIVE_FL_KEY = "drive_frontleft";
    public static final String DRIVE_RL_KEY = "drive_rearleft";
    public static final String DRIVE_FR_KEY = "drive_frontRight";
    public static final String DRIVE_RR_KEY = "drive_rearRight";


    //CollectorSystem
    public static final String COLLECTOR_KEY = "collector";
    public static final String COLLECTOR_COLOR_KEY = "collectorColor";
    public static final I2cAddr COLLECTOR_COLOR_ADDRESS = I2cAddr.create8bit(0x5c);
    public static final String COLLECTOR_COLOR2_KEY = "collectorColor2";
    public static final I2cAddr COLLECTOR_COLOR2_ADDRESS = I2cAddr.create8bit(0x6c);


    //ShooterSystem
    public static final String SHOOTER_KEY = "shooter";
    public static final String SHOOTER2_KEY = "shooter2";


    public static final String FEEDER_KEY = "feeder";
    //BeaconButtonSystem
    public static final String LEFT_PUSHER_KEY = "leftPusher";


    public static final String RIGHT_PUSHER_KEY = "rightPusher";
    //Lift
    public static final String LIFT_KEY = "lift";
    public static final String LIFT_HOOK_LEFT_KEY = "hookleft";
    public static final String LIFT_CAP_CLAMP_KEY = "capclamp";
    public static final String LIFT_KICKER_KEY = "kicker";

    public static SimpleColor ALLIANCE_COLOR;


    public Tyche(OpMode opMode, SimpleColor alliance) {
        super(opMode);
        ALLIANCE_COLOR = alliance;
        hardwareMap.servoController.get("SC1").pwmDisable();
        putSubSystem("drive", new DriveSystem(this));
        putSubSystem("collector", new CollectorSystem(this));
        putSubSystem("shooter", new ShooterSystem(this));
        putSubSystem("beacons", new BeaconButtonSystem(this));
        putSubSystem("lift", new LiftSystem(this));
        putSubSystem("sensors", new SensorSystem(this));
        hardwareMap.servoController.get("SC1").pwmEnable();
    }

    public static double extrapolateShooterPower(double voltage, double base) {
        return 0.000029865203663606 * voltage * voltage - 0.04323090099223 * voltage + 0.83962127908855 + base;
    }
}
