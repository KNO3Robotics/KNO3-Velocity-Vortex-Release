package net.kno3.testing.pixy;

/**
 * @author Jaxon A Brown
 */
public class GeneralSignedPixyBlock extends PixyBlock {
    private final int signature;

    public GeneralSignedPixyBlock(int signature, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.signature = signature;
    }

    public int getSignature() {
        return signature;
    }
}
