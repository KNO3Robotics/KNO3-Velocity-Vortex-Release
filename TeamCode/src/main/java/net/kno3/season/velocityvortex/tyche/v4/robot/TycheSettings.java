package net.kno3.season.velocityvortex.tyche.v4.robot;

import com.qualcomm.robotcore.util.ReadWriteFile;
import org.firstinspires.ftc.robotcore.internal.AppUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author Jaxon A Brown
 */
public class TycheSettings {
    private JSONObject json;

    private double beaconExtent;
    private double beaconRightBase;
    private double beaconLeftBase;
    private double shooterShortBase;
    private double shooterLongBase;
    private double feederFeedingPosition;
    private double feederLoadingPosition;
    private double liftHookClampedPosition;
    private double liftHookReleasedPosition;
    private double liftClampStartingPosition;
    private double liftClampClampedPosition;
    private double liftClampIdlePosition;
    private double liftKickerIdlePosition;
    private double liftKickerKickPosition;
    private int liftTopLimit, liftLoweringLimit, shooterFeedDelay;

    public TycheSettings() {
        try {
            this.json = (JSONObject) new JSONParser().parse(
                    ReadWriteFile.readFile(
                            AppUtil.getInstance().getSettingsFile("TycheSettings.json")
                    )
            );
        } catch(Exception ex) {
            ex.printStackTrace();
            this.json = new JSONObject();
        }

        Object o = this.json.get("beaconExtent");
        if(o != null) {
            this.beaconExtent = (Double) o;
        }
        o = this.json.get("beaconRightBase");
        if(o != null) {
            this.beaconRightBase = (Double) o;
        }
        o = this.json.get("beaconLeftBase");
        if(o != null) {
            this.beaconLeftBase = (Double) o;
        }
        o = this.json.get("shooterShortBase");
        if(o != null) {
            this.shooterShortBase = (Double) o;
        }
        o = this.json.get("shooterLongBase");
        if(o != null) {
            this.shooterLongBase = (Double) o;
        }
        o = this.json.get("feederFeedingPosition");
        if(o != null) {
            this.feederFeedingPosition = (Double) o;
        }
        o = this.json.get("feederLoadingPosition");
        if(o != null) {
            this.feederLoadingPosition = (Double) o;
        }
        o = this.json.get("liftHookClampedPosition");
        if(o != null) {
            this.liftHookClampedPosition = (Double) o;
        }
        o = this.json.get("liftHookReleasedPosition");
        if(o != null) {
            this.liftHookReleasedPosition = (Double) o;
        }
        o = this.json.get("liftClampStartingPosition");
        if(o != null) {
            this.liftClampStartingPosition = (Double) o;
        }
        o = this.json.get("liftClampClampedPosition");
        if(o != null) {
            this.liftClampClampedPosition = (Double) o;
        }
        o = this.json.get("liftClampIdlePosition");
        if(o != null) {
            this.liftClampIdlePosition = (Double) o;
        }
        o = this.json.get("liftKickerIdlePosition");
        if(o != null) {
            this.liftKickerIdlePosition = (Double) o;
        }
        o = this.json.get("liftKickerKickPosition");
        if(o != null) {
            this.liftKickerKickPosition = (Double) o;
        }
        o = this.json.get("liftTopLimit");
        if(o != null) {
            this.liftTopLimit = ((Long) o).intValue();
        }
        o = this.json.get("liftLoweringLimit");
        if(o != null) {
            this.liftLoweringLimit = ((Long) o).intValue();
        }
        o = this.json.get("shooterFeedDelay");
        if(o != null) {
            this.shooterFeedDelay = ((Long) o).intValue();
        }
    }

    public void save() {
        ReadWriteFile.writeFile(AppUtil.getInstance().getSettingsFile("TycheSettings.json"), json.toJSONString());
    }

    public double getBeaconExtent() {
        return beaconExtent;
    }

    public void setBeaconExtent(double beaconExtent) {
        this.beaconExtent = beaconExtent;
        this.json.put("beaconExtent", beaconExtent);
    }

    public double getBeaconRightBase() {
        return beaconRightBase;
    }

    public void setBeaconRightBase(double beaconRightBase) {
        this.beaconRightBase = beaconRightBase;
        this.json.put("beaconRightBase", beaconRightBase);
    }

    public double getBeaconLeftBase() {
        return beaconLeftBase;
    }

    public void setBeaconLeftBase(double beaconLeftBase) {
        this.beaconLeftBase = beaconLeftBase;
        this.json.put("beaconLeftBase", beaconLeftBase);
    }

    public double getShooterShortBase() {
        return shooterShortBase;
    }

    public void setShooterShortBase(double shooterShortBase) {
        this.shooterShortBase = shooterShortBase;
        this.json.put("shooterShortBase", shooterShortBase);
    }

    public double getShooterLongBase() {
        return shooterLongBase;
    }

    public void setShooterLongBase(double shooterLongBase) {
        this.shooterLongBase = shooterLongBase;
        this.json.put("shooterLongBase", shooterLongBase);
    }

    public double getFeederFeedingPosition() {
        return feederFeedingPosition;
    }

    public void setFeederFeedingPosition(double feederFeedingPosition) {
        this.feederFeedingPosition = feederFeedingPosition;
        this.json.put("feederFeedingPosition", feederFeedingPosition);
    }

    public double getFeederLoadingPosition() {
        return feederLoadingPosition;
    }

    public void setFeederLoadingPosition(double feederLoadingPosition) {
        this.feederLoadingPosition = feederLoadingPosition;
        this.json.put("feederLoadingPosition", feederLoadingPosition);
    }

    public double getLiftHookClampedPosition() {
        return liftHookClampedPosition;
    }

    public void setLiftHookClampedPosition(double liftHookClampedPosition) {
        this.liftHookClampedPosition = liftHookClampedPosition;
        this.json.put("liftHookClampedPosition", liftHookClampedPosition);
    }

    public double getLiftHookReleasedPosition() {
        return liftHookReleasedPosition;
    }

    public void setLiftHookReleasedPosition(double liftHookReleasedPosition) {
        this.liftHookReleasedPosition = liftHookReleasedPosition;
        this.json.put("liftHookReleasedPosition", liftHookReleasedPosition);
    }

    public double getLiftClampStartingPosition() {
        return liftClampStartingPosition;
    }

    public void setLiftClampStartingPosition(double liftClampStartingPosition) {
        this.liftClampStartingPosition = liftClampStartingPosition;
        this.json.put("liftClampStartingPosition", liftClampStartingPosition);
    }

    public double getLiftClampClampedPosition() {
        return liftClampClampedPosition;
    }

    public void setLiftClampClampedPosition(double liftClampClampedPosition) {
        this.liftClampClampedPosition = liftClampClampedPosition;
        this.json.put("liftClampClampedPosition", liftClampClampedPosition);
    }

    public double getLiftClampIdlePosition() {
        return liftClampIdlePosition;
    }

    public void setLiftClampIdlePosition(double liftClampIdlePosition) {
        this.liftClampIdlePosition = liftClampIdlePosition;
        this.json.put("liftClampIdlePosition", liftClampIdlePosition);
    }

    public double getLiftKickerIdlePosition() {
        return liftKickerIdlePosition;
    }

    public void setLiftKickerIdlePosition(double liftKickerIdlePosition) {
        this.liftKickerIdlePosition = liftKickerIdlePosition;
        this.json.put("liftKickerIdlePosition", liftKickerIdlePosition);
    }

    public double getLiftKickerKickPosition() {
        return liftKickerKickPosition;
    }

    public void setLiftKickerKickPosition(double liftKickerKickPosition) {
        this.liftKickerKickPosition = liftKickerKickPosition;
        this.json.put("liftKickerKickPosition", liftKickerKickPosition);
    }

    public int getLiftTopLimit() {
        return liftTopLimit;
    }

    public void setLiftTopLimit(int liftTopLimit) {
        this.liftTopLimit = liftTopLimit;
        this.json.put("liftTopLimit", liftTopLimit);
    }

    public int getLiftLoweringLimit() {
        return liftLoweringLimit;
    }

    public void setLiftLoweringLimit(int liftLoweringLimit) {
        this.liftLoweringLimit = liftLoweringLimit;
        this.json.put("liftLoweringLimit", liftLoweringLimit);
    }

    public int getShooterFeedDelay() {
        return shooterFeedDelay;
    }

    public void setShooterFeedDelay(int shooterFeedDelay) {
        this.shooterFeedDelay = shooterFeedDelay;
        this.json.put("shooterFeedDelay", shooterFeedDelay);
    }
}
