/**
 * Interface class of the screens
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.Team11.Cluedo.UI;

public interface Screen {
    /**
     *  Method handled to create the screen
     */
    void createScreen(String name);

    /**
     *  Method handled to set up the screen
     */
    void setupScreen(int state);

    /**
     *  Method handled to display the screen
     */
    void displayScreen();

    /**
     *  Method handled to close the screen
     */
    void closeScreen();

    /**
     *  Method handled to re-draw the screen
     */
    void reDraw();
}
