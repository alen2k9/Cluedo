/*
 * The Main Class of the game.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo;

import com.team11.cluedo.ui.MenuScreen;

import javax.swing.*;
import java.io.IOException;

public class Cluedo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Welcome to Cluedo" + '\n' + "---    Team11      ---");
                new MenuScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
