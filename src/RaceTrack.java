import javax.swing.*;
import java.awt.*;
import java.io.File;

public class RaceTrack {

    public JLabel redKartSpeedLabel = new JLabel();
    public JLabel blueKartSpeedLabel = new JLabel();
    public JLabel redKartSpeed = new JLabel();
    public JLabel blueKartSpeed = new JLabel();

    public static final int OUTER_LEFT_EDGE = 50;
    public static final int OUTER_RIGHT_EDGE = 800;
    public static final int OUTER_TOP_EDGE = 75; //100
    public static final int OUTER_BOTTOM_EDGE = 575; //600

    public static final int INNER_LEFT_EDGE = 150;
    public static final int INNER_RIGHT_EDGE = 700;
    public static final int INNER_TOP_EDGE = 175; //200
    public static final int INNER_BOTTOM_EDGE = 475; //500

    public static final int WIDTH_OF_GREEN_AREA = (INNER_RIGHT_EDGE - INNER_LEFT_EDGE);
    public static final int WIDTH_OF_TRACK = (OUTER_RIGHT_EDGE - OUTER_LEFT_EDGE);
    public static final int HEIGHT_OF_TRACK = (OUTER_BOTTOM_EDGE - OUTER_TOP_EDGE);
    public static final int SUM_OF_LEFT_EDGES = (OUTER_LEFT_EDGE + INNER_LEFT_EDGE);
    public static final int SUM_OF_TOP_EDGES = (OUTER_TOP_EDGE + INNER_TOP_EDGE);
    public static final int WIDTH_MINUS_RIGHT_ROAD = (INNER_RIGHT_EDGE - OUTER_LEFT_EDGE);
    public static final int HEIGHT_MINUS_BOTTOM_ROAD = (INNER_BOTTOM_EDGE - OUTER_TOP_EDGE);


    public static final int START_LINE_WIDTH = 9;
    public static final int START_LINE_LEFT_EDGE = ((INNER_RIGHT_EDGE + INNER_LEFT_EDGE) / 2) - START_LINE_WIDTH;
    public static final int START_LINE_RIGHT_EDGE = START_LINE_LEFT_EDGE + START_LINE_WIDTH;

    public JLabel raceLightsLabel = new JLabel();
    public ImageIcon raceLights;

    public SoundsManager countDownSoundManager;

    public RaceTrack() {
        // Labels for karts' speed
        Font font = new Font("Sans-Serif", Font.BOLD, 25);
        redKartSpeedLabel.setText("Red speed: ");
        redKartSpeedLabel.setFont(font);
        blueKartSpeedLabel.setText("Blue speed: ");
        redKartSpeedLabel.setBounds(OUTER_LEFT_EDGE, 0, 150, 50);
        blueKartSpeedLabel.setFont(font);
        blueKartSpeedLabel.setBounds(OUTER_RIGHT_EDGE - 200, 0, 160, 50);

        // Labels that will contain the actual speed value
        redKartSpeed.setFont(font);
        blueKartSpeed.setFont(font);
        redKartSpeed.setBounds(OUTER_LEFT_EDGE + 150, 0, 40, 50);
        blueKartSpeed.setBounds(OUTER_RIGHT_EDGE - 40, 0, 40, 50);
    }

    public void renderTrack(Graphics g) {
        g.setColor(Color.black);
        // draw outer edge
        g.drawRect(OUTER_LEFT_EDGE, OUTER_TOP_EDGE, WIDTH_OF_TRACK, HEIGHT_OF_TRACK);
        // draw inner edge
        g.drawRect(INNER_LEFT_EDGE, INNER_TOP_EDGE, WIDTH_OF_GREEN_AREA, (INNER_BOTTOM_EDGE - INNER_TOP_EDGE));

        g.setColor(Color.gray);
        // fill in the road
        g.fillRect(OUTER_LEFT_EDGE, OUTER_TOP_EDGE, WIDTH_OF_TRACK, (HEIGHT_OF_TRACK));

        g.setColor(Color.yellow);

        // mid-lane marker
        g.drawRect(SUM_OF_LEFT_EDGES / 2, SUM_OF_TOP_EDGES / 2, WIDTH_MINUS_RIGHT_ROAD, HEIGHT_MINUS_BOTTOM_ROAD);

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
        g.fillRect(INNER_LEFT_EDGE, INNER_TOP_EDGE, WIDTH_OF_GREEN_AREA, (INNER_BOTTOM_EDGE - INNER_TOP_EDGE));
    }

    public void setUpRaceLights() {
        if (raceLights != null) {
            int width = raceLights.getIconWidth();
            int height = raceLights.getIconHeight();
            // center vertically
            int yPosition = ((INNER_BOTTOM_EDGE + INNER_TOP_EDGE) / 2) - (height / 2);
            raceLightsLabel.setBounds(START_LINE_LEFT_EDGE - (width / 2), yPosition, width, height);
        }
    }

    public void updateSpeedInformation(Kart redKart, Kart blueKart) {
        redKartSpeed.setText(String.valueOf(redKart.getSpeed()));
        blueKartSpeed.setText(String.valueOf(blueKart.getSpeed()));
    }

    public void setRaceLightsImage(int index) {
        raceLights = new ImageIcon(getClass().getResource("images" + File.separator + "lights" + index + ".png"));
        raceLightsLabel.setIcon(raceLights);
    }

    public boolean detectCollision(Kart kart) {
        // store values in variables to avoid having to call the getters multiple times
        int xPosition = kart.getxPosition();
        int yPosition = kart.getyPosition();

        // shave 10 pixels from inner edges to avoid crashing easily
        // since the kart doesn't fill all 50 pixels and it's easy to crash because of that
        int inner_top_edge = INNER_TOP_EDGE + 10;
        int inner_bottom_edge = INNER_BOTTOM_EDGE - 10;
        int inner_left_edge = INNER_LEFT_EDGE + 10;
        int inner_right_edge = INNER_RIGHT_EDGE - 10;

        boolean crashedIntoXOuterEdges = xPosition < OUTER_LEFT_EDGE || xPosition > OUTER_RIGHT_EDGE;
        boolean crashedIntoYOuterEdges = yPosition < OUTER_TOP_EDGE || yPosition > (OUTER_BOTTOM_EDGE - kart.IMAGE_SIZE);
        boolean betweenInnerYEdges = yPosition > inner_top_edge && yPosition < inner_bottom_edge;
        boolean betweenInnerXEdges = xPosition > inner_left_edge && xPosition < inner_right_edge;

        // only check collisions when kart is moving
        if (kart.getSpeed() != 0) {

            if (crashedIntoXOuterEdges) {
                // crashed into left or right outer edges
                return true;
            }
            if (crashedIntoYOuterEdges) {
                // subtract image size otherwise the condition only applies when the whole image is below the point
                // crashed into top or bottom outer edges
                return true;
            }
            if (betweenInnerYEdges && (xPosition + kart.IMAGE_SIZE) > inner_left_edge && xPosition < inner_right_edge) {
                // crashed into inner left edge
                // add image size otherwise the condition only applies when the whole kart has gone over the edge
                return true;
            }
            if (betweenInnerXEdges && yPosition > (inner_top_edge - kart.IMAGE_SIZE) && yPosition < inner_bottom_edge) {
                // subtract image size otherwise the condition only applies when the whole image is below the point
                // crashed into top inner edge
                return true;
            }
            if (betweenInnerYEdges && xPosition < inner_right_edge && xPosition > inner_left_edge) {
                // crashed into inner right edge
                return true;
            }
            if (betweenInnerXEdges && yPosition < inner_bottom_edge && yPosition > inner_top_edge) {
                // crashed into inner bottom edge
                return true;
            }
        }
        return false;
    }

    public void detectCollisionWithTrack(Kart kart) {
        if (detectCollision(kart)) {
            SoundsManager soundsManager = new SoundsManager("accident");
            soundsManager.playSound();
            kart.stop();
        }
    }
}
