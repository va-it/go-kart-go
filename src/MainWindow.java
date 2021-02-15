import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

class MainWindow extends JFrame implements KeyListener {

    public static final int WIDTH = 850;
    public static final int HEIGHT = 650;
    private final Container pane;

    private RaceTrackPanel raceTrackPanel;
    private final MainMenuPanel mainMenuPanel;

    private int player;

    public MainWindow(int player) {
        // call constructor of JFrame
        super();

        this.player = player;

        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        this.setTitle("Go-Kart-Go");
        this.setIconImage(HelperClass.getWindowIcon().getImage());
        this.setLocationRelativeTo(null); // center the window

        // ensure the program closes when this window is closed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addKeyListener(this);

        pane = getContentPane();

        mainMenuPanel = new MainMenuPanel();

        pane.add(mainMenuPanel);
    }

    public void instantiateRaceTrackPanel() {
        raceTrackPanel = new RaceTrackPanel(player);
        raceTrackPanel.setLayout(null);
        pane.add(raceTrackPanel);
    }

    public void replaceMainMenuWithRaceTrack() {
        if (pane.getComponent(0) == mainMenuPanel) {
            // only replace the panel to be the track the first time that the user presses enter

            mainMenuPanel.stopMusic();
            mainMenuPanel.playEnterSound();
            pane.remove(mainMenuPanel);

            this.instantiateRaceTrackPanel();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (raceTrackPanel.RACE_IN_PROGRESS && !raceTrackPanel.blueKart.isCrashed() && player == 2) {
            // only allow control of kart when race starts
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
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (raceTrackPanel.RACE_IN_PROGRESS && !raceTrackPanel.redKart.isCrashed() && player == 1) {
                    raceTrackPanel.redKart.accelerate();
                }
                break;
            case KeyEvent.VK_DOWN:
                if (raceTrackPanel.RACE_IN_PROGRESS && !raceTrackPanel.redKart.isCrashed() && player == 1) {
                    raceTrackPanel.redKart.decelerate();
                }
                break;
            case KeyEvent.VK_LEFT:
                if (raceTrackPanel.RACE_IN_PROGRESS && !raceTrackPanel.redKart.isCrashed() && player == 1) {
                    raceTrackPanel.redKart.turnLeft();
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (raceTrackPanel.RACE_IN_PROGRESS && !raceTrackPanel.redKart.isCrashed() && player == 1) {
                    raceTrackPanel.redKart.turnRight();
                }
                break;
            case KeyEvent.VK_ENTER:
                if (pane.getComponent(0) == raceTrackPanel && !raceTrackPanel.RACE_IN_PROGRESS) {
                    // if the user presses enter while the race is not in progress then restart
                    raceTrackPanel.stopAllSounds();
                    pane.remove(raceTrackPanel);
                    this.instantiateRaceTrackPanel();
                }
                replaceMainMenuWithRaceTrack();
                break;
            case KeyEvent.VK_ESCAPE:
                // quit game
                if (pane.getComponent(0) == raceTrackPanel) {
                    raceTrackPanel.stopAllSounds();
                    pane.remove(raceTrackPanel);
                }
                System.exit(0);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}