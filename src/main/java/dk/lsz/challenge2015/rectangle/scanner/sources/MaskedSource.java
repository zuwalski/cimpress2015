package dk.lsz.challenge2015.rectangle.scanner.sources;

import dk.lsz.challenge2015.Level;

import java.util.Arrays;

/**
 * Created by lars on 18/05/15.
 */
public class MaskedSource implements PuzzleSource {
    private final int[] row;
    private final Level mask;
    private final PuzzleSource src;
    private int x, y;

    public MaskedSource(PuzzleSource src, Level mask, int width) {
        this.row = new int[width];
        this.mask = mask;
        this.src = src;
    }

    @Override
    public void begin() {
        x = y = 0;

        prepareRow();
        src.begin();
    }

    @Override
    public boolean nextCell() {
        int v = row[x++];
        return src.nextCell() && (v == 0);
    }

    @Override
    public void nextRow() {
        y++;
        x = 0;
        prepareRow();
        src.nextRow();
    }

    private void prepareRow() {
        Arrays.fill(row, 0);

        for (Level l = mask; l != Level.ROOT; l = l.prev) {
            if (l.y <= y && l.y + l.size >= y) {
                for (int i = l.x; i <= l.x + l.size; ++i) {
                    row[i]++;
                }
            }
        }
    }
}
