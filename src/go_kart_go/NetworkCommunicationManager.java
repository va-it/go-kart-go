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
        String serverResponse;
        tcpClientCommunicationSocket.sendMessage(Messages.sendingKartInfo);
        serverResponse = tcpClientCommunicationSocket.getMessage();
        if (!serverResponse.isBlank()) {
            if (serverResponse.equals(Messages.readyToReceiveKart)) {
                // all good, send the kart
                tcpClientCommunicationSocket.sendKart(kart);
                serverResponse = tcpClientCommunicationSocket.getMessage();
                if (!serverResponse.isBlank()) {
                    if (serverResponse.equals(Messages.kartInfoReceived)) {
                        // all good, kart received
                    }
                } else {
                    // something went wrong. Can't talk to server
                }
            }
        } else {
            // something went wrong. Can't talk to server
        }
    }

    public int getOpponentSpeed() {
        // udpClientCommunicationSocket.sendMessage(Messages.getOpponentSpeed);
        tcpClientCommunicationSocket.sendMessage(Messages.getOpponentSpeed);
        String opponentSpeed = tcpClientCommunicationSocket.getMessage();

        if (!opponentSpeed.isBlank()) {
            return Integer.parseInt(opponentSpeed);
        } else {
            // something went wrong. Can't talk to server
        }
        return 0;
    }

    public int getOpponentImageIndex() {
        tcpClientCommunicationSocket.sendMessage(Messages.getOpponentIndex);
        String opponentImageIndex = tcpClientCommunicationSocket.getMessage();

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
