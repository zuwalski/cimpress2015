package dk.lsz.challenge2015.rectangle.scanner;

import dk.lsz.challenge2015.PuzzleSolver;
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
        final JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/test1.json"))));

        final PuzzleSolver puzzle = new PuzzleSolver(json.getJSONArray("puzzle"));

        puzzle.print();

        puzzle.solve();
    }
}
