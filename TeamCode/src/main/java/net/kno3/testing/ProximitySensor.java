package net.kno3.testing;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import net.kno3.util.Threading;

/**
 * @author Jaxon A Brown
 */
public class ProximitySensor {
    private static final int VCNL4010_I2CADDR_DEFAULT = 0x26;
    private static final int VCNL4010_COMMAND = 0x80;
    private static final int VCNL4010_PRODUCTID = 0x81;
    private static final int VCNL4010_PROXRATE = 0x82;
    private static final int VCNL4010_IRLED = 0x83;
    private static final int VCNL4010_AMBIENTPARAMETER = 0x84;
    private static final int VCNL4010_AMBIENTDATA = 0x85;
    private static final int VCNL4010_PROXIMITYDATA = 0x87;
    private static final int VCNL4010_INTCONTROL = 0x89;
    private static final int VCNL4010_PROXINITYADJUST = 0x8A;
    private static final int VCNL4010_INTSTAT = 0x8E;
    private static final int VCNL4010_MODTIMING = 0x8F;
    public enum VCNL1410_Freq {
        VCNL4010_3M125(0x3),
        VCNL4010_1M5625(0x2),
        VCNL4010_781K25(0x1),
        VCNL4010_390K625(0x0);

        public final int val;

        VCNL1410_Freq(int val) {
            this.val = val;
        }
    }
    private static final int VCNL4010_MEASUREAMBIENT = 0x10;
    private static final int VCNL4010_MEASUREPROXIMITY = 0x08;
    private static final int VCNL4010_AMBIENTREADY = 0x40;
    private static final int VCNL4010_PROXIMITYREADY = 0x20;

    private I2cDeviceSynch i2cDevice;

    public ProximitySensor(I2cDeviceSynch i2cDevice, I2cAddr addr) {
        i2cDevice.setI2cAddress(addr);
        this.i2cDevice = i2cDevice;
        this.i2cDevice.engage();
        int rev = this.i2cDevice.read8(VCNL4010_PRODUCTID);
        //Serial.println(rev, HEX);
        if ((rev & 0xF0) != 0x20) {
            throw new RuntimeException("Wrong revision!");
        }

        setLEDCurrent(20);
        setFrequency(VCNL1410_Freq.VCNL4010_390K625);

        this.i2cDevice.write8(VCNL4010_INTCONTROL, 0x08);
    }

    public int getLEDCurrent() {
        return this.i2cDevice.read8(VCNL4010_IRLED);
    }

    public void setLEDCurrent(int current) {
        if (current > 20) current = 20;
        this.i2cDevice.write8(VCNL4010_IRLED, current);
    }

    public void setFrequency(VCNL1410_Freq frequency) {
        int r =  this.i2cDevice.read8(VCNL4010_MODTIMING);
        r &= ~(0b00011000);
        r |= frequency.val << 3;
        this.i2cDevice.write8(VCNL4010_MODTIMING, r);
    }

    public int readProximity() {
        int i = this.i2cDevice.read8(VCNL4010_INTSTAT);
        i &= ~0x80;
        this.i2cDevice.write8(VCNL4010_INTSTAT, i);

        this.i2cDevice.write8(VCNL4010_COMMAND, VCNL4010_MEASUREPROXIMITY);
        while (true) {
            //Serial.println(read8(VCNL4010_INTSTAT), HEX);
            int result = this.i2cDevice.read8(VCNL4010_COMMAND);
            //Serial.print("Ready = 0x"); Serial.println(result, HEX);
            if ((result & VCNL4010_PROXIMITYREADY) != 0) {
                return read16(VCNL4010_PROXIMITYDATA);
            }
            Threading.delay(0.001);
        }
    }

    public int readAmbient() {
        int i = this.i2cDevice.read8(VCNL4010_INTSTAT);
        i &= ~0x40;
        this.i2cDevice.write8(VCNL4010_INTSTAT, i);


        this.i2cDevice.write8(VCNL4010_COMMAND, VCNL4010_MEASUREAMBIENT);
        while (true) {
            //Serial.println(read8(VCNL4010_INTSTAT), HEX);
            int result = this.i2cDevice.read8(VCNL4010_COMMAND);
            //Serial.print("Ready = 0x"); Serial.println(result, HEX);
            if ((result & VCNL4010_AMBIENTREADY) != 0) {
                return read16(VCNL4010_AMBIENTDATA);
            }
            Threading.delay(0.001);
        }
    }

    private int read16(int addr) {
        return 256 * this.i2cDevice.read8(addr) + this.i2cDevice.read8(addr + 1);
    }
}
