package rttt;

import java.util.EnumMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ScreenManager {
    JPanel displayPanel;
    private static ScreenManager instance = new ScreenManager(new JPanel());

    public static enum Screen {
        MENU_SCREEN, GAME_CONFIG_SCREEN, GAME_SCREEN;
    }

    Map<Screen, JPanel> screenMap;
    Screen currentScreen;

    private ScreenManager(JPanel displayPanel) {
        screenMap = new EnumMap<>(Screen.class);
        this.displayPanel = displayPanel;

        //starting screen
    }

    public static ScreenManager getInstance() {
        return instance;
    }

    public void setDisplayScreen(JPanel newScreen) {
        displayPanel = newScreen;
    }

    public void addScreen(Screen id, JPanel screen) {
        if (!screenMap.containsKey(id)) {
            screen.setPreferredSize(displayPanel.getSize());
            screenMap.put(id, screen);
        } else {
            System.out.println("Cannot add screen " + id + " because it already exists (Use .overwriteScreen to replace a currently added screen)");
        }
    }

    public void overwriteScreen(Screen id, JPanel screen) {
        screen.setPreferredSize(displayPanel.getSize());
        screenMap.put(id, screen);
    }

    public void switchScreen(Screen screenID) {
        System.out.println(screenMap);
        SwingUtilities.invokeLater( () -> {

            if (screenMap.containsKey(screenID)) {
                System.out.println("adding screen");
                if (currentScreen == null) {
                    currentScreen = screenID;
                    screenMap.get(screenID).setPreferredSize(displayPanel.getSize());
                    displayPanel.add(screenMap.get(screenID));
                    displayPanel.revalidate(); displayPanel.repaint();
                    System.out.println("screen added");
                } else {
                    System.out.println("Switching screens");
                    displayPanel.remove(screenMap.get(currentScreen));
                    JPanel nextScreen = screenMap.get(screenID);
                    nextScreen.setPreferredSize(displayPanel.getSize());

                    displayPanel.add(screenMap.get(screenID));
                    displayPanel.revalidate(); displayPanel.repaint();
                    currentScreen = screenID;
                }
            } else {
                System.out.println("[ScreenManager]: Invalid Screen");
            }

        });
    }
}