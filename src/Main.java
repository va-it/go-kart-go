public class Main {

    private static int player;

    public static void main(String[] args) {
        boolean connected = NetworkCommunicationManager.connectToServer();

        if (connected) {
            player = NetworkCommunicationManager.determinePlayer();

            MainWindow mainWindow = new MainWindow(player);
            mainWindow.setVisible(true);
        } else {
            System.out.println("Connection error");
        }
    }
}
