package dk.lsz.challenge2015.rectangle.scanner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lars on 18/05/15.
 */
public class ClusterScanner {
    private final RowTracker tracker;
    private final int width;
    private final int height;

    public ClusterScanner(int width, int height) {
        this.width = width;
        this.height = height;
        this.tracker = new RowTracker(width);
    }

    public Collection<List<Rectangle>> split(List<Rectangle> recs) {
        final Rectangle[] src = recs.stream().filter(Rectangle::notSingleWidth)
                .sorted((a, b) -> a.y - b.y).toArray(Rectangle[]::new);

        if (src.length < 2) {
            return Arrays.asList(Arrays.asList(src));
        }

        int i = 0;
        tracker.beginNewScan();

        for (int y = 0; y < height; ++y) {
            for (; i < src.length; ++i) {
                Rectangle head = src[i];

                if (head.y == y) {
                    tracker.addAt(head, head.x);
                } else
                    break;
            }

            for (int x = 0; x < width; ++x) {
                updateCell(x, y);

                tracker.rectsHere().reduce((a, b) -> {
                    final Rectangle leader = a.leader();
                    b.leader().setLeader(leader);
                    return leader;
                });

                tracker.step();
            }

            tracker.nextRow();
        }

        return Arrays.stream(src).collect(Collectors.groupingBy(Rectangle::leader)).values();
    }

    private void updateCell(int x, int y) {
        tracker.rectsAbove().filter(r -> x == r.x && r.sy >= y)
                .forEach(tracker::add);

        tracker.rectsToTheLeft().filter(r -> x <= r.sx)
                .forEach(tracker::add);
    }
}
