package main.java.view.component.misc.minesweeper;

// Source - https://stackoverflow.com/a/41798128
// Posted by Andrew Thompson
// Retrieved 2026-09-04, License - CC BY-SA 3.0

import main.java.view.util.image.ImageRegistry;
import main.java.view.component.misc.minesweeper.model.MineFieldModel;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MineSweeperBoard {

    private JComponent ui = null;
    Color[] colors = {
            Color.BLUE,
            Color.CYAN.darker(),
            Color.GREEN.darker(),
            Color.YELLOW.darker(),
            Color.ORANGE.darker(),
            Color.PINK.darker(),
            Color.MAGENTA,
            Color.RED
    };
    private final static String BOMB = "*";
    int size = 16;

    public MineSweeperBoard(JButton btnReload) {
        initUI(btnReload);
    }

    public final void initUI(JButton btnReload) {
//        if (ui != null) {
//            return;
//        }

        ui = new JPanel(new BorderLayout(4, 4));
        ui.setBorder(new EmptyBorder(4, 4, 4, 4));

        MineFieldModel mineFieldModel = new MineFieldModel(16, 45);
        List<JButton> buttonList = new ArrayList<>();
        JPanel mineFieldContainer = new JPanel(new GridLayout(
                size, size));
        ui.add(mineFieldContainer, BorderLayout.CENTER);
        int in = 5;
        Insets insets = new Insets(in, in, in, in);
        //Font f = new Font("JetBrains Mono", Font.BOLD, 16);
        for (int ii = 0; ii < size; ii++) {
            for (int jj = 0; jj < size; jj++) {
                JButton b = new JButton();
                b.setMargin(insets);
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
                b.addActionListener(l -> {
                    int i = iii[0];
                    int j = jjj[0];
                    if(b.getText().equals("?")) {
                        if (mineFieldModel.isBomb(i, j)) {
                            b.setForeground(Color.red);
                            b.setForeground(Color.BLACK);
                            b.setText(BOMB);
                            setButtonsEnabled(buttonList, false);
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
                });
                mineFieldContainer.add(b);
                buttonList.add(b);
            }
        }
    }

    private void setButtonsEnabled(List<JButton> buttons, boolean isEnabled) {
        for(JButton button : buttons) {
            button.setEnabled(isEnabled);
        }
    }

    private Vector<Font> getCompatibleFonts() {
        Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        Vector<Font> fontVector = new Vector<>();

        for (Font font : fonts) {
            if (font.canDisplayUpTo("12345678" + BOMB) < 0) {
                fontVector.add(font);
            }
        }
        return fontVector;
    }

    public JComponent getUI() {
        return ui;
    }

//    public static void main(String[] args) {
//        Runnable r = () -> {
//            try {
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            } catch (Exception useDefault) {
//            }
//            MineSweeper o = new MineSweeper();
//
//            JFrame f = new JFrame(o.getClass().getSimpleName());
//            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            f.setLocationByPlatform(true);
//
//            f.setContentPane(o.getUI());
//            f.pack();
//            f.setMinimumSize(f.getSize());
//
//            f.setVisible(true);
//        };
//        SwingUtilities.invokeLater(r);
//
//    }
}


