package net.kno3.testing.pixy;

/**
 * @author Jaxon A Brown
 */
public abstract class PixyBlock {
    private final int x, y, width, height;

    public PixyBlock(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
