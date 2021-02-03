import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

class MainWindowPanel extends JPanel implements ActionListener {

    private final Timer animationTimer;
    // 30 times a second
    private static final int delay = 1000 / 30;

    private static final int OUTER_LEFT_EDGE = 50;
    private static final int OUTER_RIGHT_EDGE = 800;
    private static final int OUTER_TOP_EDGE = 100;
    private static final int OUTER_BOTTOM_EDGE = 600;

    private static final int INNER_LEFT_EDGE = 150;
    private static final int INNER_RIGHT_EDGE = 700;
    private static final int INNER_TOP_EDGE = 200;
    private static final int INNER_BOTTOM_EDGE = 500;

    private static final int START_LINE_WIDTH = 9;
    private static final int START_LINE_LEFT_EDGE = ((INNER_RIGHT_EDGE + INNER_LEFT_EDGE) / 2) - START_LINE_WIDTH;
    private static final int START_LINE_RIGHT_EDGE = START_LINE_LEFT_EDGE + START_LINE_WIDTH;

    JLabel redKartSpeedLabel = new JLabel();
    JLabel blueKartSpeedLabel = new JLabel();
    JLabel redKartSpeed = new JLabel();
    JLabel blueKartSpeed = new JLabel();

    public Kart redKart;
    public Kart blueKart;

    public MainWindowPanel() {
        super();

        this.redKart = new Kart("red");
        this.blueKart = new Kart("blue");

        // set initial karts position to be just before the starting line
        this.redKart.setxPosition(START_LINE_RIGHT_EDGE);
        this.redKart.setyPosition(INNER_BOTTOM_EDGE);
        // displace blue kart of 50 pixels (image size) down so it looks below the red kart (other lane)
        this.blueKart.setxPosition(START_LINE_RIGHT_EDGE);
        this.blueKart.setyPosition(INNER_BOTTOM_EDGE + this.redKart.IMAGE_SIZE);

        // Labels for karts' speed
        redKartSpeedLabel.setText("Red speed: ");
        blueKartSpeedLabel.setText("Blue speed: ");
        redKartSpeedLabel.setBounds(50, 50, 80, 20);
        blueKartSpeedLabel.setBounds(200, 50, 80, 20);
        add(redKartSpeedLabel);
        add(blueKartSpeedLabel);

        // Labels that will contain the actual speed value
        redKartSpeed.setBounds(130, 50, 20, 20);
        blueKartSpeed.setBounds(280, 50, 20, 20);
        add(redKartSpeed);
        add(blueKartSpeed);

        // create swing timer with 100ms delay and start it
        animationTimer = new Timer(delay, this);
        animationTimer.start();

        this.setBounds(0, 0, MainWindow.WIDTH, MainWindow.HEIGHT);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // call superclass paintComponent

        renderTrack(g);

        if (animationTimer.isRunning()) {

            detectCollisionWithTrack(redKart);
            detectCollisionWithTrack(blueKart);

            detectCollisionBetweenKarts();

            updateSpeedInformation();

            blueKart.setNextXPosition();
            blueKart.setNextYPosition();

            blueKart.getImageAtCurrentIndex().paintIcon(this, g, blueKart.getxPosition(), blueKart.getyPosition());

            redKart.setNextXPosition();
            redKart.setNextYPosition();

            redKart.getImageAtCurrentIndex().paintIcon(this, g, redKart.getxPosition(), redKart.getyPosition());
        }
    }

    public void updateSpeedInformation() {
        redKartSpeed.setText(String.valueOf(redKart.getSpeed()));
        blueKartSpeed.setText(String.valueOf(blueKart.getSpeed()));
    }

    public void renderTrack(Graphics g) {
        g.setColor(Color.black);
        // draw outer edge
        g.drawRect(OUTER_LEFT_EDGE, OUTER_TOP_EDGE, (OUTER_RIGHT_EDGE - OUTER_LEFT_EDGE), (OUTER_BOTTOM_EDGE - OUTER_TOP_EDGE));
        // draw inner edge
        g.drawRect(INNER_LEFT_EDGE, INNER_TOP_EDGE, (INNER_RIGHT_EDGE - INNER_LEFT_EDGE), (INNER_BOTTOM_EDGE - INNER_TOP_EDGE));

        g.setColor(Color.gray);
        // fill in the road
        g.fillRect(OUTER_LEFT_EDGE, OUTER_TOP_EDGE, (OUTER_RIGHT_EDGE - OUTER_LEFT_EDGE), (OUTER_BOTTOM_EDGE - OUTER_TOP_EDGE));

        g.setColor(Color.yellow);

        // mid-lane marker
        g.drawRect((OUTER_LEFT_EDGE + INNER_LEFT_EDGE) / 2, (OUTER_TOP_EDGE + INNER_TOP_EDGE) / 2, (INNER_RIGHT_EDGE - OUTER_LEFT_EDGE), (INNER_BOTTOM_EDGE - OUTER_TOP_EDGE)
        );

        g.setColor(Color.white);
        // draw start line
        g.fillRect(START_LINE_LEFT_EDGE, INNER_BOTTOM_EDGE, START_LINE_WIDTH, (OUTER_BOTTOM_EDGE - INNER_BOTTOM_EDGE));

        // === draw checkerboard on start line ===
        g.setColor(Color.black);
        int square_size = 3;
        for (int i = 0; i <= 99; i += 6) {
            g.fillRect(START_LINE_LEFT_EDGE, INNER_BOTTOM_EDGE + 1 + i, square_size, square_size);
            g.fillRect(START_LINE_LEFT_EDGE + (square_size * 2), INNER_BOTTOM_EDGE + 1 + i, square_size, square_size);
        }
        for (int i = 0; i <= 93; i += 6) {
            g.fillRect(START_LINE_LEFT_EDGE + square_size, INNER_BOTTOM_EDGE + 1 + square_size + i, square_size, square_size);
        }
        // =======================================

        g.setColor(Color.green);
        // draw central grassed area
        g.fillRect(INNER_LEFT_EDGE, INNER_TOP_EDGE, (INNER_RIGHT_EDGE - INNER_LEFT_EDGE), (INNER_BOTTOM_EDGE - INNER_TOP_EDGE));
    }

    public void detectCollisionWithTrack(Kart kart) {

        // store values in variables to avoid having to call the getters multiple times
        int xPosition = kart.getxPosition();
        int yPosition = kart.getyPosition();
        boolean crashed = false;

        // only check collisions when kart is moving
        if (kart.getSpeed() != 0) {

            if (xPosition < OUTER_LEFT_EDGE || xPosition > OUTER_RIGHT_EDGE) {
                // crashed into left or right outer edges
                crashed = true;
            } else {
                if (yPosition < OUTER_TOP_EDGE || yPosition > (OUTER_BOTTOM_EDGE - kart.IMAGE_SIZE)) {
                    // subtract image size otherwise the condition only applies when the whole image is below the point
                    // crashed into top or bottom outer edges
                    crashed = true;
                } else {
                    if (yPosition > INNER_TOP_EDGE && yPosition < INNER_BOTTOM_EDGE && xPosition > INNER_LEFT_EDGE && xPosition < INNER_RIGHT_EDGE) {
                        // crashed into inner left edge
                        crashed = true;
                    } else {
                        if (xPosition > INNER_LEFT_EDGE && xPosition < INNER_RIGHT_EDGE && yPosition > (INNER_TOP_EDGE - kart.IMAGE_SIZE) && yPosition < INNER_BOTTOM_EDGE) {
                            // subtract image size otherwise the condition only applies when the whole image is below the point
                            crashed = true;
                        } else {
                            if (yPosition > INNER_TOP_EDGE && yPosition < INNER_BOTTOM_EDGE && xPosition < INNER_RIGHT_EDGE && xPosition > INNER_LEFT_EDGE) {
                                // crashed into inner right edge
                                crashed = true;
                            } else {
                                if (xPosition > INNER_LEFT_EDGE && xPosition < INNER_RIGHT_EDGE && yPosition < INNER_BOTTOM_EDGE && yPosition > INNER_TOP_EDGE) {
                                    // crashed into inner bottom edge
                                    crashed = true;
                                }
                            }
                        }
                    }
                }
            }

            if (crashed) {
                SoundsManager soundsManager = new SoundsManager();
                soundsManager.playSound("accident");
                kart.setSpeed(0);
            }
        }
    }

    private void detectCollisionBetweenKarts() {
        int yDifference = redKart.getyPosition() - blueKart.getyPosition();
        int xDifference = redKart.getxPosition() - blueKart.getxPosition();

        if (Math.abs(yDifference) < 40 && Math.abs(xDifference) < 40) {
            // Should be minus image size, but since the karts don't fill the whole 50 pixels...
            SoundsManager soundsManager = new SoundsManager();
            soundsManager.playSound("accident");
            animationTimer.stop();
            // GAME OVER
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }
}




