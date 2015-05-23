package dk.lsz.challenge2015.rectangle.scanner;

import dk.lsz.challenge2015.Square;
import dk.lsz.challenge2015.rectangle.scanner.sources.PuzzleSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by lars on 03/05/15.
 */
public class RectangleScanner {
    private final RowTracker tracker;
    private final short[][] puzzle;
    private final int width;
    private final int height;

    private int numOfTiles = -1;

    public RectangleScanner(short[][] puzzle) {
        this.puzzle = puzzle;
        this.tracker = new RowTracker(puzzle[0].length);
        this.height = puzzle.length;
        this.width = puzzle[0].length;
    }

    public RectangleScanner(int width, int height) {
        this.width = width;
        this.height = height;
        this.tracker = new RowTracker(width);
        this.puzzle = null;
    }

    public int getNumberOfTiles() {
        return numOfTiles;
    }

    public List<Rectangle> scan(PuzzleSource src, Square l) {
        final List<Rectangle> recs = new ArrayList<>();
        numOfTiles = 0;

        tracker.beginNewScan();
        src.begin();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (src.nextCell() && !l.cover(x, y)) {
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
            src.nextRow();
        }

        return recs;
    }

    /**
     * DELETE
     * @param lockLevel
     * @return
     */
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
        tracker.rectsToTheLeft().filter(r -> r.stepX(x, y)).forEach(tracker::add);

        tracker.rectsAbove().filter(r -> r.stepY(x, y)).forEach(tracker::add);

        if (tracker.notCovered()) {
            final Optional<Rectangle> tallest = tracker.rectsAbove().reduce((a, b) -> a.y < b.y ? a : b);

            final Rectangle rec = tallest.map(
                    // copy tallest - extend back far as possible
                    r -> new Rectangle(Math.max(r.x, minX(x)), r.y, r.sx, y, false)
            ).orElse(
                    // or create one extending back
                    new Rectangle(minX(x), y, x, y));

            recs.add(rec);

            // add this rec to prev cells
            for (int i = rec.x; i <= x; ++i) {
                tracker.addAt(rec, i);
            }
        }
    }

    private int minX(int x) {
        return tracker.rectsToTheLeft().mapToInt(r -> r.x).min().orElse(x);
    }

    /**
     * TRY AS LOOP
     * @param x
     * @param y
     * @param recs
     */
    private void stop(int x, int y, List<Rectangle> recs) {
        tracker.rectsToTheLeft().filter(r -> r.stopX(x, y)).forEach(r -> {
            Rectangle replace = new Rectangle(r.x, r.y, x - 1, y, false);
            recs.add(replace);

            tracker.replaceRectangle(r, replace);
        });
    }
}
