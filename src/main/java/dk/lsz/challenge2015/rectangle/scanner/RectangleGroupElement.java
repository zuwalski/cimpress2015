package dk.lsz.challenge2015.rectangle.scanner;

/**
 * Created by lars on 08/05/15.
 */
public class RectangleGroupElement {
    public RectangleGroupElement next;
    public Rectangle rec;

    public RectangleGroupElement init(RectangleGroupElement next, Rectangle rec) {
        this.next = next;
        this.rec = rec;
        return this;
    }
}
