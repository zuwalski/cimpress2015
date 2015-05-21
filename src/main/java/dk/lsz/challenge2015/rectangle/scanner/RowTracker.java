package dk.lsz.challenge2015.rectangle.scanner;

import java.util.stream.Stream;

/**
 * Created by lars on 08/05/15.
 */
public class RowTracker {
    private final int width;
    private RectangleGroupElement[] above;
    private RectangleGroupElement[] current;

    private RectangleGroupElement objPool = null;
    private int numberOfGroupElements = 0;

    private RectangleGroupElement before;
    private int x = 0;

    public RowTracker(int width) {
        this.width = width;
        this.above = new RectangleGroupElement[width];
        this.current = new RectangleGroupElement[width];
    }

    public void beginNewScan() {
        clearRow(current);
        clearRow(above);

        before = null;
        x = 0;
    }

    public void nextRow() {
        // swap
        RectangleGroupElement[] tmp = above;
        above = current;
        current = tmp;

        clearRow(current);
        before = null;
        x = 0;
    }

    public void step() {
        before = current[x++];
    }

    public Stream<Rectangle> rectsAbove() {
        return streamSet(above[x]);
    }

    public Stream<Rectangle> rectsToTheLeft() {
        return streamSet(before);
    }

    public Stream<Rectangle> rectsHere() {
        return streamSet(current[x]);
    }

    public boolean notCovered() {
        return current[x] == null;
    }

    private Stream<Rectangle> streamSet(RectangleGroupElement group) {
        final Stream.Builder<Rectangle> builder = Stream.builder();
        for (; group != null; group = group.next) {
            builder.add(group.rec);
        }
        return builder.build();
    }

    public Rectangle add(Rectangle rec) {
        return addAt(rec, x);
    }

    public Rectangle addAt(Rectangle rec, int at) {
        RectangleGroupElement res = objPool;
        if (res == null) {
            res = new RectangleGroupElement();
            numberOfGroupElements++;
        }

        objPool = res.next;
        current[at] = res.init(current[at], rec);
        return rec;
    }

    public void replaceRectangle(Rectangle oldRec, Rectangle newRec) {
        for (int i = oldRec.x; i < x; ++i) {
            for (RectangleGroupElement c = current[i]; c != null; c = c.next) {
                if (c.rec == oldRec) {
                    c.rec = newRec;
                    break;
                }
            }
        }
    }

    public int getNumberOfGroupElements() {
        return numberOfGroupElements;
    }

    private void clearRow(RectangleGroupElement[] row) {
        for (int i = 0; i < width; ++i) {
            RectangleGroupElement old = row[i];
            row[i] = null;

            while (old != null) {
                RectangleGroupElement tmp = old.next;
                old.next = objPool;
                objPool = old;
                old.rec = null;
                old = tmp;
            }
        }
    }

    private static class RectangleGroupElement {
        public RectangleGroupElement next;
        public Rectangle rec;

        public RectangleGroupElement init(RectangleGroupElement next, Rectangle rec) {
            this.next = next;
            this.rec = rec;
            return this;
        }
    }
}
