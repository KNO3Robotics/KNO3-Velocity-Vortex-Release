package net.kno3.testing.pixy;

/**
 * @author Jaxon A Brown
 */
public class GeneralColorizedPixyBlock extends PixyBlock {
    private final int color, angle;


    public GeneralColorizedPixyBlock(int color, int x, int y, int width, int height, int angle) {
        super(x, y, width, height);
        this.color = color;
        this.angle = angle;
    }

    public int getColor() {
        return this.color;
    }

    public int getAngle() {
        return angle;
    }
}
