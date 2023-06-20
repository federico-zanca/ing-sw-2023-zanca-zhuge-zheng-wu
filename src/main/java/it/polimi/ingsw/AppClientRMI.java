package it.polimi.ingsw;

import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TextualUI;
import javafx.application.Application;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class AppClientRMI {

    public static void main(String[] args) throws RemoteException, NotBoundException {
        VirtualView view;
        System.out.println("Inserisci 0 per avviare la TUI, qualsiasi altro carattere o parola per la GUI");
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        if (s.equals("0")) {
            //Registry registry = LocateRegistry.getRegistry("192.168.1.87", 1099);
            Registry registry = LocateRegistry.getRegistry("172.20.10.6", 1099);
            AppServer server = (AppServer) registry.lookup("server");
            view = new TextualUI();
            ClientImpl client = new ClientImpl(server.connect());
            client.run();
        } else {
            //view = new MessageHandler();
            Application.launch(GUI.class);
        }

        //ClientImpl client = new ClientImpl(server.connect(), view);
        //client.run();

        /*
            if (args.length > 0 && args[0].equalsIgnoreCase("CLI")) {
                System.out.println(
                        "███╗   ███╗██╗   ██╗███████╗██╗  ██╗███████╗██╗     ███████╗██╗███████╗\n" +
                                "████╗ ████║╚██╗ ██╔╝██╔════╝██║  ██║██╔════╝██║     ██╔════╝██║██╔════╝\n" +
                                "██╔████╔██║ ╚████╔╝ ███████╗███████║█████╗  ██║     █████╗  ██║█████╗  \n" +
                                "██║╚██╔╝██║  ╚██╔╝  ╚════██║██╔══██║██╔══╝  ██║     ██╔══╝  ██║██╔══╝  \n" +
                                "██║ ╚═╝ ██║   ██║   ███████║██║  ██║███████╗███████╗██║     ██║███████╗\n" +
                                "╚═╝     ╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝╚══════╝╚═╝     ╚═╝╚══════╝\n" +
                                "                                                                       \n" +
                                "\n");
                new TextualUI().run();
            } else {
                Application.launch(GUI.class);
            }
    }

         */
    }
}
