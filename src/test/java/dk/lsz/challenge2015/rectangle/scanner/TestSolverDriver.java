package dk.lsz.challenge2015.rectangle.scanner;

import dk.lsz.challenge2015.SolverDriver;
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

/**
 * Created by lars on 25/05/15.
 */
public class TestSolverDriver {

    @Test
    public void testDriver() throws InterruptedException {
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

        SolverDriver driver = new SolverDriver(new ArraySource(test), size, size);

        long start = System.currentTimeMillis();

        final Square solution = driver.solve();

        System.out.println("time: " + (System.currentTimeMillis() - start));

        TestSolver.verify(solution, test);
    }

    @Test
    public void testOnJson() throws IOException, JSONException, InterruptedException {
        final JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/test4.json"))));

        final JSONArray jsonArray = json.getJSONArray("puzzle");

        final short[][] puzzle = ArraySource.translate2array(jsonArray);

        SolverDriver driver = new SolverDriver(new ArraySource(puzzle), puzzle[0].length, puzzle.length);

        long start = System.currentTimeMillis();

        final Square solution = driver.solve();

        System.out.println("time: " + (System.currentTimeMillis() - start));

        TestSolver.verify(solution, puzzle);
    }
}
