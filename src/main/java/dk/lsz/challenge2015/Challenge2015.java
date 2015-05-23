package dk.lsz.challenge2015;

import dk.lsz.challenge2015.rectangle.scanner.sources.JsonSource;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

import java.util.ArrayList;
import java.util.List;

import static us.monoid.web.Resty.content;

/**
 * Created by lars on 30/04/15.
 */
public class Challenge2015 {
    static final String key = "1f682d7418f041f79a83119cfc8faf90";

    static final String env = "trial";
    static final String base = "http://techchallenge.cimpress.com";

    public static void main(String[] args) throws Exception {
        final Resty rest = new Resty();

        JSONResource puzzleResource = rest.json(url("puzzle"));

        String id = puzzleResource.get("id").toString();
        JSONArray array = (JSONArray) puzzleResource.get("puzzle");

        int width = Integer.parseInt(puzzleResource.get("width").toString());
        int height = Integer.parseInt(puzzleResource.get("height").toString());

        PuzzleSolver2 solver2 = new PuzzleSolver2(new JsonSource(array), width, height);

        List<JSONObject> squares = collectSquares(solver2.remainingSquares(solver2.solve()));

        JSONObject response = rest.json(url("solution"), content(new JSONObject().put("id", id).put("squares", squares))).object();

        System.out.printf("w: %d h: %d - obj: %d\n", width, height, squares.size());
        System.out.println(response);
    }

    private static List<JSONObject> collectSquares(Level l) throws JSONException {
        List<JSONObject> res = new ArrayList<>();

        for (; l != Level.ROOT; l = l.prev) {
            res.add(new JSONObject().put("X", l.x).put("Y", l.y).put("Size", l.size + 1));
        }

        return res;
    }

    private static String url(String endpoint) {
        return String.join("/", base, key, env, endpoint);
    }
}
