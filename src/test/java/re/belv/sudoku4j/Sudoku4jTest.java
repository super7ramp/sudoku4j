/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: MIT
 */

package re.belv.sudoku4j;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
    void partiallyFilledInput() {
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
        System.out.println(Arrays.deepToString(outputBoard));
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
}
