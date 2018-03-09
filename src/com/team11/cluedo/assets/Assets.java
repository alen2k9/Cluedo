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
import java.io.IOException;
import java.io.InputStream;

public class Assets {
    public Assets() {
        setupTitleFont();
        setupPixelFont();
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
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("ropeToken.png"));
    }

    ////////////////////////////////////////////
    //                                        //
    //             WEAPON CARDS               //
    //                                        //
    ////////////////////////////////////////////

    public Image getRevolverCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("revolverCard.png"));
    }

    public Image getWrenchCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("wrenchCard.png"));
    }

    public Image getDaggerCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("daggerCard.png"));
    }

    public Image getPoisonCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("poisonCard.png"));
    }

    public Image getHatchetCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("hatchetCard.png"));
    }

    public Image getRopeCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("ropeCard.png"));
    }

    public Image getSelectedRevolverCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("revolverCard_Selected.png"));
    }

    public Image getSelectedWrenchCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("wrenchCard_Selected.png"));
    }

    public Image getSelectedDaggerCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("daggerCard_Selected.png"));
    }

    public Image getSelectedPoisonCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("poisonCard_Selected.png"));
    }

    public Image getSelectedHatchetCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("hatchetCard_Selected.png"));
    }

    public Image getSelectedRopeCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("ropeCard_Selected.png"));
    }

    ////////////////////////////////////////////
    //                                        //
    //             ROOM CARDS                 //
    //                                        //
    ////////////////////////////////////////////

    public Image getKitchenCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("kitchenCard.png"));
    }

    public Image getBallroomCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("ballroomCard.png"));
    }

    public Image getConservatoryCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("roomCard.png"));
    }

    public Image getDiningCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("roomCard.png"));
    }

    public Image getBilliardCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("roomCard.png"));
    }

    public Image getLibraryCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("roomCard.png"));
    }

    public Image getLoungeCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("roomCard.png"));
    }

    public Image getHallCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("hallCard.png"));
    }

    public Image getStudyCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("roomCard.png"));
    }

    public Image getSelectedKitchenCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("roomCard.png"));
    }

    public Image getSelectedBallroomCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("roomCard.png"));
    }

    public Image getSelectedConservatoryCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("roomCard.png"));
    }

    public Image getSelectedDiningCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("roomCard.png"));
    }

    public Image getSelectedBilliardCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("roomCard.png"));
    }

    public Image getSelectedLibraryCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("roomCard.png"));
    }

    public Image getSelectedLoungeCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("roomCard.png"));
    }

    public Image getSelectedHallCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("roomCard.png"));
    }

    public Image getSelectedStudyCard() {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("roomCard.png"));
    }

    ////////////////////////////////////////////
    //                                        //
    //             CUSTOM FONTS               //
    //                                        //
    ////////////////////////////////////////////

    private void setupTitleFont() {
        InputStream fontIn = getClass().getResourceAsStream("BulkyPixel.TTF");
        try {
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(
                    Font.createFont(Font.TRUETYPE_FONT, fontIn));
        } catch (FontFormatException f) {
            System.out.println("Looks like I can't make your font.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupManaspaceFont() {
        InputStream fontIn = getClass().getResourceAsStream("manaspc.TTF");
        try {
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(
                    Font.createFont(Font.TRUETYPE_FONT, fontIn));
        } catch (FontFormatException f) {
            System.out.println("Looks like I can't make your font.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupPixelFont() {
        InputStream fontIn = getClass().getResourceAsStream("orange_kid.ttf");
        try {
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(
                    Font.createFont(Font.TRUETYPE_FONT, fontIn));
        } catch (FontFormatException f) {
            System.out.println("Looks like I can't make your font.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Color getDarkerGrey() {
        return new Color(46, 46, 46);
    }
}
