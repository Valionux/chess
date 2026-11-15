package ui;

import static ui.EscapeSequences.*;

public class Menus {
    static final String START_MENU = String.format("%-40s %s%n", "Command", "Action") +
            "-----------------------------------------------------------------------------------------\n";

    public static void printMenu(ConsoleUI.STATE state) {
        switch (state) {
            case START -> System.out.printf(startMenu() + "%n");
            case LOGGED_IN -> System.out.printf(loggedInMenu());
            case LOGGED_OUT -> System.out.printf(loggedOutMenu());
        }
    }

    private static String startMenu() {
        return """
                Welcome to the Chess App!
                """;
    }

    public static String loggedOutMenu() {

        return START_MENU +
                formatCommand(
                        "register <USERNAME> <PASSWORD> <EMAIL>",
                        "Create a new user") +
                formatCommand(
                        "login <USERNAME> <PASSWORD>",
                        "Join as an existing user and start playing chess") +
                formatCommand(
                        "quit",
                        "Close the app") +
                formatCommand(
                        "help",
                        "Make this menu appear") +
                endMenu();
    }

    public static String loggedInMenu() {

        return START_MENU +
                formatCommand(
                        "logout",
                        "Log out of your current session") +
                formatCommand(
                        "create <GAME NAME>",
                        "Start a new game") +
                formatCommand(
                        "list",
                        "List all existing games"
                ) +
                formatCommand(
                        "play <NUMBER> <WHITE/BLACK>",
                        "Join an existing game as white or black"
                ) +
                formatCommand(
                        "observe <NUMBER>",
                        "Join an existing game as observer"
                ) +
                formatCommand(
                        "quit",
                        "Close the app") +
                formatCommand(
                        "help",
                        "Make this menu appear") +
                endMenu();
    }

    private static String formatCommand(String command, String action) {
        return SET_TEXT_COLOR_BLUE +
                String.format("%-40s", command) +
                RESET_TEXT_COLOR +
                String.format(" %s%n", action);
    }

    private static String endMenu() {
        return "\n" +
                SET_TEXT_ITALIC +
                "Please do not include any whitespaces in the arguments\n\n" +
                RESET_TEXT_ITALIC;
    }
}
