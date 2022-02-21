package com.company.menuScreens;

import com.company.Display;
import com.company.GUIResources;
import com.company.Resources;
import com.company.TicTacToe;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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

        maxDepthEntry.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                if (isValidInput()) {
                    int numOfGames = (int) Math.pow(9, Integer.parseInt(maxDepthEntry.getText()));
                    startButton.setText("START " + numOfGames + " GAMES");
                }
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {

            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                isValidInput();
            }

            private boolean isValidInput() {
                int num = 0;
                try {
                    num = Integer.parseInt(maxDepthEntry.getText());
                } catch (NumberFormatException e) {
                    if (maxDepthEntry.getText().equals("-")) {
                        JOptionPane.showMessageDialog(GameConfigScreen.this, "Negative Number Error: Choose a positive number");
                    } else {
                        JOptionPane.showMessageDialog(GameConfigScreen.this, "Enter a number value");
                    }
                    return false;
                }

                if (num > 9) {
                    JOptionPane.showMessageDialog(GameConfigScreen.this, "Integer Overload: Choose a number equal or lower than 9");
                    return false;
                } else if (num < 0) {
                    JOptionPane.showMessageDialog(GameConfigScreen.this, "Negative Number Error: Choose a positive number");
                    return false;
                }
                return true;
            }
        });

        startButton = GUIResources.createLabel("START 81 GAMES");
        ((GUIResources.TextLabel) startButton).setTextColor(Color.CYAN);

        MouseAdapter startButtonActions = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String input = maxDepthEntry.getText();
                try {
                    int maxDepth = Integer.parseInt(input);
                    if (maxDepth > 9 || maxDepth < 0) {
                        System.out.println("Invalid max depth, setting max depth to 2");
                        maxDepth = 2;
                    }
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

        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        JPanel startButtonBackground = new LabelBackground();
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButtonBackground.setBackground(Color.GREEN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startButtonBackground.setBackground(Color.BLACK);
            }
        });
        startButtonBackground.add(startButton);
        add(startButtonBackground, constraints);

        JPanel footnoteBackground = new LabelBackground();
        JLabel footnote = GUIResources.createLabel("Note: You will probably not play all these games");
        ((GUIResources.TextLabel) footnote).setTextColor(Color.CYAN);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 3;
        footnoteBackground.add(footnote);
        add(footnoteBackground, constraints);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        super.paintComponent(g);
        g2D.drawImage(Resources.getImage(Resources.ImageEnum.TTT_BACKGROUND), 0, 0, getWidth(), getHeight(), null);
    }

    private static class LabelBackground extends JPanel {
        LabelBackground() {
            setBackground(Color.BLACK);
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            setLayout(new BorderLayout());
        }
    }


}
