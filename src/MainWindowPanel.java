import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

class MainWindowPanel extends JPanel implements ActionListener {

    private final Timer animationTimer;
    private static final int delay = 75;

    private final int MIN_X_OUTER_EDGE = 50;
    private final int MAX_X_OUTER_EDGE = 750;
    private final int MIN_Y_OUTER_EDGE = 100;
    private final int MAX_Y_OUTER_EDGE = 500;

    private final int MIN_X_INNER_EDGE = 150;
    private final int MAX_X_INNER_EDGE = 550;
    private final int MIN_Y_INNER_EDGE = 200;
    private final int MAX_Y_INNER_EDGE = 300;

    JLabel redKartSpeedLabel = new JLabel();
    JLabel blueKartSpeedLabel = new JLabel();

    public Kart redKart;
    public Kart blueKart;

    public MainWindowPanel() {
        super();

        this.redKart = new Kart("red");
        this.blueKart = new Kart("blue");

        // set initial karts position to be just before the starting line
        this.redKart.setxPosition(425);
        this.redKart.setyPosition(500);
        // displace blue kart of 50 pixels (image size) down so it looks below the red kart (other lane)
        this.blueKart.setxPosition(425);
        this.blueKart.setyPosition(550);


        // create swing timer with 100ms delay and start it
        animationTimer = new Timer(delay, this);
        animationTimer.start();

        this.setBounds(0, 0, MainWindow.WIDTH, MainWindow.HEIGHT);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // call superclass paintComponent

        if (animationTimer.isRunning()) {

            renderTrack(g);

            blueKart.setNextXPosition();
            blueKart.setNextYPosition();

            blueKart.getImageAtCurrentIndex().paintIcon(this, g, blueKart.getxPosition(), blueKart.getyPosition());

            redKart.setNextXPosition();
            redKart.setNextYPosition();

            redKart.getImageAtCurrentIndex().paintIcon(this, g, redKart.getxPosition(), redKart.getyPosition());

            printSpeedInformation();
        }
    }

    public void printSpeedInformation() {
        redKartSpeedLabel.setText("Red speed: " + redKart.getSpeed());
        blueKartSpeedLabel.setText("Blue speed: " + blueKart.getSpeed());

        redKartSpeedLabel.setBounds(50, 50, 100, 30);
        blueKartSpeedLabel.setBounds(200, 50, 100, 30);

        add(redKartSpeedLabel);
        add(blueKartSpeedLabel);
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

    @Override
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }
}




