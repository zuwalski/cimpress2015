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
public class PuzzleSolver {
    private static final int TARGETLEVEL = 5;

    private final RectangleScanner rectangles;
    private final ClusterScanner cluster;
    private final PuzzleSource puzzle;
    private final int width;
    private final int height;

    public PuzzleSolver(PuzzleSource puzzle, int width, int height) {
        this.puzzle = puzzle;
        this.width = width;
        this.height = height;

        rectangles = new RectangleScanner(width, height);
        cluster = new ClusterScanner(width, height);
    }

    public Square solve() {
        return solveLevel(puzzle, Square.ROOT);
    }

    public Square solveFrom(Square prev) {
        return solveLevel(new MaskedSource(puzzle, prev, width), Square.ROOT);
    }

    private Square solveLevel(PuzzleSource source, Square prev) {
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
                        prev = new Square(i, r.y, square, prev);
                    }
                } else {
                    // slide-y
                    for (int i = r.y; i < r.sy; i += (square + 1)) {
                        prev = new Square(r.x, i, square, prev);
                    }
                }
            } else if (!recs.isEmpty()) {
                Square best = Square.WORST;

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
                            best = Square.bestOf(best, solveLevel(clsSrc, new Square(i, r.y, square, prev)));
                        }
                    } else {
                        // slide-y
                        for (int i = r.y; i + square <= r.sy; ++i) {
                            best = Square.bestOf(best, solveLevel(clsSrc, new Square(r.x, i, square, prev)));
                        }
                    }
                }

                prev = best;
            }
        }

        return prev;
    }
}
