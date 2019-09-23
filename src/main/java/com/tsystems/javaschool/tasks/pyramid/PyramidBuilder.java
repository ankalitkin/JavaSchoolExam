package com.tsystems.javaschool.tasks.pyramid;

import java.util.Iterator;
import java.util.List;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        int length = inputNumbers.size();
        int rowsCount = getRowsCount(length);
        if (rowsCount < 0 || inputNumbers.contains(null))
            throw new CannotBuildPyramidException();

        int colsCount = getColumnsCount(rowsCount);
        int[][] res = new int[rowsCount][colsCount];
        inputNumbers.sort(Integer::compareTo);
        Iterator<Integer> iterator = inputNumbers.iterator();

        for (int i = 0; i < rowsCount; i++) {
            int rowNumber = i + 1;
            int j = getPadding(colsCount, rowNumber);
            for (int count = 0; count < rowNumber; count++) {
                res[i][j] = iterator.next();
                j += 2;
            }
        }

        return res;
    }

    private int getRowsCount(int numberOfElements) {
        double d = Math.sqrt(8 * numberOfElements + 1);
        if (d != (int) d)
            return -1;

        return ((int) d - 1) / 2;
    }

    private int getColumnsCount(int rowsCount) {
        return 2 * rowsCount - 1;
    }

    private int getPadding(int columnsCount, int rowNumber) {
        return (columnsCount - (2 * rowNumber - 1)) / 2;
    }

}
