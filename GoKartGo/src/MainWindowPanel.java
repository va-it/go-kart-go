import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.ImageIcon;

class MainWindowPanel extends JPanel {

    private static final int delay = 100;

    private final static String redKartImageName = "redKart"; // base image name
    private final static String blueKartImageName = "blueKart"; // base image name
    protected ImageIcon redKart[];
    protected ImageIcon blueKart[];
    protected int NUMBER_OF_IMAGES = 16;

    public MainWindowPanel() {
        super();
        
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                repaint();
            }
        };

        // load images into array
        loadImages();

        // create swing timer with 100ms delay and start it
        new Timer(delay, taskPerformer).start();
    }

    public void loadImages() {
        redKart = new ImageIcon[NUMBER_OF_IMAGES];
        blueKart = new ImageIcon[NUMBER_OF_IMAGES];
    
        for ( int i = 0; i < NUMBER_OF_IMAGES; i++ ) {
            redKart[i] = new ImageIcon(getClass().getResource("images/" + redKartImageName + i + ".png" ));
            blueKart[i] = new ImageIcon(getClass().getResource("images/" + blueKartImageName + i + ".png" ));
        }
    }
}




