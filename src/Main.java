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

        Scanner scanner = new Scanner(System.in);

        // Declare client socket
        Socket clientSocket = null;

        // Declare output stream and string to send to server
        DataOutputStream outputStream = null;
        String request;

        // Declare input stream from server and string to store input received from server
        BufferedReader inputStream = null;
        String responseLine;

        ObjectOutput output = null;

        // replace "localhost" with the remote server address, if needed
        // 5000 is the server port
        String serverHost = "localhost";

        // Create a socket on port 5000 and open input and output streams on that socket
        try
        {
            clientSocket = new Socket(serverHost, 5000);

            outputStream = new DataOutputStream(
                    clientSocket.getOutputStream()
            );

            inputStream = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()
                    )
            );

            output = new ObjectOutputStream(
                    clientSocket.getOutputStream()
            );
        }
        catch (UnknownHostException e)
        {
            System.err.println("Don't know about host: " + serverHost);
        }
        catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to: " + serverHost);
        }

        // Write data to the socket
        if (clientSocket != null && outputStream != null && inputStream != null && output != null)
        {
            try
            {


                Kart kart = new Kart("red", 1);

                output.writeObject(kart);

                output.flush();


                // do {
//
//                System.out.print("CLIENT: ");
//    			   request = scanner.nextLine();
//
//    			   outputStream.writeBytes( request + "\n" );
//
//
//    				if((responseLine = inputStream.readLine()) != null)
//    				{
//    					System.out.println("SERVER: " + responseLine);
//    				}
//
//                if (request.equals("CLOSE")) {
//                   break;
//                }
//
//             } while(true);


                // close the input/output streams and socket
                outputStream.close();
                inputStream.close();
                clientSocket.close();
            }
            catch (UnknownHostException e)
            {
                System.err.println("Trying to connect to unknown host: " + e);
            }
            catch (IOException e)
            {
                System.err.println("IOException:  " + e);
            }
        }


//        NetworkCommunicationManager networkCommunicationManager = new NetworkCommunicationManager();
//
//        if (networkCommunicationManager.connectToServer()) {
//            player = networkCommunicationManager.determinePlayer();
//            System.out.println(player);
//            MainWindow mainWindow = new MainWindow(player, networkCommunicationManager);
//            mainWindow.setVisible(true);
//        } else {
//            System.out.println("Connection error");
//        }
    }
}
