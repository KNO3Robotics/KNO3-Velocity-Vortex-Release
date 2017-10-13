package net.kno3.season.velocityvortex.tyche.v4.robot;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import net.kno3.robot.Robot;
import net.kno3.robot.SubSystem;
import net.kno3.util.AdafruitIMU;
import net.kno3.util.Color;
import net.kno3.util.SimpleColor;
import net.kno3.util.Threading;
import org.firstinspires.ftc.robotcore.external.Consumer;

/**
 * @author Jaxon A Brown
 */
public class SensorSystem extends SubSystem {
    private AdafruitIMU imu;
    private ColorSensor leftColor, rightColor, collectorColor, collectorColor2;
    private OpticalDistanceSensor leftODS, rightODS;

    private int ballCount = 0;
    private boolean wasSeeing = false;
    private Consumer<SimpleColor> telemetryRejection;

    public SensorSystem(Robot robot) {
        super(robot);
    }

    @Override
    public void init() {
        Threading.startThread(() -> {
            this.imu = new AdafruitIMU(hardwareMap(), Tyche.IMU_KEY);
            telemetry().addData("robot ready to start", true);
            telemetry().addData("heading", () -> getIMU().getHeading());
            telemetry().update();
        });

        this.leftColor = hardwareMap().colorSensor.get(Tyche.LEFT_COLOR_KEY);
        this.leftColor.setI2cAddress(Tyche.LEFT_COLOR_ADDRESS);
        this.rightColor = hardwareMap().colorSensor.get(Tyche.RIGHT_COLOR_KEY);
        this.rightColor.setI2cAddress(Tyche.RIGHT_COLOR_ADDRESS);
        this.collectorColor = hardwareMap().colorSensor.get(Tyche.COLLECTOR_COLOR_KEY);
        this.collectorColor.setI2cAddress(Tyche.COLLECTOR_COLOR_ADDRESS);
        this.collectorColor2 = hardwareMap().colorSensor.get(Tyche.COLLECTOR_COLOR2_KEY);
        this.collectorColor2.setI2cAddress(Tyche.COLLECTOR_COLOR2_ADDRESS);

        this.leftODS = hardwareMap().opticalDistanceSensor.get(Tyche.LEFT_ODS_KEY);
        this.rightODS = hardwareMap().opticalDistanceSensor.get(Tyche.RIGHT_ODS_KEY);

        this.leftColor.enableLed(false);
        this.rightColor.enableLed(false);
        this.collectorColor.enableLed(true);
        this.collectorColor2.enableLed(true);
        this.leftODS.enableLed(true);
        this.rightODS.enableLed(true);

        telemetry().addData("leftColor", this::getLeftColor);
        telemetry().addData("rightColor", this::getRightColor);
        telemetry().addData("collectorColor", this::getCollectorColor);
        telemetry().addData("collectorColor2", this::getCollectorColor2);
        telemetry().addData("collectorSees", () -> {
            SimpleColor sc = this.collectorSees();
            if(sc != null) {
                return sc.name();
            } else {
                return "null";
            }
        });
        telemetry().addData("ballcount", () -> {
            SimpleColor sc = this.collectorSees(0);
            if(telemetryRejection != null) {
                telemetryRejection.accept(sc);
            }
            if(sc != null) {
                if(!wasSeeing) {
                    ballCount++;
                    wasSeeing = true;
                }
            } else {
                if(wasSeeing) {
                    wasSeeing = false;
                }
            }
            return ballCount;
        });
        telemetry().addData("leftODS", this::getLeftLightPercent);
        telemetry().addData("rightODS", this::getRightLightPercent);
    }

    @Override
    public void handle() {}

    @Override
    public void stop() {
        this.leftColor.enableLed(false);
        this.rightColor.enableLed(false);
        this.leftODS.enableLed(false);
        this.rightODS.enableLed(false);
    }

    public AdafruitIMU getIMU() {
        return this.imu;
    }

    public Color getLeftColor() {
        return new Color(leftColor.red(), leftColor.green(), leftColor.blue());
    }

    public Color getLeftColorStandardized() {
        return Color.getClosestColor(getLeftColor());
    }

    public Color getRightColor() {
        return new Color(rightColor.red(), rightColor.green(), rightColor.blue());
    }

    public Color getRightColorStandardized() {
        return Color.getClosestColor(getRightColor());
    }

    public boolean isLeftRed() {
        Color color = getLeftColor();
        return color.getR() > color.getB();
    }

    public boolean isRightRed() {
        Color color = getRightColor();
        return color.getR() > color.getB();
    }

    public boolean isLeftBlue() {
        Color color = getLeftColor();
        return color.getR() < color.getB();
    }

    public boolean isRightBlue() {
        Color color = getRightColor();
        return color.getR() < color.getB();
    }

    public double getLeftLightPercent() {
        return leftODS.getLightDetected();
    }

    public double getRightLightPercent() {
        return rightODS.getLightDetected();
    }

    public void turnOnLights() {
        leftColor.enableLed(true);
        rightColor.enableLed(true);
    }

    public void turnOnCollectorLights() {
        collectorColor.enableLed(true);
        collectorColor2.enableLed(true);
    }

    public void turnOffLights() {
        leftColor.enableLed(false);
        rightColor.enableLed(false);
    }

    public Color getCollectorColor() {
        return new Color(collectorColor.red(), collectorColor.green(), collectorColor.blue());
    }

    public Color getCollectorColor2() {
        return new Color(collectorColor2.red(), collectorColor2.green(), collectorColor2.blue());
    }

    public SimpleColor collectorSees() {
        return collectorSees(3);
    }

    public SimpleColor collectorSees(int diff) {
        int c1r = collectorColor.red();
        int c1b = collectorColor.blue();
        int c2r = collectorColor2.red();
        int c2b = collectorColor2.blue();
        SimpleColor c1 = SimpleColor.diffGetSimpleColor(c1r, c1b, diff);
        SimpleColor c2 = SimpleColor.diffGetSimpleColor(c2r, c2b, diff);
        if(c1 == c2) {
            return c1;
        }
        int r = c1r + c2r;
        int b = c1b + c2b;
        if(r > b) {
            return SimpleColor.RED;
        } else if(b > r) {
            return SimpleColor.BLUE;
        } else {
            return null;
        }
    }

    public int getBallCount() {
        return this.ballCount;
    }

    void decrementBall() {
        if(ballCount > 0) {
            this.ballCount--;
        }
    }

    void emptyBall() {
        this.ballCount = 0;
    }

    void telemetryRejection(Consumer<SimpleColor> consumer) {
        this.telemetryRejection = consumer;
    }
}
