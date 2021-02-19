package go_kart_go;

import go_kart_go_network.*;

public class NetworkCommunicationManager {

    public UDPCommunicationSocket udpCommunicationSocket;

    public TCPClientCommunicationSocket tcpClientCommunicationSocket;

    public NetworkCommunicationManager() {

        udpCommunicationSocket = new UDPCommunicationSocket();
//        PacketSender.sendPacket(
//                Messages.getPlayerNumber, ServerDetails.getAddress(), ServerDetails.port, udpCommunicationSocket.socket
//        );
//        // And listen for answer
//        String serverResponse = PacketReceiver.receivePacket(udpCommunicationSocket.socket);

        tcpClientCommunicationSocket = new TCPClientCommunicationSocket();
    }

    public String getMessage() {
        String serverResponse = null;
        // TCP
        serverResponse = tcpClientCommunicationSocket.getMessage();

        if (!serverResponse.isBlank()) {
            return serverResponse;
        } else {
            // something went wrong. Can't talk to server
        }
        return "";
    }

    public boolean connectToServer() {
        String serverResponse = null;

        tcpClientCommunicationSocket.sendMessage(Messages.establishConnection);
        serverResponse = tcpClientCommunicationSocket.getMessage();

        if (!serverResponse.isBlank()) {
            if (serverResponse.equals(Messages.connectionSuccessful)) {
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

        String serverResponse = null;

        // TCP
        tcpClientCommunicationSocket.sendMessage(Messages.getPlayerNumber);
        serverResponse = tcpClientCommunicationSocket.getMessage();

        if (!serverResponse.isBlank()) {
            return Integer.parseInt(serverResponse);
        } else {
            // something went wrong. Can't talk to server
        }
        return 0;
    }

    public void sendKartInfo(Kart kart) {

        String serverResponse = null;
        // TCP
        tcpClientCommunicationSocket.sendMessage(Messages.sendingKartInfo);
        tcpClientCommunicationSocket.sendKart(kart);
        serverResponse = tcpClientCommunicationSocket.getMessage();

        if (!serverResponse.isBlank()) {
            if (serverResponse.equals(Messages.kartInfoReceived)) {
                // if answer == connection successful then proceed
                System.out.println("Kart sent correctly and Server received it");
            }
        } else {
            // something went wrong. Can't talk to server
        }


//        // String message = "player:1;speed:10;index:10";
//        String message = Messages.kartInfo(kart.getPlayer(), kart.getSpeed(), kart.getImageIndex());
//        // send this player's kart info to server
//        PacketSender.sendPacket(message, ServerDetails.getAddress(), ServerDetails.port, udpCommunicationSocket.socket);
    }

    public int getOpponentSpeed(Kart kart) {
        String message = Messages.getOpponentSpeed(kart.getPlayer());
        PacketSender.sendPacket(message, ServerDetails.getAddress(), ServerDetails.port, udpCommunicationSocket.socket);

        String opponentSpeed = PacketReceiver.receivePacket(udpCommunicationSocket.socket);

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
        PacketSender.sendPacket(message, ServerDetails.getAddress(), ServerDetails.port, udpCommunicationSocket.socket);

        String opponentImageIndex = PacketReceiver.receivePacket(udpCommunicationSocket.socket);

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
        PacketSender.sendPacket(message, ServerDetails.getAddress(), ServerDetails.port, udpCommunicationSocket.socket);
    }

    public void sendStopRace() {
        String message = Messages.stopRace;
        PacketSender.sendPacket(message, ServerDetails.getAddress(), ServerDetails.port, udpCommunicationSocket.socket);
    }

}
