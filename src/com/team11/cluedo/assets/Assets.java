/*
 * Code to handle the import and distribution of assets.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.assets;

import javax.swing.*;
import java.awt.*;

public class Assets {
    public Assets() {
    }

    public Image getBoardImage() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Board.png"));
    }

    public ImageIcon getBackgroundTile() {
        return new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("backgroundTile.png")));
    }

    public Image getSplashImage() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("splashImage.png"));
    }

    ////////////////////////////////////////////
    //                                        //
    //             PLAYER TOKENS              //
    //                                        //
    ////////////////////////////////////////////

    public Image getWhiteToken() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("whiteToken.png"));
    }

    public Image getGreenToken() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("greenToken.png"));
    }

    public Image getPeacockToken() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("peacockToken.png"));
    }

    public Image getPlumToken() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("plumToken.png"));
    }

    public Image getScarletToken() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("scarletToken.png"));
    }

    public Image getMustardToken() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("mustardToken.png"));
    }

    ////////////////////////////////////////////
    //                                        //
    //             PLAYER CARDS               //
    //                                        //
    ////////////////////////////////////////////

    public Image getWhiteCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("whiteCard.png"));
    }

    public Image getGreenCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("greenCard.png"));
    }

    public Image getPeacockCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("peacockCard.png"));
    }

    public Image getPlumCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("plumCard.png"));
    }

    public Image getScarletCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("scarletCard.png"));
    }

    public Image getMustardCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("mustardCard.png"));
    }

    public Image getSelectedWhiteCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("whiteCard_Selected.png"));
    }

    public Image getSelectedGreenCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("greenCard_Selected.png"));
    }

    public Image getSelectedPeacockCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("peacockCard_Selected.png"));
    }

    public Image getSelectedPlumCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("plumCard_Selected.png"));
    }

    public Image getSelectedScarletCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("scarletCard_Selected.png"));
    }

    public Image getSelectedMustardCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("mustardCard_Selected.png"));
    }

    ////////////////////////////////////////////
    //                                        //
    //             WEAPON TOKENS              //
    //                                        //
    ////////////////////////////////////////////

    public Image getRevolverToken() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("revolverToken.png"));
    }

    public Image getWrenchToken() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("wrenchToken.png"));
    }

    public Image getDaggerToken() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("daggerToken.png"));
    }

    public Image getPoisonToken() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("poisonToken.png"));
    }

    public Image getHatchetToken() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("hatchetToken.png"));
    }

    public Image getRopeToken() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("revolverToken.png"));
    }

    public Color getDarkerGrey() {
        return new Color(46, 46, 46);
    }
}
