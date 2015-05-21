package dk.lsz.challenge2015.rectangle.scanner.sources;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;

/**
 * Created by lars on 19/05/15.
 */
public class JsonSource implements PuzzleSource {
    private final JSONArray puzzle;
    private JSONArray row;
    private int x, y;

    public JsonSource(JSONArray puzzle) throws JSONException {
        this.puzzle = puzzle;
        row = puzzle.getJSONArray(0);
    }

    @Override
    public int getWidth() {
        return row.length();
    }

    @Override
    public int getHeight() {
        return puzzle.length();
    }

    @Override
    public void begin() {
        try {
            x = y = 0;
            row = puzzle.getJSONArray(0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean nextCell() {
        try {
            return row.getBoolean(x++);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void nextRow() {
        try {
            x = 0;
            y++;
            if (y < puzzle.length())
                row = puzzle.getJSONArray(y);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
