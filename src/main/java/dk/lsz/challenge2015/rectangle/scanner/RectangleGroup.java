package dk.lsz.challenge2015.rectangle.scanner;

/**
 * Created by lars on 08/05/15.
 */
public class RectangleGroup {
    private RectangleGroupElement objPool = null;

    public RectangleGroupElement newElement(RectangleGroupElement next, Rectangle rec) {
        RectangleGroupElement res = objPool;
        if (res == null) {
            res = new RectangleGroupElement();
        }

        objPool = res.next;
        res.next = next;
        res.rec = rec;
        return res;
    }

    public void remove(RectangleGroupElement[] olds) {
        for (int i = 0; i < olds.length; ++i) {
            RectangleGroupElement old = olds[i];
            olds[i] = null;

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
