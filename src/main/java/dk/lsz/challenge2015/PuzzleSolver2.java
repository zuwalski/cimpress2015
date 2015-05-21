package dk.lsz.challenge2015;

import dk.lsz.challenge2015.rectangle.scanner.ClusterScanner;
import dk.lsz.challenge2015.rectangle.scanner.Rectangle;
import dk.lsz.challenge2015.rectangle.scanner.RectangleScanner;
import dk.lsz.challenge2015.rectangle.scanner.sources.PuzzleSource;
import dk.lsz.challenge2015.rectangle.scanner.sources.RectangleSource;

import java.util.Collection;
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

    public Collection<Puzzle.Square> solve() {
        long start = System.currentTimeMillis();

        Level2 solution = solveCluster(puzzle, Level2.ROOT);

        System.out.printf("time %d ms\n", (System.currentTimeMillis() - start));

        for (Level2 l = solution; l != Level2.ROOT; l = l.prev) {
            System.out.println(l);
        }

        return Collections.emptyList();
    }

    private long solves = 0;

    private Level2 solveCluster(PuzzleSource source, Level2 prev) {
        solves++;
        if (solves % 10000 == 0) {
            System.out.printf("solves %d at (%d)\n", solves, prev.level);
        }

        List<Rectangle> src = rectangles.scan(source, prev);

        final Collection<List<Rectangle>> split = cluster.split(src);

        for (List<Rectangle> recs : split) {

            if (recs.size() == 1) {
                final Rectangle r = recs.get(0);

                int square = r.minWidth();
                if (r.sy - r.y == square) {
                    // slide-x
                    for (int i = 0; i <= r.sx; i += square) {
                        prev = new Level2(r.x + i, r.y, square, prev);
                    }
                } else {
                    // slide-y
                    for (int i = 0; i <= r.sy; i += square) {
                        prev = new Level2(r.x, r.y + i, square, prev);
                    }
                }
            } else if (!recs.isEmpty()) {
                Level2 best = Level2.WORST;

                final RectangleSource clsSrc = new RectangleSource(recs, puzzle.getHeight(), puzzle.getWidth());

                Collections.sort(recs);

                int minSquare = 1;
                for (Rectangle r : recs) {
                    int square = r.minWidth();
                    if (square < minSquare)
                        break;
                    minSquare = square;

                    if (r.sy - r.y == square) {
                        // slide-x
                        int steps = r.sx - r.x - square;
                        for (int i = 0; i <= steps; ++i) {
                            best = bestOf(best, solveCluster(clsSrc, new Level2(r.x + i, r.y, square, prev)));
                        }
                    } else {
                        // slide-y
                        int steps = r.sy - r.y - square;
                        for (int i = 0; i <= steps; ++i) {
                            best = bestOf(best, solveCluster(clsSrc, new Level2(r.x, r.y + i, square, prev)));
                        }
                    }
                }

                prev = best;
            }
        }

        return prev;
    }

    private static Level2 bestOf(Level2 l1, Level2 l2) {
        return l1.level < l2.level ? l1 : l2;
    }
}
