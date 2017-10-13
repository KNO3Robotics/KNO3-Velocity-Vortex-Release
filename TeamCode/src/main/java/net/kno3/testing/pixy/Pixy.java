package net.kno3.testing.pixy;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import net.kno3.util.Wire;

/**
 * @author Jaxon A Brown
 */
public class Pixy {
    private static final int ADDR_GENERAL_QUERRY = 0x50;
    private static final int ADDR_SIGNATURE_QUERRY_BASE = 0x50;
    private static final int ADDR_COLOR_CODE_QUERRY = 0x58;
    private static final int ADDR_ANGLE_QUERRY = 0x60;

    private Wire wire;

    public Pixy(I2cDevice i2cDevice, I2cAddr i2cAddr) {
        this.wire = new Wire(i2cDevice, i2cAddr.get7Bit());
    }

    public PixyBlock generalQuery() {
        this.wire.beginWrite(ADDR_GENERAL_QUERRY);
        this.wire.endWrite();
        if(!this.wire.getResponse()) {
            return null;
        }
        int sigorcolor = this.wire.readLH();
        if(sigorcolor == 0) {
            return null;
        }

        int x = this.wire.read();
        int y = this.wire.read();
        int width = this.wire.read();
        int height = this.wire.read();

        if(sigorcolor > 7) {
            this.wire.beginWrite(ADDR_ANGLE_QUERRY);
            this.wire.endWrite();
            if(!this.wire.getResponse()) {
                return null;
            }
            int angle = this.wire.read();
            return new GeneralColorizedPixyBlock(sigorcolor, x, y, width, height, angle);
        } else {
            return new GeneralSignedPixyBlock(sigorcolor, x, y, width, height);
        }
    }

    public SignedPixyBlock signatureQuery(int signature) {
        if(signature <= 0 || signature > 7) {
            throw new IllegalArgumentException();
        }
        this.wire.beginWrite(ADDR_SIGNATURE_QUERRY_BASE + signature);
        this.wire.endWrite();
        if(!this.wire.getResponse()) {
            return null;
        }
        int numblocks = this.wire.read();
        if(numblocks == 0) {
            return null;
        }

        int x = this.wire.read();
        int y = this.wire.read();
        int width = this.wire.read();
        int height = this.wire.read();

        return new SignedPixyBlock(numblocks, signature, x, y, width, height);
    }

    public ColorizedPixyBlock colorCodeQuery(int colorcode) {
        this.wire.beginWrite(ADDR_COLOR_CODE_QUERRY);
        this.wire.endWrite();
        if(!this.wire.getResponse()) {
            return null;
        }
        int numblocks = this.wire.read();
        if(numblocks == 0) {
            return null;
        }

        int x = this.wire.read();
        int y = this.wire.read();
        int width = this.wire.read();
        int height = this.wire.read();
        int angle = this.wire.read();

        return new ColorizedPixyBlock(numblocks, colorcode, x, y, width, height, angle);
    }
}
