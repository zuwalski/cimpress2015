package dk.lsz.challenge2015;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;

import java.util.Collection;

/**
 * Created by lars on 03/05/15.
 */
public class PuzzleSolver extends Puzzle {

    public PuzzleSolver(JSONArray puzzle) throws JSONException {
        super(puzzle);
    }

    public PuzzleSolver(short[][] puzzle) {
        super(puzzle);
    }

    @Override
    public Collection<Square> solve() {
        return super.solve();
    }
}
