package main.java.view.component.misc.minesweeper.flow;

import main.java.view.component.misc.minesweeper.model.Tile;
import main.java.view.component.misc.minesweeper.model.TileStatus;
import java.awt.*;

public class TileStatusProcessorImpl implements TileStatusProcessor {

    @Override
    public TileStatus handle(Point point, Tile[][] tiles) {
        int mineCount = countSurroundingMines(point.x, point.y, tiles);
        if (mineCount == -1) {
            return new TileStatus.Bomb();
        }
        return new TileStatus.Surrounding(mineCount);
    }


    private int countSurroundingMines(int x, int y, Tile[][] tiles) {
        if (tiles[x][y].isBomb()) {
            return -1;
        }

        int lowX = Math.max(x - 1, 0);
        int highX = Math.min(x + 2, tiles.length);

        int lowY = Math.max(y - 1, 0);
        int highY = Math.min(y + 2, tiles[0].length);

        int count = 0;
        for (int i = lowX; i < highX; i++) {
            for (int j = lowY; j < highY; j++) {
                if (i != x || j != y) {
                    if (tiles[i][j].isBomb()) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

}
