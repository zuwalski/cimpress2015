package dk.lsz.challenge2015.rectangle.scanner;

import dk.lsz.challenge2015.Level;
import dk.lsz.challenge2015.Puzzle;
import dk.lsz.challenge2015.PuzzleSolver2;
import dk.lsz.challenge2015.rectangle.scanner.sources.ArraySource;
import org.junit.Test;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by lars on 12/05/15.
 */
public class TestSolver {

    @Test
    public void testSolver3() throws IOException, JSONException {
        final JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/test1.json"))));

        final JSONArray jsonArray = json.getJSONArray("puzzle");

        //final PuzzleSolver solver = new PuzzleSolver(Puzzle.translate2array(jsonArray));
        final PuzzleSolver2 solver = new PuzzleSolver2(new ArraySource(Puzzle.translate2array(jsonArray)));

        long start = System.currentTimeMillis();
        final Level solution = solver.solve();

        System.out.printf("=> Time: %d Level: %d Area: %d\n", (System.currentTimeMillis() - start), solution.level, solution.area);

        verify(solution, Puzzle.translate2array(jsonArray));
    }

    public void verify(Level s, short[][] puzzle) {
        System.out.printf("Before %d squares\n", countCells(puzzle));

        for (; s != Level.ROOT; s = s.prev) {
            System.out.println(s);

            markSquare(s, puzzle);
        }

        System.out.printf("After %d squares\n", countCells(puzzle));

        print(puzzle);
    }

    private int countCells(short[][] puzzle) {
        int i = 0;
        for (int y = 0; y < puzzle.length; ++y) {
            short[] row = puzzle[y];
            for (int x = 0; x < row.length; ++x) {
                if (row[x] == Short.MAX_VALUE)
                    i++;
            }
        }
        return i;
    }

    private void markSquare(Level level, short[][] puzzle) {
        for (int sy = 0; sy <= level.size; ++sy) {
            for (int sx = 0; sx <= level.size; ++sx) {
                short v = puzzle[sy + level.y][sx + level.x];

                if (v == 0) {
                    System.out.printf("Illegal (%d, %d)\n", level.x, level.y);
                } else if (v != Short.MAX_VALUE) {
                    System.out.printf("Taken (%d, %d)\n", level.x, level.y);
                } else
                    puzzle[sy + level.y][sx + level.x] = (short) level.level;
            }
        }
    }

    private void print(short[][] puzzle) {
        System.out.println();

        for (int y = 0; y < puzzle.length; ++y) {
            short[] row = puzzle[y];
            for (int x = 0; x < row.length; ++x) {
                short v = row[x];
                if (v == Short.MAX_VALUE) {
                    System.out.print("X ");
                } else if (v > 0) {
                    System.out.print("1 ");
                } else {
                    System.out.print("_ ");
                }
            }
            System.out.println();
        }

        System.out.println();
    }
}