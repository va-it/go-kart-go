public class Main {

    private static int player;

    public static void main(String[] args) {

        determinePlayer();

        MainWindow mainWindow = new MainWindow(player);
        mainWindow.setVisible(true);
    }

    private static void determinePlayer() {
        // the idea is that the 1st person to connect to the server gets to be Player 1

        // so here we need to check if there is a client already connected to the server
        // if so then this user becomes player 2
        // otherwise this user becomes player 1
        player = 1;
    }
}
