import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.io.File;

class MainWindowPanel extends JPanel implements ActionListener {

    private Timer animationTimer;
    private static final int delay = 100;
    private final static String redKartImageName = "kartRed"; // base image name
    private final static String blueKartImageName = "kartBlue";
    private int blueKartImageIndex = 12;
    private int redKartImageIndex = 12;
    private int blueKartXPosition = 425;
    private int blueKartYPosition = 500;
    private int redKartXPosition = 425;
    private int redKartYPosition = 550;
    protected int NUMBER_OF_IMAGES = 16;
    protected ImageIcon redKart[];
    protected ImageIcon blueKart[];

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

        for (int i = 0; i < NUMBER_OF_IMAGES; i++) {
            redKart[i] = new ImageIcon(
                    getClass().getResource("images" + File.separator + redKartImageName + i + ".png")
            );
            blueKart[i] = new ImageIcon(
                    getClass().getResource("images" + File.separator + blueKartImageName + i + ".png")
            );
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // call superclass paintComponent


        if (animationTimer.isRunning()) {
            // increase by 1 to show next image
            // % ensures that the first image is prepared when we display the last one
            //currentImage = (currentImage + 1) % NUMBER_OF_IMAGES;

            renderTrack(g);

            blueKart[blueKartImageIndex].paintIcon(this, g, blueKartXPosition, blueKartYPosition);

            redKart[redKartImageIndex].paintIcon(this, g, redKartXPosition, redKartYPosition);
        }

    }

    public void renderTrack(Graphics g) {
        Color black = Color.black;
        g.setColor(black);
        g.drawRect(50, 100, 750, 500);  // outer edge
        g.drawRect(150, 200, 550, 300); // inner edge

        Color gray = Color.gray;
        g.setColor(gray);
        g.fillRect(50, 100, 750, 500); // road

        Color yellow = Color.yellow;
        g.setColor(yellow);
        g.drawRect(100, 150, 650, 400); // mid-lane marker

        Color white = Color.white;
        g.setColor(white);
        g.fillRect(417, 501, 9, 99); // start line
        // === checkerboard ===
        g.setColor(black);
        for(int i = 0; i<=99; i+=6) {
            g.fillRect(417, 501 + i, 3, 3);
            g.fillRect(423, 501 + i, 3, 3);
        }
        for(int i = 0; i<=93; i+=6) {
            g.fillRect(420, 504 + i, 3, 3);
        }
        // ====================

        Color green = Color.green;
        g.setColor(green);
        g.fillRect(150, 200, 550, 300); // grass
    }

    public void speedPlus(int player) {
        switch (player) {
            case 1:
                redKartXPosition -= 10;
                break;
            case 2:
                blueKartXPosition -= 10;
                break;
        }
    }

    public void speedMinus(int player) {
        switch (player) {
            case 1:
                redKartXPosition += 10;
                break;
            case 2:
                blueKartXPosition += 10;
                break;
        }

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }
}




