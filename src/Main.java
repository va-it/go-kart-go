public class Main {

    public static void main(String[] args) {

        System.out.println("Application started");

        MainWindow mainWindow = new MainWindow();
        MyKeyListener keyListener = new MyKeyListener(mainWindow.getPanel());

        // instruct the JFrame (MainWindow) to listen for key events
        mainWindow.addKeyListener(keyListener);

        SoundsMnager soundsMnager = new SoundsMnager();

        soundsMnager.playSound();

        mainWindow.setVisible(true);


    }
}
