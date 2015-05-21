package dk.lsz.challenge2015.rectangle.scanner;

import dk.lsz.challenge2015.PuzzleSolver;
import dk.lsz.challenge2015.PuzzleSolver2;
import dk.lsz.challenge2015.rectangle.scanner.sources.JsonSource;
import org.junit.Test;
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
    public void test1() throws IOException, JSONException {
        final JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/test2.json"))));

        final PuzzleSolver puzzle = new PuzzleSolver(json.getJSONArray("puzzle"));

        puzzle.print();

        puzzle.solve();
    }


    @Test
    public void testSolver2() throws IOException, JSONException {
        final JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/test1.json"))));

        final JsonSource puzzle = new JsonSource(json.getJSONArray("puzzle"));

        final PuzzleSolver2 solver2 = new PuzzleSolver2(puzzle);

        solver2.solve();
    }
}