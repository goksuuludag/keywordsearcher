package main.java.view.component.misc.minesweeper.model;

import javax.swing.JButton;

public record Tile(int x, int y, JButton button, TileStatus tileStatus, boolean isBomb) {
}
