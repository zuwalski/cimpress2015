package dk.lsz.challenge2015.rectangle.scanner.sources;

/**
 * Created by lars on 19/05/15.
 */
public interface PuzzleSource {
    void begin();

    boolean nextCell();

    void nextRow();
}
