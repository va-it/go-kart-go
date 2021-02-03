import java.awt.*;
import javax.swing.*;

class MainWindow extends JFrame {

    public static final int WIDTH = 850;
    public static final int HEIGHT = 650;

    private MainWindowPanel panel;

    public MainWindow() {
        // call constructor of JFrame
        super();

        Container pane = getContentPane();

        panel = new MainWindowPanel();

        pane.add(panel);

        this.setTitle("Go-Kart-Go");
        // x, y (form top-left), width, height
        this.setBounds(0, 0, WIDTH, HEIGHT);

        // ensure the program closes when this window is closed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public MainWindowPanel getPanel() {
        return panel;
    }
}