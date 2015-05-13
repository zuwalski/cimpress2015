package dk.lsz.challenge2015.rectangle.scanner;

import java.util.stream.Stream;

/**
 * Created by lars on 08/05/15.
 */
public class RowTracker {
    private RectangleGroupElement[] above;
    private RectangleGroupElement[] current;

    private RectangleGroupElement objPool = null;
    private int numberOfGroupElements = 0;

    private RectangleGroupElement before;
    private int x = 0;

    public RowTracker(int width) {
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

    public Stream<Rectangle> aboveSet() {
        return streamSet(above[x]);
    }

    public Stream<Rectangle> leftSet() {
        return streamSet(before);
    }

    public boolean notCovered() {
        return current[x] == null;
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

    private Stream<Rectangle> streamSet(RectangleGroupElement group) {
        final Stream.Builder<Rectangle> builder = Stream.builder();
        for (; group != null; group = group.next) {
            builder.add(group.rec);
        }
        return builder.build();
    }

    private void clearRow(RectangleGroupElement[] row) {
        for (int i = 0; i < row.length; ++i) {
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
}
