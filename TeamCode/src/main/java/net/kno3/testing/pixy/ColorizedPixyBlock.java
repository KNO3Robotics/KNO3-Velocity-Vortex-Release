package net.kno3.testing.pixy;

/**
 * @author Jaxon A Brown
 */
public class ColorizedPixyBlock extends GeneralColorizedPixyBlock {
    private final int numblocks;

    public ColorizedPixyBlock(int numblocks, int color, int x, int y, int width, int height, int angle) {
        super(color, x, y, width, height, angle);
        this.numblocks = numblocks;
    }

    public int getNumBlocks() {
        return numblocks;
    }
}
