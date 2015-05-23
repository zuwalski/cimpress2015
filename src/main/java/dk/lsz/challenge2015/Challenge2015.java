package dk.lsz.challenge2015;

import dk.lsz.challenge2015.rectangle.scanner.sources.ArraySource;
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
    private static final int RUNTIMES = 10;

    public static void main(String[] args) throws Exception {
        final Resty rest = new Resty();

        for (int i = 0; i < RUNTIMES; ++i) {
            long start = System.currentTimeMillis();
            System.out.println("request");
            JSONResource puzzleResource = rest.json(url("puzzle"));

            String id = puzzleResource.get("id").toString();
            JSONArray array = (JSONArray) puzzleResource.get("puzzle");

            int width = Integer.parseInt(puzzleResource.get("width").toString());
            int height = Integer.parseInt(puzzleResource.get("height").toString());

            SolverDriver solver2 = new SolverDriver(new ArraySource(array), width, height);

            List<JSONObject> squares = collectSquares(solver2.solve());

            System.out.printf("done w: %d h: %d - obj: %d time: %d ms\n", width, height, squares.size(), (System.currentTimeMillis() - start));

            JSONObject response = rest.json(url("solution"), content(new JSONObject().put("id", id).put("squares", squares))).object();

            System.out.printf("send time: %d ms\n", (System.currentTimeMillis() - start));

            if (response.getJSONArray("errors").length() > 0) {
                System.out.println(array);
                break;
            }

            System.out.println(response);
        }
    }

    private static List<JSONObject> collectSquares(Square l) throws JSONException {
        List<JSONObject> res = new ArrayList<>();

        for (; l != Square.ROOT; l = l.prev) {
            res.add(new JSONObject().put("X", l.x).put("Y", l.y).put("Size", l.size + 1));
        }

        return res;
    }

    private static String url(String endpoint) {
        return String.join("/", base, key, env, endpoint);
    }
}
