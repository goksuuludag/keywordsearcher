package main.java.view.component.misc.minesweeper.model;


public sealed interface TileStatus {
    record Unknown() implements TileStatus {}
    record Flagged() implements TileStatus {}
    record Bomb() implements TileStatus {}
    record Surrounding(int numberOfSurroundingMines) implements TileStatus {}
}
