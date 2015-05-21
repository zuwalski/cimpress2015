package dk.lsz.challenge2015.rectangle.scanner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lars on 17/05/15.
 */
public class Cluster {
    private short[][] puzzle;

    public Cluster(int width, int height) {
        this.puzzle = new short[height][width];
    }

    public Cluster(short[][] puzzle) {
        this.puzzle = puzzle;
    }

    public int cluster(Collection<Rectangle> recs) {


        final Rectangle[] rar = recs.toArray(new Rectangle[recs.size()]);

        for (int y = 0; y < puzzle.length; ++y) {
            Arrays.fill(puzzle[y], (short)-1);
        }

        for (int i = 0; i < rar.length; ++i) {
            Rectangle r = rar[i];
            if (r.notSingleWidth()) {
                for (int y = r.y; y <= r.sy; ++y) {
                    for (int x = r.x; x <= r.sx; ++x) {
                        int lead = puzzle[y][x];
                        if (lead < 0) {
                            puzzle[y][x] = (short) i;
                        } else {
                            r.leader().setLeader(rar[lead].leader());
                        }
                    }
                }
            }
        }


        int count = 0;
        for (int i = 0; i < rar.length; ++i) {
            Rectangle r = rar[i];

            if (r.notSingleWidth() && r.leader() == r)
                count++;
        }

        return count;
    }
}
