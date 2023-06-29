package it.polimi.ingsw;

import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.distributed.socket.middleware.ServerStub;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TextualUI;
import javafx.application.Application;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class AppClient {

    public static void main(String[] args) throws RemoteException, NotBoundException {
        VirtualView view;
        String ip;
        System.out.println("Inserisci 0 per avviare la TUI, qualsiasi altro carattere o parola per la GUI");
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        if (s.equals("0")) {
            System.out.println("Enter the IP address of the server: ");
            s = scanner.nextLine();
            while (!s.matches("localhost") && !s.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$")) {
                System.out.println("Invalid IP address. Please try again.");
                s = scanner.nextLine();
            }
            ip = s;
            System.out.println("Choose how to connect to the server:\n" +
                    "1. Socket connection\n" +
                    "2. JavaRMI connection\n");
            s = scanner.nextLine();
            while(!s.equals("1") && !s.equals("2")) {
                System.out.println("Invalid input. Please try again.");
                s = scanner.nextLine();
            }
            if(s.equals("1")){
                ServerStub serverStub = new ServerStub(ip, 1234);
                ClientImpl client = new ClientImpl(serverStub);
                new Thread(() -> {
                    while(true) {
                        try {
                            serverStub.receive(client);
                        } catch (RemoteException e) {
                            System.err.println("Error while receiving message: " + e);
                            //Close socket
                            try {
                                serverStub.close();
                            } catch (RemoteException ex) {
                                System.err.println("Cannot close connection with server. Halting... " + ex);
                            }
                            //Chiudi l'app
                            System.exit(1);
                        }
                    }
                }).start();

                client.run();
            } else {
                Registry registry = LocateRegistry.getRegistry(ip, 1099);
                AppServer server = (AppServer) registry.lookup("server");
                view = new TextualUI();
                ClientImpl client = new ClientImpl(server.connect());
                client.run();
            }
        }
        else {
            Application.launch(GUI.class);
        }
    }
}
