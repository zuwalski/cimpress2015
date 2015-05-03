package dk.lsz.challenge2015;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;

import java.util.Collection;
import java.util.function.Function;

/**
 * Created by lars on 01/05/15.
 */
public class Puzzle {
    private final short[][] puzzle;

    public Puzzle(short[][] puzzle) {
        this.puzzle = puzzle;
    }

    public Puzzle(JSONArray puzzle) throws JSONException {
        this(translate2array(puzzle));
    }

    public static class Square {
        public final int x, y, size;

        public Square(int x, int y, int size) {
            this.x = x;
            this.y = y;
            this.size = size;
        }
    }

    public Collection<Square> solve(Function<short[][], Collection<Square>> solver) {
        return solver.apply(puzzle);
    }

    private static short[][] translate2array(JSONArray puzzle) throws JSONException {
        final short[][] translated = new short[puzzle.length()][];

        for (int y = 0; y < puzzle.length(); ++y) {
            final JSONArray row = puzzle.getJSONArray(y);
            final short[] r = translated[y] = new short[row.length()];

            for (int x = 0; x < row.length(); ++x) {
                r[x] = row.getBoolean(x) ? Short.MAX_VALUE : 0;
            }
        }

        return translated;
    }
}
