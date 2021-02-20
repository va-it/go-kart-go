package go_kart_go;

import go_kart_go_network.*;

public class NetworkCommunicationManager {

    public UDPClientCommunicationSocket udpClientCommunicationSocket;
    public TCPClientCommunicationSocket tcpClientCommunicationSocket;

    public NetworkCommunicationManager() {

        udpClientCommunicationSocket = new UDPClientCommunicationSocket();
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
                return true;
            }
        }
        // something went wrong. Can't talk to server
        return false;
    }

    public void sendKartInfo(Kart kart) {
        udpClientCommunicationSocket.sendMessage(Messages.sendingKartInfo);
        udpClientCommunicationSocket.sendKart(kart);
    }

    public int getOpponentSpeed() {
        udpClientCommunicationSocket.sendMessage(Messages.getOpponentSpeed);
        String opponentSpeed = udpClientCommunicationSocket.getMessage();

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
        udpClientCommunicationSocket.sendMessage(Messages.getOpponentIndex);
        String opponentImageIndex = udpClientCommunicationSocket.getMessage();

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
        udpClientCommunicationSocket.sendMessage(Messages.startRace);
    }

    public void sendStopRace() {
        udpClientCommunicationSocket.sendMessage(Messages.stopRace);
    }

}
