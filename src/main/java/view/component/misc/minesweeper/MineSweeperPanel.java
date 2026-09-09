package main.java.view.component.misc.minesweeper;

import main.java.view.util.image.ImageRegistry;

import javax.swing.*;
import java.awt.*;

public class MineSweeperPanel extends JPanel {

    public MineSweeperPanel() {
        setLayout(new BorderLayout());
        JButton btnReload = new JButton();
        btnReload.addActionListener(l -> {
            removeAll();
            btnReload.setIcon(ImageRegistry.getIcon("smiley_32.png"));
            JPanel pnlHeader = new JPanel();
            pnlHeader.add(btnReload);
            MineSweeperBoard board = new MineSweeperBoard(btnReload);
            add(pnlHeader, BorderLayout.NORTH);
            add(board.getUI(), BorderLayout.CENTER);
            validate();
            repaint();
        });
        btnReload.doClick();
    }
}
