public class Main {

    private static int player;

    public static void main(String[] args) {

        connectToServer();

        MainWindow mainWindow = new MainWindow(player);
        mainWindow.setVisible(true);
    }

    private static void connectToServer() {
        //  here we try to connect to the game server

        // sendPacket with "establish connection" message.
        // listen for answer from server (receivePacket)

        // if answer == connection successful then proceed

        // Once a connection is established we determine which player we are
        determinePlayer();
    }

    private static void determinePlayer() {
        // the idea is that the 1st person to connect to the server gets to be Player 1

        // so here we need to check if there is a client already connected to the server
        // if so then this user becomes player 2
        // otherwise this user becomes player 1

        // the logic above needs to be handled by server

        player = 1;
    }
}
