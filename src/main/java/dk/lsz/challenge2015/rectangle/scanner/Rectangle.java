package dk.lsz.challenge2015.rectangle.scanner;

/**
 * Created by lars on 07/05/15.
 */
public class Rectangle {
    public final int x, y;
    public int sx, sy;

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
        if (y == this.y) {
            sx = x;
        }

        return (x <= sx);
    }

    /**
     * if this is the first column
     *
     * @param x
     * @param y
     * @return
     */
    public boolean stepY(int x, int y) {
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
        if ((sy == y) && (y != this.y) && (x <= sx)) {
            sy = y - 1;
            return true;
        }

        return false;
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
