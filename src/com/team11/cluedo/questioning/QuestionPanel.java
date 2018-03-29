/*
 * Code to handle the question panel
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
*/
package com.team11.cluedo.questioning;

import com.team11.cluedo.assets.Assets;
import com.team11.cluedo.cards.Card;
import com.team11.cluedo.components.T11Label;
import com.team11.cluedo.suspects.SuspectData;
import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class QuestionPanel extends JPanel {

    private Assets gameAssets = new Assets();

    //Player Cards (Not Selected)
    private ImageIcon[] playerIcons = new ImageIcon[]{
            new ImageIcon(gameAssets.getWhiteCard()),
            new ImageIcon(gameAssets.getGreenCard()),
            new ImageIcon(gameAssets.getPeacockCard()),
            new ImageIcon(gameAssets.getPlumCard()),
            new ImageIcon(gameAssets.getScarletCard()),
            new ImageIcon(gameAssets.getMustardCard()),
    };

    private ImageIcon[] selectedPlayerIcons = new ImageIcon[]{
            new ImageIcon(gameAssets.getSelectedWhiteCard()),
            new ImageIcon(gameAssets.getSelectedGreenCard()),
            new ImageIcon(gameAssets.getSelectedPeacockCard()),
            new ImageIcon(gameAssets.getSelectedPlumCard()),
            new ImageIcon(gameAssets.getSelectedScarletCard()),
            new ImageIcon(gameAssets.getSelectedMustardCard()),
    };

    private T11Label[] cardLabels = new T11Label[]{
            new T11Label(new ImageIcon(gameAssets.getWhiteCard())),
            new T11Label(new ImageIcon(gameAssets.getGreenCard())),
            new T11Label(new ImageIcon(gameAssets.getPeacockCard())),
            new T11Label(new ImageIcon(gameAssets.getPlumCard())),
            new T11Label(new ImageIcon(gameAssets.getScarletCard())),
            new T11Label(new ImageIcon(gameAssets.getMustardCard())),
    };

    private T11Label[] roomCards = new T11Label[]{
            new T11Label(new ImageIcon(gameAssets.getKitchenCard())),
            new T11Label(new ImageIcon(gameAssets.getBallroomCard())),
            new T11Label(new ImageIcon(gameAssets.getConservatoryCard())),
            new T11Label(new ImageIcon(gameAssets.getDiningCard())),
            new T11Label(new ImageIcon(gameAssets.getBilliardCard())),
            new T11Label(new ImageIcon(gameAssets.getLibraryCard())),
            new T11Label(new ImageIcon(gameAssets.getLoungeCard())),
            new T11Label(new ImageIcon(gameAssets.getHallCard())),
            new T11Label(new ImageIcon(gameAssets.getStudyCard())),
    };

    private Resolution resolution;

    private Point[] cardPoints = new Point[6];

    private double scale = 0.42;

    private int cardYDown, cardYUp;
    private int playerTargetX, playerTargetY;
    private int weaponTargetX, weaponTargetY;

    private boolean selected;
    private boolean moving = false;
    private boolean inWeaponState = false;

    private T11Label selectedPlayer;
    private T11Label selectedWeapon;
    private T11Label currentRoom;

    private T11Label[] selectedCards = new T11Label[3];

    public QuestionPanel(Resolution resolution) {
        this.resolution = resolution;

        this.setLayout(null);
        this.setLocation((int)(10 * resolution.getScalePercentage()),(int)(100 * resolution.getScalePercentage()));
        this.setSize((int)(760 * resolution.getScalePercentage()),(int)(600 * resolution.getScalePercentage()));

        this.cardYUp = (int)((this.getHeight() - (int)(resolution.getScalePercentage() * 100) - cardLabels[0].getIcon().getIconHeight() * scale) - (int)(resolution.getScalePercentage()*40));
        this.cardYDown =  this.cardYUp + (int)(resolution.getScalePercentage() * 40);

        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
    }

    private int sumWidth(int index){
        int sum = 0;

        for (int i = 0; i < index; i++){
            sum += cardLabels[i].getIcon().getIconWidth() * scale;
        }
        return sum;
    }

    public void displayQuestionPanel(int roomNum){
        this.setSelected(false);
        inWeaponState = false;
        selected = false;
        this.displayPanel(roomNum);
        this.setVisible(true);
        this.setFocusable(true);
    }

    public void hideQuestionPanel(){
        this.setSelected(false);
        this.removeAll();
        this.setVisible(false);
    }

    public void displayPanel(int roomNum){

        int extraWidth = 0;
        for (int i = 0; i < cardPoints.length; i++){
            cardLabels[i].setSize(new Dimension((int)(cardLabels[i].getIcon().getIconWidth() * this.scale), (int)(cardLabels[i].getIcon().getIconHeight() * scale)));
            cardLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            if (i == 0){
                cardPoints[i] = new Point((int)(resolution.getScalePercentage()*22),
                        (int)(this.getHeight() - cardLabels[i].getIcon().getIconHeight() * scale) - (int)(resolution.getScalePercentage() * 100));
            }

            else{
                cardPoints[i] = new Point((int)(resolution.getScalePercentage()*15) + (int)(resolution.getScalePercentage() * 5) + sumWidth(i) + extraWidth
                        , (int)(this.getHeight() - cardLabels[i].getIcon().getIconHeight() * scale) - (int)(resolution.getScalePercentage() * 100));
            }

            extraWidth += (int)(resolution.getScalePercentage() * 15);
        }

        for (int i = 0; i < roomCards.length; i++){
            roomCards[i].setSize(new Dimension((int)(roomCards[i].getIcon().getIconWidth() * 0.66), (int)(roomCards[i].getIcon().getIconHeight() * 0.66)));
        }


        roomCards[roomNum].setLocation(this.getWidth()/2 - ((roomCards[roomNum].getWidth()/2)), (int)(resolution.getScalePercentage() * 20));
        roomCards[roomNum].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        currentRoom = roomCards[roomNum];
        selectedCards[1] = currentRoom;

        this.add(roomCards[roomNum]);

        this.playerTargetX = this.getWidth()/6 - roomCards[roomNum].getWidth()/2;
        this.playerTargetY = (int)(resolution.getScalePercentage() * 20);

        this.weaponTargetX = ((this.getWidth()/3)*2 + (this.getWidth()/6) - roomCards[roomNum].getWidth()/2);
        this.weaponTargetY = (int)(resolution.getScalePercentage() * 20);


        for (int i = 0; i < cardLabels.length; i++){
            cardLabels[i].setLocation(cardPoints[i]);

            int finalI = i;
            cardLabels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (!selected && !inWeaponState && !moving){
                        setSelected(true);
                        new AnimateCard(cardLabels, finalI, 2, playerTargetX, playerTargetY).execute();
                        selectedPlayer = cardLabels[finalI];
                        selectedCards[0] = selectedPlayer;
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
        ImageIcon[] weaponsCards = new ImageIcon[]{
                new ImageIcon(gameAssets.getRopeCard()),
                new ImageIcon(gameAssets.getRevolverCard()),
                new ImageIcon(gameAssets.getDaggerCard()),
                new ImageIcon(gameAssets.getHatchetCard()),
                new ImageIcon(gameAssets.getPoisonCard()),
                new ImageIcon(gameAssets.getWrenchCard()),
        };

        ImageIcon[] selectedWeaponCards = new ImageIcon[]{
                new ImageIcon(gameAssets.getSelectedRopeCard()),
                new ImageIcon(gameAssets.getSelectedRevolverCard()),
                new ImageIcon(gameAssets.getSelectedDaggerCard()),
                new ImageIcon(gameAssets.getSelectedHatchetCard()),
                new ImageIcon(gameAssets.getSelectedPoisonCard()),
                new ImageIcon(gameAssets.getSelectedWrenchCard()),
        };

        T11Label[] weaponsLabels = new T11Label[]{
                new T11Label(weaponsCards[0]),
                new T11Label(weaponsCards[1]),
                new T11Label(weaponsCards[2]),
                new T11Label(weaponsCards[3]),
                new T11Label(weaponsCards[4]),
                new T11Label(weaponsCards[5]),
        };

        int extraWidth = 0;
        for (int i = 0; i < 6; i++){
            weaponsLabels[i].setLocation(getWidth() + sumWidth(i) + (int)(resolution.getScalePercentage()*22) + extraWidth,
                    (int)(this.getHeight() - weaponsLabels[i].getIcon().getIconHeight() * scale)
                            - (int)(resolution.getScalePercentage() * 100));
            weaponsLabels[i].setSize((int)(weaponsLabels[i].getIcon().getIconWidth() * this.scale), (int)(weaponsLabels[i].getIcon().getIconHeight() * scale));

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
                        new AnimateCard(weaponsLabels, finalI1, 4, weaponTargetX, weaponTargetY).execute();
                        selectedWeapon = weaponsLabels[finalI1];
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    System.out.println(selected + "  " + inWeaponState + "  " + moving);
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

    private T11Label[] question(){

        //new AnimateCard(selectedCards, 0 , 5, 0,0).execute();

        return selectedCards;
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

        private T11Label[] cards;

        private T11Label cardToAnimate;

        private int heightUp;
        private int heightDown;

        private final int distance = 2, delay = 4;
        private final int moveDistance = 1, moveDelay = 2;

        private final int fastDistance = 4;

        private boolean weaponsAdded = false;

        public AnimateCard(T11Label[] cards, int index, int animateFlag, int heightDown, int heightUp ){
            this.cards = cards;
            this.index = index;
            this.cardToAnimate = cards[index];
            this.animateFlag = animateFlag;
            this.heightDown = heightDown;
            this.heightUp = heightUp;
        }

        public void setAnimateFlag(int flag){
            this.animateFlag = flag;
        }

        public void setCardToAnimate(T11Label button){
            this.cardToAnimate = button;
        }

        @Override
        protected Integer doInBackground() throws Exception{
            process(new ArrayList<>());

            int targetW = roomCards[index].getWidth();
            int targetH = roomCards[index].getHeight();

            int nextH = cardToAnimate.getHeight();
            int nextW = cardToAnimate.getWidth();
            int nextX = cardToAnimate.getX();
            int nextY = cardToAnimate.getY();

            int scaleModifier = 1;

            switch (this.animateFlag) {
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

                //Moving Up to Room
                case (2) :
                    ArrayList<T11Label> remainingPlayerCards = new ArrayList<>(5);

                    moveToCorner(nextH, nextW, targetH, targetW, scaleModifier, nextX, nextY, playerTargetX, playerTargetY, remainingPlayerCards);

                    if (!weaponsAdded){
                        selected = false;
                        addWeaponCards();
                        weaponsAdded = true;
                    }

                    break;

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
                    break;
                case (4) :
                    ArrayList<T11Label> remainingWeaponCards = new ArrayList<>(5);

                    //Card is to the left of the room card
                    moveToCorner(nextH, nextW, targetH, targetW, scaleModifier, nextX, nextY,weaponTargetX, weaponTargetY, remainingWeaponCards);


                    selectedCards[2] = cardToAnimate;

                    for (T11Label label : remainingWeaponCards){
                        remove(label);
                    }

                    Thread.sleep(500);

                    int desiredY = getHeight()/2 - cards[0].getIcon().getIconHeight()/2 + (int)(resolution.getScalePercentage() * 75);

                    while (selectedCards[0].getY() != desiredY || selectedCards[1].getY() != desiredY || selectedCards[2].getY() != desiredY){

                        if (selectedCards[0].getY() >= desiredY){
                            selectedCards[0].setLocation(selectedCards[0].getX(), desiredY);
                        } else{
                            selectedCards[0].setLocation(selectedCards[0].getX(), selectedCards[0].getY() + distance);
                        }

                        if (selectedCards[1].getY() >= desiredY){
                            selectedCards[1].setLocation(selectedCards[1].getX(), desiredY);
                        } else{
                            selectedCards[1].setLocation(selectedCards[1].getX(), selectedCards[1].getY() + distance );
                        }

                        if (selectedCards[2].getY() >= desiredY){
                            selectedCards[2].setLocation(selectedCards[2].getX(), desiredY);
                        } else{
                            selectedCards[2].setLocation(selectedCards[2].getX(), selectedCards[2].getY() + distance );
                        }

                        process(new ArrayList<>());
                        Thread.sleep(delay);
                    }
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

        protected void moveToCorner(int nextH, int nextW, int targetH, int targetW, int scaleModifier, int currX, int currY, int targetX, int targetY, ArrayList<T11Label> remainingCards) throws Exception{

            System.out.println("Target Y: " + targetY);

            while((cardToAnimate.getX() != targetX) || (cardToAnimate.getY() != targetY)){
                if (nextH >= targetH){
                    nextH = targetH;
                } else{
                    nextH += scaleModifier;
                }

                if (nextW >= targetW){
                    nextW = targetW;
                } else {
                    nextW += scaleModifier;
                }

                if (currX <= targetX){
                    currX += moveDistance;
                } else if (currX > targetX){
                    currX -= moveDistance;
                }

                if (currY <= targetY){
                    currY = targetY;
                } else {
                    currY -= moveDistance;
                }

                cardToAnimate.setSize(new Dimension(nextW, nextH));
                cardToAnimate.setLocation(currX, currY);

                process(new ArrayList<>());
                Thread.sleep(moveDelay);
            }

        for (int i = 0; i < cards.length; i++){
            if (i != this.index){
                remainingCards.add(cards[i]);
            }
        }

        while (remainingCards.get(remainingCards.size()-1).getX() + remainingCards.get(0).getWidth() > 0){
            remainingCards.get(0).setLocation(remainingCards.get(0).getX() - fastDistance, remainingCards.get(0).getY());
            remainingCards.get(1).setLocation(remainingCards.get(1).getX() - fastDistance, remainingCards.get(1).getY());
            remainingCards.get(2).setLocation(remainingCards.get(2).getX() - fastDistance, remainingCards.get(2).getY());
            remainingCards.get(3).setLocation(remainingCards.get(3).getX() - fastDistance, remainingCards.get(3).getY());
            remainingCards.get(4).setLocation(remainingCards.get(4).getX() - fastDistance, remainingCards.get(4).getY());

            process(new ArrayList<>());
            Thread.sleep(delay);
        }
        for (T11Label label : remainingCards){
            remove(label);
        }

    }

    }


}

