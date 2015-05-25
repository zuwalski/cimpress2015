package dk.lsz.challenge2015;

/**
 * Created by lars on 21/05/15.
 */
public class Square {
    public final int x, y, size, level;
    public final Square prev;

    public final int area;

    public final static Square ROOT = new Square(0);
    public final static Square WORST = new Square(Integer.MAX_VALUE);

    private Square(int level) {
        x = y = -1;
        size = 0;
        this.level = level;
        prev = this;
        area = 0;
    }

    public Square(int x, int y, int size, Square prev) {
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

    public Square union(Square l) {
        Square n = this;

        for (; l != ROOT; l = l.prev) {
            if (l == this)
                throw new IllegalArgumentException("union will create cycle");

            n = new Square(l.x, l.y, l.size, n);
        }

        return n;
    }

    public static Square bestOf(Square l1, Square l2) {
        return l1.area > l2.area ? l1 : l2;
/*        if (l1.level == l2.level)
            return l1.area > l2.area ? l1 : l2;

        return l1.level < l2.level ? l1 : l2;
        */
    }

    @Override
    public String toString() {
        return "Square{" +
                "x=" + x +
                ", y=" + y +
                ", size=" + size +
                ", level=" + level +
                ", area=" + area +
                '}';
    }
}
