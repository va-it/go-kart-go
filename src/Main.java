import go_kart_go.MainWindow;
import go_kart_go.NetworkCommunicationManager;

public class Main {

    private static int player;

    public static void main(String[] args) {

        NetworkCommunicationManager networkCommunicationManager = new NetworkCommunicationManager();

        if (networkCommunicationManager.connectToServer()) {
            // ask server for the player number
            player = networkCommunicationManager.getPlayerNumber();
            if (player == 1 || player == 2) {
                MainWindow mainWindow = new MainWindow(player, networkCommunicationManager);
                mainWindow.setVisible(true);
            } else {
                System.err.println("Something went wrong");
            }
        } else {
            System.err.println("Connection error");
        }
    }
}
