package main.java.view.component.misc.minesweeper.main;

import main.java.view.component.misc.minesweeper.flow.TileStatusProcessor;
import main.java.view.component.misc.minesweeper.flow.TileStatusProcessorImpl;
import main.java.view.component.misc.minesweeper.model.Tile;
import main.java.view.component.misc.minesweeper.model.TileStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class MineField {
    private final TileStatusProcessor processor;
    private final Tile[][] tiles;
    private final Color[] colors = {
            new Color(140, 150, 250),
            Color.CYAN.darker(),
            Color.GREEN.darker(),
            Color.YELLOW.darker(),
            Color.ORANGE.darker(),
            Color.PINK.darker(),
            Color.MAGENTA,
            Color.RED
    };
    private final String BOMB = "*";
    private final String FLAG = Character.toString(0x1F6A9);

    public MineField(int width, int height, int numberOfMines, Runnable setDeadRunnable, Runnable setSwagRunnable) {
        tiles = new Tile[width][height];
        boolean[][] isBomb = initAndGetBombs(width, height, numberOfMines);
        initTiles(tiles, isBomb, setDeadRunnable, setSwagRunnable);
        processor = new TileStatusProcessorImpl();
    }

    private boolean[][] initAndGetBombs(int width, int height, int numberOfMines) {
        boolean[][] isBomb = new boolean[width][height];
        int numberOfMinesTemp = numberOfMines;
        for (int i = 0; i < isBomb.length; i++) {
            for (int j = 0; j < isBomb[i].length; j++) {
                isBomb[i][j] = numberOfMinesTemp > 0;
                numberOfMinesTemp--;
            }
        }
        shuffle(isBomb);
        return isBomb;
    }

    private void initTiles(Tile[][] tiles, boolean[][] isBomb, Runnable setDeadRunnable, Runnable setSwagRunnable) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                JButton button = new JButton();
                button.setFocusable(false);
                final Color pressedButtonBg = button.getBackground().darker();
                final int x = i;
                final int y = j;
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (!button.isEnabled()) {
                            return;
                        }
                        if (SwingUtilities.isRightMouseButton(e)) {
                            processStatus(button, processRightClick(x, y), setDeadRunnable, setSwagRunnable);
                        } else {
                            processStatus(button, processLeftClick(x, y), setDeadRunnable, setSwagRunnable);
                            button.setBackground(pressedButtonBg);
                        }
                    }
                });
                tiles[i][j] = new Tile(i, j, button, new TileStatus.Unknown(), isBomb[i][j]);
            }
        }
    }

    private void processStatus(JButton button, TileStatus tileStatus, Runnable setDeadRunnable, Runnable setSwagRunnable) {
        switch (tileStatus) {
            case TileStatus.Unknown _ -> button.setText("");
            case TileStatus.Flagged _ -> button.setText(FLAG);
            case TileStatus.Bomb _ -> {
                button.setText(BOMB);
                setTilesEnabled(false);
                setDeadRunnable.run();
            }
            case TileStatus.Surrounding(int count) -> {
                if (count > 0) {
                    button.setForeground(colors[count - 1]);
                    button.setText("" + count);
                } else {
                    button.setText("");
                }
            }
            case TileStatus.WinningTile _ -> {
                setTilesEnabled(false);
                setSwagRunnable.run();
            }
        }
    }

    private void shuffle(boolean[][] arr) {
        Random random = new Random();
        for (int i = arr.length - 1; i > 0; i--) {
            for (int j = arr[i].length - 1; j > 0; j--) {
                int m = random.nextInt(i + 1);
                int n = random.nextInt(j + 1);

                boolean temp = arr[i][j];
                arr[i][j] = arr[m][n];
                arr[m][n] = temp;
            }
        }
    }

    private TileStatus processLeftClick(int x, int y) {
        TileStatus status = processor.processLeftClick(new Point(x, y), tiles[x][y].tileStatus(), tiles);
        Tile tile = tiles[x][y];
        if (status != tile.tileStatus()) {
            tiles[x][y] = new Tile(tile.x(), tile.y(), tile.button(), status, tile.isBomb());
        }
        return status;
    }

    private TileStatus processRightClick(int x, int y) {
        TileStatus status = processor.processRightClick(tiles[x][y].tileStatus());
        Tile tile = tiles[x][y];
        if (status != tiles[x][y].tileStatus()) {
            tiles[x][y] = new Tile(tile.x(), tile.y(), tile.button(), status, tile.isBomb());
        }
        return status;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    private void setTilesEnabled(boolean isEnabled) {
        for (Tile[] tileRows : tiles) {
            for (Tile tile : tileRows) {
                tile.button().setEnabled(isEnabled);
            }
        }
    }
}
