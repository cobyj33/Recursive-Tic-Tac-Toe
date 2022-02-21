package com.company.menuScreens;

import com.company.GUIResources;
import com.company.Resources;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuScreen extends JPanel {
    TitleArea title;
    JButton playButton;
    JButton quitButton;


    public MenuScreen() {
        title = new TitleArea("Tic Tac Toe");
        setLayout(new GridBagLayout());
        playButton = new ActionButton();
        playButton.setText("Play");
        quitButton = new ActionButton();
        quitButton.setText("Quit");

        playButton.addActionListener(l -> ScreenManager.getInstance().switchScreen(ScreenManager.Screen.GAME_CONFIG_SCREEN));
        quitButton.addActionListener(l -> System.exit(0));

        GridBagConstraints constraints = GUIResources.getDefaultConstraints();
        constraints.gridwidth = 3;
        constraints.gridheight = 2;
        add(title, constraints);

        constraints.fill = GridBagConstraints.NONE;

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        add(playButton, constraints);

        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        add(quitButton, constraints);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        super.paintComponent(g);
        g2D.drawImage(Resources.getImage(Resources.ImageEnum.TTT_BACKGROUND), 0, 0, getWidth(), getHeight(), null);
    }

    private class TitleArea extends JPanel {
        String title;
        int topOffset;
        javax.swing.Timer animator;
        TitleArea(String title) {
            this.title = title;
            topOffset = 0;
            animator = new javax.swing.Timer(75, l -> repaint());
            setOpaque(false);

            addAncestorListener(new AncestorListener() {
                @Override
                public void ancestorAdded(AncestorEvent ancestorEvent) {
                    animator.restart();
                }

                @Override
                public void ancestorRemoved(AncestorEvent ancestorEvent) {
                    animator.stop();
                }

                @Override
                public void ancestorMoved(AncestorEvent ancestorEvent) {

                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2D = (Graphics2D) g;
            g2D.setFont(new Font("Helvetica", Font.BOLD, 12));
            g2D.setPaint(Color.CYAN);
            g2D.setFont( g2D.getFont().deriveFont(getMaxTextSize(g2D, title)));
            g2D.setFont(g2D.getFont().deriveFont( g2D.getFont().getSize() + (float) topOffset * 7 / 20 ));
            FontMetrics metrics = g2D.getFontMetrics();

            topOffset += metrics.getHeight() * 7 / 64;
            int currentLineHeight = -topOffset;
            System.out.println("metrics height: " + metrics.getHeight());
            if (topOffset > metrics.getHeight()) {
                topOffset = 0;
                System.out.println("topoffset reset");
            }


            while (g2D.getFont().getSize() > 1f && currentLineHeight < g2D.getClipBounds().height) {
                metrics = g2D.getFontMetrics();
                currentLineHeight += metrics.getHeight();
                g2D.drawString(title, getWidth() / 2 - metrics.stringWidth(title) / 2, currentLineHeight);
                g2D.setFont(g2D.getFont().deriveFont( g2D.getFont().getSize() * 3 / 5f ));
                System.out.println(g2D.getFont().getSize());
            }
        }

        private float getMaxTextSize(Graphics2D g2D, String text) {
            Font originalFont = g2D.getFont();
            FontMetrics metrics = g2D.getFontMetrics();
            while (metrics.stringWidth(text) < getWidth()) {
                g2D.setFont( g2D.getFont().deriveFont( g2D.getFont().getSize() + 1f ));
                metrics = g2D.getFontMetrics();
            }

            float fontSize = g2D.getFont().getSize() - 1f;
            g2D.setFont(originalFont);
            return fontSize;
        }
    }

    private class ActionButton extends JButton {
        ActionButton() {
            setFont(new Font("Helvetica", Font.BOLD, 16));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBackground(Color.BLACK);
            setForeground(Color.CYAN);
            setFocusable(false);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    setBackground(Color.CYAN);
                    setForeground(Color.BLACK);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    setBackground(Color.BLACK);
                    setForeground(Color.CYAN);
                }
            });
        }
    }
}
