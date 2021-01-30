import java.awt.*;
import javax.swing.*;

class MainWindow extends JFrame {

    public static final int WIDTH = 850;
    public static final int HEIGHT = 650;

    public MainWindow() {
        // call constructor of JFrame
        super();

        Container pane = getContentPane();

        MainWindowPanel panel = new MainWindowPanel();

        pane.add(panel);

        // x, y (form top-left), width, height
        this.setBounds(100, 100, WIDTH, HEIGHT);

        // ensure the program closes when this window is closed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}