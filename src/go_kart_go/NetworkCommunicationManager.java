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
//        udpClientSocket.sendMessage(Messages.sendingKartInfo(kart.getPlayer()));
//        String message = udpClientSocket.getMessage();
//        if (message.equals(Messages.readyToReceiveKart(kart.getPlayer()))) {
//            udpClientSocket.sendKart(kart);
//        }

        // CODE BELOW IS TO SEND KART VIA TCP -
        // FOR SOME REASON THE KART SENT THIS WAY ALWAYS HAS A SPEED OF ZERO
        // I AM NOT SURE WHY, MAYBE SOME SORT OF DELAY? NO IDEA
        // SPENT HOURS DEBUGGING WITH NO LUCK
        // SAME OBJECT SENT VIA UDP WORKS FINE...

        // WHY IS SPEED ZERO WHEN THE OBJECT IS RECEIVED???
        // PRINT AND DEBUG HERE SHOWS CORRECT NUMBER !!!!!!!!!!!!!!!!!!!!

        String serverResponse;
        tcpClient.sendRequest(Messages.sendingKartInfo(kart.getPlayer()));
        serverResponse = tcpClient.getResponse();
        if (!serverResponse.isBlank()) {
            if (serverResponse.equals(Messages.readyToReceiveKart(kart.getPlayer()))) {
                // all good, send the kart
                tcpClient.sendObject(kart);
            }
        } else {
            // something went wrong. Can't talk to server
            System.err.println("Unable to contact server to send kart info");
        }
    }

    public int getOpponentSpeed(int player) {
//        udpClientSocket.sendMessage(Messages.getOpponentSpeed(player));
//        String opponentSpeed = udpClientSocket.getMessage();
//
//        if (!opponentSpeed.isBlank()) {
//            try {
//                return Integer.parseInt(opponentSpeed);
//            } catch (final NumberFormatException e) {
//                System.err.println("speed is not a number: " + e);
//            }
//        } else {
//            // something went wrong. Can't talk to server
//            System.err.println("connection error when requesting speed");
//        }
//        return 0;

        tcpClient.sendRequest(Messages.getOpponentSpeed(player));
        String opponentSpeed = tcpClient.getResponse();

        if (!opponentSpeed.isBlank()) {
            return Integer.parseInt(opponentSpeed);
        } else {
            // something went wrong. Can't talk to server
        }
        return 0;
    }

    public int getOpponentImageIndex(int player) {
//        udpClientSocket.sendMessage(Messages.getOpponentIndex(player));
//        String opponentIndex = udpClientSocket.getMessage();
//
//        if (!opponentIndex.isBlank()) {
//            try {
//                return Integer.parseInt(opponentIndex);
//            } catch (final NumberFormatException e) {
//                System.err.println("index is not a number: " + e);
//            }
//        } else {
//            // something went wrong. Can't talk to server
//            System.err.println("connection error when requesting index");
//        }
//        return 0;

        tcpClient.sendRequest(Messages.getOpponentIndex(player));
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

}
