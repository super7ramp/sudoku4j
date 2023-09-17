/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: MIT
 */

package re.belv.sudoku4j;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.util.Objects;

/**
 * A sudoku solver.
 * <p>
 * This is just a translation to Java/Sat4j of Martin Hořeňovský's example solver in C++/MiniSat.
 *
 * @see <a href="https://github.com/horenmar/sudoku-example">Original C++ example</a>
 * @see <a
 * href="https://codingnest.com/modern-sat-solvers-fast-neat-underused-part-1-of-n/">Blogpost about
 * SAT solving</a>
 */
public final class Sudoku4j {

    private static final int NUMBER_OF_ROWS = 9;
    private static final int NUMBER_OF_COLUMNS = 9;
    private static final int NUMBER_OF_VALUES = 9;
    private static final int BOX_SIDE_SIZE = 3;
    private static final int DEFAULT_TIMEOUT = 10; // seconds

    private final int[][] board;
    private final ISolver solver;

    /**
     * Constructs a solver for solving a board without predefined cells, using default timeout.
     */
    public Sudoku4j() {
        this(new int[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS], DEFAULT_TIMEOUT);
    }

    /**
     * Constructs a solver for solving the given board, using default timeout.
     *
     * @param boardArg the board, i.e. a two-dimensional 9x9 array, indexed by rows then by columns,
     *                 whose values are between 0 and 9, 0 denoting the absence of an actual value
     * @throws NullPointerException     if given board is {@code null}
     * @throws IllegalArgumentException if given board is non-{@code null} but invalid
     */
    public Sudoku4j(final int[][] boardArg) {
        this(boardArg, DEFAULT_TIMEOUT);
    }

    /**
     * Constructs a solver for solving the given board.
     *
     * @param boardArg   the board, i.e. a two-dimensional 9x9 array, indexed by rows then by
     *                   columns, whose values are between 0 and 9, 0 denoting the absence of an
     *                   actual value
     * @param timeoutArg the timeout in seconds
     * @throws NullPointerException     if given board is {@code null}
     * @throws IllegalArgumentException if given board is non-{@code null} but invalid
     */
    public Sudoku4j(final int[][] boardArg, final int timeoutArg) {
        board = validateBoard(boardArg);
        solver = SolverFactory.newDefault();
        solver.setTimeout(timeoutArg);
    }

    /**
     * Solves the board.
     *
     * @return the solved board or and empty array if no solution is found before timeout
     */
    public int[][] solve() {
        try {
            initSolver();
            addRules();
            return findSolution();
        } catch (final ContradictionException e) {
            throw new IllegalStateException("Unexpected solver error");
        } catch (final TimeoutException e) {
            return noSolution();
        }
    }

    /**
     * General solver initialization.
     */
    private void initSolver() {
        solver.newVar(NUMBER_OF_VALUES * NUMBER_OF_COLUMNS * NUMBER_OF_ROWS);
    }

    /**
     * Add rules to the solver.
     *
     * @throws ContradictionException should not happen
     */
    private void addRules() throws ContradictionException {
        noRowContainsDupe();
        noColumnContainsDupe();
        noBoxContainsDupe();
        noCellContainsDupe();
        inputBoardsConstraintsAreSatisfied();
    }

    /**
     * Rule 1: No row contains a duplicate number
     *
     * @throws ContradictionException should not happen
     */
    private void noRowContainsDupe() throws ContradictionException {
        for (int row = 0; row < NUMBER_OF_ROWS; row++) {
            for (int value = 0; value < NUMBER_OF_VALUES; value++) {
                final VecInt literals = new VecInt(NUMBER_OF_COLUMNS);
                for (int column = 0; column < NUMBER_OF_COLUMNS; column++) {
                    final int variable = toVar(row, column, value);
                    literals.push(variable);
                }
                solver.addExactly(literals, 1);
            }
        }
    }

    /**
     * Rule 2: No column contains duplicate number
     *
     * @throws ContradictionException should not happen
     */
    private void noColumnContainsDupe() throws ContradictionException {
        for (int column = 0; column < NUMBER_OF_COLUMNS; column++) {
            for (int value = 0; value < NUMBER_OF_VALUES; value++) {
                final VecInt literals = new VecInt(NUMBER_OF_ROWS);
                for (int row = 0; row < NUMBER_OF_ROWS; row++) {
                    final int variable = toVar(row, column, value);
                    literals.push(variable);
                }
                solver.addExactly(literals, 1);
            }
        }
    }

    /**
     * Rule 3: None of the 3x3 boxes contain duplicate numbers.
     *
     * @throws ContradictionException should not happen
     */
    private void noBoxContainsDupe() throws ContradictionException {
        for (int startRow = 0; startRow < NUMBER_OF_ROWS; startRow += BOX_SIDE_SIZE) {
            for (int startColumn = 0; startColumn < NUMBER_OF_COLUMNS;
                 startColumn += BOX_SIDE_SIZE) {
                for (int value = 0; value < NUMBER_OF_VALUES; value++) {
                    final VecInt literals = new VecInt(BOX_SIDE_SIZE * BOX_SIDE_SIZE);
                    for (int rowOffset = 0; rowOffset < BOX_SIDE_SIZE; rowOffset++) {
                        for (int columnOffset = 0; columnOffset < BOX_SIDE_SIZE; columnOffset++) {
                            final int variable = toVar(startRow + rowOffset,
                                                       startColumn + columnOffset,
                                                       value);
                            literals.push(variable);
                        }
                    }
                    solver.addExactly(literals, 1);
                }
            }
        }
    }

    /**
     * Rule 4: Each position contains exactly one number
     *
     * @throws ContradictionException should not happen
     */
    private void noCellContainsDupe() throws ContradictionException {
        for (int row = 0; row < NUMBER_OF_ROWS; row++) {
            for (int column = 0; column < NUMBER_OF_COLUMNS; column++) {
                final VecInt literals = new VecInt(NUMBER_OF_VALUES);
                for (int value = 0; value < NUMBER_OF_VALUES; value++) {
                    final int variable = toVar(row, column, value);
                    literals.push(variable);
                }
                solver.addExactly(literals, 1);
            }
        }
    }

    /**
     * Rule 5: Solution must satisfy input board constraints.
     *
     * @throws ContradictionException should not happen
     */
    private void inputBoardsConstraintsAreSatisfied() throws ContradictionException {
        for (int row = 0; row < NUMBER_OF_ROWS; row++) {
            for (int column = 0; column < NUMBER_OF_COLUMNS; column++) {
                final int value = board[row][column];
                if (value > 0) {
                    final int variable = toVar(row, column, value - 1);
                    final VecInt literal = new VecInt(1);
                    literal.push(variable);
                    solver.addClause(literal);
                }
            }
        }
    }

    /**
     * Runs the solver.
     *
     * @return the solution or an empty array if problem is not satisfiable
     * @throws TimeoutException if search times out
     */
    private int[][] findSolution() throws TimeoutException {
        final IProblem problem = solver;
        if (!problem.isSatisfiable()) {
            return noSolution();
        }
        final int[] solution = problem.model();
        return toBoard(solution);
    }

    /**
     * Returns a new empty 2D array, denoting the absence of solution.
     *
     * @return a new empty 2D array
     */
    private static int[][] noSolution() {
        return new int[][]{};
    }

    /**
     * Translates solution to a sudoku board.
     *
     * @param solution the solution
     * @return the solved sudoku board
     */
    private static int[][] toBoard(final int[] solution) {
        final int[][] board = new int[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
        for (int row = 0; row < NUMBER_OF_ROWS; row++) {
            for (int column = 0; column < NUMBER_OF_COLUMNS; column++) {
                for (int value = 0; value < NUMBER_OF_VALUES; value++) {
                    final int variable = toVar(row, column, value) - 1;
                    if (solution[variable] > 0) {
                        board[row][column] = value + 1;
                        break;
                    }
                }
            }
        }
        return board;
    }

    /**
     * Returns the variable corresponding to the given value at given position.
     *
     * @param row    the row
     * @param column the column
     * @param value  the value
     * @return the variable corresponding to the given value at given position
     */
    private static int toVar(final int row, final int column, final int value) {
        return row * NUMBER_OF_COLUMNS * NUMBER_OF_VALUES +
               column * NUMBER_OF_VALUES +
               value +
               1; // variables must be strictly positive
    }

    /**
     * Validates input board
     *
     * @param board the input board
     * @return the input board
     * @throws NullPointerException     if given board is {@code null}
     * @throws IllegalArgumentException if given board is non-{@code null} but invalid
     */
    private static int[][] validateBoard(final int[][] board) {
        Objects.requireNonNull(board);
        if (board.length != NUMBER_OF_ROWS) {
            throw new IllegalArgumentException(
                    "Invalid number of rows: Expected " + NUMBER_OF_ROWS + ", got " + board.length);
        }
        for (int row = 0; row < NUMBER_OF_ROWS; row++) {
            final int[] rowValues = board[row];
            if (rowValues.length != NUMBER_OF_COLUMNS) {
                throw new IllegalArgumentException(
                        "Invalid number of columns for row #" + row + ": Expected " +
                        NUMBER_OF_COLUMNS + ", got " + rowValues.length);
            }
            for (int column = 0; column < NUMBER_OF_COLUMNS; column++) {
                final int value = rowValues[column];
                if (value < 0 || value > 9) {
                    // value of 0 is accepted, it means value is not set
                    throw new IllegalArgumentException(
                            "Invalid value at column #" + column + ", row #" + row +
                            ": Expected a value between 0 and 9, got " + value);
                }
            }
        }
        return board;
    }
}
