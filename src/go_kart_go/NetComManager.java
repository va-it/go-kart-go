package go_kart_go;

import go_kart_go_network.*;

public class NetComManager {

    public UDPClientSocket udpClientSocket;
    public TCPClient tcpClient;

    public NetComManager() {
        udpClientSocket = new UDPClientSocket();
        tcpClient = new TCPClient();
    }

    public int getPlayerNumber() {
        String serverResponse = null;
        tcpClient.sendRequest(Messages.getPlayerNumber);
        serverResponse = tcpClient.getResponse();

        if (!serverResponse.isBlank()) {
            return Integer.parseInt(serverResponse);
        } else {
            System.err.println("Cannot reach server");
        }
        return 0;
    }

    public boolean connectToServer() {
        // can improve this by putting a timeout maybe
        // try for 5 seconds, then give up and print message?
        String serverResponse = null;
        tcpClient.sendRequest(Messages.establishConnection);
        serverResponse = tcpClient.getResponse();

        if (!serverResponse.isBlank()) {
            if (serverResponse.equals(Messages.connectionSuccessful)) {
                return true;
            }
        } else {
            System.err.println("Cannot reach server");
        }
        return false;
    }

    public void sendKartInfo(Kart kart) {
        udpClientSocket.sendMessage(Messages.sendingKartInfo);
        udpClientSocket.sendKart(kart);
    }

    public Kart getOpponentKartInfo(int player) {
        udpClientSocket.sendMessage(Messages.getOpponentKartInfo(player));
        try {
            return (Kart) udpClientSocket.getKart();
        } catch (ClassCastException e) {
            System.err.println("Error casting object received from server: " + e);
        }
        return null;
    }

    public void sendReady() {
        tcpClient.sendRequest(Messages.ready);
        String confirmation = tcpClient.getResponse();
        if (!confirmation.isBlank() || !confirmation.equals(Messages.readyReceived)) {
            System.err.println("Cannot reach server");
        }
    }

    public String requestToStart() {
        tcpClient.sendRequest(Messages.requestToStart);
        String confirmation = tcpClient.getResponse();
        if (confirmation.isBlank()) {
            System.err.println("Cannot reach server");
        } else {
            return confirmation;
        }
        return Messages.error;
    }

    public void sendStopRace() {
        tcpClient.sendRequest(Messages.stopRace);
        String confirmation = tcpClient.getResponse();
        if (confirmation.isBlank()) {
            System.err.println("Cannot reach server");
        }
    }

    public String getOpponentConnectionStatus() {
        tcpClient.sendRequest(Messages.checkOpponentConnection);
        String confirmation = tcpClient.getResponse();
        if (confirmation.isBlank()) {
            System.err.println("Cannot reach server");
            return Messages.error;
        } else {
            return confirmation;
        }
    }

    public String getRaceStatus() {
        tcpClient.sendRequest(Messages.requestRaceStatus);
        String status = tcpClient.getResponse();
        if (status.isBlank()) {
            System.err.println("Cannot reach server");
            return Messages.error;
        } else {
            return status;
        }
    }

}
