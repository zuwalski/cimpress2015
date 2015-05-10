package dk.lsz.challenge2015.rectangle.scanner;

import org.junit.Test;

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

        assertThat(scanner.scanAtLevel(5).size(), is(0));

        assertThat(scanner.getNumberOfTiles(), is(0));

        assertThat(scanner.scanAtLevel(4).size(), is(1));

        assertThat(scanner.getNumberOfTiles(), is(16));
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

        assertThat(scanner.scanAtLevel(0).size(), is(0));

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

        assertThat(scanner.scanAtLevel(0), hasItems(
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

        assertThat(scanner.scanAtLevel(0), hasItems(
                new Rectangle(1, 0, 2, 0),
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

        assertThat(scanner.scanAtLevel(0), hasItems(
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

        assertThat(scanner.scanAtLevel(0), hasItems(
                new Rectangle(0, 0, 1, 2),
                new Rectangle(3, 0, 3, 0),
                new Rectangle(3, 2, 3, 3),
                new Rectangle(1, 0, 1, 3)
        ));

        assertThat(scanner.getNumberOfTiles(), is(10));
    }
}
