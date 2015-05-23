package dk.lsz.challenge2015;

/**
 * Created by lars on 21/05/15.
 */
public class Level {
    public final int x, y, size, level;
    public final Level prev;

    public final int area;

    public final static Level ROOT = new Level(0);
    public final static Level WORST = new Level(Integer.MAX_VALUE);

    private Level(int level) {
        x = y = -1;
        size = 0;
        this.level = level;
        prev = this;
        area = 0;
    }

    public Level(int x, int y, int size, Level prev) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.prev = prev;
        this.level = prev.level + 1;
        this.area = prev.area + (size + 1) * (size + 1);
    }

    public boolean cover(int x, int y) {
        return this.y <= y && this.y + size >= y
                && this.x <= x && this.x + size >= x;
    }

    public Level union(Level l) {
        Level n = this;

        for (; l != ROOT; l = l.prev) {
            if (l == this)
                throw new IllegalArgumentException("union will create cycle");

            n = new Level(l.x, l.y, l.size, n);
        }

        return n;
    }

    @Override
    public String toString() {
        return "Level{" +
                "x=" + x +
                ", y=" + y +
                ", size=" + size +
                ", level=" + level +
                ", area=" + area +
                '}';
    }
}
