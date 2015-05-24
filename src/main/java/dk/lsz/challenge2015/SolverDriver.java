package dk.lsz.challenge2015;

import dk.lsz.challenge2015.rectangle.scanner.sources.MaskedSource;
import dk.lsz.challenge2015.rectangle.scanner.sources.PuzzleSource;

/**
 * Created by lars on 23/05/15.
 */
public class SolverDriver {
    private final PuzzleSource puzzle;
    private final int width;
    private final int height;


    public SolverDriver(PuzzleSource puzzle, int width, int height) {
        this.puzzle = puzzle;
        this.width = width;
        this.height = height;
    }

    public Square solve() {
        int targetLevel = 5;
        PuzzleSolver solver = new PuzzleSolver(puzzle, width, height, targetLevel);

        Square solution = solver.solve();

        int level = solution.level;
        while (true) {
            System.out.printf("solution at %d\n", level);
            solution = solution.union(solver.solveFrom(solution));

            if (solution.level <= level)
                break;

            level = solution.level;
        }

        return remainingSquares(solution, puzzle, width, height);
    }

    public static Square remainingSquares(Square sqrs, PuzzleSource puzzle, int width, int height) {
        PuzzleSource source = new MaskedSource(puzzle, sqrs, width);

        source.begin();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (source.nextCell()) {
                    sqrs = new Square(x, y, 0, sqrs);
                }
            }
            source.nextRow();
        }

        return sqrs;
    }
}
