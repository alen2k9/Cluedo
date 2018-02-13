/**
 * Code to handle the import and distribution of assets.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.Team11.Cluedo.Assets;

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;

public class Assets {
    private Image boardImage, splashImage;
    private ImageIcon backgroundTile;

    private Image whiteToken, greenToken, peacockToken, plumToken, scarletToken, mustardToken;

    public Assets() {
    }

    public Image getBoardImage() {
        this.boardImage = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Board.png"));
        return boardImage;
    }

    public ImageIcon getBackgroundTile() {
        this.backgroundTile = new ImageIcon(this.getClass().getResource("backgroundTile.png"));
        return backgroundTile;
    }

    public Image getWhiteToken() {
        this.whiteToken = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("whiteToken.png"));
        return whiteToken;
    }

    public Image getGreenToken() {
        this.greenToken = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("greenToken.png"));
        return greenToken;
    }

    public Image getPeacockToken() {
        this.peacockToken = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("peacockToken.png"));
        return peacockToken;
    }

    public Image getPlumToken() {
        this.plumToken = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("plumToken.png"));
        return plumToken;
    }

    public Image getScarletToken() {
        this.scarletToken = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("scarletToken.png"));
        return scarletToken;
    }

    public Image getMustardToken() {
        this.mustardToken = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("mustardToken.png"));
        return mustardToken;
    }

    public Image getSplashImage() {
        this.splashImage = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("splashImage.png"));
        return splashImage;
    }
}
