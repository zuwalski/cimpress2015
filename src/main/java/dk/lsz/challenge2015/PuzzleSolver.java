package dk.lsz.challenge2015;

import dk.lsz.challenge2015.rectangle.scanner.Rectangle;
import dk.lsz.challenge2015.rectangle.scanner.RectangleScanner;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by lars on 03/05/15.
 */
public class PuzzleSolver extends Puzzle {

    public PuzzleSolver(JSONArray puzzle) throws JSONException {
        super(puzzle);
    }

    public PuzzleSolver(short[][] puzzle) {
        super(puzzle);
    }

    @Override
    public Collection<Square> solve() {
        RectangleScanner scanner = new RectangleScanner(puzzle);

        scanLevel(scanner, 1);


        return Collections.emptyList();
    }

    private void scanLevel(RectangleScanner scanner, int level) {
        int minSquare = 2;
        final List<Rectangle> rectangles = scanner.scanAtLevel(level);
        for (Rectangle r : rectangles) {
            int square = r.minWidth();
            if (square < minSquare)
                break;
            minSquare = square;

            if (r.sy - r.y == square) {
                // slide-x
                int steps = r.sx - r.x - square;
                for (int i = 0; i <= steps; ++i) {
                    solveLevel(scanner, level + 1, r.x + i, r.y, square);
                }
            } else {
                // slide-y
                int steps = r.sy - r.y - square;
                for (int i = 0; i <= steps; ++i) {
                    solveLevel(scanner, level + 1, r.x, r.y + i, square);
                }
            }
        }
    }

    private void solveLevel(RectangleScanner scanner, int level, int x, int y, int size) {
        markSquare((short) level, x, y, size);

        scanLevel(scanner, level);
    }

    private void markSquare(short level, int x, int y, int size) {
        for (int sy = 0; sy < size; ++sy) {
            for (int sx = 0; sx < size; ++sx) {
                puzzle[sy + y][sx + x] = level;
            }
        }
    }
}
