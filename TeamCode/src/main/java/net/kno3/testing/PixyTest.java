package net.kno3.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;
import net.kno3.testing.pixy.Pixy;
import net.kno3.testing.pixy.SignedPixyBlock;

/**
 * @author Jaxon A Brown
 */
public class PixyTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Pixy pixy = new Pixy(hardwareMap.i2cDevice.get("pixy"), I2cAddr.create8bit(0x00));
        double p = 0.03;

        waitForStart();

        while(opModeIsActive()) {
            SignedPixyBlock block = pixy.signatureQuery(1);
            if(block != null) {
                double error = 127 - block.getX();
                drive(0.5 - error * p, 0.5 + error * p);
            }
        }
    }

    public void drive(double left, double right) {

    }
}
