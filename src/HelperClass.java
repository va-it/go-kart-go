import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class HelperClass {
    private InputStream customFont;
    public Font font;
    public static final int NUMBER_OF_LAPS = 2;

    public HelperClass() {
        loadCustomFont();
    }

    public void loadCustomFont() {
        // https://docs.oracle.com/javase/7/docs/api/java/awt/Font.html#createFont(int,%20java.io.InputStream)
        // https://docs.oracle.com/javase/7/docs/api/java/awt/GraphicsEnvironment.html#registerFont(java.awt.Font)
        try {
            customFont = getClass().getResource("fonts" + File.separator + "Eight-Bit-Madness.ttf").openStream();
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
}
