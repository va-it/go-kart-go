import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import java.io.File;


class MainWindowPanel extends JPanel implements ActionListener {

    private static final int delay = 100;

    private final static String redKartImageName = "kartRed"; // base image name
    private final static String blueKartImageName = "kartBlue";
    protected ImageIcon redKart[];
    protected ImageIcon blueKart[];
    protected int NUMBER_OF_IMAGES = 16;

    public MainWindowPanel() {
        super();

        // load images into array
        loadImages();

        // create swing timer with 100ms delay and start it
        new Timer(delay, this).start();

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

    @Override
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }
}




