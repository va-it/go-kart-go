import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.io.File;

class MainWindowPanel extends JPanel implements ActionListener {

    private final Timer animationTimer;
    private static final int delay = 50;
    private final static String redKartImage = "kartRed";
    private final static String blueKartImage = "kartBlue";

    // initially the car is shown pointing left
    private int blueKartImageIndex = 12;
    private int redKartImageIndex = 12;

    private int blueKartSpeed = 0;
    private int redKartSpeed = 0;

    // initially the blue car is in the innermost lane, just before the starting line
    private int blueKartXPosition = 425;
    private int blueKartYPosition = 500;

    // initially the red car is in the outermost lane (below blue), just before the starting line
    private int redKartXPosition = 425;
    private int redKartYPosition = 550;

    private final int MIN_X_OUTER_EDGE = 50;
    private final int MAX_X_OUTER_EDGE = 750;
    private final int MIN_Y_OUTER_EDGE = 100;
    private final int MAX_Y_OUTER_EDGE = 500;

    private final int MIN_X_INNER_EDGE = 150;
    private final int MAX_X_INNER_EDGE = 550;
    private final int MIN_Y_INNER_EDGE = 200;
    private final int MAX_Y_INNER_EDGE = 300;


    protected int NUMBER_OF_IMAGES = 16;
    protected ImageIcon[] redKart;
    protected ImageIcon[] blueKart;

    JLabel redKartSpeedLabel = new JLabel();
    JLabel blueKartSpeedLabel = new JLabel();

    public MainWindowPanel() {
        super();

        // load images into array
        loadImages();

        // create swing timer with 100ms delay and start it
        animationTimer = new Timer(delay, this);
        animationTimer.start();

        this.setBounds(0, 0, MainWindow.WIDTH, MainWindow.HEIGHT);
    }

    public void loadImages() {
        redKart = new ImageIcon[NUMBER_OF_IMAGES];
        blueKart = new ImageIcon[NUMBER_OF_IMAGES];

        Class cl = this.getClass();

        for (int i = 0; i < NUMBER_OF_IMAGES; i++) {
            redKart[i] = new ImageIcon(cl.getResource("images" + File.separator + redKartImage + i + ".png"));
            blueKart[i] = new ImageIcon(cl.getResource("images" + File.separator + blueKartImage + i + ".png"));
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // call superclass paintComponent

        if (animationTimer.isRunning()) {

            renderTrack(g);

            blueKartXPosition = getXPosition(blueKartImageIndex, blueKartXPosition, blueKartSpeed);
            blueKartYPosition = getYPosition(blueKartImageIndex, blueKartYPosition, blueKartSpeed);

            blueKart[blueKartImageIndex].paintIcon(this, g, blueKartXPosition, blueKartYPosition);

            redKartXPosition = getXPosition(redKartImageIndex, redKartXPosition, redKartSpeed);
            redKartYPosition = getYPosition(redKartImageIndex, redKartYPosition, redKartSpeed);

            redKart[redKartImageIndex].paintIcon(this, g, redKartXPosition, redKartYPosition);

            printSpeedInformation();
        }
    }

    public void printSpeedInformation() {
        redKartSpeedLabel.setText("Red speed: " + redKartSpeed);
        blueKartSpeedLabel.setText("Blue speed: " + blueKartSpeed);

        redKartSpeedLabel.setBounds(50, 50, 100, 30);
        blueKartSpeedLabel.setBounds(200, 50, 100, 30);

        add(redKartSpeedLabel);
        add(blueKartSpeedLabel);
    }

    public int getXPosition(int imageIndex, int xPosition, int speed) {
        // multiple cases as shown in docs:
        // https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html
        switch (imageIndex) {
            case 1: case 7:
                xPosition = (xPosition + ((speed*25)/100)); // + 25%
                break;
            case 2: case 6:
                xPosition = (xPosition + ((speed*42)/100)); // + 42%
                break;
            case 3: case 4: case 5:
                xPosition = (xPosition + ((speed*50)/100)); // + 50%
                break;
            case 9: case 15:
                xPosition = (xPosition - ((speed*25)/100)); // - 25%
                break;
            case 10: case 14:
                xPosition = (xPosition - ((speed*42)/100)); // - 42%
                break;
            case 11: case 12: case 13:
                xPosition = (xPosition - ((speed*50)/100)); // - 50%
                break;
            default:
                // 0 and 8, no change in position
                break;
        }
        return xPosition;
    }

    public int getYPosition(int imageIndex, int yPosition, int speed) {
        switch (imageIndex) {
            case 0: case 1: case 15:
                yPosition = (yPosition - ((speed*50)/100)); // should be 100% but it's weird
                break;
            case 2: case 14:
                yPosition = (yPosition - ((speed*42)/100)); // - 42%
                break;
            case 3: case 13:
                yPosition = (yPosition - ((speed*25)/100)); // - 25%
                break;
            case 5: case 11:
                yPosition = (yPosition + ((speed*25)/100)); // + 25%
                break;
            case 6: case 10:
                yPosition = (yPosition + ((speed*42)/100)); // + 42%
                break;
            case 7: case 8: case 9:
                yPosition = (yPosition + ((speed*50)/100)); // + 50%
                break;
            default:
                //4 and 12, no change in position
                break;
        }
        return yPosition;
    }

    public void renderTrack(Graphics g) {
        g.setColor(Color.black);
        g.drawRect(MIN_X_OUTER_EDGE, MIN_Y_OUTER_EDGE, MAX_X_OUTER_EDGE, MAX_Y_OUTER_EDGE);  // draw outer edge
        g.drawRect(MIN_X_INNER_EDGE, MIN_Y_INNER_EDGE, MAX_X_INNER_EDGE, MAX_Y_INNER_EDGE); // draw inner edge

        g.setColor(Color.gray);
        g.fillRect(50, 100, 750, 500); // fill in the road

        g.setColor(Color.yellow);
        g.drawRect(100, 150, 650, 400); // mid-lane marker

        g.setColor(Color.white);
        g.fillRect(417, 501, 9, 99); // draw start line

        // === draw checkerboard on start line ===
        g.setColor(Color.black);
        for(int i = 0; i<=99; i+=6) {
            g.fillRect(417, 501 + i, 3, 3);
            g.fillRect(423, 501 + i, 3, 3);
        }
        for(int i = 0; i<=93; i+=6) {
            g.fillRect(420, 504 + i, 3, 3);
        }
        // =======================================

        g.setColor(Color.green);
        g.fillRect(150, 200, 550, 300); // draw central grassed area
    }

    public void speedPlus(int player) {
        switch (player) {
            case 1:
                redKartSpeed += 10;
                break;
            case 2:
                blueKartSpeed += 10;
                break;
        }
    }

    public void speedMinus(int player) {
        switch (player) {
            case 1:
                redKartSpeed -= 10;
                break;
            case 2:
                blueKartSpeed -= 10;
                break;
        }

    }

    public void turnLeft(int player) {
        switch (player) {
            case 1:
                if (redKartSpeed != 0) {
                    if (redKartImageIndex == 0) {
                        // if the index is 0 then jump to the last image in the array (can't go negative index)
                        redKartImageIndex = NUMBER_OF_IMAGES - 1;
                    } else {
                        redKartImageIndex -= 1;
                    }
                }
                break;
            case 2:
                if (blueKartSpeed != 0) {
                    if (blueKartImageIndex == 0) {
                        blueKartImageIndex = NUMBER_OF_IMAGES - 1;
                    } else {
                        blueKartImageIndex -= 1;
                    }
                }
                break;
        }
    }

    public void turnRight(int player) {
        switch (player) {
            case 1:
                if (redKartSpeed != 0) {
                    redKartImageIndex = (redKartImageIndex + 1) % NUMBER_OF_IMAGES;
                }
                break;
            case 2:
                if (blueKartSpeed != 0) {
                    blueKartImageIndex = (blueKartImageIndex + 1) % NUMBER_OF_IMAGES;
                }
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }
}




