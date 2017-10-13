package net.kno3.season.velocityvortex.tyche.v4.program.calibration;

import com.qualcomm.robotcore.hardware.HardwareMap;
import net.kno3.season.velocityvortex.tyche.v4.robot.Tyche;
import net.kno3.season.velocityvortex.tyche.v4.robot.TycheSettings;

/**
 * @author Jaxon A Brown
 */
class ServoDefaults {
    static void resetAllServos(HardwareMap hardwareMap, TycheSettings settings) {
        hardwareMap.servo.get(Tyche.LEFT_PUSHER_KEY).setPosition(settings.getBeaconLeftBase());
        hardwareMap.servo.get(Tyche.RIGHT_PUSHER_KEY).setPosition(settings.getBeaconRightBase());
        hardwareMap.servo.get(Tyche.FEEDER_KEY).setPosition(settings.getFeederLoadingPosition());
        hardwareMap.servo.get(Tyche.LIFT_HOOK_LEFT_KEY).setPosition(settings.getLiftHookClampedPosition());
        hardwareMap.servo.get(Tyche.LIFT_CAP_CLAMP_KEY).setPosition(settings.getLiftClampIdlePosition());
        hardwareMap.servo.get(Tyche.LIFT_KICKER_KEY).setPosition(settings.getLiftKickerIdlePosition());
    }
}
