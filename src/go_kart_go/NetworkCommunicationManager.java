package go_kart_go;

import go_kart_go_network.*;

public class NetworkCommunicationManager {

    public UDPClientCommunicationSocket udpClientCommunicationSocket;
    public TCPClientCommunicationSocket tcpClientCommunicationSocket;

    public NetworkCommunicationManager() {

        udpClientCommunicationSocket = new UDPClientCommunicationSocket();
        tcpClientCommunicationSocket = new TCPClientCommunicationSocket();
    }

    public int getPlayerNumber() {
        String serverResponse = null;
        tcpClientCommunicationSocket.sendMessage(Messages.getPlayerNumber);
        serverResponse = tcpClientCommunicationSocket.getMessage();

        if (!serverResponse.isBlank()) {
            return Integer.parseInt(serverResponse);
        } else {
            // something went wrong. Can't talk to server
        }
        return 0;
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
    }

    public void sendKartInfo(Kart kart) {
//        udpClientCommunicationSocket.sendMessage(Messages.sendingKartInfo);
//        // udpClientCommunicationSocket.sendMessage(Messages.sendingKartInfo);
//        // udpClientCommunicationSocket.sendKart(kart);
//        udpClientCommunicationSocket.sendKart(kart);
//        // server won't acknowledge so we don't listen for a response

        String serverResponse;
        tcpClientCommunicationSocket.sendMessage(Messages.sendingKartInfo);
        serverResponse = tcpClientCommunicationSocket.getMessage();
        if (!serverResponse.isBlank()) {
            if (serverResponse.equals(Messages.sendingKartInfo)) {
                // all good, send the kart
                tcpClientCommunicationSocket.sendKart(kart);
                serverResponse = tcpClientCommunicationSocket.getMessage();
            }
        } else {
            // something went wrong. Can't talk to server
        }
    }

    public int getOpponentSpeed() {
        udpClientCommunicationSocket.sendMessage(Messages.getOpponentSpeed);
        String opponentSpeed = udpClientCommunicationSocket.getMessage();

        if (!opponentSpeed.isBlank()) {
            return Integer.parseInt(opponentSpeed);
        } else {
            // something went wrong. Can't talk to server
        }
        return 0;
    }

    public int getOpponentImageIndex() {
        udpClientCommunicationSocket.sendMessage(Messages.getOpponentIndex);
        String opponentImageIndex = udpClientCommunicationSocket.getMessage();

        if (!opponentImageIndex.isBlank()) {
            return Integer.parseInt(opponentImageIndex);
        } else {
            // something went wrong. Can't talk to server
        }
        return 0;
    }

    public void sendStartRace() {
        tcpClientCommunicationSocket.sendMessage(Messages.startRace);
        String confirmation = tcpClientCommunicationSocket.getMessage();
        if (confirmation.isBlank()) {
            System.err.println("Cannot reach server");
        }
    }

    public void sendStopRace() {
        tcpClientCommunicationSocket.sendMessage(Messages.stopRace);
        String confirmation = tcpClientCommunicationSocket.getMessage();
        if (confirmation.isBlank()) {
            System.err.println("Cannot reach server");
        }
    }

}
