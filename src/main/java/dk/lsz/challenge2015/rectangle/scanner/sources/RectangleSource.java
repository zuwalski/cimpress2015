package dk.lsz.challenge2015.rectangle.scanner.sources;

import dk.lsz.challenge2015.rectangle.scanner.Rectangle;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by lars on 18/05/15.
 */
public class RectangleSource implements PuzzleSource {
    private final int width, height;

    private final Collection<Rectangle> recs;
    private final int[] row;
    private int x, y;

    public RectangleSource(Collection<Rectangle> recs, int height, int width) {
        this.row = new int[width];
        this.recs = recs;

        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void begin() {
        x = y = 0;

        prepareRow();
    }

    @Override
    public boolean nextCell() {
        return row[x++] != 0;
    }

    @Override
    public void nextRow() {
        y++;
        x = 0;
        prepareRow();
    }

    private void prepareRow() {
        Arrays.fill(row, 0);

        for (Rectangle r : recs) {
            if (r.y <= y && r.sy >= y) {
                for (int i = r.x; i <= r.sx; ++i) {
                    row[i]++;
                }
            }
        }
    }
}
