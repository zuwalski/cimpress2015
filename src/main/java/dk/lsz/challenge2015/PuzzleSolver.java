package dk.lsz.challenge2015;

import dk.lsz.challenge2015.rectangle.scanner.Rectangle;
import dk.lsz.challenge2015.rectangle.scanner.RectangleScanner;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;

import java.util.*;

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

        scanLevel(scanner);

        return Collections.emptyList();
    }

    private static class Level {
        public int x, y, size, level;

        public Level(int x, int y, int size, int level) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.level = level;
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
        final Queue<Level> queue = new LinkedList<>();
        final List<Level> solution = new ArrayList<>();
        Collection<Level> best = Collections.emptyList();
        int max = Integer.MAX_VALUE;

        long solves = 0;

        Level l = new Level(0, 0, 0, 0);

        while (true) {
            solves++;
/*            if (solves % 2000 == 0) {
                System.out.printf("solves %d at %d (%d)\n", solves, max, best.size());
            }*/

            final List<Rectangle> rectangles = scanner.scanAtLevel(l.level + 1);

            if (rectangles.isEmpty() || rectangles.get(0).minWidth() == 0) {
                int numOfSquares = scanner.getNumberOfTiles() + l.level;
                if (numOfSquares < max) {
                    max = numOfSquares;
                    best = new ArrayList<>(solution.subList(0, l.level));

                    System.out.printf("solves %d at %d (%d)\n", solves, max, best.size());
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
                            queue.add(new Level(r.x + i, r.y, square, l.level + 1));
                        }
                    } else {
                        // slide-y
                        int steps = r.sy - r.y - square;
                        for (int i = 0; i <= steps; ++i) {
                            queue.add(new Level(r.x, r.y + i, square, l.level + 1));
                        }
                    }
                }
            }

            l = queue.poll();
            if (l == null)
                break;
            markSquare((short) l.level, l.x, l.y, l.size);

            if (solution.size() >= l.level)
                solution.set(l.level - 1, l);
            else
                solution.add(l);
        }

        System.out.printf("solves %d (%d)\n", solves, scanner.getNumberOfElementsUsed());

        System.out.printf("best soloution: %d (%d)\n", max, best.size());

        best.stream().forEach(System.out::println);
    }

    private void markSquare(short level, int x, int y, int size) {
        for (int sy = 0; sy <= size; ++sy) {
            for (int sx = 0; sx <= size; ++sx) {
                puzzle[sy + y][sx + x] = level;
            }
        }
    }
}
