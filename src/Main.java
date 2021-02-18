import go_kart_go.MainWindow;
import go_kart_go.NetworkCommunicationManager;

import go_kart_go.Kart;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {

    private static int player;

    public static void main(String[] args) {

        NetworkCommunicationManager networkCommunicationManager = new NetworkCommunicationManager();

        if (networkCommunicationManager.connectToServer()) {
            System.out.println("Connected");
//            player = networkCommunicationManager.determinePlayer();
//            System.out.println(player);
//            MainWindow mainWindow = new MainWindow(player, networkCommunicationManager);
//            mainWindow.setVisible(true);
        } else {
            System.out.println("Connection error");
        }
    }
}
