import go_kart_go.MainWindow;
import go_kart_go.NetworkCommunicationManager;
import go_kart_go_network.PacketSender;
import go_kart_go_network.ServerDetails;
import go_kart_go_network.UDPCommunicationSocket;

public class Main {

    private static int player;

    public static void main(String[] args) {

        NetworkCommunicationManager networkCommunicationManager = new NetworkCommunicationManager();

        if (networkCommunicationManager.connectToServer()) {
            // the server will respond with the player number
            player = Integer.parseInt(networkCommunicationManager.getMessage());
            System.out.println(player);
            MainWindow mainWindow = new MainWindow(player, networkCommunicationManager);
            mainWindow.setVisible(true);
        } else {
            System.out.println("Connection error");
        }
    }
}
