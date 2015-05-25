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
    private final RectangleScanner rectangles;
    private final ClusterScanner cluster;
    private final PuzzleSource puzzle;
    private final int width;

    private int targetLevel;
    private int iterations;
    private int realIterations;

    public PuzzleSolver(PuzzleSource puzzle, int width, int height, int targetLevel) {
        this.puzzle = puzzle;
        this.width = width;
        this.targetLevel = targetLevel;

        rectangles = new RectangleScanner(width, height);
        cluster = new ClusterScanner(width, height);
    }

    public Square solve() {
        return solveLevel(puzzle, Square.ROOT);
    }

    public Square solveFrom(Square prev) {
        return complexityAccelerate(solveLevel(new MaskedSource(puzzle, prev, width), Square.ROOT));
    }

    private Square solveLevel(PuzzleSource source, Square depth) {

        if (!complexityBrake(depth)) {

            List<Rectangle> src = rectangles.scan(source, depth);

            final int level = depth.level + 1;
            for (List<Rectangle> recs : cluster.split(src)) {
                if (recs.size() == 1) {
                    final Rectangle r = recs.get(0);

                    int square = r.minWidth();
                    if (r.sy - r.y == square) {
                        // slide-x
                        for (int i = r.x; i + square <= r.sx; i += (square + 1)) {
                            depth = new Square(i, r.y, square, depth, level);
                        }
                    } else {
                        // slide-y
                        for (int i = r.y; i + square <= r.sy; i += (square + 1)) {
                            depth = new Square(r.x, i, square, depth, level);
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
                                best = Square.bestOf(best, solveLevel(clsSrc, new Square(i, r.y, square, depth)));
                            }
                        } else {
                            // slide-y
                            for (int i = r.y; i + square <= r.sy; ++i) {
                                best = Square.bestOf(best, solveLevel(clsSrc, new Square(r.x, i, square, depth)));
                            }
                        }
                    }

                    depth = best;
                }
            }
        }

        return depth;
    }

    private boolean complexityBrake(Square depth) {
        if (depth == Square.ROOT)
            iterations = realIterations = 0;

        if (depth.level > targetLevel)
            return true;

        iterations++;
        if (iterations > 100) {
            targetLevel = Math.max(1, targetLevel - 1);
            realIterations += iterations;
            iterations = 0;
            //System.out.printf("brake to %d\n", targetLevel);
        }

        return false;
    }

    private Square complexityAccelerate(Square solution) {
        if (realIterations + iterations < 50) {
            targetLevel++;
            //System.out.printf("accelerate to %d\n", targetLevel);
        }

        return solution;
    }

    public int getIterations() {
        return realIterations + iterations;
    }
}
