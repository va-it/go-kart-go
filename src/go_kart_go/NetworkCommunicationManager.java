package go_kart_go;

import go_kart_go_network.*;

public class NetworkCommunicationManager {

    public UDPClientSocket udpClientSocket;
    public TCPClient tcpClient;

    public NetworkCommunicationManager() {

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
        String message = udpClientSocket.getMessage();
        if (message.equals(Messages.readyToReceiveKart)) {
            udpClientSocket.sendKart(kart);
        }

        // CODE BELOW IS TO SEND KART VIA TCP -
        // FOR SOME REASON THE KART SENT THIS WAY ALWAYS HAS A SPEED OF ZERO
        // I AM NOT SURE WHY, MAYBE SOME SORT OF DELAY? NO IDEA
        // SPENT HOURS DEBUGGING WITH NO LUCK
        // SAME OBJECT SENT VIA UDP WORKS FINE...

//        System.out.println(kart.getSpeed());
//        String serverResponse;
//        tcpClient.sendRequest(Messages.sendingKartInfo);
//        serverResponse = tcpClient.getResponse();
//        if (!serverResponse.isBlank()) {
//            if (serverResponse.equals(Messages.readyToReceiveKart)) {
//                // all good, send the kart
//                tcpClient.sendObject(kart);
//                serverResponse = tcpClient.getResponse();
//                if (!serverResponse.isBlank()) {
//                    if (serverResponse.equals(Messages.kartInfoReceived)) {
//                        // all good, kart received
//                    }
//                } else {
//                    // something went wrong. Can't talk to server
//                }
//            }
//        } else {
//            // something went wrong. Can't talk to server
//        }
    }

    public int getOpponentSpeed(int player) {
        tcpClient.sendRequest(Messages.getOpponentSpeed);
        String opponentSpeed = tcpClient.getResponse();

        if (!opponentSpeed.isBlank()) {
            return Integer.parseInt(opponentSpeed);
        } else {
            // something went wrong. Can't talk to server
        }
        return 0;
    }

    public int getOpponentImageIndex(int player) {
        tcpClient.sendRequest(Messages.getOpponentIndex);
        String opponentImageIndex = tcpClient.getResponse();

        if (!opponentImageIndex.isBlank()) {
            return Integer.parseInt(opponentImageIndex);
        } else {
            // something went wrong. Can't talk to server
        }
        return 0;
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
        if (!confirmation.isBlank()) {
            if (confirmation.equals(Messages.startRace)) {
                return true;
            }
        } else {
            System.err.println("Cannot reach server");
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

}
