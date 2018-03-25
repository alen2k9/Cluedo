package com.team11.cluedo.questioning;

import com.team11.cluedo.assets.Assets;
import com.team11.cluedo.cards.Card;
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

    private CardLabel[] cardLabels = new CardLabel[]{
            new CardLabel(new ImageIcon(gameAssets.getSelectedWhiteCard())),
            new CardLabel(new ImageIcon(gameAssets.getSelectedGreenCard())),
            new CardLabel(new ImageIcon(gameAssets.getSelectedPeacockCard())),
            new CardLabel(new ImageIcon(gameAssets.getSelectedPlumCard())),
            new CardLabel(new ImageIcon(gameAssets.getSelectedScarletCard())),
            new CardLabel(new ImageIcon(gameAssets.getSelectedMustardCard())),
    };

    /*
    private CardLabel[] weaponsCards = new CardLabel[]{
            new CardLabel(new ImageIcon(gameAssets.getRopeCard())),
            new CardLabel(new ImageIcon(gameAssets.getRevolverCard())),
            new CardLabel(new ImageIcon(gameAssets.getDaggerCard())),
            new CardLabel(new ImageIcon(gameAssets.getHatchetCard())),
            new CardLabel(new ImageIcon(gameAssets.getPoisonCard())),
            new CardLabel(new ImageIcon(gameAssets.getWrenchCard())),
    };

    private CardLabel[] selectedWeaponCards = new CardLabel[]{
            new CardLabel(new ImageIcon(gameAssets.getSelectedRopeCard())),
            new CardLabel(new ImageIcon(gameAssets.getSelectedRevolverCard())),
            new CardLabel(new ImageIcon(gameAssets.getSelectedDaggerCard())),
            new CardLabel(new ImageIcon(gameAssets.getSelectedHatchetCard())),
            new CardLabel(new ImageIcon(gameAssets.getSelectedPoisonCard())),
            new CardLabel(new ImageIcon(gameAssets.getSelectedWrenchCard())),
    };
*/

    private CardLabel[] roomCards = new CardLabel[]{
            new CardLabel(new ImageIcon(gameAssets.getKitchenCard())),
            new CardLabel(new ImageIcon(gameAssets.getBallroomCard())),
            new CardLabel(new ImageIcon(gameAssets.getConservatoryCard())),
            new CardLabel(new ImageIcon(gameAssets.getDiningCard())),
            new CardLabel(new ImageIcon(gameAssets.getBilliardCard())),
            new CardLabel(new ImageIcon(gameAssets.getLibraryCard())),
            new CardLabel(new ImageIcon(gameAssets.getLoungeCard())),
            new CardLabel(new ImageIcon(gameAssets.getHallCard())),
            new CardLabel(new ImageIcon(gameAssets.getStudyCard())),
    };

    private Resolution resolution;

    private Point[] cardPoints = new Point[6];

    private CardLabel roomImage;

    private double scale = 0.42;

    private int cardYDown, cardYUp;

    private SuspectData suspectData = new SuspectData();

    private boolean selected;

    public QuestionPanel(Resolution resolution) {
        this.resolution = resolution;

        this.setLayout(null);
        this.setLocation((int)(10 * resolution.getScalePercentage()),(int)(100 * resolution.getScalePercentage()));
        this.setSize((int)(760 * resolution.getScalePercentage()),(int)(600 * resolution.getScalePercentage()));

        this.cardYUp = (int)((this.getHeight() - (int)(resolution.getScalePercentage() * 100) - cardLabels[0].getIcon().getIconHeight() * scale) - (int)(resolution.getScalePercentage()*40));
        this.cardYDown =  this.cardYUp + (int)(resolution.getScalePercentage() * 40);

        int extraWidth = 0;

        for (int i = 0; i < cardPoints.length; i++){
            cardLabels[i].setSize(new Dimension((int)(cardLabels[i].getIcon().getIconWidth() * this.scale), (int)(cardLabels[i].getIcon().getIconHeight() * scale)));
            cardLabels[i].setBorder(BorderFactory.createLineBorder(suspectData.getSuspectColor(i), 1));
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

        roomCards[roomNum].setLocation(this.getWidth()/2 - ((roomCards[roomNum].getWidth()/2)), (int)(resolution.getScalePercentage() * 20));
        roomCards[roomNum].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        System.out.println("Current Room Card Size: " + roomCards[roomNum].getX());

        this.add(roomCards[roomNum]);

        for (int i = 0; i < cardLabels.length; i++){
            cardLabels[i].setLocation(cardPoints[i]);

            int finalI = i;
            cardLabels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (!selected){
                        setSelected(true);
                        cardLabels[finalI].setIcon(playerIcons[finalI]);
                        cardLabels[finalI].revalidate();
                        new AnimateRoomCard(roomCards[roomNum], roomCards[roomNum].getX()).execute();
                    }

                }

                @Override
                public void mouseEntered(MouseEvent e){
                    super.mouseEntered(e);
                    if (!selected){
                        cardLabels[finalI].setIcon(playerIcons[finalI]);
                        cardLabels[finalI].setBorder(BorderFactory.createLineBorder(suspectData.getSuspectColor(finalI), 3));
                        cardLabels[finalI].revalidate();
                        new AnimateCard(cardLabels,finalI, 0, cardYDown, cardYUp).execute();
                    }

                }

                @Override
                public void mouseExited(MouseEvent e){
                    super.mouseExited(e);
                    if (!selected) {
                        cardLabels[finalI].setIcon(selectedPlayerIcons[finalI]);
                        cardLabels[finalI].setBorder(BorderFactory.createLineBorder(suspectData.getSuspectColor(finalI), 1));
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

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2.drawRect(0, 0, this.getWidth(), this.getHeight());
    }

    private class AnimateCard extends SwingWorker<Integer, String>{

        private int animateFlag;

        private int index;

        private CardLabel[] cards;

        private CardLabel cardToAnimate;

        private int heightUp;
        private int heightDown;

        private final int distance = 2, delay = 4;

        public AnimateCard(CardLabel[] cards, int index, int animateFlag, int heightDown, int heightUp ){
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

        public void setCardToAnimate(CardLabel button){
            this.cardToAnimate = button;
        }

        @Override
        protected Integer doInBackground() throws Exception{
            process(new ArrayList<>());

            //Moving the card up
            switch (this.animateFlag) {

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

                default:
                    System.out.println("Default");
            }
            process(new ArrayList<>());
            return null;
        }

        @Override
        protected void process(List<String> chunks) {
            this.cardToAnimate.revalidate();
        }

    }

    private class AnimateRoomCard extends SwingWorker<Integer, String>{

        private CardLabel roomCard;

        private final int delay = 4, distance = 2;

        public AnimateRoomCard(CardLabel roomCard, int currentX){
            this.roomCard = roomCard;
            this.roomCard.setLocation(currentX, roomCard.getY());
        }

        @Override
        protected Integer doInBackground() throws Exception{
            process(new ArrayList<>());

            System.out.println(roomCard.getX());
            int distanceRight = (int)roomCard.getX() + (int)(resolution.getScalePercentage()*100);

            while(roomCard.getX() != distanceRight){
                roomCard.setLocation(roomCard.getX()+distance, roomCard.getY());
                process(new ArrayList<>());
                Thread.sleep(delay);
            }

            process(new ArrayList<>());
            return null;
        }

        @Override
        protected void process(List<String> chunks){
            this.roomCard.revalidate();
        }
    }

    public class CardLabel extends JLabel {
        public CardLabel(ImageIcon icon) {
            super(icon);
        }

        @Override
        public void paintComponent(Graphics g){
             super.paintComponent(g);
             ImageIcon image = ((ImageIcon)super.getIcon());
             g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }
}

