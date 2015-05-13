package dk.lsz.challenge2015.rectangle.scanner;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
}
