package dk.lsz.challenge2015.rectangle.scanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by lars on 03/05/15.
 */
public class RectangleScanner {
    private final RectangleGroup group = new RectangleGroup();
    private final short[][] puzzle;

    private RectangleGroupElement[] above;
    private RectangleGroupElement[] current;
    private int numOfTiles = -1;

    public RectangleScanner(short[][] puzzle) {
        this.puzzle = puzzle;

        above = new RectangleGroupElement[puzzle[0].length];
        current = new RectangleGroupElement[above.length];
    }

    public int getNumberOfTiles() {
        return numOfTiles;
    }

    public Collection<Rectangle> scanAtLevel(final int lockLevel) {
        final List<Rectangle> recs = new ArrayList<>();

        group.remove(above);
        numOfTiles = 0;

        for (int y = 0; y < puzzle.length; ++y) {
            final short[] row = puzzle[y];
            RectangleGroupElement before = null;

            for (int x = 0; x < row.length; ++x) {
                if (row[x] > lockLevel) {
                    // open
                    current[x] = extendTo(before, above[x], x, y, recs);
                    numOfTiles++;
                } else {
                    // closed
                    stop(before, x, y, recs);
                }

                before = current[x];
            }

            // swap
            RectangleGroupElement[] tmp = above;
            above = current;
            current = tmp;
            group.remove(current);
        }

        return recs;
    }

    private RectangleGroupElement extendTo(RectangleGroupElement before, RectangleGroupElement above, int x, int y, List<Rectangle> recs) {
        RectangleGroupElement list = null;

        int min = x;
        boolean create = true;
        for (RectangleGroupElement r = before; r != null; r = r.next) {
            if (r.rec.stepX(x, y)) {
                list = group.newElement(list, r.rec);
            }

            if (r.rec.y == y) {
                create = false;
            }

            if (r.rec.x < min) {
                min = r.rec.x;
            }
        }

        if (create && above == null) {
            // add a new rectangle that "slips" under blocker above
            list = addNewRectangle(min, y, x, y, list, recs);

            // add this rec to prev cells
            for (int i = min; i < x; ++i) {
                current[i] = group.newElement(current[i], list.rec);
            }
        }

        min = y;
        create = true;
        for (RectangleGroupElement r = above; r != null; r = r.next) {
            if (r.rec.stepY(x, y)) {
                list = group.newElement(list, r.rec);
            }

            if (r.rec.x == x) {
                create = false;
            }

            if (r.rec.y < min) {
                min = r.rec.y;
            }
        }

        if (create && before == null && above != null) {
            // add a new rectangle that stands besides blocker
            list = addNewRectangle(x, min, x, y, list, recs);
        }

        return list;
    }

    private RectangleGroupElement addNewRectangle(int x, int y, int sx, int sy, RectangleGroupElement list, List<Rectangle> recs) {
        Rectangle rec = new Rectangle(x, y, sx, sy);
        recs.add(rec);

        return group.newElement(list, rec);
    }

    private void stop(RectangleGroupElement before, int x, int y, List<Rectangle> recs) {

        for (RectangleGroupElement r = before; r != null; r = r.next) {

            if (r.rec.stopX(x, y)) {
                Rectangle replace = new Rectangle(r.rec.x, r.rec.y, x - 1, y);
                recs.add(replace);

                for (int i = r.rec.x; i < x; ++i) {
                    for (RectangleGroupElement c = current[i]; c != null; c = c.next) {
                        if (c.rec == r.rec) {
                            c.rec = replace;
                            break;
                        }
                    }
                }
            }
        }
    }
}
