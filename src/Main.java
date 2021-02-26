import go_kart_go.MainWindow;
import go_kart_go.NetComManager;

public class Main {

    private static int player;

    public static void main(String[] args) {

        NetComManager netComManager = new NetComManager();

        try {
            netComManager.connectToServer();
            // ask server for the player number
            player = netComManager.getPlayerNumber();
            if (player == 1 || player == 2) {
                MainWindow mainWindow = new MainWindow(player, netComManager);
                mainWindow.setVisible(true);
            } else {
                System.err.println("Server cannot determine player number");
            }
        } catch (NullPointerException e) {
            System.err.println("Cannot reach the server");
        }
    }
}
