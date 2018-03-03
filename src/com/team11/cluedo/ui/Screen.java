/*
 * Interface class of the screens
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.ui;

public interface Screen {
    void createScreen(String name);

    void setupScreen(int state);

    void displayScreen();

    void closeScreen();

    void reDraw(int currentPlayer);
}
