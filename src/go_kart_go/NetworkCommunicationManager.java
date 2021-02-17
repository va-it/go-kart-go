package go_kart_go;

import go_kart_go.Kart;
import go_kart_go_network.*;

public class NetworkCommunicationManager {
    public CommunicationSocket communicationSocket;

    public NetworkCommunicationManager() {
        communicationSocket = new CommunicationSocket();
    }

    public boolean connectToServer() {
        //  here we try to connect to the game server

        // sendPacket with "establish connection" message.
        PacketSender.sendPacket(
                Messages.establishConnection, ServerDetails.getAddress(), ServerDetails.port, communicationSocket.socket
        );

        // listen for answer from server (receivePacket)
        String serverResponse = PacketReceiver.receivePacket(communicationSocket.socket);

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

    public int determinePlayer() {
        // the idea is that the 1st person to connect to the server gets to be Player 1

        // so here we need to check if there is a client already connected to the server
        // if so then this user becomes player 2
        // otherwise this user becomes player 1

        // the logic above needs to be handled by server

        // Ask the server what player to be
        PacketSender.sendPacket(
                Messages.getPlayerNumber, ServerDetails.getAddress(), ServerDetails.port, communicationSocket.socket
        );
        // And listen for answer
        String serverResponse = PacketReceiver.receivePacket(communicationSocket.socket);

        if (!serverResponse.isBlank()) {
            return Integer.parseInt(serverResponse);
        } else {
            // something went wrong. Can't talk to server
        }
        return 0;
    }

    public void sendKartInfo(Kart kart) {
        // String message = "player:1;speed:10;index:10";
        String message = Messages.kartInfo(kart.getPlayer(), kart.getSpeed(), kart.getImageIndex());
        // send this player's kart info to server
        PacketSender.sendPacket(message, ServerDetails.getAddress(), ServerDetails.port, communicationSocket.socket);
    }

    public int getOpponentSpeed(Kart kart) {
        String message = Messages.getOpponentSpeed(kart.getPlayer());
        PacketSender.sendPacket(message, ServerDetails.getAddress(), ServerDetails.port, communicationSocket.socket);

        String opponentSpeed = PacketReceiver.receivePacket(communicationSocket.socket);

        if (!opponentSpeed.isBlank()) {
            // ideally the answer is something like: player_X_speed:XX
            // and we parse it here?
            return Integer.parseInt(opponentSpeed);
        } else {
            // something went wrong. Can't talk to server
        }
        return 0;
    }

    public int getOpponentImageIndex(Kart kart) {
        String message = Messages.getOpponentIndex(kart.getPlayer());
        PacketSender.sendPacket(message, ServerDetails.getAddress(), ServerDetails.port, communicationSocket.socket);

        String opponentImageIndex = PacketReceiver.receivePacket(communicationSocket.socket);

        if (!opponentImageIndex.isBlank()) {
            // ideally the answer is something like: player_X_index:XX
            // and we parse it here?
            return Integer.parseInt(opponentImageIndex);
        } else {
            // something went wrong. Can't talk to server
        }
        return 0;
    }

    public void sendStartRace() {
        String message = Messages.startRace;
        PacketSender.sendPacket(message, ServerDetails.getAddress(), ServerDetails.port, communicationSocket.socket);
    }

    public void sendStopRace() {
        String message = Messages.stopRace;
        PacketSender.sendPacket(message, ServerDetails.getAddress(), ServerDetails.port, communicationSocket.socket);
    }

}
