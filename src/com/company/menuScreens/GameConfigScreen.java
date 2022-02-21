package com.company.menuScreens;

import com.company.Display;
import com.company.GUIResources;
import com.company.Resources;
import com.company.TicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameConfigScreen extends JPanel {
    JLabel playLabel;
    JLabel maxDepthPrompt;
    JTextField maxDepthEntry;
    JLabel startButton;

    public GameConfigScreen() {
        setLayout(new GridBagLayout());
        playLabel = GUIResources.createLabel("PLAY");
        ((GUIResources.TextLabel) playLabel).setTextColor(Color.CYAN);
        maxDepthPrompt = GUIResources.createLabel("Max Depth: ");
        ((GUIResources.TextLabel) maxDepthPrompt).setTextColor(Color.CYAN);
        maxDepthEntry = new JTextField();
        maxDepthEntry.setForeground(Color.CYAN);
        maxDepthEntry.setBackground(Color.BLACK);
        maxDepthEntry.setText("2");
        maxDepthEntry.setBorder(BorderFactory.createLineBorder(Color.CYAN, 5));
        maxDepthEntry.setFont(new Font("Helvetica", Font.BOLD, 24));
        startButton = GUIResources.createLabel("START");
        ((GUIResources.TextLabel) startButton).setTextColor(Color.CYAN);

        MouseAdapter startButtonActions = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String input = maxDepthEntry.getText();
                try {
                    int maxDepth = Integer.parseInt(input);
                    TicTacToe newGame = new TicTacToe(maxDepth);
                    ScreenManager.getInstance().overwriteScreen(ScreenManager.Screen.GAME_SCREEN, new Display(newGame));
                    newGame.start();
                    ScreenManager.getInstance().switchScreen(ScreenManager.Screen.GAME_SCREEN);
                } catch (NumberFormatException exception) {
                    maxDepthEntry.setText("INVALID");
                }
            }
        };

        startButton.addMouseListener(startButtonActions);

        GridBagConstraints constraints = GUIResources.getDefaultConstraints();
        constraints.gridwidth = 3;
        add(playLabel, constraints);

        constraints.gridy = 1;
        constraints.gridwidth = 1;
        add(maxDepthPrompt, constraints);

        constraints.gridx = 2;
        add(maxDepthEntry, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        add(startButton, constraints);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        super.paintComponent(g);
        g2D.drawImage(Resources.getImage(Resources.ImageEnum.TTT_BACKGROUND), 0, 0, getWidth(), getHeight(), null);
    }


}
