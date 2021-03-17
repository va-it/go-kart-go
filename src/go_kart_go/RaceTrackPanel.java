package go_kart_go;

import go_kart_go_network.Messages;

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
    //private static final int delay = 1000;

    private final JLabel centralMessage = new JLabel("", JLabel.CENTER);
    private final String endGameMessage = "<br> PRESS ENTER TO RESTART <br><br> PRESS ESC TO QUIT";
    private final String gameOverMessage = "<html>GAME OVER<br>" + endGameMessage + "</html>";
    private final String errorMessage = "<html> *** GAME ERROR *** <br><br> PRESS ESC TO QUIT</html>";

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

        initialiseKarts();

        this.redKart.playSpeedSound();
        this.blueKart.playSpeedSound();

        // create the kart objects and set their initial positions
        initialiseKartsPositions();

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

    private void initialiseKarts() {
        this.redKart = new Kart(HelperClass.playerOneColour, 1);
        this.blueKart = new Kart(HelperClass.playerTwoColour, 2);
    }

    private void initialiseKartsPositions() {
        // set initial karts position to be just before the starting line
        this.redKart.setXPosition(RaceTrack.START_LINE_RIGHT_EDGE);
        this.redKart.setYPosition(RaceTrack.INNER_BOTTOM_EDGE);
        // displace blue kart of 50 pixels (image size) down so it looks below the red kart (other lane)
        this.blueKart.setXPosition(RaceTrack.START_LINE_RIGHT_EDGE);
        this.blueKart.setYPosition(RaceTrack.INNER_BOTTOM_EDGE + HelperClass.IMAGE_SIZE);
    }

    // NEED TO CREATE SEPARATE THREAD FOR TCP COMMUNICATION AFTER THE RACE HAS STARTED
    // USED TO ASK IF OPPONENT HAS QUIT
    // WHEN OPPONENT QUITS THEN DISPLAY WAITING FOR PLAYER X
    // SEPARATE THREAD IS NEEDED OTHERWISE APPLICATION IS STUCK WAITING FOR TCP MESSAGE

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

                String requestToStart = netComManager.requestToStart();

                // we constantly ask the server if we can start
                // if they reply positevely then we start the timer etc.
                if(requestToStart.equals(Messages.startRace)) {
                    startRaceTimer.start();
                } else {
                    if (requestToStart.equals(Messages.wait)) {
                        // shows Waiting for opponent until server sends start_race response
                        centralMessage.setText(getWaitingMessage());
                    } else {

                        if (requestToStart.equals(Messages.wait)) {
                            centralMessage.setText(errorMessage);
                        }
                    }
                }
            }

            if (RACE_IN_PROGRESS) {

                // Here ask if opponent has quit. If so then display message
                String opponentConnectionStatus = netComManager.getOpponentConnectionStatus();

                if (opponentConnectionStatus.equals(Messages.opponentConnected)) {


                    // ************* TO IMPROVE ***********
                    // ask server status of race (or if other is ready)
                    // if not then stop race
                    String raceInProgress = netComManager.getRaceStatus();
                    if (raceInProgress.equals(Messages.raceInProgress)) {
                        // good

                        // ***************** SEND/RETRIEVE KART INFO ********************
                        sendAndReceiveKarts();
                        // **************************************************************

                        // ====================== DETECT COLLISIONS =====================
                        raceTrack.detectCollisionWithTrack(redKart);
                        raceTrack.detectCollisionWithTrack(blueKart);

                        stopGameIfBothKartsAreCrashed();

                        detectCollisionBetweenKarts();

                        raceTrack.updateSpeedInformation(redKart, blueKart);
                        // ==============================================================

                        if (player == 1) {
                            redKart.setNextXPosition();
                            redKart.setNextYPosition();
                            redKart.updateCheckpoint();
                        }
                        redKart.getImageAtCurrentIndex().paintIcon(
                                this, g, redKart.getXPosition(), redKart.getYPosition()
                        );

                        if (player == 2) {
                            blueKart.setNextXPosition();
                            blueKart.setNextYPosition();
                            blueKart.updateCheckpoint();
                        }
                        blueKart.getImageAtCurrentIndex().paintIcon(
                                this, g, blueKart.getXPosition(), blueKart.getYPosition()
                        );

                        raceTrack.updateLapInformation(redKart, blueKart);

                        checkAndDeclareWinner(redKart);
                        checkAndDeclareWinner(blueKart);



                    } else {
                        if (raceInProgress.equals(Messages.stopRace)) {
                            sendAndReceiveKarts();
                            // check things and set appropriate messages
                            stopRace();
                        } else {
                            if (opponentConnectionStatus.equals(Messages.error)) {
                                this.centralMessage.setText(errorMessage);
                            }
                        }
                    }
                    // **************************************
                } else {
                    if (opponentConnectionStatus.equals(Messages.opponentNotConnected)) {
                        this.centralMessage.setText(getWaitingMessage());
                    } else {
                        if (opponentConnectionStatus.equals(Messages.error)) {
                            this.centralMessage.setText(errorMessage);
                        }
                    }
                    this.stopRace();
                }
            }
        }
    }

    private void sendAndReceiveKarts() {
        // ***************** SEND/RETRIEVE KART INFO ********************
        if (player == 1) {
            sendAndReceiveKartInfo(redKart, blueKart);
        } else {
            sendAndReceiveKartInfo(blueKart, redKart);
        }
        // **************************************************************
    }

    private void sendAndReceiveKartInfo(Kart kartToSend, Kart kartToReceive) {
        Kart receivedKart;
        netComManager.sendKartInfo(kartToSend);
        receivedKart = netComManager.getOpponentKartInfo(HelperClass.getOpponentPlayerNumber(player));
        // if the kart received is indeed the opponent's one
        // Due to the speed at which the objects are sent and read
        // it can happen that they get mixed (e.g. kart read is not the opponent's but own)
        if (receivedKart != null && receivedKart.getPlayer() != kartToSend.getPlayer()) {
            // cannot simply copy the object received because that one doesn't have images
            kartToReceive.setSpeed(receivedKart.getSpeed());
            kartToReceive.setImageIndex(receivedKart.getImageIndex());
            kartToReceive.setXPosition(receivedKart.getXPosition());
            kartToReceive.setYPosition(receivedKart.getYPosition());
            kartToReceive.setWinner(receivedKart.isWinner());
            kartToReceive.setCheckPoint(receivedKart.getCheckPoint());
            kartToReceive.setLap(receivedKart.getLap());
            kartToReceive.setCrashed(receivedKart.isCrashed());
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
                this.centralMessage.setText(gameOverMessage);
                this.stopRace();
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
            this.centralMessage.setText(getWinnerMessage(kart.getPlayer()));
            raceTrack.playCheeringSound();
            this.stopRace();
        }
    }

    public void startRace() {
        // in case there is a delay with server and karts are still crashed...
        this.initialiseKartsPositions();
        this.redKart.setInitialIndex();
        this.blueKart.setInitialIndex();

        this.RACE_IN_PROGRESS = true;
    }

    public void stopRace() {
        this.redKart.stop(); // stop to change speed and sound
        this.blueKart.stop();
        this.animationTimer.stop();
        this.RACE_IN_PROGRESS = false;
        // inform the server
        netComManager.sendStopRace();

        // here we reset the positions of the karts, otherwise
        // when the race is restarted the positions might still be wrong
        // if there is a delay between the clients (which would reset it) and the server
        this.initialiseKartsPositions();
        this.sendAndReceiveKarts();
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

    private String getWinnerMessage(int player) {
        return "<html>PLAYER " + player + " WINS !!!<br>" + endGameMessage + "</html>";
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }
}




