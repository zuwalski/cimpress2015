package dk.lsz.challenge2015.rectangle.scanner.sources;

import dk.lsz.challenge2015.rectangle.scanner.Rectangle;
import dk.lsz.challenge2015.rectangle.scanner.RowTracker;

import java.util.Collection;

/**
 * Created by lars on 18/05/15.
 */
public class RectangleSource implements PuzzleSource {
    private final RowTracker tracker;
    private final Rectangle[] src;
    private int x, y, i;
    private final int width, height;

    public RectangleSource(Collection<Rectangle> recs, int height, int width) {
        this.tracker = new RowTracker(width);

        src = recs.stream().sorted((a, b) -> a.y - b.y).toArray(Rectangle[]::new);

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
        x = y = i = 0;
        tracker.beginNewScan();

        addNewOnRow();
    }

    @Override
    public boolean nextCell() {
        tracker.rectsAbove().filter(r -> x == r.x && r.sy >= y)
                .forEach(tracker::add);

        tracker.rectsToTheLeft().filter(r -> x <= r.sx)
                .forEach(tracker::add);

        boolean empty = tracker.notCovered();

        tracker.step();
        x++;

        return !empty;
    }

    @Override
    public void nextRow() {
        tracker.nextRow();
        x = 0;
        y++;
        addNewOnRow();
    }

    private void addNewOnRow() {
        for (; i < src.length; ++i) {
            Rectangle head = src[i];

            if (head.y == y) {
                tracker.addAt(head, head.x);
            } else
                break;
        }
    }
}
