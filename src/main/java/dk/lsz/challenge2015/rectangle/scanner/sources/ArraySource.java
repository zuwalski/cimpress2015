package dk.lsz.challenge2015.rectangle.scanner.sources;

/**
 * Created by lars on 19/05/15.
 */
public class ArraySource implements PuzzleSource {
    private final short[][] puzzle;
    private int x, y;

    public ArraySource(short[][] puzzle) {
        this.puzzle = puzzle;
    }

    @Override
    public void begin() {
        x = y = 0;
    }

    @Override
    public boolean nextCell() {
        return puzzle[y][x++] > 0;
    }

    @Override
    public void nextRow() {
        x = 0;
        y++;
    }
}
