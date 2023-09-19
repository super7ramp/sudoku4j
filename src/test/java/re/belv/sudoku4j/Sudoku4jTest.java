/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: MIT
 */

package re.belv.sudoku4j;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link Sudoku4j}.
 */
final class Sudoku4jTest {

    private static final int[][] NO_SOLUTION = new int[][]{};

    @Test
    void emptyInput() {
        final int[][] outputBoard = new Sudoku4j().solve();

        final int[][] expected = new int[][]{
                {2, 7, 6, 1, 3, 5, 8, 4, 9},
                {8, 3, 5, 2, 4, 9, 1, 7, 6},
                {1, 4, 9, 7, 8, 6, 2, 3, 5},
                {5, 6, 3, 4, 1, 2, 7, 9, 8},
                {4, 1, 2, 9, 7, 8, 5, 6, 3},
                {7, 9, 8, 5, 6, 3, 4, 1, 2},
                {6, 5, 4, 3, 2, 1, 9, 8, 7},
                {3, 2, 1, 8, 9, 7, 6, 5, 4},
                {9, 8, 7, 6, 5, 4, 3, 2, 1}
        };
        assertArrayEquals(expected, outputBoard);
    }

    @Test
    void partiallyFilledInput_firstExample() {
        final int[][] inputBoard = new int[][]{
                {0, 7, 6, 1, 0, 5, 0, 4, 9},
                {8, 3, 5, 2, 4, 9, 1, 7, 6},
                {1, 4, 0, 7, 8, 6, 2, 0, 5},
                {5, 6, 0, 4, 1, 2, 7, 9, 8},
                {4, 1, 2, 9, 7, 8, 0, 6, 3},
                {0, 9, 0, 0, 6, 0, 4, 1, 2},
                {6, 5, 4, 3, 2, 1, 9, 8, 0},
                {3, 2, 1, 8, 9, 7, 0, 5, 4},
                {0, 8, 7, 0, 5, 4, 3, 2, 1}
        };

        final int[][] outputBoard = new Sudoku4j(inputBoard).solve();

        final int[][] expected = new int[][]{
                {2, 7, 6, 1, 3, 5, 8, 4, 9},
                {8, 3, 5, 2, 4, 9, 1, 7, 6},
                {1, 4, 9, 7, 8, 6, 2, 3, 5},
                {5, 6, 3, 4, 1, 2, 7, 9, 8},
                {4, 1, 2, 9, 7, 8, 5, 6, 3},
                {7, 9, 8, 5, 6, 3, 4, 1, 2},
                {6, 5, 4, 3, 2, 1, 9, 8, 7},
                {3, 2, 1, 8, 9, 7, 6, 5, 4},
                {9, 8, 7, 6, 5, 4, 3, 2, 1}
        };
        assertArrayEquals(expected, outputBoard);
    }

    @Test
    void partiallyFilledInput_secondExample() {
        final int[][] inputBoard = new int[][]{
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        final int[][] outputBoard = new Sudoku4j(inputBoard).solve();

        final int[][] expected = new int[][]{
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };
        assertArrayEquals(expected, outputBoard);
    }

    @Test
    void partiallyFilledInput_thirdExample() {
        final int[][] inputBoard = new int[][]{
                {0, 2, 6, 0, 0, 0, 8, 1, 0},
                {3, 0, 0, 7, 0, 8, 0, 0, 6},
                {4, 0, 0, 0, 5, 0, 0, 0, 7},
                {0, 5, 0, 1, 0, 7, 0, 9, 0},
                {0, 0, 3, 9, 0, 5, 1, 0, 0},
                {0, 4, 0, 3, 0, 2, 0, 5, 0},
                {1, 0, 0, 0, 3, 0, 0, 0, 2},
                {5, 0, 0, 2, 0, 4, 0, 0, 9},
                {0, 3, 8, 0, 0, 0, 4, 6, 0}
        };

        final int[][] outputBoard = new Sudoku4j(inputBoard).solve();

        final int[][] expected = new int[][]{
                {7, 2, 6, 4, 9, 3, 8, 1, 5},
                {3, 1, 5, 7, 2, 8, 9, 4, 6},
                {4, 8, 9, 6, 5, 1, 2, 3, 7},
                {8, 5, 2, 1, 4, 7, 6, 9, 3},
                {6, 7, 3, 9, 8, 5, 1, 2, 4},
                {9, 4, 1, 3, 6, 2, 7, 5, 8},
                {1, 9, 4, 8, 3, 6, 5, 7, 2},
                {5, 6, 7, 2, 1, 4, 3, 8, 9},
                {2, 3, 8, 5, 7, 9, 4, 6, 1}
        };
        assertArrayEquals(expected, outputBoard);
    }

    @Test
    void completelyFilledInput() {
        final int[][] inputBoard = new int[][]{
                {2, 7, 6, 1, 3, 5, 8, 4, 9},
                {8, 3, 5, 2, 4, 9, 1, 7, 6},
                {1, 4, 9, 7, 8, 6, 2, 3, 5},
                {5, 6, 3, 4, 1, 2, 7, 9, 8},
                {4, 1, 2, 9, 7, 8, 5, 6, 3},
                {7, 9, 8, 5, 6, 3, 4, 1, 2},
                {6, 5, 4, 3, 2, 1, 9, 8, 7},
                {3, 2, 1, 8, 9, 7, 6, 5, 4},
                {9, 8, 7, 6, 5, 4, 3, 2, 1}
        };
        final int[][] outputBoard = new Sudoku4j(inputBoard).solve();

        assertArrayEquals(inputBoard, outputBoard);
    }

    @Test
    void impossibleInput() {
        final int[][] inputBoard = new int[][]{
                {2, 2, 6, 1, 3, 5, 8, 4, 9}, // 2 is duplicated
                {8, 3, 5, 2, 4, 9, 1, 7, 6},
                {1, 4, 9, 7, 8, 6, 2, 3, 5},
                {5, 6, 3, 4, 1, 2, 7, 9, 8},
                {4, 1, 2, 9, 7, 8, 5, 6, 3},
                {7, 9, 8, 5, 6, 3, 4, 1, 2},
                {6, 5, 4, 3, 2, 1, 9, 8, 7},
                {3, 2, 1, 8, 9, 7, 6, 5, 4},
                {9, 8, 7, 6, 5, 4, 3, 2, 1}
        };
        final int[][] outputBoard = new Sudoku4j(inputBoard).solve();
        assertArrayEquals(NO_SOLUTION, outputBoard);
    }

    @Test
    void illegalInput_null() {
        assertThrows(NullPointerException.class, () -> new Sudoku4j(null));
    }

    @Test
    void illegalInput_missingRow() {
        final int[][] inputBoard = new int[][]{
                {2, 7, 6, 1, 3, 5, 8, 4, 9},
                {8, 3, 5, 2, 4, 9, 1, 7, 6},
                {1, 4, 9, 7, 8, 6, 2, 3, 5},
                {5, 6, 3, 4, 1, 2, 7, 9, 8},
                {4, 1, 2, 9, 7, 8, 5, 6, 3},
                {7, 9, 8, 5, 6, 3, 4, 1, 2},
                {6, 5, 4, 3, 2, 1, 9, 8, 7},
                {3, 2, 1, 8, 9, 7, 6, 5, 4},
        };
        final var exception =
                assertThrows(IllegalArgumentException.class, () -> new Sudoku4j(inputBoard));
        assertEquals("Invalid number of rows: Expected 9, got 8", exception.getMessage());
    }

    @Test
    void illegalInput_missingColumn() {
        final int[][] inputBoard = new int[][]{
                {2, 7, 6, 1, 3, 5, 8, 4},
                {8, 3, 5, 2, 4, 9, 1, 7},
                {1, 4, 9, 7, 8, 6, 2, 3},
                {5, 6, 3, 4, 1, 2, 7, 9},
                {4, 1, 2, 9, 7, 8, 5, 6},
                {7, 9, 8, 5, 6, 3, 4, 1},
                {6, 5, 4, 3, 2, 1, 9, 8},
                {3, 2, 1, 8, 9, 7, 6, 5},
                {9, 8, 7, 6, 5, 4, 3, 2}
        };
        final var exception =
                assertThrows(IllegalArgumentException.class, () -> new Sudoku4j(inputBoard));
        assertEquals("Invalid number of columns for row #0: Expected 9, got 8",
                     exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-42, -1, 10, 42})
    void illegalInput_invalidValue(final int invalidValue) {
        final int[][] inputBoard = new int[][]{
                {2, 7, 6, 1, 3, 5, 8, 4, 9},
                {8, 3, 5, 2, 4, 9, 1, 7, 6},
                {1, 4, 9, 7, 8, 6, 2, 3, 5},
                {5, 6, invalidValue, 4, 1, 2, 7, 9, 8},
                {4, 1, 2, 9, 7, 8, 5, 6, 3},
                {7, 9, 8, 5, 6, 3, 4, 1, 2},
                {6, 5, 4, 3, 2, 1, 9, 8, 7},
                {3, 2, 1, 8, 9, 7, 6, 5, 4},
                {9, 8, 7, 6, 5, 4, 3, 2, 1}
        };
        final var exception =
                assertThrows(IllegalArgumentException.class, () -> new Sudoku4j(inputBoard));
        assertEquals("Invalid value at column #2, row #3: Expected a value between 0 and 9, got " +
                     invalidValue, exception.getMessage());
    }
}
