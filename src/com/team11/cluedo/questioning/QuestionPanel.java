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
import com.team11.cluedo.cards.WeaponCard;
import com.team11.cluedo.components.T11Label;
import com.team11.cluedo.players.PlayerHand;
import com.team11.cluedo.suspects.Suspect;
import com.team11.cluedo.suspects.SuspectData;
import com.team11.cluedo.ui.GameScreen;
import com.team11.cluedo.ui.Resolution;
import com.team11.cluedo.weapons.WeaponData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class QuestionPanel extends JPanel {

    private Assets gameAssets = new Assets();

    //Player Cards (Not Selected)
    private ImageIcon[] playerIcons;

    private ImageIcon[] selectedPlayerIcons;

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

    private T11ExtendedLabel[] cardLabels;
    private T11ExtendedLabel[] roomCards;


    public QuestionPanel(GameScreen gameScreen, Resolution resolution) {
        this.gameScreen = gameScreen;
        this.resolution = resolution;

        this.setLayout(null);
        this.setLocation((int)(10 * resolution.getScalePercentage()),(int)(100 * resolution.getScalePercentage()));
        this.setSize((int)(760 * resolution.getScalePercentage()),(int)(600 * resolution.getScalePercentage()));

        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

    }

    private int sumWidth(int index){
        int sum = 0;

        for (int i = 0; i < index; i++){
            sum += cardLabels[i].getIcon().getIconWidth() * (resolution.getScalePercentage() * 0.42);
        }
        return sum;
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
        this.setSelected(false);
        this.removeAll();
        this.setVisible(false);
    }

    private void displayPanel(int roomNum){
        SuspectData suspectData = new SuspectData();
        RoomData roomData = new RoomData();
        moving = false;
        selectedPlayerIcons = new ImageIcon[]{
                new ImageIcon(gameAssets.getSelectedWhiteCard()),
                new ImageIcon(gameAssets.getSelectedGreenCard()),
                new ImageIcon(gameAssets.getSelectedPeacockCard()),
                new ImageIcon(gameAssets.getSelectedPlumCard()),
                new ImageIcon(gameAssets.getSelectedScarletCard()),
                new ImageIcon(gameAssets.getSelectedMustardCard()),
        };
        playerIcons = new ImageIcon[]{
                new ImageIcon(gameAssets.getWhiteCard()),
                new ImageIcon(gameAssets.getGreenCard()),
                new ImageIcon(gameAssets.getPeacockCard()),
                new ImageIcon(gameAssets.getPlumCard()),
                new ImageIcon(gameAssets.getScarletCard()),
                new ImageIcon(gameAssets.getMustardCard()),
        };

        cardLabels = new T11ExtendedLabel[]{
                new T11ExtendedLabel(playerIcons[0], suspectData.getSuspectName(0)),
                new T11ExtendedLabel(playerIcons[1], suspectData.getSuspectName(1)),
                new T11ExtendedLabel(playerIcons[2], suspectData.getSuspectName(2)),
                new T11ExtendedLabel(playerIcons[3], suspectData.getSuspectName(3)),
                new T11ExtendedLabel(playerIcons[4], suspectData.getSuspectName(4)),
                new T11ExtendedLabel(playerIcons[5], suspectData.getSuspectName(5)),
        };
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
            roomCards[i].setSize(new Dimension((int)(roomCards[i].getIcon().getIconWidth() * (resolution.getScalePercentage() * 0.66)), ((int)(roomCards[i].getIcon().getIconHeight() * (resolution.getScalePercentage() * 0.66)))));
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

    public void setSelected(boolean bool){
        this.selected = bool;
    }

    private void addWeaponCards(){
        WeaponData weaponData = new WeaponData();

        ImageIcon[] weaponsCards = new ImageIcon[]{
                new ImageIcon(gameAssets.getHatchetCard()),
                new ImageIcon(gameAssets.getDaggerCard()),
                new ImageIcon(gameAssets.getPoisonCard()),
                new ImageIcon(gameAssets.getRevolverCard()),
                new ImageIcon(gameAssets.getRopeCard()),
                new ImageIcon(gameAssets.getWrenchCard()),
        };

        ImageIcon[] selectedWeaponCards = new ImageIcon[]{
                new ImageIcon(gameAssets.getSelectedHatchetCard()),
                new ImageIcon(gameAssets.getSelectedDaggerCard()),
                new ImageIcon(gameAssets.getSelectedPoisonCard()),
                new ImageIcon(gameAssets.getSelectedRevolverCard()),
                new ImageIcon(gameAssets.getSelectedRopeCard()),
                new ImageIcon(gameAssets.getSelectedWrenchCard()),
        };

        T11ExtendedLabel[] weaponsLabels = new T11ExtendedLabel[]{
                new T11ExtendedLabel(weaponsCards[0], weaponData.getWeaponName(0)),
                new T11ExtendedLabel(weaponsCards[1], weaponData.getWeaponName(1)),
                new T11ExtendedLabel(weaponsCards[2], weaponData.getWeaponName(2)),
                new T11ExtendedLabel(weaponsCards[3], weaponData.getWeaponName(3)),
                new T11ExtendedLabel(weaponsCards[4], weaponData.getWeaponName(4)),
                new T11ExtendedLabel(weaponsCards[5], weaponData.getWeaponName(5)),
        };

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

                        selectedCards[0].setLocation(getWidth()/6 - roomCards[0].getWidth()/2, (int)(resolution.getScalePercentage() * 20));
                        add(selectedCards[0]);
                        revalidate();
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
        int nextPlayer;
        boolean hasAnswered = true;
        if (currentPlayer + 1 == gameScreen.getGamePlayers().getPlayerCount()){
            nextPlayer = 0;
        } else{
            nextPlayer = currentPlayer + 1;
        }

        remove(selectedCards[0]);
        remove(selectedCards[2]);

        selectedCards[0].setLocation(getWidth()/6 - roomCards[0].getWidth()/2, (int)(resolution.getScalePercentage() * 20));
        selectedCards[2].setLocation(((getWidth()/3)*2 + (getWidth()/6) - roomCards[0].getWidth()/2), (int)(resolution.getScalePercentage() * 20));

        add(selectedCards[0]);
        add(selectedCards[2]);
        revalidate();

        T11ExtendedLabel nextCard = new T11ExtendedLabel(playerIcons[nextPlayer], cardLabels[nextPlayer].getName());

        nextCard.setSize(new Dimension(selectedPlayer.getWidth(), selectedPlayer.getHeight()));
        nextCard.setLocation(0, (int)(this.getHeight() - (nextCard.getIcon().getIconHeight() * (resolution.getScalePercentage() * 0.42)))
                - (int)(resolution.getScalePercentage() * 125));
        this.add(nextCard);
        new AnimateCard(nextCard, nextPlayer, 6).execute();

        //while (!hasAnswered) {
            ArrayList<T11ExtendedLabel> validCards = findValidSelectedCards(nextPlayer);

            ArrayList<ImageIcon> icons = getSelectedIcons(nextPlayer, 1);
            ArrayList<ImageIcon> selectedIcons = getSelectedIcons(nextPlayer, 0);

            System.out.println(icons.size());
            System.out.println(selectedIcons.size());

            int extraWidth = 0;
            for (int i = 0; i < validCards.size(); i++){

                if (validCards.size() == 1){
                    validCards.get(i).setLocation(this.getWidth(), nextCard.getY());
                    validCards.get(i).setSize(new Dimension(selectedCards[1].getWidth(), selectedCards[1].getHeight()));
                } else if (validCards.size() == 2){


                    if ( (int)(resolution.getScalePercentage() * 10) % 2 != 0){
                        validCards.get(i).setLocation(this.getWidth() + sumWidth(i) + extraWidth + 1, nextCard.getY() + (int)(resolution.getScalePercentage() * 20));
                        System.out.println(validCards.get(i).getLocation());
                    }
                    else{
                        validCards.get(i).setLocation(this.getWidth() + sumWidth(i) + extraWidth, nextCard.getY() + (int)(resolution.getScalePercentage() * 20));
                        System.out.println(validCards.get(i).getLocation());
                    }

                    validCards.get(i).setSize(new Dimension((int)(nextCard.getWidth() * (resolution.getScalePercentage() * 0.9)), (int)(nextCard.getHeight() * (resolution.getScalePercentage() * 0.9))));

                } else if (validCards.size() == 3){
                    validCards.get(i).setLocation(this.getWidth() + sumWidth(i) + extraWidth, nextCard.getY() + (int)(resolution.getScalePercentage() * 30));
                    validCards.get(i).setSize(new Dimension((int)(nextCard.getWidth() * (resolution.getScalePercentage() * 0.7)), (int)(nextCard.getHeight() * (resolution.getScalePercentage() * 0.7))));
                }
                extraWidth += (int)(resolution.getScalePercentage() * 220);
                this.add(validCards.get(i));
            }



            switch (validCards.size()) {
                case (0):
                    JLabel doneButton = new JLabel();
                    doneButton.setText("DONE");
                    doneButton.setFont(new Font("Bulky Pixels", Font.BOLD, (int) (35 * resolution.getScalePercentage())));
                    doneButton.setHorizontalAlignment(SwingConstants.CENTER);
                    doneButton.setVerticalAlignment(SwingConstants.CENTER);

                    doneButton.setLocation(selectedCards[1].getX() + selectedCards[1].getIcon().getIconWidth() / 2
                            + (int) (20 * resolution.getScalePercentage()), nextCard.getY() + nextCard.getIcon().getIconHeight() / 6);

                    doneButton.setSize(new Dimension((int) (resolution.getScalePercentage() * 150), (int) (resolution.getScalePercentage() * 50)));
                    doneButton.setForeground(Color.WHITE);
                    doneButton.setBackground(Color.DARK_GRAY);
                    doneButton.setLayout(null);

                    this.add(doneButton);
                    this.validate();

                    doneButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            super.mouseClicked(e);
                            doneButton.setForeground(Color.RED);

                            //Show confirmation of change

                            //Update the next players cards

                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            super.mouseEntered(e);
                            doneButton.setForeground(Color.RED);
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            super.mouseExited(e);
                            doneButton.setForeground(Color.WHITE);
                        }
                    });

                    break;

                //Player only has one valid card that they can show
                case (1):
                     new AnimateNextCards(validCards).execute();

                    break;

                case (2):
                    JLabel infoLabel = new JLabel("Pick a Card to Show");
                    infoLabel.setSize((int)(resolution.getScalePercentage() * 400), (int)(resolution.getScalePercentage() * 70));
                    infoLabel.setLocation((getWidth()/2 - validCards.get(0).getWidth()/2),validCards.get(0).getY() - (int)(resolution.getScalePercentage() * 70));

                    infoLabel.setForeground(Color.WHITE);
                    infoLabel.setBackground(Color.DARK_GRAY);
                    infoLabel.setFont(new Font("Bulky Pixels", Font.BOLD, (int) (18 * resolution.getScalePercentage())));
                    infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    add(infoLabel);

                    infoLabel.revalidate();
                    System.out.println(infoLabel.getSize());
                    System.out.println(infoLabel.getLocation());
                    new AnimateNextCards(validCards).execute();

                    break;

                case (3):
                    new AnimateNextCards(validCards).execute();
                    break;
            }

            moving = false;
            for (int i = 0; i < validCards.size(); i++){
                int finalI = i;
                System.out.println(moving);
                validCards.get(i).addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        if (!moving){
                            validCards.get(finalI).setIcon(icons.get(finalI));
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        if (!moving) {
                            validCards.get(finalI).setIcon(icons.get(finalI));
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);
                        if (!moving){
                            validCards.get(finalI).setIcon(selectedIcons.get(finalI));
                        }
                    }
                });
            }
    }

    private ArrayList<T11ExtendedLabel> findValidSelectedCards(int nextPlayer){
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

        private final int playerTargetX = getWidth()/6 - roomCards[0].getWidth()/2;
        private final int weaponTargetX = ((getWidth()/3)*2 + (getWidth()/6) - roomCards[0].getWidth()/2);

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

        private final int distance = 2, delay = 4;

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
                    while (cards.get(0).getX() != selectedCards[2].getX()){
                        cards.get(0).setLocation(cards.get(0).getX() - distance, cards.get(0).getY());
                        process(new ArrayList<>());
                        Thread.sleep(delay);
                    }
                    break;

                case (2):
                    while (cards.get(0).getX() != (getWidth()/2 - cards.get(0).getWidth()/2) || cards.get(1).getX() != (selectedCards[2].getX() +  cards.get(0).getWidth()/13)){

                        if (cards.get(0).getX() <= (getWidth()/2 - cards.get(0).getWidth()/2) ){
                            cards.get(0).setLocation((getWidth()/2 - cards.get(0).getWidth()/2), cards.get(0).getY());
                        } else{
                            cards.get(0).setLocation(cards.get(0).getX() - distance - 2, cards.get(0).getY());
                        }

                        if (cards.get(1).getX() <= (selectedCards[2].getX() + cards.get(0).getWidth()/13)){
                            cards.get(1).setLocation((selectedCards[2].getX() + cards.get(0).getWidth()/13), cards.get(1).getY());
                        } else{
                            cards.get(1).setLocation(cards.get(1).getX() - distance - 2 , cards.get(1).getY());
                        }
                        process(new ArrayList<>());
                        Thread.sleep(delay);
                    }
                    break;

                case (3):
                    while ((cards.get(0).getX() != (selectedCards[1].getX() - (int)(resolution.getScalePercentage() * 20))
                            || (cards.get(1).getX() != (selectedCards[1].getX() + selectedCards[1].getWidth()) + (int)(resolution.getScalePercentage() * 10) - (int)(resolution.getScalePercentage() * 20))
                            || (cards.get(1).getX() != (selectedCards[1].getX() +  selectedCards[1].getWidth() * 2) + (int)(resolution.getScalePercentage() * 10) - (int)(resolution.getScalePercentage() * 20)) )){

                        if ((cards.get(0).getX() <= (selectedCards[1].getX() - (int)(resolution.getScalePercentage() * 20)))){
                            cards.get(0).setLocation( selectedCards[1].getX() - (int)(resolution.getScalePercentage() * 20), cards.get(0).getY());
                        } else {
                            cards.get(0).setLocation(cards.get(0).getX() - distance-2, cards.get(0).getY());
                        }
                        if ((cards.get(1).getX() <= (selectedCards[1].getX() + selectedCards[1].getWidth()) + (int)(resolution.getScalePercentage() * 10) - (int)(resolution.getScalePercentage() * 20))){
                            cards.get(1).setLocation( selectedCards[1].getX() + selectedCards[1].getWidth() + (int)(resolution.getScalePercentage() * 10) - (int)(resolution.getScalePercentage() * 20), cards.get(0).getY());
                        } else {
                            cards.get(1).setLocation(cards.get(1).getX() - distance-2, cards.get(1).getY());
                        }
                        if ((cards.get(2).getX() <= (selectedCards[1].getX() +  selectedCards[1].getWidth() * 2) + (int)(resolution.getScalePercentage() *10) - (int)(resolution.getScalePercentage() * 20))){
                            cards.get(2).setLocation( (selectedCards[1].getX() +  selectedCards[1].getWidth() * 2) + (int)(resolution.getScalePercentage() * 10) - (int)(resolution.getScalePercentage() * 20), cards.get(0).getY());
                        } else {
                            cards.get(2).setLocation(cards.get(2).getX() - distance-2, cards.get(2).getY());
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

