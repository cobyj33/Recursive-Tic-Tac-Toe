package com.company;

import com.company.menuScreens.GameConfigScreen;
import com.company.menuScreens.MenuScreen;
import com.company.menuScreens.ScreenManager;

import javax.swing.*;
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
        ScreenManager.getInstance().setDisplayScreen(gameHolder);
        ScreenManager.getInstance().addScreen(ScreenManager.Screen.GAME_CONFIG_SCREEN, new GameConfigScreen());
        ScreenManager.getInstance().addScreen(ScreenManager.Screen.MENU_SCREEN, new MenuScreen());
        ScreenManager.getInstance().addScreen(ScreenManager.Screen.GAME_SCREEN, new JPanel());

        int holderSize = Math.min(frame.getWidth() * 9 / 10, frame.getHeight() * 9 / 10);
        gameHolder.setPreferredSize(new Dimension(holderSize, holderSize));

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
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
}
