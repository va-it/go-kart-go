package go_kart_go;

import javax.swing.*;

public class MainMenuPanel extends JPanel {

    private SoundsManager soundsManager;

    public MainMenuPanel(int player) {

        ImageIcon backgroundImage = new ImageIcon(getClass().getResource(
                HelperClass.images + "menuPlayer" + player + ".png")
        );
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

    public void playEnterSound() {
        soundsManager = new SoundsManager("menu-select");
        soundsManager.playSound();
    }
}
