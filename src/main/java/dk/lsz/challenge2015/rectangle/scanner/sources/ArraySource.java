package dk.lsz.challenge2015.rectangle.scanner.sources;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;

/**
 * Created by lars on 19/05/15.
 */
public class ArraySource implements PuzzleSource {
    private final short[][] puzzle;
    private int x, y;

    public ArraySource(short[][] puzzle) {
        this.puzzle = puzzle;
    }

    public ArraySource(JSONArray puzzle) throws JSONException {
        this.puzzle = translate2array(puzzle);
    }

    @Override
    public void begin() {
        x = y = 0;
    }

    @Override
    public boolean nextCell() {
        return puzzle[y][x++] > 0;
    }

    @Override
    public void nextRow() {
        x = 0;
        y++;
    }

    public static short[][] translate2array(JSONArray puzzle) throws JSONException {
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
