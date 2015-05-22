package dk.lsz.challenge2015;

import dk.lsz.challenge2015.rectangle.scanner.Rectangle;
import dk.lsz.challenge2015.rectangle.scanner.RectangleScanner;
import dk.lsz.challenge2015.rectangle.scanner.sources.ArraySource;
import dk.lsz.challenge2015.rectangle.scanner.sources.PuzzleSource;
import dk.lsz.challenge2015.rectangle.scanner.sources.RectangleSource;

import java.util.Collections;
import java.util.List;

/**
 * Created by lars on 03/05/15.
 */
public class PuzzleSolver {
    private final RectangleScanner scanner;
    private final short[][] puzzle;

    private Level best;
    private int max;

    private final int width;
    private final int height;

    public PuzzleSolver(short[][] puzzle) {
        this.puzzle = puzzle;
        scanner = new RectangleScanner(puzzle);

        this.height = puzzle.length;
        this.width = puzzle[0].length;
    }

    public Level solve() {
        best = Level.ROOT;
        max = Integer.MAX_VALUE;

        scanLevel(new ArraySource(puzzle), Level.ROOT);

        return best;
    }

    private void scanLevel(PuzzleSource src, Level l) {

        //final List<Rectangle> rectangles = scanner.scanAtLevel(l.level + 1);

        List<Rectangle> rectangles = scanner.scan(src, l);

        Collections.sort(rectangles);

        if (l.level > 4 || rectangles.isEmpty() || rectangles.get(0).minWidth() == 0) {
            int free = scanner.getNumberOfTiles();

            /*
            for (Rectangle r : rectangles) {
                int square = r.minWidth();
                if (square == 0)
                    continue;
                if (r.sy - r.y == square) {
                    square++;
                    free -= square * square;
                    // slide-x
                    for (int i = 0; i < r.sx; i += square) {
                        l = new Level(r.x + i, r.y, square, l);
                    }
                } else {
                    square++;
                    free -= square * square;
                    // slide-y
                    for (int i = 0; i < r.sy; i += square) {
                        l = new Level(r.x, r.y + i, square, l);
                    }
                }
            }
            */

            int numOfSquares = free + l.level;
            if (numOfSquares <= max) {
                max = numOfSquares;
                best = l;

                System.out.printf("%d %d\n", max, l.level);
            }
        } else {
            final RectangleSource clsSrc = new RectangleSource(rectangles, height, width);

            int minSquare = 1;
            for (Rectangle r : rectangles) {
                int square = r.minWidth();
                if (square < minSquare)
                    break;
                minSquare = square;

                if (r.sy - r.y == square) {
                    // slide-x
                    int steps = r.sx - r.x - square;
                    for (int i = 0; i <= steps; ++i) {
                        scanLevel(clsSrc, new Level(r.x + i, r.y, square, l));
                    }
                } else {
                    // slide-y
                    int steps = r.sy - r.y - square;
                    for (int i = 0; i <= steps; ++i) {
                        scanLevel(clsSrc, new Level(r.x, r.y + i, square, l));
                    }
                }
            }
        }
    }
}
