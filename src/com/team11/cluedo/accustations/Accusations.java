package com.team11.cluedo.accustations;

import com.team11.cluedo.assets.Assets;
import com.team11.cluedo.cards.Cards;
import com.team11.cluedo.components.T11Label;
import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Accusations extends JPanel {
    private Assets gameAssets;
    private Resolution resolution;
    private Cards gameCards;
    private int cardSelectWidth;
    private int cardSelectHeight;
    private T11Label[] playerCards = new T11Label[6];
    private T11Label[] selectedPlayerCards = new T11Label[6];
    private Bounds[] bounds = new Bounds[9];
    private Bounds[] murderBouds = new Bounds[3];
    private T11Label[] weaponsCards = new T11Label[6];
    private T11Label[] selectedWeaponCards = new T11Label[6];
    private  T11Label[] roomCards = new T11Label[9];
    private T11Label[] selectedRoomCards = new T11Label[9];
    private boolean selected = false;

    String[] playerCardsName;
    int playerCard;
    int weaponCard;
    int roomCard;

    String murderPlayerCardName;
    String murderWeaponCardName;
    String muurderRoomCardName;

    private T11Label chosenPlayerCard;
    private T11Label chosenWeaponCard;
    private T11Label chosenRoomCard;

    private T11Label murderPlayerCard;
    private T11Label murderWeaponCard;
    private T11Label muurderRoomCard;

    private MouseListener ml;

    private HashMap<Integer, String> suspectName = new HashMap<>();
    private HashMap<Integer, String> weaponName = new HashMap<>();
    private  HashMap<Integer, String> roomName = new HashMap<>();



    public Accusations(Assets gameAssets, Cards gameCards, Resolution resolution){
        this.gameAssets = gameAssets;
        this.resolution = resolution;
        this.gameCards = gameCards;

        //setUpAccustations();

        ImageIcon b = new ImageIcon(gameAssets.getGreenCard()); //card used for resizing
        cardSelectWidth =(int) (b.getIconWidth()*.5*resolution.getScalePercentage());
        cardSelectHeight =(int) (b.getIconHeight()*.5*resolution.getScalePercentage());

        this.playerCards = new T11Label[]{
                new T11Label(new ImageIcon(gameAssets.getWhiteCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getGreenCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getPeacockCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getPlumCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getScarletCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getMustardCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
        };
        suspectName.put(0, "Miss White");
        suspectName.put(1, "Mr. Green");
        suspectName.put(2, "Ms. Peacock");
        suspectName.put(3, "Mr. Plum");
        suspectName.put(4, "Miss Scarlett");
        suspectName.put(5, "Colonel Mustard");

        this.selectedPlayerCards = new T11Label[]{
                new T11Label(new ImageIcon(gameAssets.getSelectedWhiteCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getSelectedGreenCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getSelectedPeacockCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getSelectedPlumCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getSelectedScarletCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getSelectedMustardCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
        };

        this.weaponsCards = new T11Label[]{
                new T11Label(new ImageIcon(gameAssets.getRopeCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getRevolverCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getDaggerCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getHatchetCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getPoisonCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getWrenchCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
        };

        weaponName.put(0, "Rope");
        weaponName.put(1, "Revolver");
        weaponName.put(2, "Dagger");
        weaponName.put(3, "Hatchet");
        weaponName.put(4, "Poison");
        weaponName.put(5, "Wrench");

        this.selectedWeaponCards = new T11Label[]{
                new T11Label(new ImageIcon(gameAssets.getSelectedRopeCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getSelectedRevolverCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getSelectedDaggerCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getSelectedHatchetCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getSelectedPoisonCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getSelectedWrenchCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
        };
//        this.getRootPane().getContentPane().removeAll();

        this.roomCards = new T11Label[]{
                new T11Label(new ImageIcon(gameAssets.getKitchenCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getBallroomCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getDiningCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getConservatoryCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getBilliardCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getLibraryCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getLoungeCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getHallCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getStudyCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0)))
        };

        this.selectedRoomCards = new T11Label[]{
                new T11Label(new ImageIcon(gameAssets.getSelectedKitchenCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getSelectedBallroomCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getSelectedDiningCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getSelectedConservatoryCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getSelectedBilliardCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getSelectedLibraryCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getSelectedLoungeCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getSelectedHallCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0))),
                new T11Label(new ImageIcon(gameAssets.getSelectedStudyCard().getScaledInstance(cardSelectWidth, cardSelectHeight, 0)))
        };

            roomName.put(0, "Kitchen");
            roomName.put(1, "Ballroom");
            roomName.put(2, "Dining Room");
            roomName.put(3, "Conservatory");
            roomName.put(4, "Billiard Room");
            roomName.put(5, "Library");
            roomName.put(6, "Lounge");
            roomName.put(7, "Hall");
            roomName.put(8, "Study");


        this.bounds = new Bounds[]{
                new Bounds(10,135+cardSelectHeight,cardSelectWidth,cardSelectHeight),
                new Bounds(115,135+cardSelectHeight,cardSelectWidth,cardSelectHeight),
                new Bounds(215,135+cardSelectHeight,cardSelectWidth,cardSelectHeight),
                new Bounds(320,135+cardSelectHeight,cardSelectWidth,cardSelectHeight),
                new Bounds(425,135+cardSelectHeight,cardSelectWidth,cardSelectHeight),
                new Bounds(10,400,cardSelectWidth, cardSelectHeight),
                new Bounds(115,400,cardSelectWidth,cardSelectHeight),
                new Bounds(215,400,cardSelectWidth,cardSelectHeight),
                new Bounds(320,400,cardSelectWidth,cardSelectHeight)
        };

        this.murderBouds = new Bounds[]{
                new Bounds(0,cardSelectHeight*2 + 50,cardSelectWidth*2,cardSelectHeight*2),
                new Bounds(cardSelectWidth*2,cardSelectHeight*2 + 50,cardSelectWidth*2,cardSelectHeight*2),
                new Bounds(cardSelectWidth*4,cardSelectHeight*2 + 50,cardSelectWidth*2,cardSelectHeight*2)
        };



        //setUpAccustations();
        //addCharacterCards();




        //disableAccusation();
    }

    public void setUpAccustations()
    {
        this.setBackground(new Color(46, 46, 46, 107));

        this.setLayout(null);
        this.setLocation(0,0);
        this.setSize((int)(780 * resolution.getScalePercentage()),(int)(810 * resolution.getScalePercentage()));
        this.setVisible(true);
        this.setOpaque(true);
        addCharacterCards();
    }

    public void disableAccusation()
    {
        this.setVisible(false);
    }

    public void addCharacterCards()
    {
        for (int i = 0; i < this.playerCards.length;i++){
            playerCards[i].setIcon(playerCards[i].getIcon());
            add(playerCards[i]).setBounds(bounds[i].getX(), bounds[i].getY(), bounds[i].getCardSelectWidth(), bounds[i].getCardSelectHeight());
        }

        for (int i = 0; i < selectedPlayerCards.length;i++){
            selectedPlayerCards[i].setIcon(selectedPlayerCards[i].getIcon());
            add(selectedPlayerCards[i]).setBounds(bounds[i].getX(), bounds[i].getY(), bounds[i].getCardSelectWidth(), bounds[i].getCardSelectHeight());
            selectedPlayerCards[i].setVisible(false);
        }

        for (int i = 0; i < playerCards.length;i++) {

            int finalI = i;
            playerCards[i].addMouseListener(ml = new MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if(!selected) {

                        selectedPlayerCards[finalI].setVisible(true);
                        playerCards[finalI].setVisible(false);
                    }

                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    super.mouseMoved(e);
                    selectedPlayerCards[finalI].setIcon(selectedPlayerCards[finalI].getIcon());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if(!selected) {
                        selectedPlayerCards[finalI].setVisible(false);
                        playerCards[finalI].setVisible(true);

                    }

                }

                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (!selected) {
                        selected = true;

                        playerCard = finalI;
                        chosenPlayerCard = playerCards[finalI];
                        chosenPlayerCard.setVisible(false);
                        //System.out.println(playerCardsName[finalI]);
                        remove(playerCards[finalI]);
                        remove(selectedPlayerCards[finalI]);
                        removePlayerCards(finalI);
                        //setUpAccustations();


                        addWeaponCards();
                    }
                }

            });

        }
    }



    public void addWeaponCards(){
        selected = false;
        for (int i = 0; i < weaponsCards.length;i++){
            add(weaponsCards[i]).setBounds(bounds[i].getX(), bounds[i].getY(), bounds[i].getCardSelectWidth(), bounds[i].getCardSelectHeight());
        }

        for (int i = 0; i < selectedWeaponCards.length;i++){
            add(selectedWeaponCards[i]).setBounds(bounds[i].getX(), bounds[i].getY(), bounds[i].getCardSelectWidth(), bounds[i].getCardSelectHeight());
            selectedWeaponCards[i].setVisible(false);
        }

        for (int i = 0; i < weaponsCards.length;i++) {

            int finalI = i;
            weaponsCards[i].addMouseListener(ml = new MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if(!selected) {
                        weaponsCards[finalI].setVisible(false);
                        selectedWeaponCards[finalI].setVisible(true);
                    }
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if(!selected) {
                        weaponsCards[finalI].setVisible(true);
                        selectedWeaponCards[finalI].setVisible(false);
                    }
                }
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {

                    if (!selected) {
                        selected = true;

                        weaponCard = finalI;
                        chosenWeaponCard = weaponsCards[finalI];
                        chosenWeaponCard.setVisible(false);

                        remove(selectedWeaponCards[finalI]);
                        removeWeaponCards(finalI);
                        //setUpAccustations();



                    }

                }
            });

        }
    }

    public void removePlayerCards(int a)
    {

        ArrayList<T11Label> remainingLabels = new ArrayList<>(5);
        for (int i = 0; i < playerCards.length; i++){
            if (i != a){
                remainingLabels.add(playerCards[i]);
            }
        }

        for (T11Label label : remainingLabels){
            remove(label);
        }
        getRootPane().getContentPane().repaint();
        getRootPane().getContentPane().revalidate();
        int length = cardSelectWidth;
        //add(chosenPlayerCard).setBounds(0,0,cardSelectWidth*2,cardSelectHeight*2);
        chosenPlayerCard.removeMouseListener(ml);
        add(chosenPlayerCard).setBounds(0,5,cardSelectWidth*2,cardSelectHeight*2);

        addWeaponCards();

    }

    public void removeWeaponCards(int a)
    {
        ArrayList<T11Label> remainingLabels = new ArrayList<>(6);

        for (int i = 0; i < weaponsCards.length; i++){
            if (i != a){
                remainingLabels.add(weaponsCards[i]);
            }
        }

        for (T11Label label : remainingLabels){
            remove(label);
        }
        getRootPane().getContentPane().repaint();
        getRootPane().getContentPane().revalidate();
        chosenWeaponCard.removeMouseListener(ml);
        //add(chosenPlayerCard).setBounds(10,5,cardSelectWidth*2,cardSelectHeight*2);
        add(chosenWeaponCard).setBounds(cardSelectWidth*2,5,cardSelectWidth*2,cardSelectHeight*2);

        addRoomCards();



    }

    public void addRoomCards(){
        selected = false;
        for (int i = 0; i < roomCards.length;i++){
            add(roomCards[i]).setBounds(bounds[i].getX(), bounds[i].getY(), bounds[i].getCardSelectWidth(), bounds[i].getCardSelectHeight());
        }

        for (int i = 0; i < selectedRoomCards.length;i++){
            add(selectedRoomCards[i]).setBounds(bounds[i].getX(), bounds[i].getY(), bounds[i].getCardSelectWidth(), bounds[i].getCardSelectHeight());
            selectedRoomCards[i].setVisible(false);
        }

        for (int i = 0; i < roomCards.length;i++) {

            int finalI = i;
            roomCards[i].addMouseListener(ml = new MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if(!selected) {
                        roomCards[finalI].setVisible(false);
                        selectedRoomCards[finalI].setVisible(true);
                    }
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if(!selected) {
                        roomCards[finalI].setVisible(true);
                        selectedRoomCards[finalI].setVisible(false);
                    }
                }
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {

                    if(!selected) {
                        selected = true;

                        roomCard = finalI;
                        chosenRoomCard = roomCards[finalI];
                        chosenRoomCard.setVisible(false);
                        remove(selectedRoomCards[finalI]);
                        removeRoomCards(finalI);
                        //setUpAccustations();

                        //addRoomCards();
                    }


                }
            });

        }
    }

    public void removeRoomCards(int a)
    {
        ArrayList<T11Label> remainingLabels = new ArrayList<>(9);

        for (int i = 0; i < roomCards.length; i++){
            if (i != a){
                remainingLabels.add(roomCards[i]);
            }
        }

        for (T11Label label : remainingLabels){
            remove(label);
        }
        getRootPane().getContentPane().repaint();
        getRootPane().getContentPane().revalidate();

        chosenRoomCard.removeMouseListener(ml);
       // add(chosenPlayerCard).setBounds(0,5,cardSelectWidth*2,cardSelectHeight*2);
        //add(chosenWeaponCard).setBounds(cardSelectWidth*2,5,cardSelectWidth*2,cardSelectHeight*2);
        add(chosenRoomCard).setBounds( cardSelectWidth*4,5,cardSelectWidth*2,cardSelectHeight*2);
        getRootPane().getContentPane().repaint();
        getRootPane().getContentPane().revalidate();
        chosenRoomCard.setVisible(true);

        displayEnvelope();

    }

    public void displayEnvelope() {
        murderPlayerCardName = gameCards.getMurderEnvelope().getSuspectCard().getName();
        murderWeaponCardName = gameCards.getMurderEnvelope().getWeaponCard().getName();
        muurderRoomCardName = gameCards.getMurderEnvelope().getRoomCard().getName();

        murderPlayerCard = new T11Label(new ImageIcon(gameCards.getMurderEnvelope().getSuspectCard().getCardImage().getScaledInstance(cardSelectWidth, cardSelectHeight, 0)));
        murderWeaponCard = new T11Label(new ImageIcon(gameCards.getMurderEnvelope().getWeaponCard().getCardImage().getScaledInstance(cardSelectWidth, cardSelectHeight, 0)));
        muurderRoomCard = new T11Label(new ImageIcon(gameCards.getMurderEnvelope().getRoomCard().getCardImage().getScaledInstance(cardSelectWidth, cardSelectHeight, 0)));

        add(murderPlayerCard).setBounds(murderBouds[0].getX(),murderBouds[0].getY(), murderBouds[0].getCardSelectWidth(), murderBouds[0].getCardSelectHeight());
        add(murderWeaponCard).setBounds(murderBouds[1].getX(),murderBouds[1].getY(), murderBouds[1].getCardSelectWidth(), murderBouds[1].getCardSelectHeight());
        add(muurderRoomCard).setBounds(murderBouds[2].getX(),murderBouds[2].getY(), murderBouds[2].getCardSelectWidth(), murderBouds[2].getCardSelectHeight());

        test();
    }

    public void test()
    {
        if(suspectName.get(playerCard).equals(murderPlayerCardName) && weaponName.get(weaponCard).equals(murderWeaponCardName)
                && roomName.get(roomCard).equals(muurderRoomCardName))
        {
            System.out.println("congrats you won");
        }
        else
        {
            System.out.println("you lose");
        }
    }

    public int getCardSelectHeight() {
        return cardSelectHeight;
    }

    public int getCardSelectWidth() {
        return cardSelectWidth;
    }
}
