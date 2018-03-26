/*
 * Extra code to handle extra commands processing.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.components;

import javax.swing.*;

public class CommandProcessing {
    public static void printRemainingMoves(int remainingMoves, JTextArea infoOutput){
        infoOutput.append("You have " + remainingMoves + " moves remaining.\n");
    }
}
