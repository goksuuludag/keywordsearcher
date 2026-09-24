package main.java.view.component.misc.minesweeper.flow;

import main.java.view.component.misc.minesweeper.model.Tile;
import main.java.view.component.misc.minesweeper.model.TileStatus;

import java.awt.Point;

public interface TileStatusProcessor {
    default TileStatus processLeftClick(Point point, TileStatus tileStatus, Tile[][] tiles) {
        return switch (tileStatus) {
            case TileStatus.Unknown _, TileStatus.Flagged _ -> this.processLeftClick(point, this.handle(point, tiles), tiles);
            case TileStatus.Bomb bomb -> bomb;
            case TileStatus.Surrounding surrounding -> surrounding;
            case TileStatus.WinningTile winningTile -> winningTile;
        };
    }
    default TileStatus processRightClick(TileStatus tileStatus) {
        return switch (tileStatus) {
            case TileStatus.Unknown _ -> new TileStatus.Flagged();
            case TileStatus.Flagged _ -> new TileStatus.Unknown();
            case TileStatus.Bomb bomb -> bomb;
            case TileStatus.Surrounding surrounding -> surrounding;
            case TileStatus.WinningTile winningTile -> winningTile;
        };
    }
    TileStatus handle(Point point, Tile[][] tiles);
}
