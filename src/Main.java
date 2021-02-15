import go_kart_go_network.*;

public class Main {

    private static int player;

    public static void main(String[] args) {
        boolean connected = connectToServer();

        if (connected) {

            determinePlayer();

            MainWindow mainWindow = new MainWindow(player);
            mainWindow.setVisible(true);
        } else {
            System.out.println("Connection error");
        }
    }

    private static boolean connectToServer() {
        //  here we try to connect to the game server

        // sendPacket with "establish connection" message.
        PacketSender.sendPacket("establish_connection", Server.address, Server.port);

        // listen for answer from server (receivePacket)
        String serverResponse = PacketReceiver.receivePacket();

        if (!serverResponse.isBlank()) {
            if (serverResponse == "connection_successful") {
                // if answer == connection successful then proceed
                return true;
            }
        } else {
            // something went wrong. Can't talk to server
        }

        return false;
    }

    private static void determinePlayer() {
        // the idea is that the 1st person to connect to the server gets to be Player 1

        // so here we need to check if there is a client already connected to the server
        // if so then this user becomes player 2
        // otherwise this user becomes player 1

        // the logic above needs to be handled by server

        // Ask the server what player to be
        PacketSender.sendPacket("get_player", Server.address, Server.port);
        // And listen for answer
        String serverResponse = PacketReceiver.receivePacket();

        if (!serverResponse.isBlank()) {
            player = Integer.parseInt(serverResponse);
        } else {
            // something went wrong. Can't talk to server
        }

    }
}
