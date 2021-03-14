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
            // something went wrong. Can't talk to server
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
        }
        // something went wrong. Can't talk to server
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
        if (!confirmation.isBlank()) {
            if (confirmation.equals(Messages.readyReceived)) {
                // all good
            }
        } else {
            System.err.println("Cannot reach server");
        }
    }

    public boolean requestToStart() {
        tcpClient.sendRequest(Messages.requestToStart);
        String confirmation = tcpClient.getResponse();
        if (confirmation.isBlank()) {
            System.err.println("Cannot reach server");
        } else {
            if (confirmation.equals(Messages.startRace)) {
                return true;
            } else {
                // need to wait
                if (confirmation.equals(Messages.wait)) {
                    System.out.println("waiting");
                }
            }
        }
        return false;
    }

    public void sendStopRace() {
        tcpClient.sendRequest(Messages.stopRace);
        String confirmation = tcpClient.getResponse();
        if (confirmation.isBlank()) {
            System.err.println("Cannot reach server");
        }
    }

    public boolean isOpponentConnected() {
        tcpClient.sendRequest(Messages.checkOpponentConnection);
        String confirmation = tcpClient.getResponse();
        if (confirmation.isBlank()) {
            System.err.println("Cannot reach server");
        } else {
            if (confirmation.equals(Messages.opponentConnected)) {
                // all good, opponent still connected
                return true;
            } else {
                if (confirmation.equals(Messages.opponentQuit)) {
                    // the opponent quit. Stop race, show message etc.
                    return false;
                }
            }
        }
        return false;
    }

}
