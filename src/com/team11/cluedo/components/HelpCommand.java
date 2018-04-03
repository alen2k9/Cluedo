package com.team11.cluedo.components;

import com.team11.cluedo.players.Player;

import javax.swing.*;

public class HelpCommand {
    private JTextArea infoOutput;

    public HelpCommand(JTextArea infoOutput) {
        this.infoOutput = infoOutput;
    }

    public void output(Player currentPlayer, int gameState) {
        infoOutput.append("List of available commands.\nType 'help command' for more information.\n");
        switch (gameState) {
            case 1: //  Pre-Roll
                if (currentPlayer.getSuspectToken().isInRoom()) {
                    infoOutput.append("     - Roll\n" +
                            "     - Question\n" +
                            "     - Done\n" +
                            "     - Quit\n" +
                            "     - Notes\n" +
                            "     - Cards\n" +
                            "     - Log\n" +
                            "     - Cheat\n");
                } else {
                    infoOutput.append("     - Roll\n" +
                            "     - Done\n" +
                            "     - Quit\n" +
                            "     - Notes\n" +
                            "     - Cards\n" +
                            "     - Log\n" +
                            "     - Cheat\n");
                }
                break;
            case 2: //  After-Roll
                if (currentPlayer.getSuspectToken().isInRoom()) {
                    infoOutput.append("     - Passage\n" +
                            "     - Exit\n" +
                            "     - Done\n" +
                            "     - Quit\n" +
                            "     - Notes\n" +
                            "     - Cards\n" +
                            "     - Log\n" +
                            "     - Cheat\n");

                } else {
                    infoOutput.append("     - Done\n" +
                        "     - Quit\n" +
                        "     - Notes\n" +
                        "     - Cards\n" +
                        "     - Log\n" +
                        "     - Cheat\n" +
                        "Enter 'U', 'R', 'D', or 'L' to move.\n" +
                        "Click on a highlighted square to move.\n" +
                        "Use the arrow keys to move.\n");
                }
                break;
            case 3: //  After move
                if (currentPlayer.getSuspectToken().isInRoom()) {
                    infoOutput.append("     - Question\n" +
                            "     - Done\n" +
                            "     - Quit\n" +
                            "     - Notes\n" +
                            "     - Cards\n" +
                            "     - Log\n" +
                            "     - Cheat\n");
                } else {
                    infoOutput.append("     - Done\n" +
                            "     - Quit\n" +
                            "     - Notes\n" +
                            "     - Cards\n" +
                            "     - Log\n" +
                            "     - Cheat\n");
                }
                break;
        }
    }

    public void output(String command) {
        switch (command) {
            case "roll":
                infoOutput.append("'Roll' :\n" +
                        "    Dice would roll giving you the number\n" +
                        "    of movements you are allowed to make\n\n");
                break;

            case "exit":
                infoOutput.append("'Exit' :\n" +
                        "User can exit a room with this command.\n" +
                        "Exit depends on number of door's in room.\n" +
                        "  Example: 'exit 2' :\n" +
                        "Player will exit through second door.\n" +
                        "  Default: 'exit' :\n" +
                        "Player will exit through first door.\n\n");
                break;

            case "cards":
                infoOutput.append("'Cards' :\n" +
                        "This will display the current \ncards that the user has.\n\n");
                break;

            case "passage":
                infoOutput.append("'Passage' : \n" +
                        "If player is in a room with secret passage\n" +
                        "this command will let them use the secret passage.\n\n");
                break;

            case "notes":
                infoOutput.append("'Notes' :\n" +
                        "Opens the users notes page.\n" +
                        "You can click on any cell to highlight\n" +
                        "what cards you think are not part of the\n" +
                        "murder.\n\n");
                break;

            case "cheat":
                infoOutput.append("'Cheat' :\n" +
                        "Displays the cards in the murder envelope\n\n");
                break;

            case "done":
                infoOutput.append("'Done' :\n" +
                        "Current player will end their turn.\n\n");
                break;

            case "quit":
                infoOutput.append("'Quit' :\n" +
                        "Player can quit/stop entire game.\n\n");
                break;

            default:
                infoOutput.append("Unknown command\n" +
                        "Use command 'help' + 'type command' for instructions.\n");
                break;
        }
    }
}
