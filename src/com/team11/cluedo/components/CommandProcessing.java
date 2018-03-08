package com.team11.cluedo.components;

import javax.swing.*;

public class CommandProcessing {
    public static void printRemainingMoves(int remainingMoves, JTextArea infoOutput){
        infoOutput.append("You have " + remainingMoves + " moves remaining.\n");
    }
}
