package main.java.view.component.misc.minesweeper;

// Source - https://stackoverflow.com/a/41798128
// Posted by Andrew Thompson
// Retrieved 2026-09-04, License - CC BY-SA 3.0

import main.java.view.util.image.ImageRegistry;
import main.java.view.component.misc.minesweeper.model.MineFieldModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MineSweeperBoard {

    private JComponent ui = null;
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
    private final int size = 16;
    private final JButton btnReload;

    public MineSweeperBoard(JButton btnReload) {
        this.btnReload = btnReload;
        initUI(btnReload);
    }

    public final void initUI(JButton btnReload) {
        ui = new JPanel(new BorderLayout(4, 4));
        ui.setBorder(new EmptyBorder(4, 4, 4, 4));

        MineFieldModel mineFieldModel = new MineFieldModel(16, 45);
        JPanel mineFieldContainer = new JPanel(new GridLayout(size, size));
        ui.add(mineFieldContainer, BorderLayout.CENTER);
        int in = 5;
        Insets insets = new Insets(in, in, in, in);
        List<JButton> buttonList = initAndGetMineFields(size, mineFieldModel, insets);
        buttonList.forEach(mineFieldContainer::add);
    }

    private List<JButton> initAndGetMineFields(int size, MineFieldModel mineFieldModel, Insets insets) {
        List<JButton> buttonList = new ArrayList<>();
        for (int ii = 0; ii < size; ii++) {
            for (int jj = 0; jj < size; jj++) {
                JButton b = new JButton();
                b.setMargin(insets);
                //Font f = new Font("JetBrains Mono", Font.BOLD, 16);
                //b.setFont(f);
                b.setText("?");
                if (mineFieldModel.isExposed(ii, jj)) {
                    if (mineFieldModel.isBomb(ii, jj)) {
                        b.setForeground(Color.red);
                        b.setForeground(Color.BLACK);
                        b.setText(BOMB);
                    } else if (mineFieldModel.countSurroundingMines(ii, jj) > 0) {
                        int count = mineFieldModel.countSurroundingMines(ii, jj);
                        if (count > 0) {
                            b.setForeground(colors[count - 1]);
                            b.setText("" + count);
                        }
                    } else {
                        b.setText("");
                    }
                }
                final int[] iii = new int[]{ii};
                final int[] jjj = new int[]{jj};
                b.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (!b.isEnabled()) {
                            return;
                        }
                        int i = iii[0];
                        int j = jjj[0];
                        if (SwingUtilities.isRightMouseButton(e) && !mineFieldModel.isExposed(i,j)) {
                            b.setText(Character.toString(0x1F6A9));
                        } else {
                            exposeField(b, mineFieldModel, i, j, buttonList);
                        }
                    }
                });
                buttonList.add(b);
            }
        }
        return buttonList;
    }

    private void exposeField(JButton b, MineFieldModel mineFieldModel, int i, int j, List<JButton> buttonList) {
        if (mineFieldModel.isBomb(i, j)) {
            b.setForeground(Color.red);
            b.setForeground(Color.BLACK);
            b.setText(BOMB);
            buttonList.forEach(btn -> btn.setEnabled(false));
            btnReload.setIcon(ImageRegistry.getIcon("smiley_dead_32.png"));
        } else if (mineFieldModel.countSurroundingMines(i, j) > 0) {
            int count = mineFieldModel.countSurroundingMines(i, j);
            if (count > 0) {
                b.setForeground(colors[count - 1]);
                b.setText("" + count);
            }
        } else {
            b.setText("");
        }
    }

    public JComponent getUI() {
        return ui;
    }
}


