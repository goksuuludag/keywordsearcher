package main.java.view.component.misc.minesweeper.main;

import main.java.view.component.misc.minesweeper.model.Tile;
import main.java.view.util.image.ImageRegistry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MineSweeperPanel extends JPanel {

    public MineSweeperPanel(int width, int height, int numberOfMines) {
        setLayout(new BorderLayout());
        SmileyButton btnReload = new SmileyButton();
        btnReload.addActionListener(l -> {
            removeAll();
            btnReload.setDead(false);
            JPanel pnlHeader = new JPanel();
            pnlHeader.add(btnReload);
            add(pnlHeader, BorderLayout.NORTH);
            add(initAndGetPnlTiles(width, height, numberOfMines, () -> btnReload.setDead(true)), BorderLayout.CENTER);
            validate();
            repaint();
        });
        btnReload.doClick();
    }

    private JPanel initAndGetPnlTiles(int width, int height, int numberOfMines, Runnable setDeadRunnable) {
        JPanel pnlTiles = new JPanel(new BorderLayout(4, 4));
        pnlTiles.setBorder(new EmptyBorder(4, 4, 4, 4));
        MineField mineField = new MineField(width, height, numberOfMines, setDeadRunnable);
        JPanel mineFieldContainer = new JPanel(new GridLayout(width, height));
        pnlTiles.add(mineFieldContainer, BorderLayout.CENTER);
        List<JButton> buttonList = initAndGetMineFields(mineField);
        buttonList.forEach(mineFieldContainer::add);
        return pnlTiles;
    }

    private List<JButton> initAndGetMineFields(MineField mineField) {
        List<JButton> buttonList = new ArrayList<>();
        for (Tile[] tileRows : mineField.getTiles()) {
            for (Tile tile : tileRows) {
                buttonList.add(tile.button());
            }
        }
        return buttonList;
    }

    private static class SmileyButton extends JButton {
        public void setDead(boolean isDead) {
            if (isDead) {
                setIcon(ImageRegistry.getIcon("smiley_dead_32.png"));
            } else {
                setIcon(ImageRegistry.getIcon("smiley_32.png"));
            }
        }
    }
}
