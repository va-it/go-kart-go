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
        // server won't acknowledge so we don't listen for a response
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
        // this maybe it's best sent through TCP?
        udpClientCommunicationSocket.sendMessage(Messages.startRace);
    }

    public void sendStopRace() {
        // this maybe it's best sent through TCP?
        udpClientCommunicationSocket.sendMessage(Messages.stopRace);
    }

}
