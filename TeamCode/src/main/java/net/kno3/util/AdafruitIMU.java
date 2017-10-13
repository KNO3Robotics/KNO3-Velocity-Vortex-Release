package net.kno3.util;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

/**
 * @author Jaxon A Brown
 */
public class AdafruitIMU {
    private BNO055IMU imu;

    private float headingOffset, pitchOffset, rollOffset;
    private boolean isAccelIntegRunning;

    public AdafruitIMU(HardwareMap hardwareMap, String name) {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json";
        parameters.loggingEnabled      = false;
        parameters.loggingTag          = "IMU";

        imu = hardwareMap.get(BNO055IMU.class, name);
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 50);
        this.isAccelIntegRunning = true;
    }

    public float getHeading() {
        return AngleUtil.normalize(imu.getAngularOrientation().firstAngle * -1 - headingOffset);
    }

    public void zeroHeading() {
        this.headingOffset = imu.getAngularOrientation().firstAngle * -1;
    }

    public float getPitch() {
        return AngleUtil.normalize(imu.getAngularOrientation().thirdAngle * -1 - pitchOffset);
    }

    public void zeroPitch() {
        this.pitchOffset = imu.getAngularOrientation().thirdAngle * -1;
    }

    public float getRoll() {
        return AngleUtil.normalize(imu.getAngularOrientation().secondAngle * -1 - rollOffset);
    }

    public void zeroRoll() {
        this.rollOffset = imu.getAngularOrientation().secondAngle * -1;
    }

    public Position getPosition() {
        return imu.getPosition();
    }

    public void startIntegration(Position position, Velocity velocity, int pollInterval) {
        if(this.isAccelIntegRunning) {
            imu.stopAccelerationIntegration();
        }

        imu.startAccelerationIntegration(position, velocity, pollInterval);
    }

    public double getFrontBackAccel() {
        return -imu.getAcceleration().xAccel;
    }
}
