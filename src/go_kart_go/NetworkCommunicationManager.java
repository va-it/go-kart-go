package go_kart_go;

import go_kart_go_network.*;

public class NetworkCommunicationManager {

    public UDPClientCommunicationSocket udpClientCommunicationSocket;
    public TCPClientCommunicationSocket tcpClientCommunicationSocket;
    PacketReceiver packetReceiver;

    public NetworkCommunicationManager() {

        udpClientCommunicationSocket = new UDPClientCommunicationSocket();
        tcpClientCommunicationSocket = new TCPClientCommunicationSocket();
        packetReceiver = new PacketReceiver();
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
                return true;
            }
        }
        // something went wrong. Can't talk to server
        return false;

//        String message = Messages.establishConnection;
//        PacketSender.sendPacket(message, ServerDetails.getAddress(), ServerDetails.port, udpClientCommunicationSocket.socket);
//        serverResponse = packetReceiver.receivePacket(udpClientCommunicationSocket.socket);
//        if (!serverResponse.isBlank()) {
//            if (serverResponse.equals(Messages.connectionSuccessful)) {
//                return true;
//            }
//        }
//        // something went wrong. Can't talk to server
//        return false;

    }

    public void sendKartInfo(Kart kart) {
        String serverResponse = null;
//        tcpClientCommunicationSocket.sendMessage(Messages.sendingKartInfo);
//        tcpClientCommunicationSocket.sendKart(kart);
//        serverResponse = tcpClientCommunicationSocket.getMessage();
//
//        if (!serverResponse.isBlank()) {
//            if (serverResponse.equals(Messages.kartInfoReceived)) {
//                // if answer == connection successful then proceed
//                System.out.println("Kart sent correctly and Server received it");
//            }
//        } else {
//            // something went wrong. Can't talk to server
//        }
        String message = Messages.sendingKartInfo;
        PacketSender.sendPacket(message, ServerDetails.getAddress(), ServerDetails.port, udpClientCommunicationSocket.socket);
        udpClientCommunicationSocket.sendKart(kart, ServerDetails.getAddress(), ServerDetails.port);

    }

    public int getOpponentSpeed() {
        String message = Messages.getOpponentSpeed;
        PacketSender.sendPacket(message, ServerDetails.getAddress(), ServerDetails.port, udpClientCommunicationSocket.socket);

        String opponentSpeed = packetReceiver.receivePacket(udpClientCommunicationSocket.socket);

        if (!opponentSpeed.isBlank()) {
            // ideally the answer is something like: player_X_speed:XX
            // and we parse it here?
            return Integer.parseInt(opponentSpeed);
        } else {
            // something went wrong. Can't talk to server
        }
        return 0;
    }

    public int getOpponentImageIndex() {
        String message = Messages.getOpponentIndex;
        PacketSender.sendPacket(message, ServerDetails.getAddress(), ServerDetails.port, udpClientCommunicationSocket.socket);

        String opponentImageIndex = packetReceiver.receivePacket(udpClientCommunicationSocket.socket);

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
        PacketSender.sendPacket(message, ServerDetails.getAddress(), ServerDetails.port, udpClientCommunicationSocket.socket);
    }

    public void sendStopRace() {
        String message = Messages.stopRace;
        PacketSender.sendPacket(message, ServerDetails.getAddress(), ServerDetails.port, udpClientCommunicationSocket.socket);
    }

}
