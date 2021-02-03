import java.awt.*;
import javax.swing.*;

class MainWindow extends JFrame {

    public static final int WIDTH = 850;
    public static final int HEIGHT = 650;

    private MainWindowPanel panel;

    public MainWindow() {
        // call constructor of JFrame
        super();

        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);

        Container pane = getContentPane();

        panel = new MainWindowPanel();

        panel.setLayout(null);

        pane.add(panel);

        this.setTitle("Go-Kart-Go");

        // ensure the program closes when this window is closed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public MainWindowPanel getPanel() {
        return panel;
    }
}