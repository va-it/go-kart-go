import go_kart_go_network.Messages;
import go_kart_go_network.PacketReceiver;
import go_kart_go_network.PacketSender;
import go_kart_go_network.Server;

public class NetworkCommunicationManager {

    public static boolean connectToServer() {
        //  here we try to connect to the game server

        // sendPacket with "establish connection" message.
        PacketSender.sendPacket(Messages.establishConnection, Server.address, Server.port);

        // listen for answer from server (receivePacket)
        String serverResponse = PacketReceiver.receivePacket();

        if (!serverResponse.isBlank()) {
            if (serverResponse == Messages.connectionSuccessful) {
                // if answer == connection successful then proceed
                return true;
            }
        } else {
            // something went wrong. Can't talk to server
        }

        return false;
    }

    public static int determinePlayer() {
        // the idea is that the 1st person to connect to the server gets to be Player 1

        // so here we need to check if there is a client already connected to the server
        // if so then this user becomes player 2
        // otherwise this user becomes player 1

        // the logic above needs to be handled by server

        // Ask the server what player to be
        PacketSender.sendPacket(Messages.getPlayerNumber, Server.address, Server.port);
        // And listen for answer
        String serverResponse = PacketReceiver.receivePacket();

        if (!serverResponse.isBlank()) {
            return Integer.parseInt(serverResponse);
        } else {
            // something went wrong. Can't talk to server
        }
        return 0;
    }

    public static void sendKartInfo(Kart kart) {
        // String message = "player:1;speed:10;index:10";
        String message = Messages.kartInfo(kart.getPlayer(), kart.getSpeed(), kart.getImageIndex());
        // send this player's kart info to server
        PacketSender.sendPacket(message, Server.address, Server.port);
    }

    public static int getOpponentSpeed(Kart kart) {
        String message = Messages.getOpponentSpeed(kart.getPlayer());
        PacketSender.sendPacket(message, Server.address, Server.port);

        String opponentSpeed = PacketReceiver.receivePacket();

        if (!opponentSpeed.isBlank()) {
            // ideally the answer is something like: player_X_speed:XX
            // and we parse it here?
            return Integer.parseInt(opponentSpeed);
        } else {
            // something went wrong. Can't talk to server
        }
        return 0;
    }

    public static int getOpponentImageIndex(Kart kart) {
        String message = Messages.getOpponentIndex(kart.getPlayer());
        PacketSender.sendPacket(message, Server.address, Server.port);

        String opponentImageIndex = PacketReceiver.receivePacket();

        if (!opponentImageIndex.isBlank()) {
            // ideally the answer is something like: player_X_index:XX
            // and we parse it here?
            return Integer.parseInt(opponentImageIndex);
        } else {
            // something went wrong. Can't talk to server
        }
        return 0;
    }

    public static void sendStartRace() {
        String message = Messages.startRace;
        PacketSender.sendPacket(message, Server.address, Server.port);
    }

    public static void sendStopRace() {
        String message = Messages.stopRace;
        PacketSender.sendPacket(message, Server.address, Server.port);
    }

}
