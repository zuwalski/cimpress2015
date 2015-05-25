package dk.lsz.challenge2015;

import dk.lsz.challenge2015.rectangle.scanner.ClusterScanner;
import dk.lsz.challenge2015.rectangle.scanner.Rectangle;
import dk.lsz.challenge2015.rectangle.scanner.RectangleScanner;
import dk.lsz.challenge2015.rectangle.scanner.sources.MaskedSource;
import dk.lsz.challenge2015.rectangle.scanner.sources.PuzzleSource;
import dk.lsz.challenge2015.rectangle.scanner.sources.RectangleSource;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by lars on 03/05/15.
 */
public class PuzzleSolver {
    private final RectangleScanner rectangles;
    private final ClusterScanner cluster;
    private final PuzzleSource puzzle;
    private final int width;

    private int targetLevel;
    private int iterations;
    private int realIterations;

    public PuzzleSolver(PuzzleSource puzzle, int width, int height, int targetLevel) {
        this(puzzle, width, height, targetLevel, s -> {
        });
    }

    public PuzzleSolver(PuzzleSource puzzle, int width, int height, int targetLevel, Consumer<Square> forward) {
        this.puzzle = puzzle;
        this.width = width;
        this.targetLevel = targetLevel;

        rectangles = new RectangleScanner(width, height);
        cluster = new ClusterScanner(width, height);
    }

    public Square solve() {
        realIterations = iterations = 0;
        return solveLevel(puzzle, Square.ROOT);
    }

    public Square solveFrom(Square prev) {
        realIterations = iterations = 0;
        Square s = solveLevel(new MaskedSource(puzzle, prev, width), Square.ROOT);
        realIterations += iterations;
        if (realIterations < 50) {
            targetLevel++;
        }
        return s;
    }

    public int getIterations() {
        return realIterations;
    }

    private Square solveLevel(PuzzleSource source, Square prev) {
        if (prev.level > targetLevel)
            return prev;

        iterations++;
        if (iterations > 100) {
            targetLevel = Math.max(1, targetLevel - 1);
            realIterations += iterations;
            iterations = 0;
        }

        List<Rectangle> src = rectangles.scan(source, prev);

        for (List<Rectangle> recs : cluster.split(src)) {
            if (recs.size() == 1) {
                final Rectangle r = recs.get(0);

                int square = r.minWidth();
                if (r.sy - r.y == square) {
                    // slide-x
                    for (int i = r.x; i + square <= r.sx; i += (square + 1)) {
                        prev = new Square(i, r.y, square, prev);
                    }
                } else {
                    // slide-y
                    for (int i = r.y; i + square <= r.sy; i += (square + 1)) {
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
