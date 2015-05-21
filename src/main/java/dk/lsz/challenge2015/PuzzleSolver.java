package dk.lsz.challenge2015;

import dk.lsz.challenge2015.rectangle.scanner.Rectangle;
import dk.lsz.challenge2015.rectangle.scanner.RectangleScanner;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

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

        long start = System.currentTimeMillis();

        scanLevel(scanner);

        //scanLevelRecur(scanner, Level.ROOT);

        System.out.printf("time %d ms\n", (System.currentTimeMillis() - start));

        /*
        System.out.printf("solves %d (%d)\n", solves, scanner.getNumberOfElementsUsed());

        System.out.printf("best soloution: %d (%d)\n", max, best.level);

        for (Level sl = best; sl != Level.ROOT; sl = sl.prev) {
            System.out.println(sl);
        }

    */
        return Collections.emptyList();
    }

    private static class Level {
        public final int x, y, size, level;
        public final Level prev;

        public final static Level ROOT = new Level();

        private Level() {
            x = y = size = level = 0;
            prev = this;
        }

        public Level(int x, int y, int size, Level prev) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.prev = prev;
            this.level = prev.level + 1;
        }

        @Override
        public String toString() {
            return "Level{" +
                    "x=" + x +
                    ", y=" + y +
                    ", size=" + size +
                    ", level=" + level +
                    '}';
        }
    }

    private void scanLevel(RectangleScanner scanner) {
        final Stack<Level> stack = new Stack<>();

        Level best = Level.ROOT;
        int max = Integer.MAX_VALUE;

        long solves = 0;

        Level l = Level.ROOT;
        while (true) {
            solves++;
            /*if (solves % 10000 == 0) {
                System.out.printf("solves %d at %d (%d)\n", solves, max, best.level);
            }*/

            final List<Rectangle> rectangles = scanner.scanAtLevel(l.level + 1);

            if (rectangles.isEmpty() || rectangles.get(0).minWidth() == 0) {
                int numOfSquares = scanner.getNumberOfTiles() + l.level;
                if (numOfSquares < max) {
                    max = numOfSquares;
                    best = l;

                    //System.out.printf("solution %d (%d + %d) at %d\n", max, l.level, scanner.getNumberOfTiles(), solves);
                }
            } else {
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
                            stack.push(new Level(r.x + i, r.y, square, l));
                        }
                    } else {
                        // slide-y
                        int steps = r.sy - r.y - square;
                        for (int i = 0; i <= steps; ++i) {
                            stack.push(new Level(r.x, r.y + i, square, l));
                        }
                    }
                }
            }

            if (stack.isEmpty())
                break;
            l = stack.pop();

            markSquare(l);
        }

        System.out.printf("solves %d (%d)\n", solves, scanner.getNumberOfElementsUsed());

        System.out.printf("best soloution: %d (%d)\n", max, best.level);

        for (Level sl = best; sl != Level.ROOT; sl = sl.prev) {
            System.out.println(sl);
        }
    }

    Level best = Level.ROOT;
    int max = Integer.MAX_VALUE;

    long solves = 0;

    private void scanLevelRecur(RectangleScanner scanner, Level l) {

        markSquare(l);

        solves++;
            /*if (solves % 10000 == 0) {
                System.out.printf("solves %d at %d (%d)\n", solves, max, best.level);
            }*/

        final List<Rectangle> rectangles = scanner.scanAtLevel(l.level + 1);

        if (rectangles.isEmpty() || rectangles.get(0).minWidth() == 0) {
            int numOfSquares = scanner.getNumberOfTiles() + l.level;
            if (numOfSquares < max) {
                max = numOfSquares;
                best = l;

                //System.out.printf("solution %d (%d + %d) at %d\n", max, l.level, scanner.getNumberOfTiles(), solves);
            }
        } else {
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
                        scanLevelRecur(scanner, new Level(r.x + i, r.y, square, l));
                    }
                } else {
                    // slide-y
                    int steps = r.sy - r.y - square;
                    for (int i = 0; i <= steps; ++i) {
                        scanLevelRecur(scanner, new Level(r.x, r.y + i, square, l));
                    }
                }
            }
        }
    }

    private void markSquare(Level level) {
        for (int sy = 0; sy <= level.size; ++sy) {
            for (int sx = 0; sx <= level.size; ++sx) {
                puzzle[sy + level.y][sx + level.x] = (short) level.level;
            }
        }
    }
}
