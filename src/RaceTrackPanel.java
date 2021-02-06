import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

class RaceTrackPanel extends JPanel implements ActionListener {

    private final Timer animationTimer;
    private final Timer startRaceTimer;
    private int secondsElapsed = 0;

    // 30 times a second
    private static final int delay = 1000 / 30;

    public Kart redKart;
    public Kart blueKart;

    public boolean RACE_STARTED = false;

    RaceTrack raceTrack = new RaceTrack();

    public RaceTrackPanel() {
        super();

        this.setBounds(0, 0, MainWindow.WIDTH, MainWindow.HEIGHT);

        this.redKart = new Kart("red");
        this.blueKart = new Kart("blue");

        // set initial karts position to be just before the starting line
        this.redKart.setXPosition(RaceTrack.START_LINE_RIGHT_EDGE);
        this.redKart.setYPosition(RaceTrack.INNER_BOTTOM_EDGE);
        // displace blue kart of 50 pixels (image size) down so it looks below the red kart (other lane)
        this.blueKart.setXPosition(RaceTrack.START_LINE_RIGHT_EDGE);
        this.blueKart.setYPosition(RaceTrack.INNER_BOTTOM_EDGE + this.redKart.IMAGE_SIZE);

        add(raceTrack.redKartSpeedLabel);
        add(raceTrack.blueKartSpeedLabel);
        add(raceTrack.redKartSpeed);
        add(raceTrack.blueKartSpeed);

        add(raceTrack.redKartLapLabel);
        add(raceTrack.blueKartLapLabel);
        add(raceTrack.redKartLap);
        add(raceTrack.blueKartLap);

        raceTrack.setRaceLightsImage(0);
        raceTrack.setUpRaceLights();
        add(raceTrack.raceLightsLabel);

        startRaceTimer = new Timer(1000, startRaceCountDown());
        startRaceTimer.start();

        // create swing timer with 100ms delay and start it
        animationTimer = new Timer(delay, this);
        animationTimer.start();
    }

    public ActionListener startRaceCountDown() {
        raceTrack.countDownSoundManager = new SoundsManager("race-start-ready-go");

        // replaced new ActionListener() { @Override actionPerformed ... } with lambda expression
        ActionListener actionListener = e -> {
            if (secondsElapsed == 0) {
                // only play countdown sound the first time
                raceTrack.countDownSoundManager.playSound();
            }
            secondsElapsed += 1;
            if (secondsElapsed < 5) {
                // there are only 4 images
                raceTrack.setRaceLightsImage(secondsElapsed);
            }
            switch (secondsElapsed) {
                case 4:
                    RACE_STARTED = true;
                    break;
                case 5: // let the lights display for an extra second
                    startRaceTimer.stop();
                    remove(raceTrack.raceLightsLabel);
                    break;
            }
        };
        return actionListener;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // call superclass paintComponent

        raceTrack.renderTrack(g);

        if (animationTimer.isRunning()) {

            // ====================== DETECT COLLISIONS =====================
            raceTrack.detectCollisionWithTrack(redKart);
            raceTrack.detectCollisionWithTrack(blueKart);

            detectCollisionBetweenKarts();

            raceTrack.updateSpeedInformation(redKart, blueKart);
            // ==============================================================

            blueKart.setNextXPosition();
            blueKart.setNextYPosition();

            blueKart.updateCheckpoint();

            blueKart.getImageAtCurrentIndex().paintIcon(this, g, blueKart.getXPosition(), blueKart.getYPosition());

            redKart.setNextXPosition();
            redKart.setNextYPosition();

            redKart.updateCheckpoint();

            redKart.getImageAtCurrentIndex().paintIcon(this, g, redKart.getXPosition(), redKart.getYPosition());

            raceTrack.updateLapInformation(redKart, blueKart);
        }
    }

    private void detectCollisionBetweenKarts() {
        int yDifference = redKart.getYPosition() - blueKart.getYPosition();
        int xDifference = redKart.getXPosition() - blueKart.getXPosition();

        if (Math.abs(yDifference) < 40 && Math.abs(xDifference) < 40) {
            // Shave a few pixels since the karts don't fill the whole 50 pixels...
            SoundsManager soundsManager = new SoundsManager("accident");
            soundsManager.playSound();
            redKart.stop();
            blueKart.stop();
            animationTimer.stop();
            // GAME OVER
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }
}




