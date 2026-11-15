package client;

import chess.*;
import facade.ServerFacade;
import facade.ServerFacades;
import ui.ConsoleUI;

public class ClientMain {
    static void main() {
        String severUrl = "http://localhost:8080";

        ServerFacades facade = new ServerFacade(severUrl);

        ConsoleUI ui = new ConsoleUI(facade);

        ui.run();
    }
}
