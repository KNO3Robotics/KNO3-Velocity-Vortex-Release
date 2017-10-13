package net.kno3.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cAddr;

/**
 * @author Jaxon A Brown
 */
@TeleOp(name = "proximitytest")
public class ProximitySensorTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();

        ProximitySensor sensor = new ProximitySensor(hardwareMap.i2cDeviceSynch.get("proximity"), I2cAddr.create8bit(0x26));


        while(opModeIsActive()) {
            telemetry.addData("prox", sensor.readProximity());
            telemetry.update();
        }

    }
}
