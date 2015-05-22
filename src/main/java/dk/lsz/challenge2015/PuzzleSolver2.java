package dk.lsz.challenge2015;

import dk.lsz.challenge2015.rectangle.scanner.ClusterScanner;
import dk.lsz.challenge2015.rectangle.scanner.Rectangle;
import dk.lsz.challenge2015.rectangle.scanner.RectangleScanner;
import dk.lsz.challenge2015.rectangle.scanner.sources.PuzzleSource;
import dk.lsz.challenge2015.rectangle.scanner.sources.RectangleSource;

import java.util.Collections;
import java.util.List;

/**
 * Created by lars on 03/05/15.
 */
public class PuzzleSolver2 {
    private final RectangleScanner rectangles;
    private final ClusterScanner cluster;
    private final PuzzleSource puzzle;

    public PuzzleSolver2(PuzzleSource puzzle) {
        this.puzzle = puzzle;
        rectangles = new RectangleScanner(puzzle.getWidth());
        cluster = new ClusterScanner(puzzle.getWidth(), puzzle.getHeight());
    }

    public Level solve() {
        return solveCluster(puzzle, Level.ROOT);
    }

    private Level solveCluster(PuzzleSource source, Level prev) {
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
            } else if (!recs.isEmpty() && prev.level < 9) {
                Level best = Level.WORST;

                final RectangleSource clsSrc = new RectangleSource(recs, puzzle.getHeight(), puzzle.getWidth());

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
                            best = bestOf(best, solveCluster(clsSrc, new Level(i, r.y, square, prev)));
                        }
                    } else {
                        // slide-y
                        for (int i = r.y; i + square <= r.sy; ++i) {
                            best = bestOf(best, solveCluster(clsSrc, new Level(r.x, i, square, prev)));
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
