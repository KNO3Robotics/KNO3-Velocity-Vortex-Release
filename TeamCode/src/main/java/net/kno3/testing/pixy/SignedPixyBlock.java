package net.kno3.testing.pixy;

/**
 * @author Jaxon A Brown
 */
public class SignedPixyBlock extends GeneralSignedPixyBlock {
    private final int numblocks;

    public SignedPixyBlock(int numblocks, int signature, int x, int y, int width, int height) {
        super(signature, x, y, width, height);
        this.numblocks = numblocks;
    }

    public int getNumBlocks() {
        return numblocks;
    }
}
