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
    private int targetLevel = TARGETLEVEL;

    public PuzzleSolver2(PuzzleSource puzzle, int width, int height) {
        this.puzzle = puzzle;
        this.width = width;

        rectangles = new RectangleScanner(width, height);
        cluster = new ClusterScanner(width, height);
    }

    public Level solve() {
        return solveLevel(puzzle, Level.ROOT);
    }

    public Level solveFrom(Level prev) {
        targetLevel = 9;
        return solveLevel(new MaskedSource(puzzle, prev, width), Level.ROOT);
    }

    private Level solveLevel(PuzzleSource source, Level prev) {
        List<Rectangle> src = rectangles.scan(source, prev);

        for (List<Rectangle> recs : cluster.split(src)) {

            if (recs.size() == 1) {
                final Rectangle r = recs.get(0);

                int square = r.minWidth();
                if (r.sy - r.y == square) {
                    // slide-x
                    for (int i = r.x; i < r.sx; i += (square + 1)) {
                        prev = new Level(i, r.y, square, prev, 1);
                    }
                } else {
                    // slide-y
                    for (int i = r.y; i < r.sy; i += (square + 1)) {
                        prev = new Level(r.x, i, square, prev, 2);
                    }
                }
            } else if (!recs.isEmpty() && prev.level < targetLevel) {
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
                            best = bestOf(best, solveLevel(clsSrc, new Level(i, r.y, square, prev, 3)));
                        }
                    } else {
                        // slide-y
                        for (int i = r.y; i + square <= r.sy; ++i) {
                            best = bestOf(best, solveLevel(clsSrc, new Level(r.x, i, square, prev, 4)));
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
