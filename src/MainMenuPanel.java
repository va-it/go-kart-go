import javax.swing.*;
import java.io.File;

public class MainMenuPanel extends JPanel {

    private SoundsManager soundsManager;

    public MainMenuPanel() {

        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("images" + File.separator + "menu.png"));
        JLabel backgroundImageHolder = new JLabel(backgroundImage);
        backgroundImageHolder.setBounds(0,0, MainWindow.WIDTH, MainWindow.HEIGHT);
        add(backgroundImageHolder);

        startMusic();
    }

    public void startMusic() {
        soundsManager = new SoundsManager("arcade-music-loop");
        soundsManager.playSoundInLoop();
    }

    public void stopMusic() {
        soundsManager.stopSound();
    }
}
