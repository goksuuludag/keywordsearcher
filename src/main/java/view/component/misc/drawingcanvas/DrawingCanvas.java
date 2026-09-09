package main.java.view.component.misc.drawingcanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DrawingCanvas extends JPanel {
    private final List<List<Point>> pointsList = new ArrayList<>();

    public DrawingCanvas() {
        pointsList.add(new ArrayList<>()); // get this out of the way
        add(initAndGetHeader(), BorderLayout.NORTH);
        add(initAndGetCanvas(), BorderLayout.CENTER);
    }

    private Component initAndGetCanvas() {
        JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.BLUE);
                g2d.setStroke(new BasicStroke(3)); // 3 pixels wide line
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                for(List<Point> pointList : pointsList) {
                    for(int i = 0; i < pointList.size() - 1; i++) {
                        Point p1 = pointList.get(i);
                        Point p2 = pointList.get(i + 1);
                        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                    }
                }
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(1000, 1000);
            }
        };
        canvas.setBackground(Color.WHITE);
        MouseAdapter mouseHandler = new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                pointsList.getLast().add(e.getPoint());
                canvas.repaint();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                pointsList.add(new ArrayList<>());
                canvas.repaint();
            }

        };
        canvas.addMouseListener(mouseHandler);
        canvas.addMouseMotionListener(mouseHandler);
        return canvas;
    }

    private Component initAndGetHeader() {
        JPanel header = new JPanel();
        JButton btnClear = new JButton("Clear");
        btnClear.setBorderPainted(false);
        btnClear.addActionListener(l -> {
            pointsList.clear();
            pointsList.add(new ArrayList<>());
            repaint();
        });
        header.add(btnClear);
        return header;
    }
}
