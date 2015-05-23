package dk.lsz.challenge2015;

import dk.lsz.challenge2015.rectangle.scanner.ClusterScanner;
import dk.lsz.challenge2015.rectangle.scanner.Rectangle;
import dk.lsz.challenge2015.rectangle.scanner.RectangleScanner;
import dk.lsz.challenge2015.rectangle.scanner.sources.MaskedSource;
import dk.lsz.challenge2015.rectangle.scanner.sources.PuzzleSource;
import dk.lsz.challenge2015.rectangle.scanner.sources.RectangleSource;

import java.util.Collections;
import java.util.List;

/**
 * Created by lars on 03/05/15.
 */
public class PuzzleSolver2 {
    private static final int TARGETLEVEL = 5;

    private final RectangleScanner rectangles;
    private final ClusterScanner cluster;
    private final PuzzleSource puzzle;
    private final int width;
    private final int height;

    public PuzzleSolver2(PuzzleSource puzzle, int width, int height) {
        this.puzzle = puzzle;
        this.width = width;
        this.height = height;

        rectangles = new RectangleScanner(width, height);
        cluster = new ClusterScanner(width, height);
    }

    public Level remainingSquares(Level sqrs) {
        PuzzleSource source = new MaskedSource(puzzle, sqrs, width);

        source.begin();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (source.nextCell()) {
                    sqrs = new Level(x, y, 0, sqrs);
                }
            }
            source.nextRow();
        }

        return sqrs;
    }

    public Level solve() {
        return solveLevel(puzzle, Level.ROOT);
    }

    public Level solveFrom(Level prev) {
        return solveLevel(new MaskedSource(puzzle, prev, width), Level.ROOT);
    }

    private Level solveLevel(PuzzleSource source, Level prev) {
        if (prev.level > TARGETLEVEL)
            return prev;

        List<Rectangle> src = rectangles.scan(source, prev);

        for (List<Rectangle> recs : cluster.split(src)) {

            if (recs.size() == 1) {
                final Rectangle r = recs.get(0);

                int square = r.minWidth();
                if (r.sy - r.y == square) {
                    // slide-x
                    for (int i = r.x; i < r.sx; i += (square + 1)) {
                        prev = new Level(i, r.y, square, prev);
                    }
                } else {
                    // slide-y
                    for (int i = r.y; i < r.sy; i += (square + 1)) {
                        prev = new Level(r.x, i, square, prev);
                    }
                }
            } else if (!recs.isEmpty()) {
                Level best = Level.WORST;

                final RectangleSource clsSrc = new RectangleSource(recs, width);

                Collections.sort(recs);

                int minSquare = 1;
                for (Rectangle r : recs) {
                    final int square = r.minWidth();
                    if (square < minSquare)
                        break;
                    minSquare = square;

                    if (r.sy - r.y == square) {
                        // slide-x
                        for (int i = r.x; i + square <= r.sx; ++i) {
                            best = bestOf(best, solveLevel(clsSrc, new Level(i, r.y, square, prev)));
                        }
                    } else {
                        // slide-y
                        for (int i = r.y; i + square <= r.sy; ++i) {
                            best = bestOf(best, solveLevel(clsSrc, new Level(r.x, i, square, prev)));
                        }
                    }
                }

                prev = best;
            }
        }

        return prev;
    }

    private static Level bestOf(Level l1, Level l2) {
        if (l1.level == l2.level)
            return l1.area > l2.area ? l1 : l2;

        return l1.level < l2.level ? l1 : l2;
    }
}
