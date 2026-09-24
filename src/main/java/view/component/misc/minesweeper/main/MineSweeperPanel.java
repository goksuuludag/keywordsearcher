package main.java.view.component.misc.minesweeper.main;

import main.java.config.app.ConfigHandler;
import main.java.view.component.misc.minesweeper.model.Tile;
import main.java.view.util.image.ImageRegistry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MineSweeperPanel extends JPanel {

    public MineSweeperPanel() {

        int width = Integer.parseInt(ConfigHandler.getProperty("minesweeperTileWidth"));
        int height = Integer.parseInt(ConfigHandler.getProperty("minesweeperTileHeight"));
        int numberOfMines = Integer.parseInt(ConfigHandler.getProperty("minesweeperMineCount"));
        setLayout(new BorderLayout());
        SmileyButton btnReload = new SmileyButton();
        btnReload.addActionListener(l -> {
            removeAll();
            btnReload.reset();
            JPanel pnlHeader = new JPanel();
            pnlHeader.add(btnReload);
            add(pnlHeader, BorderLayout.NORTH);
            add(initAndGetPnlTiles(width, height, numberOfMines, btnReload::setDead, btnReload::setWon), BorderLayout.CENTER);
            validate();
            repaint();
        });
        btnReload.doClick();
    }

    private JPanel initAndGetPnlTiles(int width, int height, int numberOfMines, Runnable setDeadRunnable, Runnable setSwagRunnable) {
        MineField mineField = new MineField(width, height, numberOfMines, setDeadRunnable, setSwagRunnable);

        JPanel pnlTiles = new JPanel(new BorderLayout(4, 4));
        pnlTiles.setBorder(new EmptyBorder(4, 4, 4, 4));

        JPanel pnlMineFieldContainer = new JPanel(new GridLayout(width, height));
        pnlTiles.add(pnlMineFieldContainer, BorderLayout.CENTER);
        List<JButton> buttonList = initAndGetMineFields(mineField);
        buttonList.forEach(pnlMineFieldContainer::add);
        return pnlTiles;
    }

    private List<JButton> initAndGetMineFields(MineField mineField) {
        List<JButton> buttonList = new ArrayList<>();
        Tile[][] tiles = mineField.getTiles();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                buttonList.add(tiles[i][j].button());
            }
        }
        return buttonList;
    }

    private static class SmileyButton extends JButton {
        private void reset() {
            setIcon(ImageRegistry.getIcon("smiley_32.png"));
        }

        private void setDead() {
            setIcon(ImageRegistry.getIcon("smiley_dead_32.png"));
        }

        private void setWon() {
            setIcon(ImageRegistry.getIcon("smiley_swag_32.png"));
        }
    }
}
