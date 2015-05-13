package dk.lsz.challenge2015.rectangle.scanner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lars on 03/05/15.
 */
public class RectangleScanner {
    private final RowTracker tracker;
    private final short[][] puzzle;

    private int numOfTiles = -1;

    public RectangleScanner(short[][] puzzle) {
        this.puzzle = puzzle;
        this.tracker = new RowTracker(puzzle[0].length);
    }

    public int getNumberOfTiles() {
        return numOfTiles;
    }

    public int getNumberOfElementsUsed() {
        return tracker.getNumberOfGroupElements();
    }

    public List<Rectangle> scanAtLevel(final int lockLevel) {
        final List<Rectangle> recs = new ArrayList<>();
        numOfTiles = 0;

        tracker.beginNewScan();

        for (int y = 0; y < puzzle.length; ++y) {
            final short[] row = puzzle[y];

            for (int x = 0; x < row.length; ++x) {
                if (row[x] >= lockLevel) {
                    // open
                    numOfTiles++;

                    extendTo(x, y, recs);
                } else {
                    // closed
                    stop(x, y, recs);
                }

                tracker.step();
            }

            tracker.nextRow();
        }

        Collections.sort(recs);

        return recs;
    }

    private void extendTo(int x, int y, List<Rectangle> recs) {
        tracker.leftSet().filter(r -> r.stepX(x, y)).forEach(tracker::add);

        tracker.aboveSet().filter(r -> r.stepY(x, y)).forEach(tracker::add);

        if (tracker.notCovered()) {
            final Rectangle rec = new Rectangle(minX(x), minY(y), x, y);
            recs.add(rec);

            // add this rec to prev cells
            for (int i = rec.x; i <= x; ++i) {
                tracker.addAt(rec, i);
            }
        }
    }

    private int minY(int y) {
        return tracker.aboveSet().mapToInt(r -> r.y).min().orElse(y);
    }

    private int minX(int x) {
        return tracker.leftSet().mapToInt(r -> r.x).min().orElse(x);
    }

    private void stop(int x, int y, List<Rectangle> recs) {
        tracker.leftSet().filter(r -> r.stopX(x, y)).forEach(r -> {
            Rectangle replace = new Rectangle(r.x, r.y, x - 1, y);
            recs.add(replace);

            tracker.replaceRectangle(r, replace);
        });
    }
}
