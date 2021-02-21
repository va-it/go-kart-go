package go_kart_go;

import go_kart_go_network.*;

public class NetworkCommunicationManager {

    public UDPClientCommunicationSocket udpClientCommunicationSocket;
    public TCPClient tcpClient;

    public NetworkCommunicationManager() {

        udpClientCommunicationSocket = new UDPClientCommunicationSocket();
        tcpClient = new TCPClient();
    }

    public int getPlayerNumber() {
        String serverResponse = null;
        tcpClient.sendRequest(Messages.getPlayerNumber);
        serverResponse = tcpClient.getResponse();

        if (!serverResponse.isBlank()) {
            return Integer.parseInt(serverResponse);
        } else {
            // something went wrong. Can't talk to server
        }
        return 0;
    }

    public boolean connectToServer() {
        String serverResponse = null;
        tcpClient.sendRequest(Messages.establishConnection);
        serverResponse = tcpClient.getResponse();

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
        tcpClient.sendRequest(Messages.sendingKartInfo);
        serverResponse = tcpClient.getResponse();
        if (!serverResponse.isBlank()) {
            if (serverResponse.equals(Messages.readyToReceiveKart)) {
                // all good, send the kart
                tcpClient.sendObject(kart);
                serverResponse = tcpClient.getResponse();
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
        tcpClient.sendRequest(Messages.getOpponentSpeed);
        String opponentSpeed = tcpClient.getResponse();

        if (!opponentSpeed.isBlank()) {
            return Integer.parseInt(opponentSpeed);
        } else {
            // something went wrong. Can't talk to server
        }
        return 0;
    }

    public int getOpponentImageIndex() {
        tcpClient.sendRequest(Messages.getOpponentIndex);
        String opponentImageIndex = tcpClient.getResponse();

        if (!opponentImageIndex.isBlank()) {
            return Integer.parseInt(opponentImageIndex);
        } else {
            // something went wrong. Can't talk to server
        }
        return 0;
    }

    public void sendStartRace() {
        tcpClient.sendRequest(Messages.startRace);
        String confirmation = tcpClient.getResponse();
        if (confirmation.isBlank()) {
            System.err.println("Cannot reach server");
        }
    }

    public void sendStopRace() {
        tcpClient.sendRequest(Messages.stopRace);
        String confirmation = tcpClient.getResponse();
        if (confirmation.isBlank()) {
            System.err.println("Cannot reach server");
        }
    }

}
