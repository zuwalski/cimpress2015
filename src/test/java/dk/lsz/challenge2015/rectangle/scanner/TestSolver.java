package dk.lsz.challenge2015.rectangle.scanner;

import dk.lsz.challenge2015.PuzzleSolver;
import dk.lsz.challenge2015.Square;
import dk.lsz.challenge2015.rectangle.scanner.sources.ArraySource;
import org.junit.Test;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * Created by lars on 12/05/15.
 */
public class TestSolver {

    @Test
    public void testSingleSolver() throws IOException, JSONException {
        int targetLevel = 5;
        final int size = 100;

        short[][] test = new short[size][size];
        long seed = System.currentTimeMillis();
        System.out.printf("seed: %d\n", seed);

        Random rnd = new Random(seed);

        for (int i = 0; i < size; i++) {
            Arrays.fill(test[i], Short.MAX_VALUE);

            for (int t = 0; t < 5; ++t) {
                test[i][rnd.nextInt(size)] = 0;
            }
        }

        final PuzzleSolver solver = new PuzzleSolver(new ArraySource(test), size, size, targetLevel);

        long start = System.currentTimeMillis();
        final Square solution = solver.solve();

        System.out.printf("done in %d ms\n", System.currentTimeMillis() - start);

        verify(solution, test);
    }

    @Test
    public void testSolver() throws IOException, JSONException {
        final JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/test4.json"))));

        final JSONArray jsonArray = json.getJSONArray("puzzle");

        final short[][] puzzle = ArraySource.translate2array(jsonArray);
        int targetLevel = 10;
        final PuzzleSolver solver = new PuzzleSolver(new ArraySource(puzzle), puzzle[0].length, puzzle.length, targetLevel);

        solveAndValidate(ArraySource.translate2array(jsonArray), solver);
    }

    @Test
    public void largeTest() {
        int size = 100;
        short[][] test = new short[size][size];
        long seed = System.currentTimeMillis();
        System.out.printf("seed: %d\n", seed);

        Random rnd = new Random(seed);

        for (int i = 0; i < size; i++) {
            Arrays.fill(test[i], Short.MAX_VALUE);

            for (int t = 0; t < 5; ++t) {
                test[i][rnd.nextInt(size)] = 0;
            }
        }

        int targetLevel = 10;
        final PuzzleSolver solver = new PuzzleSolver(new ArraySource(test), size, size, targetLevel);

        solveAndValidate(test, solver);
    }

    private void solveAndValidate(short[][] puzzle, PuzzleSolver solver) {
        long start = System.currentTimeMillis();

        Square solution = solver.solve();

        System.out.printf("1) => Time: %d Square: %d Area: %d (%d iterations)\n", (System.currentTimeMillis() - start), solution.level, solution.area, solver.getIterations());

        Square prev = Square.WORST;
        int it = 2;
        while (true) {
            solution = solution.union(solver.solveFrom(solution));

            System.out.printf("%d) => Time: %d Square: %d Area: %d (%d iterations)\n", it++, (System.currentTimeMillis() - start), solution.level, solution.area, solver.getIterations());
            if (solution.level == prev.level && solution.area == prev.area)
                break;

            prev = solution;
        }

        assertTrue("invalid solution", verify(solution, puzzle));
    }

    public static boolean verify(Square s, short[][] puzzle) {
        System.out.printf("Before %d squares\n", countCells(puzzle));

        boolean valid = printAndMark(s, puzzle);

        System.out.printf("After %d squares\n", countCells(puzzle));

        print(puzzle);

        return valid;
    }

    private static boolean printAndMark(Square s, short[][] puzzle) {
        if (s == Square.ROOT)
            return true;

        boolean valid = printAndMark(s.prev, puzzle);

        System.out.println(s);

        return markSquare(s, puzzle) && valid;
    }

    private static int countCells(short[][] puzzle) {
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

    private static boolean markSquare(Square level, short[][] puzzle) {
        boolean valid = true;
        for (int sy = 0; sy <= level.size; ++sy) {
            for (int sx = 0; sx <= level.size; ++sx) {
                short v = puzzle[sy + level.y][sx + level.x];

                if (v == 0) {
                    System.out.printf(" > Illegal (%d, %d)\n", sx + level.x, sy + level.y);
                    valid = false;
                } else if (v != Short.MAX_VALUE) {
                    System.out.printf(" > Taken (%d, %d)\n", sx + level.x, sy + level.y);
                    valid = false;
                } else
                    puzzle[sy + level.y][sx + level.x] = (short) level.level;
            }
        }
        return valid;
    }

    private static void print(short[][] puzzle) {
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