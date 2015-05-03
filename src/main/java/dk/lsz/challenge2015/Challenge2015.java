package dk.lsz.challenge2015;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        final JSONResource puzzleResource = rest.json(url("puzzle"));

        final String id = puzzleResource.get("id").toString();
        final JSONArray array = (JSONArray) puzzleResource.get("puzzle");

        final Collection<Puzzle.Square> solution = new Puzzle(array).solve(p -> {
                    List<Puzzle.Square> squares = new ArrayList<>();
                    for (int y = 0; y < p.length; ++y) {
                        short[] row = p[y];
                        for (int x = 0; x < row.length; ++x) {
                            if (row[x] != 0)
                                squares.add(new Puzzle.Square(x, y, 1));
                        }
                    }
                    return squares;
                }
        );

        final List<JSONObject> collect = solution.stream().map(square -> {
            try {
                return new JSONObject().put("X", square.x).put("Y", square.y).put("Size", square.size);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

        final JSONObject response = rest.json(url("solution"), content(new JSONObject().put("id", id).put("squares", collect))).object();

        System.out.println(response);
    }

    private static String url(String endpoint) {
        return String.join("/", base, key, env, endpoint);
    }
}
