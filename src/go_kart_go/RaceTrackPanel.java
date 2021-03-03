package go_kart_go;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

class RaceTrackPanel extends JPanel implements ActionListener {

    private final Timer animationTimer;
    private final Timer startRaceTimer;
    private int secondsElapsed = 0;

    // 30 times a second
    //private static final int delay = 1000 / 30;
    private static final int delay = 2000;

    private final JLabel centralMessage = new JLabel("", JLabel.CENTER);
    private final String endGameMessage = "<br> PRESS ENTER TO RESTART <br><br> PRESS ESC TO QUIT";
    private final String gameOverMessage = "<html>GAME OVER<br>" + endGameMessage + "</html>";

    public Kart redKart;
    public Kart blueKart;

    public boolean RACE_IN_PROGRESS = false;

    public RaceTrack raceTrack = new RaceTrack();

    private int player;

    private NetComManager netComManager;

    public RaceTrackPanel(int player, NetComManager netComManager) {
        super();

        this.player = player;
        this.netComManager = netComManager;

        this.setBounds(0, 0, MainWindow.WIDTH, MainWindow.HEIGHT);

        this.redKart = new Kart(HelperClass.playerOneColour, 1);
        this.blueKart = new Kart(HelperClass.playerTwoColour, 2);

        this.redKart.playSpeedSound();
        this.blueKart.playSpeedSound();

        // set initial karts position to be just before the starting line
        this.redKart.setXPosition(RaceTrack.START_LINE_RIGHT_EDGE);
        this.redKart.setYPosition(RaceTrack.INNER_BOTTOM_EDGE);
        // displace blue kart of 50 pixels (image size) down so it looks below the red kart (other lane)
        this.blueKart.setXPosition(RaceTrack.START_LINE_RIGHT_EDGE);
        this.blueKart.setYPosition(RaceTrack.INNER_BOTTOM_EDGE + HelperClass.IMAGE_SIZE);

        add(raceTrack.player1Label);
        add(raceTrack.player2Label);

        add(raceTrack.redKartSpeedLabel);
        add(raceTrack.blueKartSpeedLabel);
        add(raceTrack.redKartSpeed);
        add(raceTrack.blueKartSpeed);

        add(raceTrack.redKartLapLabel);
        add(raceTrack.blueKartLapLabel);
        add(raceTrack.redKartLap);
        add(raceTrack.blueKartLap);

        // Place central message roughly in the centre..
        HelperClass helperClass = new HelperClass();
        centralMessage.setFont(helperClass.font);
        centralMessage.setBounds(RaceTrack.START_LINE_LEFT_EDGE - 100, 200, 300, 250);
        add(centralMessage);

        // shows Waiting for opponent until server sends start_race response
        centralMessage.setText(getWaitingMessage());

        // set up race lights, but don't add them yet. Not until other player is ready
        raceTrack.setRaceLightsImage(0);
        raceTrack.setUpRaceLights();

        // create swing timer for showing the lights and running the countdown
        startRaceTimer = new Timer(1000, startRaceCountDown());

        // create swing timer with 100ms delay and start it
        animationTimer = new Timer(delay, this);
        animationTimer.start();

        // we can tell the server that we are ready to start
        netComManager.sendReady();
    }

    public ActionListener startRaceCountDown() {
        // replaced new ActionListener() { @Override actionPerformed ... } with lambda expression
        ActionListener actionListener = e -> {
            if (secondsElapsed == 0) {
                raceTrack.playCountDownSound();
                centralMessage.setText(""); // clear waiting for message
                add(raceTrack.raceLightsLabel);
            }
            secondsElapsed += 1;
            if (secondsElapsed < 5) {
                // there are only 4 images
                raceTrack.setRaceLightsImage(secondsElapsed);
            }
            switch (secondsElapsed) {
                case 4:
                    this.startRace();
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

            if (RACE_IN_PROGRESS == false && !startRaceTimer.isRunning()) {
                // we constantly ask the server if we can start
                // if they reply positevely then we start the timer etc.
                if(netComManager.requestToStart()) {
                    startRaceTimer.start();
                }
            }

            if (RACE_IN_PROGRESS) {

                // ***************** SEND/RETRIEVE KART INFO ********************
                Kart receivedKart;
                if (player == 1) {
                    sendAndReceiveKartInfo(redKart, blueKart);
                } else {
                    sendAndReceiveKartInfo(blueKart, redKart);
                }
                // **************************************************************

                // ====================== DETECT COLLISIONS =====================
                raceTrack.detectCollisionWithTrack(redKart);
                raceTrack.detectCollisionWithTrack(blueKart);

                stopGameIfBothKartsAreCrashed();

                detectCollisionBetweenKarts();

                raceTrack.updateSpeedInformation(redKart, blueKart);
                // ==============================================================

                blueKart.setNextXPosition();
                blueKart.setNextYPosition();
                blueKart.updateCheckpoint();
                blueKart.getImageAtCurrentIndex().paintIcon(
                        this, g, blueKart.getXPosition(), blueKart.getYPosition()
                );

                redKart.setNextXPosition();
                redKart.setNextYPosition();
                redKart.updateCheckpoint();
                redKart.getImageAtCurrentIndex().paintIcon(
                        this, g, redKart.getXPosition(), redKart.getYPosition()
                );

                raceTrack.updateLapInformation(redKart, blueKart);

                checkAndDeclareWinner(redKart);
                checkAndDeclareWinner(blueKart);
            }
        }
    }

    private void sendAndReceiveKartInfo(Kart kartToSend, Kart kartToReceive) {
        Kart receivedKart;
        netComManager.sendKartInfo(kartToSend);
        receivedKart = netComManager.getOpponentKartInfo(HelperClass.getOpponentPlayerNumber(player));
        if (receivedKart != null) {
            // cannot simply copy the object received because that one doesn't have images
            kartToReceive.setSpeed(receivedKart.getSpeed());
            kartToReceive.setImageIndex(receivedKart.getImageIndex());
            kartToReceive.setXPosition(receivedKart.getXPosition());
            kartToReceive.setYPosition(receivedKart.getYPosition());
        }
    }

    private void detectCollisionBetweenKarts() {
        if (redKart != null && blueKart != null) {
            int yDifference = redKart.getYPosition() - blueKart.getYPosition();
            int xDifference = redKart.getXPosition() - blueKart.getXPosition();

            if (Math.abs(yDifference) < 40 && Math.abs(xDifference) < 40) {
                // Shave a few pixels since the karts don't fill the whole 50 pixels...
                SoundsManager soundsManager = new SoundsManager("accident");
                soundsManager.playSound();
                redKart.stop();
                blueKart.stop();
                redKart.setCrashed(true);
                blueKart.setCrashed(true);
                animationTimer.stop();
                this.stopRace();
                this.centralMessage.setText(gameOverMessage);
            }
        }
    }

    public void stopGameIfBothKartsAreCrashed() {
        if (redKart.isCrashed() && blueKart.isCrashed()) {
            this.centralMessage.setText(gameOverMessage);
            this.stopRace();
        }
    }

    private void checkAndDeclareWinner(Kart kart) {
        if (kart.isWinner()) {
            this.stopRace();
            this.centralMessage.setText("<html>PLAYER " + kart.getPlayer() + " WINS !!!<br>" + endGameMessage + "</html>");
            raceTrack.playCheeringSound();
        }
    }

    public void startRace() {
        this.RACE_IN_PROGRESS = true;
    }

    public void stopRace() {
        this.redKart.stop(); // stop to change speed and sound
        this.blueKart.stop();
        this.animationTimer.stop();
        this.RACE_IN_PROGRESS = false;
        // inform the server
        netComManager.sendStopRace();
    }

    public void stopAllSounds() {
        // stop any sound that might be playing
        raceTrack.stopAllSounds();
        redKart.stopSpeedSound();
        blueKart.stopSpeedSound();
    }

    private String getWaitingMessage() {
        return "WAITING FOR PLAYER " + HelperClass.getOpponentPlayerNumber(player) + " ...";
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }
}




