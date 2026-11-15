package client;

import chess.*;
import facade.ServerFacade;
import facade.ServerFacades;
import server.Server;
import ui.ConsoleUI;

public class ClientMain {
    static void main() {
        Server server = new Server();
        var port = server.run(8080);

        ServerFacades facade = new ServerFacade(
                "http://localhost:" + port
        );

        ConsoleUI ui = new ConsoleUI(facade);

        ui.run();
    }
}
