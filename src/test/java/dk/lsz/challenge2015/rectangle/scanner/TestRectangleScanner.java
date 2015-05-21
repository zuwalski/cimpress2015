package dk.lsz.challenge2015.rectangle.scanner;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by lars on 10/05/15.
 */
public class TestRectangleScanner {

    @Test
    public void setupBasicScanner() {
        short[][] test = new short[][]{
                {5, 5, 5, 5},
                {5, 5, 5, 5},
                {5, 5, 5, 5},
                {5, 5, 5, 5}
        };

        RectangleScanner scanner = new RectangleScanner(test);

        assertThat(scanner.getNumberOfTiles(), is(-1));

        assertThat(scanner.scanAtLevel(6).size(), is(0));

        assertThat(scanner.getNumberOfTiles(), is(0));

        assertThat(scanner.scanAtLevel(5).size(), is(1));

        assertThat(scanner.getNumberOfTiles(), is(16));
    }

    //@Test
    public void print() {
        short[][] test = new short[][]{
                {5, 5, 0, 5},
                {5, 5, 0, 0},
                {5, 5, 0, 5},
                {0, 5, 0, 5}
        };

        RectangleScanner scanner = new RectangleScanner(test);

        scanner.scanAtLevel(1).forEach(System.out::println);
    }

    @Test
    public void config0() {
        short[][] test = new short[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        RectangleScanner scanner = new RectangleScanner(test);

        assertThat(scanner.scanAtLevel(1).size(), is(0));

        assertThat(scanner.getNumberOfTiles(), is(0));
    }

    @Test
    public void config1() {
        short[][] test = new short[][]{
                {0, 5, 5, 0},
                {5, 5, 5, 5},
                {5, 5, 5, 5},
                {0, 5, 5, 0}
        };

        RectangleScanner scanner = new RectangleScanner(test);

        assertThat(scanner.scanAtLevel(1), hasItems(
                new Rectangle(1, 0, 2, 3),
                new Rectangle(0, 1, 3, 2)
        ));

        assertThat(scanner.getNumberOfTiles(), is(12));
    }

    @Test
    public void config2() {
        short[][] test = new short[][]{
                {0, 5, 5, 0},
                {5, 5, 0, 5},
                {5, 5, 5, 5},
                {0, 5, 5, 0}
        };

        RectangleScanner scanner = new RectangleScanner(test);

        assertThat(scanner.scanAtLevel(1), hasItems(
                new Rectangle(1, 0, 2, 0),
                new Rectangle(1, 2, 2, 3),
                new Rectangle(0, 1, 1, 2),
                new Rectangle(1, 0, 1, 3),
                new Rectangle(3, 1, 3, 2),
                new Rectangle(0, 2, 3, 2)
        ));

        assertThat(scanner.getNumberOfTiles(), is(11));
    }

    @Test
    public void config3() {
        short[][] test = new short[][]{
                {0, 5, 5, 5},
                {5, 5, 5, 0},
                {5, 5, 5, 5},
                {0, 5, 0, 5}
        };

        RectangleScanner scanner = new RectangleScanner(test);

        assertThat(scanner.scanAtLevel(1), hasItems(
                new Rectangle(1, 0, 3, 0),
                new Rectangle(0, 1, 2, 2),
                new Rectangle(1, 0, 2, 2),
                new Rectangle(0, 2, 3, 2),
                new Rectangle(1, 0, 1, 3),
                new Rectangle(3, 2, 3, 3)
        ));

        assertThat(scanner.getNumberOfTiles(), is(12));
    }

    @Test
    public void config4() {
        short[][] test = new short[][]{
                {5, 5, 0, 5},
                {5, 5, 0, 0},
                {5, 5, 0, 5},
                {0, 5, 0, 5}
        };

        RectangleScanner scanner = new RectangleScanner(test);

        assertThat(scanner.scanAtLevel(1), hasItems(
                new Rectangle(0, 0, 1, 2),
                new Rectangle(3, 0, 3, 0),
                new Rectangle(3, 2, 3, 3),
                new Rectangle(1, 0, 1, 3)
        ));

        assertThat(scanner.getNumberOfTiles(), is(10));
    }

    @Test
    public void regress1() {
        short[][] test = new short[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        RectangleScanner scanner = new RectangleScanner(test);

        final List<Rectangle> recs = scanner.scanAtLevel(1);

        assertThat(recs, hasItems(
                new Rectangle(0, 0, 23, 1),
                new Rectangle(0, 0, 6, 2),
                new Rectangle(8, 0, 23, 2)
        ));

        assertThat(recs.size(), is(3));
    }

    @Test
    public void cover() {
        short[][] test = new short[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        RectangleScanner scanner = new RectangleScanner(test);

        final List<Rectangle> recs = scanner.scanAtLevel(1);

        assertThat(recs.stream().filter(r -> !legal(r, test)).count(), is(0L));

        recs.forEach(r -> set(r, test));

        assertTrue(allCovered(test));

        clusterPrint(test, recs);
    }

    @Test
    public void cover2() {
        short[][] test = new short[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0},
                {1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        RectangleScanner scanner = new RectangleScanner(test);

        final List<Rectangle> recs = scanner.scanAtLevel(1);

        assertThat(recs.stream().filter(r -> !legal(r, test)).count(), is(0L));

        recs.forEach(r -> set(r, test));

        assertTrue(allCovered(test));

        clusterPrint(test, recs);
    }

    public static void clusterPrint(short[][] puzzle, List<Rectangle> recs) {
        Map<Rectangle, Integer> ids = new HashMap<>();
        AtomicInteger nextId = new AtomicInteger();

        Cluster cls = new Cluster(puzzle[0].length, puzzle.length);

        System.out.println("clusters: " + cls.cluster(recs));

        recs.stream().filter(Rectangle::notSingleWidth).forEach(r -> {
            ids.computeIfAbsent(r.leader(), some -> nextId.incrementAndGet());
        });

        for (int y = 0; y < puzzle.length; ++y) {
            short[] row = puzzle[y];
            for (int x = 0; x < row.length; ++x) {
                if (row[x] != 0) {
                    row[x] = -1;
                }
            }
        }

        for (Rectangle r : recs) {
            if (r.notSingleWidth()) {
                short cid = (short) ids.getOrDefault(r.leader(), -1).intValue();

                for (int y = r.y; y <= r.sy; ++y) {
                    for (int x = r.x; x <= r.sx; ++x) {
                        if (puzzle[y][x] != 0)
                            puzzle[y][x] = cid;
                    }
                }
            }
        }

        System.out.println();

        for (int y = 0; y < puzzle.length; ++y) {
            short[] row = puzzle[y];
            for (int x = 0; x < row.length; ++x) {
                short v = row[x];
                if (v > 0) {
                    System.out.print(row[x]);
                    System.out.print(' ');
                } else if (v < 0) {
                    System.out.print("0 ");
                } else {
                    System.out.print("_ ");
                }
            }
            System.out.println();
        }

        System.out.println();
    }

    public static boolean allCovered(short[][] puzzle) {
        boolean cover = true;
        for (int y = 0; y < puzzle.length; ++y) {
            short[] row = puzzle[y];
            for (int x = 0; x < row.length; ++x) {
                if (row[x] == 1) {
                    cover = false;
                    System.out.println("miss (" + x + "," + y + ")");
                }
            }
        }

        return cover;
    }

    public static void set(Rectangle r, short[][] puzzle) {
        for (int y = r.y; y <= r.sy; ++y) {
            for (int x = r.x; x <= r.sx; ++x) {
                if (puzzle[y][x] != 0)
                    puzzle[y][x]++;
            }
        }
    }

    public static boolean legal(Rectangle r, short[][] puzzle) {
        for (int y = r.y; y <= r.sy; ++y) {
            for (int x = r.x; x <= r.sx; ++x) {
                if (puzzle[y][x] == 0) {
                    System.out.println("not legal: " + r);
                    return false;
                }
            }
        }
        return true;
    }
}
