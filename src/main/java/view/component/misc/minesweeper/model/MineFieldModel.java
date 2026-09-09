package main.java.view.component.misc.minesweeper.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MineFieldModel {

    public int size;
    /**
     * Records bomb locations.
     */
    boolean[][] mineField;
    /**
     * Records whether this location has been exposed.
     */
    boolean[][] fieldPlaceExposed;
    int numberMines;
    Random r = new Random();

    public MineFieldModel(int size, int numberMines) {
        this.size = size;
        this.numberMines = numberMines;

        mineField = new boolean[size][size];
        fieldPlaceExposed = new boolean[size][size];
        ArrayList<Point> locations = new ArrayList<>();
        for (int ii = 0; ii < this.size; ii++) {
            for (int jj = 0; jj < size; jj++) {
                mineField[ii][jj] = false;
                // must change this to false for the actual game.
                fieldPlaceExposed[ii][jj] = false;
                Point p = new Point(ii, jj);
                locations.add(p);
            }
        }
        Collections.shuffle(locations, r);
        for (int ii = 0; ii < numberMines; ii++) {
            Point p = locations.get(ii);
            mineField[p.x][p.y] = true;
        }
    }

    public boolean isBomb(int x, int y) {
        return mineField[x][y];
    }

    public boolean isExposed(int x, int y) {
        return fieldPlaceExposed[x][y];
    }

    public int getSize() {
        return size;
    }

    public int countSurroundingMines(int x, int y) {
        int lowX = x - 1;
        lowX = Math.max(lowX, 0);
        int highX = x + 2;
        highX = Math.min(highX, size);

        int lowY = y - 1;
        lowY = Math.max(lowY, 0);
        int highY = y + 2;
        highY = Math.min(highY, size);

        int count = 0;
        for (int ii = lowX; ii < highX; ii++) {
            for (int jj = lowY; jj < highY; jj++) {
                if (ii != x || jj != y) {
                    if (mineField[ii][jj]) {
                        count++;
                    }
                }
            }
        }

        return count;
    }
}
