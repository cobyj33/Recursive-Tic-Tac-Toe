package rttt;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.BevelBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        int maxDepth = 1;
        Resources.init();

        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        int frameSize = Math.min(screensize.width * 9 / 10, screensize.height * 9 / 10);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER));
        frame.setSize(frameSize, frameSize);
        frame.setTitle("Recursive TicTacToe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.BLACK);

        JPanel gameHolder = new JPanel();
        gameHolder.setLayout(new BorderLayout());
        AdvancedBevelBorder border = new AdvancedBevelBorder(Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLACK,10);
        gameHolder.setBorder(border);

//        gameHolder.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.CYAN, Color.BLUE));
        ScreenManager.getInstance().setDisplayScreen(gameHolder);
        ScreenManager.getInstance().addScreen(ScreenManager.Screen.GAME_CONFIG_SCREEN, new GameConfigScreen());
        ScreenManager.getInstance().addScreen(ScreenManager.Screen.MENU_SCREEN, new MenuScreen());
        ScreenManager.getInstance().addScreen(ScreenManager.Screen.GAME_SCREEN, new JPanel());

        int holderSize = Math.min(frame.getWidth() * 9 / 10, frame.getHeight() * 9 / 10);
        gameHolder.setPreferredSize(new Dimension(holderSize, holderSize));

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
//                if (frame.getWidth() != frame.getHeight()) {
//                    int frameSize = Math.min(frame.getWidth(), frame.getHeight());
//                    frame.setSize(frameSize, frameSize);
//                    return;
//                }

                int holderSize = Math.min(frame.getWidth() * 9 / 10, frame.getHeight() * 9 / 10);
                gameHolder.setPreferredSize(new Dimension(holderSize, holderSize));
                frame.revalidate(); frame.repaint();
            }
        });


        frame.add(gameHolder);
        ScreenManager.getInstance().switchScreen(ScreenManager.Screen.MENU_SCREEN);
        System.out.println("Gameholder children: " + gameHolder.getComponentCount());
        frame.setVisible(true);
    }

    private static class AdvancedBevelBorder extends AbstractBorder {

        private Color topColor, rightColor, bottomColor, leftColor, lineColor;
        private int borderWidth;

        public AdvancedBevelBorder(Color topColor, Color rightColor, Color bottomColor, Color leftColor, Color lineColor,
                                   int borderWidth) {
            setTopColor(topColor);
            setRightColor(rightColor);
            setBottomColor(bottomColor);
            setLeftColor(leftColor);
            setLineColor(lineColor);
            setBorderWidth(borderWidth);
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            super.paintBorder(c, g, x, y, width, height);

            int h = height;
            int w = width;
            int bw = getBorderWidth();
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(x, y);

            Polygon topPolygon = createPolygon(new Point(0, 0), new Point(w, 0), new Point(w - bw, bw), new Point(bw, bw),
                    new Point(0, 0));
            g2.setColor(getTopColor());
            g2.fill(topPolygon);
            g2.setColor(getLineColor());
            g2.draw(topPolygon);

            Polygon rightPolygon = createPolygon(new Point(w - 1, 0), new Point(w - 1, h), new Point(w - bw - 1, h - bw),
                    new Point(w - bw - 1, bw), new Point(w - 1, 0));
            g2.setColor(getRightColor());
            g2.fill(rightPolygon);
            g2.setColor(getLineColor());
            g2.draw(rightPolygon);

            Polygon bottomPolygon = createPolygon(new Point(0, h - 1), new Point(w, h - 1), new Point(w - bw, h - bw - 1),
                    new Point(bw, h - bw - 1), new Point(0, h - 1));
            g2.setColor(getBottomColor());
            g2.fill(bottomPolygon);
            g2.setColor(getLineColor());
            g2.draw(bottomPolygon);

            Polygon leftPolygon = createPolygon(new Point(0, 0), new Point(0, h), new Point(bw, h - bw), new Point(bw, bw),
                    new Point(0, 0));
            g2.setColor(getLeftColor());
            g2.fill(leftPolygon);
            g2.setColor(getLineColor());
            g2.draw(leftPolygon);

            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(getBorderWidth(), getBorderWidth(), getBorderWidth() + 1, getBorderWidth() + 1);
        }

        private Polygon createPolygon(Point... points) {
            Polygon polygon = new Polygon();
            for (Point point : points) {
                polygon.addPoint(point.x, point.y);
            }
            return polygon;
        }

        public Color getTopColor() {
            return topColor;
        }

        public void setTopColor(Color topColor) {
            this.topColor = topColor;
        }

        public Color getRightColor() {
            return rightColor;
        }

        public void setRightColor(Color rightColor) {
            this.rightColor = rightColor;
        }

        public Color getBottomColor() {
            return bottomColor;
        }

        public void setBottomColor(Color bottomColor) {
            this.bottomColor = bottomColor;
        }

        public Color getLeftColor() {
            return leftColor;
        }

        public void setLeftColor(Color leftColor) {
            this.leftColor = leftColor;
        }

        public Color getLineColor() {
            return lineColor;
        }

        public void setLineColor(Color lineColor) {
            this.lineColor = lineColor;
        }

        public int getBorderWidth() {
            return borderWidth;
        }

        public void setBorderWidth(int borderWidth) {
            this.borderWidth = borderWidth;
        }

    }
}