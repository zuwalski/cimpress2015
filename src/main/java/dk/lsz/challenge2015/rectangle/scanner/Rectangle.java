package dk.lsz.challenge2015.rectangle.scanner;

/**
 * Created by lars on 07/05/15.
 */
public class Rectangle implements Comparable<Rectangle> {
    public final int x, y;
    public int sx, sy;
    private boolean open = true;

    public Rectangle(int x, int y, int sx, int sy) {
        this.x = x;
        this.y = y;
        this.sx = sx;
        this.sy = sy;
    }

    /**
     * while on the first line - move sx
     * else x must remain within limit (sx)
     *
     * @param x
     * @param y
     * @return
     */
    public boolean stepX(int x, int y) {
        if (open) {
            sx = x;
        }

        return (x <= sx);
    }

    /**
     * if this is the first column
     * after this the rec is closed
     *
     * @param x
     * @param y
     * @return
     */
    public boolean stepY(int x, int y) {
        open = false;

        if (x == this.x) {
            sy = y;
            return true;
        }

        return false;
    }

    /**
     * if already at that line
     * and x not yet at sx
     *
     * @param x
     * @param y
     * @return
     */
    public boolean stopX(int x, int y) {
        if (sy == y && !open && x <= sx) {
            sy--;
            return true;
        }

        return false;
    }

    public boolean notSingleWidth() {
        return (sx != x) && (sy != y);
    }

    public boolean isSquare() {
        return (sx - x) == (sy - y);
    }

    public int minWidth() {
        return Math.min(sx - x, sy - y);
    }

    @Override
    public int compareTo(Rectangle o) {
        return o.minWidth() - minWidth();
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "x=" + x +
                ", y=" + y +
                ", sx=" + sx +
                ", sy=" + sy +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rectangle rectangle = (Rectangle) o;

        if (x != rectangle.x) return false;
        if (y != rectangle.y) return false;
        if (sx != rectangle.sx) return false;
        return sy == rectangle.sy;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + sx;
        result = 31 * result + sy;
        return result;
    }
}
