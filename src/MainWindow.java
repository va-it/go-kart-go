import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

class MainWindow extends JFrame implements KeyListener {

    public static final int WIDTH = 850;
    public static final int HEIGHT = 650;
    private Container pane;

    private RaceTrackPanel raceTrackPanel;
    private MainMenuPanel mainMenuPanel;

    public MainWindow() {
        // call constructor of JFrame
        super();

        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        this.setTitle("Go-Kart-Go");

        // ensure the program closes when this window is closed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addKeyListener(this);

        pane = getContentPane();

        mainMenuPanel = new MainMenuPanel();

        pane.add(mainMenuPanel);
    }

    public void replaceMainMenuWithRaceTrack() {
        if (pane.getComponent(0) == mainMenuPanel) {
            // only replace the panel to be the track the first time that the user presses enter

            mainMenuPanel.stopMusic();
            mainMenuPanel.playEnterSound();
            pane.remove(mainMenuPanel);

            raceTrackPanel = new RaceTrackPanel();
            raceTrackPanel.setLayout(null);
            pane.add(raceTrackPanel);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'w':
                raceTrackPanel.blueKart.accelerate();
                break;
            case 's':
                raceTrackPanel.blueKart.decelerate();
                break;
            case 'a':
                raceTrackPanel.blueKart.turnLeft();
                break;
            case 'd':
                raceTrackPanel.blueKart.turnRight();
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                raceTrackPanel.redKart.accelerate();
                break;
            case KeyEvent.VK_DOWN:
                raceTrackPanel.redKart.decelerate();
                break;
            case KeyEvent.VK_LEFT:
                raceTrackPanel.redKart.turnLeft();
                break;
            case KeyEvent.VK_RIGHT:
                raceTrackPanel.redKart.turnRight();
            case KeyEvent.VK_ENTER:
                replaceMainMenuWithRaceTrack();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}