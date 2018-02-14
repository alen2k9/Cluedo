package com.Team11.Cluedo.assets;

import javax.swing.*;
import java.awt.*;

public class Assets {
    private Image boardImage;
    private ImageIcon backgroundTile;

    private Image whiteToken, greenToken, peacockToken, plumToken, scarletToken, mustardToken;

    public Assets() {
    }

    public Image getBoardImage() {
        this.boardImage = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("board.png"));
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
}
