package go_kart_go;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class HelperClass {
    private InputStream customFont;
    public Font font;
    public static final int NUMBER_OF_LAPS = 1; // change this to set the number of laps
    public static final String resources = "/resources";
    public static final String images = resources + "/images/";
    public static final String sounds = resources + "/sounds/";
    public static final String fonts = resources + "/fonts/";
    public static final String playerOneColour = "red";
    public static final String playerTwoColour = "blue";
    public static final int IMAGE_SIZE = 50;


    public HelperClass() {
        loadCustomFont();
    }

    public void loadCustomFont() {
        // https://docs.oracle.com/javase/7/docs/api/java/awt/Font.html#createFont(int,%20java.io.InputStream)
        // https://docs.oracle.com/javase/7/docs/api/java/awt/GraphicsEnvironment.html#registerFont(java.awt.Font)
        try {
            customFont = getClass().getResource(fonts + "Eight-Bit-Madness.ttf").openStream();
        } catch (IOException e) {
            // fallback to standard sans-serif font
            setFallbackFont();
            System.out.println("Cannot read font");
        }

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, customFont);
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            graphicsEnvironment.registerFont(font);
            font = font.deriveFont(28f);
        } catch (FontFormatException e) {
            setFallbackFont();
            System.out.println("Font format not valid");
        } catch (IOException e) {
            setFallbackFont();
            System.out.println("Cannot read font");
        }
    }

    private void setFallbackFont() {
        font = new Font("Sans-Serif", Font.BOLD, 25);
    }

    public static ImageIcon getWindowIcon() {
        return new ImageIcon(HelperClass.class.getResource(images + "windowImage.png"));
    }

    public static int getOpponentPlayerNumber(int player) {
        if (player == 1) {
            return 2;
        } else {
            return 1;
        }
    }
}
