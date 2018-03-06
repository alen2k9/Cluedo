/*
  Code to handle the suspects data.

  Authors Team11:  Jack Geraghty - 16384181
                   Conor Beenham - 16350851
                   Alen Thomas   - 16333003
 */

package com.team11.cluedo.suspects;

import com.team11.cluedo.assets.Assets;

import java.awt.*;
import java.util.HashMap;

public class SuspectData {
    private HashMap<Integer, Point> suspectSpawn = new HashMap<>();
    private HashMap<Integer, String> suspectName = new HashMap<>();
    private HashMap<Integer, Image> suspectToken = new HashMap<>();
    private HashMap<Integer, Image> suspectCard = new HashMap<>();
    private HashMap<Integer, Image> suspectSelectedCard = new HashMap<>();
    private HashMap<Integer, String> suspectID = new HashMap<>();
    private HashMap<Integer, Color> suspectColor = new HashMap<>();

    public SuspectData() {
        setSuspectName();
        setSuspectSpawn();
        setSuspectToken();
        setSuspectCard();
        setSuspectSelectedCard();
        setSuspectID();
        setSuspectColor();
    }

    private void setSuspectName() {
        suspectName.put(0, "Miss White");
        suspectName.put(1, "Mr. Green");
        suspectName.put(2, "Ms. Peacock");
        suspectName.put(3, "Mr. Plum");
        suspectName.put(4, "Miss Scarlett");
        suspectName.put(5, "Colonel Mustard");
    }

    private void setSuspectID(){
        suspectID.put(0, "white");
        suspectID.put(1, "green");
        suspectID.put(2, "peacock");
        suspectID.put(3, "plum");
        suspectID.put(4, "scarlett");
        suspectID.put(5, "mustard");
    }

    private void setSuspectSpawn() {
        suspectSpawn.put(0, new Point(10,1));
        suspectSpawn.put(1, new Point(15,1));
        suspectSpawn.put(2, new Point(24,7));
        suspectSpawn.put(3, new Point(24,20));
        suspectSpawn.put(4, new Point(8,25));
        suspectSpawn.put(5, new Point(1,18));
    }

    private void setSuspectToken() {
        Assets gameAssets = new Assets();
        suspectToken.put(0, gameAssets.getWhiteToken());
        suspectToken.put(1, gameAssets.getGreenToken());
        suspectToken.put(2, gameAssets.getPeacockToken());
        suspectToken.put(3, gameAssets.getPlumToken());
        suspectToken.put(4, gameAssets.getScarletToken());
        suspectToken.put(5, gameAssets.getMustardToken());
    }

    private void setSuspectCard() {
        Assets gameAssets = new Assets();
        suspectCard.put(0, gameAssets.getWhiteCard());
        suspectCard.put(1, gameAssets.getGreenCard());
        suspectCard.put(2, gameAssets.getPeacockCard());
        suspectCard.put(3, gameAssets.getPlumCard());
        suspectCard.put(4, gameAssets.getScarletCard());
        suspectCard.put(5, gameAssets.getMustardCard());
    }

    private void setSuspectSelectedCard() {
        Assets gameAssets = new Assets();
        suspectSelectedCard.put(0, gameAssets.getSelectedWhiteCard());
        suspectSelectedCard.put(1, gameAssets.getSelectedGreenCard());
        suspectSelectedCard.put(2, gameAssets.getSelectedPeacockCard());
        suspectSelectedCard.put(3, gameAssets.getSelectedPlumCard());
        suspectSelectedCard.put(4, gameAssets.getSelectedScarletCard());
        suspectSelectedCard.put(5, gameAssets.getSelectedMustardCard());
    }

    private void setSuspectColor() {
        suspectColor.put(0, Color.LIGHT_GRAY);
        suspectColor.put(1, new Color(60,150,60));
        suspectColor.put(2, Color.BLUE);
        suspectColor.put(3, new Color(130,0,150));
        suspectColor.put(4, Color.RED);
        suspectColor.put(5, Color.ORANGE);
    }

    public Point getSuspectSpawn(int index) {
        return suspectSpawn.getOrDefault(index, null);
    }

    public String getSuspectName(int index) {
        return suspectName.getOrDefault(index, null);
    }

    public Image getSuspectToken(int index) {
        return suspectToken.getOrDefault(index, null);
    }

    public Image getSuspectCard(int index) {
        return suspectCard.getOrDefault(index, null);
    }

    public Image getSelectedSuspectCard(int index) {
        return suspectSelectedCard.getOrDefault(index, null);
    }

    public int getSuspectAmount() {
        return 6;
    }

    public String getSuspectID(int index){
        return suspectID.getOrDefault(index, null);
    }

    public Color getSuspectColor(int index) {
        return suspectColor.getOrDefault(index, null);
    }


}
