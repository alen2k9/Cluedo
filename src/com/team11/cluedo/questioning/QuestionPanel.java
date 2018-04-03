/*
 * Code to handle the question panel
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
*/
package com.team11.cluedo.questioning;

import com.team11.cluedo.assets.Assets;
import com.team11.cluedo.board.room.RoomData;
import com.team11.cluedo.cards.Card;
import com.team11.cluedo.components.T11Label;
import com.team11.cluedo.players.PlayerHand;
import com.team11.cluedo.suspects.SuspectData;
import com.team11.cluedo.ui.GameScreen;
import com.team11.cluedo.ui.Resolution;
import com.team11.cluedo.weapons.WeaponData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class QuestionPanel extends JPanel {

    private Assets gameAssets = new Assets();
    private SuspectData suspectData = new SuspectData();
    private WeaponData weaponData = new WeaponData();

    private Resolution resolution;

    private Point[] cardPoints = new Point[6];

    private GameScreen gameScreen;

    private int cardYDown, cardYUp;

    private int currentPlayer;

    private boolean selected;
    private boolean moving = false;
    private boolean inWeaponState = false;

    private T11ExtendedLabel selectedPlayer;
    private T11ExtendedLabel selectedWeapon;
    private T11ExtendedLabel currentRoom;
    private T11ExtendedLabel[] selectedCards = new T11ExtendedLabel[3];

    private T11ExtendedLabel answerLabel;


    private T11ExtendedLabel[] roomCards;

    private ImageIcon[] selectedPlayerIcons  = new ImageIcon[]{
        new ImageIcon(gameAssets.getSelectedWhiteCard()),
                new ImageIcon(gameAssets.getSelectedGreenCard()),
                new ImageIcon(gameAssets.getSelectedPeacockCard()),
                new ImageIcon(gameAssets.getSelectedPlumCard()),
                new ImageIcon(gameAssets.getSelectedScarletCard()),
                new ImageIcon(gameAssets.getSelectedMustardCard()),
    };
    private ImageIcon[] playerIcons = new ImageIcon[]{
        new ImageIcon(gameAssets.getWhiteCard()),
                new ImageIcon(gameAssets.getGreenCard()),
                new ImageIcon(gameAssets.getPeacockCard()),
                new ImageIcon(gameAssets.getPlumCard()),
                new ImageIcon(gameAssets.getScarletCard()),
                new ImageIcon(gameAssets.getMustardCard()),
    };

    private T11ExtendedLabel[] cardLabels = new T11ExtendedLabel[]{
        new T11ExtendedLabel(playerIcons[0], suspectData.getSuspectName(0)),
                new T11ExtendedLabel(playerIcons[1], suspectData.getSuspectName(1)),
                new T11ExtendedLabel(playerIcons[2], suspectData.getSuspectName(2)),
                new T11ExtendedLabel(playerIcons[3], suspectData.getSuspectName(3)),
                new T11ExtendedLabel(playerIcons[4], suspectData.getSuspectName(4)),
                new T11ExtendedLabel(playerIcons[5], suspectData.getSuspectName(5)),
    };

    private ImageIcon[] weaponsCards = new ImageIcon[]{
            new ImageIcon(gameAssets.getHatchetCard()),
            new ImageIcon(gameAssets.getDaggerCard()),
            new ImageIcon(gameAssets.getPoisonCard()),
            new ImageIcon(gameAssets.getRevolverCard()),
            new ImageIcon(gameAssets.getRopeCard()),
            new ImageIcon(gameAssets.getWrenchCard()),
    };

    private ImageIcon[] selectedWeaponCards = new ImageIcon[]{
            new ImageIcon(gameAssets.getSelectedHatchetCard()),
            new ImageIcon(gameAssets.getSelectedDaggerCard()),
            new ImageIcon(gameAssets.getSelectedPoisonCard()),
            new ImageIcon(gameAssets.getSelectedRevolverCard()),
            new ImageIcon(gameAssets.getSelectedRopeCard()),
            new ImageIcon(gameAssets.getSelectedWrenchCard()),
    };

    private T11ExtendedLabel[] weaponsLabels = new T11ExtendedLabel[]{
            new T11ExtendedLabel(weaponsCards[0], weaponData.getWeaponName(0)),
            new T11ExtendedLabel(weaponsCards[1], weaponData.getWeaponName(1)),
            new T11ExtendedLabel(weaponsCards[2], weaponData.getWeaponName(2)),
            new T11ExtendedLabel(weaponsCards[3], weaponData.getWeaponName(3)),
            new T11ExtendedLabel(weaponsCards[4], weaponData.getWeaponName(4)),
            new T11ExtendedLabel(weaponsCards[5], weaponData.getWeaponName(5)),
    };


    public QuestionPanel(GameScreen gameScreen, Resolution resolution) {
        this.gameScreen = gameScreen;
        this.resolution = resolution;

        this.setLayout(null);
        this.setLocation((int)(10 * resolution.getScalePercentage()),(int)(100 * resolution.getScalePercentage()));
        this.setSize((int)(760 * resolution.getScalePercentage()),(int)(600 * resolution.getScalePercentage()));

        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

    }

    public void setPresetPlayer(String player){
        for (T11ExtendedLabel label : cardLabels){
            if (label.getLabelID().matches(player)){
                this.selectedPlayer = label;
                System.out.println("Selected player is " + selectedPlayer.getLabelID());
            }
        }
    }

    public void setPresetWeapon(String weapon){
        for (T11ExtendedLabel label : weaponsLabels){
            if (label.getLabelID().matches(weapon)){
                this.selectedWeapon = label;
                System.out.println("Selected player is " + selectedWeapon.getLabelID());
            }
        }
    }

    private int sumWidth(int index){
        int sum = 0;

        for (int i = 0; i < index; i++){
            sum += cardLabels[i].getIcon().getIconWidth() * (resolution.getScalePercentage() * 0.42);
        }
        return sum;
    }

    private void setAnswerLabel(T11ExtendedLabel label){
        this.answerLabel = label;
    }

    public void displayQuestionPanel(int roomNum, int currentPlayer){
        this.setSelected(false);
        inWeaponState = false;
        selected = false;
        this.displayPanel(roomNum);
        this.setVisible(true);
        this.setFocusable(true);
        this.currentPlayer = currentPlayer;
    }

    public void hideQuestionPanel(){
        this.selectedPlayer = null;
        this.selectedWeapon = null;
        this.setSelected(false);
        this.removeAll();
        this.setVisible(false);
    }

    private void displayPanel(int roomNum){
        RoomData roomData = new RoomData();
        moving = false;

        roomCards = new T11ExtendedLabel[]{
                new T11ExtendedLabel(new ImageIcon(gameAssets.getKitchenCard()),      roomData.getRoomName(0)),
                new T11ExtendedLabel(new ImageIcon(gameAssets.getBallroomCard()),     roomData.getRoomName(1)),
                new T11ExtendedLabel(new ImageIcon(gameAssets.getConservatoryCard()), roomData.getRoomName(2)),
                new T11ExtendedLabel(new ImageIcon(gameAssets.getDiningCard()),       roomData.getRoomName(3)),
                new T11ExtendedLabel(new ImageIcon(gameAssets.getBilliardCard()),     roomData.getRoomName(4)),
                new T11ExtendedLabel(new ImageIcon(gameAssets.getLibraryCard()),      roomData.getRoomName(5)),
                new T11ExtendedLabel(new ImageIcon(gameAssets.getLoungeCard()),       roomData.getRoomName(6)),
                new T11ExtendedLabel(new ImageIcon(gameAssets.getHallCard()),         roomData.getRoomName(7)),
                new T11ExtendedLabel(new ImageIcon(gameAssets.getStudyCard()),        roomData.getRoomName(8)),
        };

        //Both player and weapon already selected
        if (selectedPlayer != null && selectedWeapon != null){
            System.out.println("Both");
        }
        //Just player already selected
        else if (selectedPlayer != null){
            System.out.println("Player");
        }
        //Just weapon already selected
        else if (selectedWeapon != null){
            System.out.println("Weapon");
        }
        //Neither selected

        else {
            System.out.println("Else");
            this.cardYUp = (int)((this.getHeight() - (int)(resolution.getScalePercentage() * 100) - cardLabels[0].getIcon().getIconHeight() * (resolution.getScalePercentage() * 0.42)) - (int)(resolution.getScalePercentage()*40));
            this.cardYDown =  this.cardYUp + (int)(resolution.getScalePercentage() * 40);

            int extraWidth = 0;
            for (int i = 0; i < cardPoints.length; i++){
                cardLabels[i].setSize((int)(cardLabels[i].getIcon().getIconWidth() * (resolution.getScalePercentage() * 0.42)), ((int)(cardLabels[i].getIcon().getIconHeight() * (resolution.getScalePercentage() * 0.42))));
                cardLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                if (i == 0){
                    if ((int)(resolution.getScalePercentage() *22) % 2 != 0){
                        cardPoints[i] = new Point(((int)(resolution.getScalePercentage()*22) + 1),
                                (int)(this.getHeight() - cardLabels[i].getIcon().getIconHeight() * (resolution.getScalePercentage() * 0.42)) - (int)(resolution.getScalePercentage() * 100));
                    } else{
                        cardPoints[i] = new Point(((int)(resolution.getScalePercentage()*22)),
                                (int)(this.getHeight() - cardLabels[i].getIcon().getIconHeight() * (resolution.getScalePercentage() * 0.42)) - (int)(resolution.getScalePercentage() * 100));
                    }

                }

                else{
                    if ((int)(resolution.getScalePercentage() * 20) % 2 != 0){
                        cardPoints[i] = new Point(((int)(resolution.getScalePercentage()*20) + 1) + sumWidth(i) + extraWidth
                                , (int)(this.getHeight() - cardLabels[i].getIcon().getIconHeight() * (resolution.getScalePercentage() * 0.42)) - (int)(resolution.getScalePercentage() * 100));
                    } else {
                        cardPoints[i] = new Point((int) (resolution.getScalePercentage() * 20)  + sumWidth(i) + extraWidth
                                , (int) (this.getHeight() - cardLabels[i].getIcon().getIconHeight() * (resolution.getScalePercentage() * 0.42)) - (int) (resolution.getScalePercentage() * 100));
                    }
                }

                extraWidth += (int)(resolution.getScalePercentage() * 15);
            }

            for (int i = 0; i < roomCards.length; i++){
                roomCards[i].setSize(new Dimension((int)(roomCards[i].getIcon().getIconWidth() * (resolution.getScalePercentage() * 0.55)), ((int)(roomCards[i].getIcon().getIconHeight() * (resolution.getScalePercentage() * 0.55)))));
            }

            roomCards[roomNum].setLocation(this.getWidth()/2 - ((roomCards[roomNum].getWidth()/2)), (int)(resolution.getScalePercentage() * 20));
            roomCards[roomNum].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            currentRoom = roomCards[roomNum];
            selectedCards[1] = currentRoom;

            this.add(roomCards[roomNum]);

            for (int i = 0; i < cardLabels.length; i++){
                cardLabels[i].setLocation(cardPoints[i]);

                int finalI = i;
                cardLabels[i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        if (!selected && !inWeaponState && !moving){
                            setSelected(true);
                            selectedPlayer = cardLabels[finalI];
                            selectedCards[0] = selectedPlayer;
                            new AnimateCorner(0, cardLabels[finalI], cardLabels, finalI).execute();
                        }

                    }

                    @Override
                    public void mouseEntered(MouseEvent e){
                        super.mouseEntered(e);
                        if (!selected && !inWeaponState && !moving){
                            for (int j = 0; j < 6; j++){
                                if (j != finalI){
                                    cardLabels[j].setIcon(selectedPlayerIcons[j]);
                                }
                            }
                            cardLabels[finalI].revalidate();
                            new AnimateCard(cardLabels,finalI, 0, cardYDown, cardYUp).execute();
                        }

                    }

                    @Override
                    public void mouseExited(MouseEvent e){
                        super.mouseExited(e);
                        if (!selected && !inWeaponState && !moving) {
                            for (int j = 0; j < 6; j++){
                                cardLabels[j].setIcon(playerIcons[j]);
                            }
                            cardLabels[finalI].revalidate();
                            new AnimateCard(cardLabels,finalI, 1, cardYDown, cardYUp).execute();
                        }
                    }
                });

                this.add(cardLabels[i]);
            }
        }
    }

    public void setSelected(boolean bool){
        this.selected = bool;
    }

    private void addWeaponCards(){
        selectedPlayer.setLocation(getWidth()/6, (int)(resolution.getScalePercentage() * 20));
        selectedPlayer.setSize(roomCards[0].getSize());

        add(selectedPlayer);

        int extraWidth = 0;
        for (int i = 0; i < 6; i++){
            weaponsLabels[i].setLocation(this.getWidth() + sumWidth(i) + (int)(resolution.getScalePercentage()*22) + extraWidth,
                    (int)(this.getHeight() - weaponsLabels[i].getIcon().getIconHeight() * (resolution.getScalePercentage() * 0.42))
                            - (int)(resolution.getScalePercentage() * 100));
            weaponsLabels[i].setSize((int)(weaponsLabels[i].getIcon().getIconWidth() * (resolution.getScalePercentage() * 0.42)), (int)(weaponsLabels[i].getIcon().getIconHeight() * (resolution.getScalePercentage() * 0.42)));

            extraWidth += (int)(resolution.getScalePercentage() * 15);

            this.add(weaponsLabels[i]);
        }

        new AnimateCard(weaponsLabels, 0, 3, 0,0).execute();
        int finalI;
        for (int i = 0; i < weaponsLabels.length; i++){
            finalI = i;
            int finalI1 = finalI;
            int finalI2 = finalI;
            weaponsLabels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (!selected && inWeaponState && !moving){
                        setSelected(true);
                        selectedWeapon = weaponsLabels[finalI1];
                        selectedCards[2] = selectedWeapon;
                        new AnimateCorner(2, weaponsLabels[finalI1], weaponsLabels, finalI1).execute();


                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    if (!selected && inWeaponState && !moving){
                        for (int j = 0; j < 6; j++){
                            if (j != finalI1){
                                weaponsLabels[j].setIcon(selectedWeaponCards[j]);
                            }
                        }

                        weaponsLabels[finalI1].revalidate();
                        new AnimateCard(weaponsLabels, finalI1, 0, cardYDown, cardYUp).execute();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    if (!selected && inWeaponState && !moving) {
                        for (int j = 0; j < 6; j++){
                            weaponsLabels[j].setIcon(weaponsCards[j]);
                        }
                        weaponsLabels[finalI2].revalidate();
                        new AnimateCard(weaponsLabels, finalI2, 1, cardYDown, cardYUp).execute();
                    }
                }
            });
        }
    }

    private void question(){
        selectedWeapon.setSize(roomCards[0].getSize());
        selectedWeapon.setLocation(((getWidth()/3)*2) - (int)(resolution.getScalePercentage() * 10) , (int)(resolution.getScalePercentage() * 20));
        add(selectedWeapon);

        int nextPlayer ;
        if (currentPlayer + 1 == 6){
            nextPlayer = 0;
        } else {
            nextPlayer = currentPlayer + 1;
        }

        ArrayList<T11ExtendedLabel> validCards = findValidCards(nextPlayer);
        ArrayList<ImageIcon> icons = getSelectedIcons(nextPlayer, 1);
        ArrayList<ImageIcon> selectedIcons = getSelectedIcons(nextPlayer, 0);

        this.gameScreen.reDraw(nextPlayer);

        //Add in the valid cards
        moving = true;
        int i = 0;
        int extraWidth = 0;
        System.out.println("Number: " + validCards.size());
        for (T11ExtendedLabel label : validCards){
            label.setSize(new Dimension(selectedWeapon.getWidth() * (int)(resolution.getScalePercentage() * 1.5), selectedWeapon.getHeight() * (int)(resolution.getScalePercentage() * 1.5)));
            label.setLocation(getWidth() + sumWidth(i) + extraWidth, (selectedPlayer.getY() + selectedPlayer.getHeight() + (int)(resolution.getScalePercentage() * 50)));
            i++;
            extraWidth += (int)(resolution.getScalePercentage() * 20);

            System.out.println("Size: " + label.getSize());
            System.out.println("Location: " + label.getLocation());
        }

        new AnimateNextCards(validCards).execute();


    }

    private ArrayList<T11ExtendedLabel> findValidCards(int nextPlayer){
        PlayerHand playerHand = this.gameScreen.getGamePlayers().getPlayer(nextPlayer).getPlayerHand();
        ArrayList<T11ExtendedLabel> validCardsFromHand = new ArrayList<>();

        for (int i = 0; i < 3; i++){
            for (Card card : playerHand.getPlayerHand()){
                if (card.getName().matches(selectedCards[i].getLabelID())){
                    validCardsFromHand.add(new T11ExtendedLabel(new ImageIcon(card.getSelectedCardImage()), card.getName()));
                    System.out.println("Added " + card.getName() + " to valid cards");
                }
            }
        }

        for (int i = 0; i < 3; i++){
            for (Card card : playerHand.getPublicHand()){
                System.out.println("Comparing " + card.getName() + " and " + selectedCards[i].getLabelID());
                if (card.getName().matches(selectedCards[i].getLabelID())){
                    validCardsFromHand.add(new T11ExtendedLabel(new ImageIcon(card.getSelectedCardImage()), card.getName()));
                    System.out.println("Added " + card.getName() + " to valid cards");
                }
            }
        }
        return validCardsFromHand;
    }

    private ArrayList<ImageIcon> getSelectedIcons(int nextPlayer, int flag){
        PlayerHand playerHand = gameScreen.getGamePlayers().getPlayer(nextPlayer).getPlayerHand();
        ArrayList<ImageIcon> icons = new ArrayList<>();

        if (flag == 0){
            for (int i = 0; i < 3; i++){
                for (Card card : playerHand.getPlayerHand()){
                    if (card.getName().matches(selectedCards[i].getLabelID())){
                        icons.add(new ImageIcon(card.getSelectedCardImage()));
                    }
                }
            }

            for (int i = 0; i < playerHand.getPublicHand().size(); i++){
                for (Card card : playerHand.getPublicHand()){
                    if (card.getName().matches(selectedCards[i].getLabelID())){
                        icons.add(new ImageIcon(card.getSelectedCardImage()));
                    }
                }
            }
        } else if (flag == 1){
            for (int i = 0; i < 3; i++){
                for (Card card : playerHand.getPlayerHand()){
                    if (card.getName().matches(selectedCards[i].getLabelID())){
                        icons.add(new ImageIcon(card.getCardImage()));
                    }
                }
            }

            for (int i = 0; i < playerHand.getPublicHand().size(); i++){
                for (Card card : playerHand.getPublicHand()){
                    if (card.getName().matches(selectedCards[i].getLabelID())){
                        icons.add(new ImageIcon(card.getCardImage()));
                    }
                }
            }
        }

        return icons;
    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);

        g2.setColor(Color.DARK_GRAY);

        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2.drawRect(0, 0, this.getWidth(), this.getHeight());
        g2.setColor(Color.BLACK);
    }

    private class AnimateCard extends SwingWorker<Integer, String>{

        private int animateFlag;
        private int index;

        private T11ExtendedLabel[] cards;
        private T11ExtendedLabel cardToAnimate;

        private int heightUp;
        private int heightDown;

        private final int distance = 2, delay = 4;
        private final int moveDistance = 1, moveDelay = 2;
        private final int fastDistance = 4;

        public AnimateCard(T11ExtendedLabel[] cards, int index, int animateFlag, int heightDown, int heightUp ){
            this.cards = cards;
            this.index = index;
            this.cardToAnimate = cards[index];
            this.animateFlag = animateFlag;
            this.heightDown = heightDown;
            this.heightUp = heightUp;
        }


        public AnimateCard(T11ExtendedLabel cardToAnimate, int index, int animateFlag){
            this.cardToAnimate = cardToAnimate;
            this.index = index;
            this.animateFlag = animateFlag;
        }

        @Override
        protected Integer doInBackground() throws Exception{
            process(new ArrayList<>());

            switch (this.animateFlag) {

                //Cases of both weapon and player cards moving up and down on hover//
                //Moving Up
                case (0) :

                    while (cardToAnimate.getY() != heightUp) {
                        this.cardToAnimate.setLocation(this.cardToAnimate.getX(), this.cardToAnimate.getY() - this.distance);
                        process(new ArrayList<>());
                        Thread.sleep(delay);
                    }
                    break;

                //Moving Down
                case (1) :
                    Thread.sleep(100);

                    while (cardToAnimate.getY() != heightDown ) {
                        this.cardToAnimate.setLocation(this.cardToAnimate.getX(), this.cardToAnimate.getY() + this.distance);
                        process(new ArrayList<>());
                        Thread.sleep(delay);
                    }

                    cards[index].setEnabled(true);

                    break;
                /////////////////////////////////////////////////////////////////////

                //Case for moving the weapon cards onto the screen from the right hand side of the panel

                //Moving weapon cards
                case (3):
                    moving = true;
                    /*
                    while (cards[0].getX()!= cardPoints[0].getX()) {

                        if (cards[0].getX() <= cardPoints[0].getX()){
                            cards[0].setLocation((int)cardPoints[0].getX(), cards[0].getY());
                        } else{
                            cards[0].setLocation( (cards[0].getX() - fastDistance), cards[0].getY());
                        }

                        if (cards[1].getX() <= cardPoints[1].getX()){
                            cards[1].setLocation((int)cardPoints[1].getX(), cards[1].getY());
                        } else{
                            cards[1].setLocation( (cards[1].getX() - fastDistance), cards[1].getY());
                        }

                        if (cards[2].getX() <= cardPoints[2].getX()){
                            cards[2].setLocation((int)cardPoints[2].getX(), cards[2].getY());
                        } else{
                            cards[2].setLocation( (cards[2].getX() - fastDistance), cards[2].getY());
                        }
                        if (cards[3].getX() <= cardPoints[3].getX()){
                            cards[3].setLocation((int)cardPoints[3].getX(), cards[3].getY());
                        } else{
                            cards[3].setLocation( (cards[3].getX() - fastDistance), cards[3].getY());
                        }
                        if (cards[4].getX() <= cardPoints[4].getX()){
                            cards[4].setLocation((int)cardPoints[4].getX(), cards[4].getY());
                        } else{
                            cards[4].setLocation( (cards[4].getX() - fastDistance), cards[4].getY());
                        }
                        if (cards[5].getX() <= cardPoints[5].getX()){
                            cards[5].setLocation((int)cardPoints[5].getX(), cards[5].getY());
                        } else{
                            cards[5].setLocation( (cards[5].getX() - fastDistance), cards[5].getY());
                        }
                        process(new ArrayList<>());
                        Thread.sleep(delay);
                    }
                    moving = false;
                    inWeaponState = true;
                    System.out.println("Finished moving weapon cards onto screen");
                    break;
*/
                //Case for moving the selected weapon card up to the top right hand corner of the panel and moving the others of the screen

                case (6) :

                    while (cardToAnimate.getX() != (selectedCards[0].getX())){
                        cardToAnimate.setLocation(cardToAnimate.getX() + moveDistance, cardToAnimate.getY());
                        if (cardToAnimate.getX() >= selectedCards[0].getX()){
                            cardToAnimate.setLocation(selectedCards[0].getX(), cardToAnimate.getY());
                        }

                        process(new ArrayList<>());
                        Thread.sleep(delay);
                    }
                    gameScreen.setTab(2);
                    break;

                default:
                    System.out.println("Default");
            }

            process(new ArrayList<>());
            return null;
        }

        @Override
        protected void process(List<String> chunks) {
            revalidate();
        }

    }

    private class AnimateCorner extends SwingWorker<Integer, String>{
        private final int distance = 2, delay= 4;

        private T11ExtendedLabel labelToAnimate;
        private T11ExtendedLabel[] labels;

        private final int playerTargetX = getWidth()/6 ;//- roomCards[0].getWidth()/2;
        private final int weaponTargetX = ((getWidth()/3)*2) - (int)(resolution.getScalePercentage() * 10) ;//- roomCards[0].getWidth()/2);

        private final int targetY = (int)(resolution.getScalePercentage() * 20);

        private int targetH;
        private int targetW;

        private int flag;
        private int index;

        private boolean weaponsAdded = false;

        public AnimateCorner(int flag, T11ExtendedLabel label, T11ExtendedLabel[] labels, int index){
            this.flag = flag;
            this.labels = labels;
            this.index = index;
            this.labelToAnimate = labels[index];
            this.targetH = roomCards[0].getHeight();
            this.targetW = roomCards[0].getWidth();

        }

        @Override
        protected Integer doInBackground() throws Exception{

            process(new ArrayList<>());

            switch (flag){
                //Player
                case (0):
                    moving = true;
                    moveToCorner(playerTargetX);
                    moveCardsOffScreen();
                    if (!weaponsAdded){
                        selected = false;
                        addWeaponCards();
                        weaponsAdded = true;
                    }

                    moving = false;
                    break;

                //Weapon
                case (2):
                    moving = true;
                    moveToCorner(weaponTargetX);
                    moveCardsOffScreen();
                    System.out.println("Here");
                    moving = false;
                    question();
                    break;
            }
            process(new ArrayList<>());
            return null;
        }

        private void moveToCorner(int targetX) throws Exception{
            int scalar = 1;

            int currX = labelToAnimate.getX();
            int currY = labelToAnimate.getY();

            int currH = labelToAnimate.getHeight();
            int currW = labelToAnimate.getWidth();

            while (currX != targetX || currY != targetY ){

                if (currX < targetX){
                    currX += distance;
                } else if (currX > targetX){
                    currX -= distance;
                }

                if (currY <= targetY){
                    currY = targetY;
                } else{
                    currY -= distance;
                }

                if (currH >= targetH){
                    currH = targetH;
                } else {
                    currH += scalar;
                }

                if (currW >= targetW){
                    currW = targetW;
                } else {
                    currW += scalar;
                }

                labelToAnimate.setLocation(currX, currY);
                labelToAnimate.setSize(new Dimension(currW, currH));

                process(new ArrayList<>());
                Thread.sleep(delay);
            }

            selectedCards[flag].setLocation(labelToAnimate.getX(), labelToAnimate.getY());
            selectedCards[flag].setSize(labelToAnimate.getSize());
            add(selectedCards[flag]);
            remove(labelToAnimate);
        }

        private void moveCardsOffScreen() throws Exception{
            //Get Remaining Cards

            ArrayList<T11ExtendedLabel> remainingLabels = new ArrayList<>(5);

            for (int i = 0; i < labels.length; i++){
                if (i != this.index){
                    remainingLabels.add(labels[i]);
                }
            }

            while (remainingLabels.get(remainingLabels.size() - 1).getX() > 0 - remainingLabels.get(0).getIcon().getIconWidth()){
                remainingLabels.get(0).setLocation(remainingLabels.get(0).getX() - distance - 2, remainingLabels.get(0).getY());
                remainingLabels.get(1).setLocation(remainingLabels.get(1).getX() - distance - 2, remainingLabels.get(1).getY());
                remainingLabels.get(2).setLocation(remainingLabels.get(2).getX() - distance - 2, remainingLabels.get(2).getY());
                remainingLabels.get(3).setLocation(remainingLabels.get(3).getX() - distance - 2, remainingLabels.get(3).getY());
                remainingLabels.get(4).setLocation(remainingLabels.get(4).getX() - distance - 2, remainingLabels.get(4).getY());

                process(new ArrayList<>());
                Thread.sleep(delay);
            }

            for (T11ExtendedLabel label : remainingLabels){
                remove(label);
            }
        }

        @Override
        protected void process(List<String> chunks){
            labelToAnimate.revalidate();
        }
    }

    private class AnimateNextCards extends SwingWorker<Integer, String> {

        private final int distance = 4, delay = 4;

        private ArrayList<T11ExtendedLabel> cards;

        public AnimateNextCards(ArrayList<T11ExtendedLabel> cards){
            this.cards = cards;
        }

        @Override
        protected Integer doInBackground() throws Exception{
            process(new ArrayList<>());
            moving = true;
            switch (cards.size()){
                case (1):
                    System.out.println("Flag 1");
                    while (cards.get(0).getX() != getWidth()/2){
                        System.out.println(cards.get(0).getLocation());
                        cards.get(0).setLocation(cards.get(0).getX() - distance, cards.get(0).getY());
                        process(new ArrayList<>());
                        Thread.sleep(delay);
                    }
                    break;

                case (2):
                    System.out.println("Flag 2");
                    while (cards.get(0).getX() != (getWidth()/2 - cards.get(0).getWidth()/2) || cards.get(1).getX() != (selectedCards[2].getX() +  cards.get(0).getWidth()/13)){

                        if (cards.get(0).getX() <= (getWidth()/2 - cards.get(0).getWidth()/2) ){
                            cards.get(0).setLocation((getWidth()/2 - cards.get(0).getWidth()/2), cards.get(0).getY());
                        } else{
                            cards.get(0).setLocation(cards.get(0).getX() - distance , cards.get(0).getY());
                        }

                        if (cards.get(1).getX() <= (selectedCards[2].getX() + cards.get(0).getWidth()/13)){
                            cards.get(1).setLocation((selectedCards[2].getX() + cards.get(0).getWidth()/13), cards.get(1).getY());
                        } else{
                            cards.get(1).setLocation(cards.get(1).getX() - distance , cards.get(1).getY());
                        }
                        process(new ArrayList<>());
                        Thread.sleep(delay);
                    }
                    break;

                case (3):
                    while ((cards.get(0).getX()  != (selectedCards[0].getX())) ||
                            (cards.get(1).getX() != selectedCards[1].getX())  ||
                            (cards.get(2).getX() != (selectedCards[2].getX()))) {

                        if (cards.get(0).getX() <= selectedCards[0].getX()) {
                            cards.get(0).setLocation(selectedCards[0].getX(), cards.get(0).getY());
                        } else {
                            cards.get(0).setLocation(cards.get(0).getX() - distance, cards.get(0).getY());
                        }

                        if (cards.get(1).getX() <= selectedCards[1].getX()) {
                            cards.get(1).setLocation(selectedCards[1].getX(), cards.get(1).getY());
                        } else {
                            cards.get(1).setLocation(cards.get(1).getX() - distance, cards.get(1).getY());
                        }

                        if (cards.get(2).getX() <= selectedCards[2].getX()) {
                            cards.get(2).setLocation(selectedCards[2].getX(), cards.get(2).getY());
                        } else {
                            cards.get(2).setLocation(cards.get(2).getX() - distance, cards.get(2).getY());
                        }

                        process(new ArrayList<>());
                        Thread.sleep(delay);
                    }
                    break;
            }
            moving = false;
            process(new ArrayList<>());
            Thread.sleep(delay);
            return null;
        }

        @Override
        protected void process(List<String>chunks){
            revalidate();
        }
    }

    private class T11ExtendedLabel extends T11Label{
        String name;

        public T11ExtendedLabel(ImageIcon icon, String name){
            super(icon);
            this.name = name;
            //System.out.println("Created instance with name: " + name);
        }

        public String getLabelID(){
            return this.name;
        }
    }

}

