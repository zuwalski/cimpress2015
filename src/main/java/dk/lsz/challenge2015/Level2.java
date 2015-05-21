package dk.lsz.challenge2015;

/**
 * Created by lars on 20/05/15.
 */
public class Level2 {
    public final int x, y, size, level;
    public final Level2 prev;

    public final static Level2 ROOT = new Level2(0);
    public final static Level2 WORST = new Level2(Integer.MAX_VALUE);

    private Level2(int level) {
        this.level = level;
        prev = this;
        x = y = -1;
        size = 0;
    }

    public Level2(int x, int y, int size, Level2 prev) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.prev = prev;
        this.level = prev.level + 1;
    }

    public boolean cover(int x, int y) {
        return this.y <= y && this.y + size >= y
                && this.x <= x && this.x + size >= x;
    }

    @Override
    public String toString() {
        return "Level2{" +
                "x=" + x +
                ", y=" + y +
                ", size=" + size +
                ", level=" + level +
                '}';
    }
}
