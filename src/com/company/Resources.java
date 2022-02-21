package com.company;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;

public class Resources {
    private static Map<ImageEnum, BufferedImage> ImageMap = new EnumMap<>(ImageEnum.class);
    private static Map<FontEnum, Font> FontMap = new EnumMap<>(FontEnum.class);
    private static Map<SoundEnum, Clip> SoundMap = new EnumMap<>(SoundEnum.class);
    private static boolean muted;
    public static final int SPRITESIZE = 16;

    public static enum ImageEnum {
        SUN_BACKGROUND, TTT_BACKGROUND;
    }

    public static enum FontEnum {
        STANDARD;
    }

    public static enum SoundEnum {

    }



    public static void init() {
        try {
            BufferedImage sunBackground = ImageIO.read(new File("res/vaporwave sun.jpg"));
            ImageMap.put(ImageEnum.SUN_BACKGROUND, sunBackground);

            BufferedImage tttBackground = ImageIO.read(new File("res/TicTacToe Background.png"));
            ImageMap.put(ImageEnum.TTT_BACKGROUND, tttBackground);
        }	catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            FontMap.put(FontEnum.STANDARD, Font.createFont(Font.TRUETYPE_FONT, new File("res/Shizuru-Regular.ttf")));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private static BufferedImage getSprite(BufferedImage sheet, int row, int col) {
        return sheet.getSubimage(col * SPRITESIZE, row * SPRITESIZE, SPRITESIZE, SPRITESIZE);
    }

    public static BufferedImage getImage(ImageEnum choice) {
        if (ImageMap.containsKey(choice)) {
            return (ImageMap.get(choice));
        } else {
            System.out.println("Invalid choice");
            return new BufferedImage(10, 10, 10);
        }
    }

    public static Font getFont(FontEnum choice) {
        if (FontMap.containsKey(choice)) {
            return FontMap.get(choice);
        } else {
            System.out.println("Invalid choice");
            return new Font("Times New Roman", Font.PLAIN, 12);
        }
    }

    public static void playSound(SoundEnum choice) {
        if (SoundMap.containsKey(choice)) {
            Clip current = SoundMap.get(choice);
            if (muted) { return; }
            current.setMicrosecondPosition(0);
            current.start();
        } else {
            System.out.println("Invalid choice");
        }
    }

    public static void stopSound(SoundEnum choice) {
        if (SoundMap.containsKey(choice)) {
            Clip current = SoundMap.get(choice);
            current.stop();
        } else {
            System.out.println("Invalid choice");
        }
    }

    public static void mute() {
        if (!muted) {
            muted = true;
            SoundMap.values().forEach(clip -> {
                if (clip == null) { return; }
                clip.stop();
            });
        } else {
            muted = false;
            System.out.println("restarting");
        }
    }

    public static boolean isMuted() {
        return muted;
    }
}