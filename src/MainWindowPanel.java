import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.io.File;

class MainWindowPanel extends JPanel implements ActionListener {

    private final Timer animationTimer;
    private static final int delay = 100;
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

            blueKart[blueKartImageIndex].paintIcon(this, g,blueKartXPosition, blueKartYPosition);

            redKartXPosition = getXPosition(redKartImageIndex, redKartXPosition, redKartSpeed);
            redKartYPosition = getYPosition(redKartImageIndex, redKartYPosition, redKartSpeed);

            redKart[redKartImageIndex].paintIcon(this, g, redKartXPosition, redKartYPosition);
        }

    }

    public int getXPosition(int imageIndex, int xPosition, int speed) {
        if (imageIndex == 0 || imageIndex == 8) {
            xPosition = xPosition;
        } else {
            if (imageIndex == 4 || (imageIndex >= 1 && imageIndex <= 3) || (imageIndex >= 5 && imageIndex <= 7) ){
                xPosition = xPosition + (speed/3);
            } else {
                xPosition = xPosition - (speed/3);
            }
        }
        return xPosition;
    }

    public int getYPosition(int imageIndex, int yPosition, int speed) {
        switch (imageIndex) {
            case 0:
                yPosition = yPosition - speed; // + 100%
                break;
            case 1:
                yPosition = (yPosition - ((speed*25)/100)); // + 25%
                break;
            case 2:
                yPosition = (yPosition - ((speed*50)/100)); // + 50%
                break;
            case 3:
                yPosition = (yPosition - ((speed*25)/100)); // + 25%
                break;
            case 4:
                yPosition = yPosition;
                break;
            case 5:
                yPosition = (yPosition + ((speed*25)/100)); // - 25%
                break;
            case 6:
                yPosition = (yPosition + ((speed*50)/100)); // - 50%
                break;
            case 7:
                yPosition = (yPosition + ((speed*75)/100)); // - 75%
                break;
            case 8:
                yPosition = yPosition + speed; // - 100%
                break;
            case 9:
                yPosition = (yPosition + ((speed*75)/100)); // - 75%
                break;
            case 10:
                yPosition = (yPosition + ((speed*50)/100)); // - 50%
                break;
            case 11:
                yPosition = (yPosition + ((speed*25)/100)); // - 25%
                break;
            case 12:
                yPosition = yPosition;
                break;
            case 13:
                yPosition = (yPosition - ((speed*25)/100)); // + 25%
                break;
            case 14:
                yPosition = (yPosition - ((speed*50)/100)); // + 50%
                break;
            case 15:
                yPosition = (yPosition - ((speed*75)/100)); // + 75%
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
                if (redKartImageIndex == 0) {
                    // if the index is 0 then jump to the last image in the array (can't go negative index)
                    redKartImageIndex = NUMBER_OF_IMAGES - 1;
                } else {
                    redKartImageIndex -= 1;
                }
                break;
            case 2:
                if (blueKartImageIndex == 0) {
                    blueKartImageIndex = NUMBER_OF_IMAGES - 1;
                } else {
                    blueKartImageIndex -= 1;
                }
                break;
        }
    }

    public void turnRight(int player) {
        switch (player) {
            case 1:
                redKartImageIndex = (redKartImageIndex + 1) % NUMBER_OF_IMAGES;
                break;
            case 2:
                blueKartImageIndex = (blueKartImageIndex + 1) % NUMBER_OF_IMAGES;
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }
}




