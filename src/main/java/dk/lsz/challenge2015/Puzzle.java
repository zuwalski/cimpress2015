package dk.lsz.challenge2015;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by lars on 01/05/15.
 */
public class Puzzle {
    protected final short[][] puzzle;

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

    public Collection<Square> solve() {
        List<Square> squares = new ArrayList<>();
        for (int y = 0; y < puzzle.length; ++y) {
            short[] row = puzzle[y];
            for (int x = 0; x < row.length; ++x) {
                if (row[x] != 0)
                    squares.add(new Puzzle.Square(x, y, 1));
            }
        }
        return squares;
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

    public static void print(short[][] puzzle) {
        for (int y = 0; y < puzzle.length; ++y) {
            short[] row = puzzle[y];
            for (int x = 0; x < row.length; ++x) {
                if (row[x] == Short.MAX_VALUE)
                    System.out.print("_ ");
                else if (row[x] == 0)
                    System.out.print("  ");
                else {
                    System.out.print(row[x]);
                    System.out.print(' ');
                }
//                System.out.print(row[x] != 0 ? "1," : "0,");
            }
            System.out.println();
        }
    }
}
