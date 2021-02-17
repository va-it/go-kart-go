public class Main {

    private static int player;

    public static void main(String[] args) {

        NetworkCommunicationManager networkCommunicationManager = new NetworkCommunicationManager();

        if (networkCommunicationManager.connectToServer()) {
            player = networkCommunicationManager.determinePlayer();
            System.out.println(player);
            MainWindow mainWindow = new MainWindow(player, networkCommunicationManager);
            mainWindow.setVisible(true);
        } else {
            System.out.println("Connection error");
        }
    }
}
