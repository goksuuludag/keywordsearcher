package main.java.view.component.misc.minesweeper.main;

import main.java.view.component.misc.minesweeper.flow.TileProcessor;
import main.java.view.component.misc.minesweeper.flow.TileProcessorImpl;
import main.java.view.component.misc.minesweeper.model.Tile;
import main.java.view.component.misc.minesweeper.model.TileStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class MineField {
    private final TileProcessor processor;
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

    public MineField(int width, int height, int numberOfMines, Runnable setDeadRunnable) {
        tiles = new Tile[width][height];
        initTiles(tiles, numberOfMines, setDeadRunnable);
        shuffle(tiles);
        processor = new TileProcessorImpl();
    }

    private void initTiles(Tile[][] tiles, int numberOfMines, Runnable setDeadRunnable) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                JButton b = new JButton();
                final int x = i;
                final int y = j;
                b.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (!b.isEnabled()) {
                            return;
                        }
                        if (SwingUtilities.isRightMouseButton(e)) {
                            processStatus(b, processRightClick(x, y), setDeadRunnable);
                        } else {
                            processStatus(b, processLeftClick(x, y), setDeadRunnable);
                            b.getModel().setPressed(true);
                        }
                    }
                });
                tiles[i][j] = new Tile(i, j, b, new TileStatus.Unknown(), numberOfMines > 0);
                numberOfMines--;
            }
        }
    }

    private void processStatus(JButton button, TileStatus tileStatus, Runnable setDeadRunnable) {
        switch (tileStatus) {
            case TileStatus.Unknown _ -> button.setText("?");
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
        }
    }

    private <T> void shuffle(T[][] a) {
        Random random = new Random();
        for (int i = a.length - 1; i > 0; i--) {
            for (int j = a[i].length - 1; j > 0; j--) {
                int m = random.nextInt(i + 1);
                int n = random.nextInt(j + 1);

                T temp = a[i][j];
                a[i][j] = a[m][n];
                a[m][n] = temp;
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
