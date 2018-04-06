package com.team11.cluedo.accustations;

import com.team11.cluedo.assets.Assets;
import com.team11.cluedo.board.room.RoomData;
import com.team11.cluedo.cards.Card;
import com.team11.cluedo.cards.Cards;
import com.team11.cluedo.cards.MurderEnvelope;
import com.team11.cluedo.components.T11Label;
import com.team11.cluedo.suspects.SuspectData;
import com.team11.cluedo.ui.Resolution;
import com.team11.cluedo.weapons.WeaponData;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AccusationPanel extends JPanel {
    private Assets gameAssets = new Assets();
    private SuspectData suspectData = new SuspectData();
    private WeaponData weaponData = new WeaponData();
    private RoomData roomData = new RoomData();

    private Resolution resolution;
    private MurderEnvelope murderEnvelope;

    private JButton doneButton;

    private ImageIcon[] suspectIcons = new ImageIcon[]{
            new ImageIcon(gameAssets.getWhiteCard()),
            new ImageIcon(gameAssets.getGreenCard()),
            new ImageIcon(gameAssets.getPeacockCard()),
            new ImageIcon(gameAssets.getPlumCard()),
            new ImageIcon(gameAssets.getScarletCard()),
            new ImageIcon(gameAssets.getMustardCard()),
    };

    private ImageIcon[] selectedSuspectIcons = new ImageIcon[]{
            new ImageIcon(gameAssets.getSelectedWhiteCard()),
            new ImageIcon(gameAssets.getSelectedGreenCard()),
            new ImageIcon(gameAssets.getSelectedPeacockCard()),
            new ImageIcon(gameAssets.getSelectedPlumCard()),
            new ImageIcon(gameAssets.getSelectedScarletCard()),
            new ImageIcon(gameAssets.getSelectedMustardCard()),
    };

    private ImageIcon[] weaponIcons = new ImageIcon[]{
            new ImageIcon(gameAssets.getHatchetCard()),
            new ImageIcon(gameAssets.getDaggerCard()),
            new ImageIcon(gameAssets.getPoisonCard()),
            new ImageIcon(gameAssets.getRevolverCard()),
            new ImageIcon(gameAssets.getRopeCard()),
            new ImageIcon(gameAssets.getWrenchCard()),
    };

    private ImageIcon[] selectedWeaponIcons = new ImageIcon[]{
            new ImageIcon(gameAssets.getSelectedHatchetCard()),
            new ImageIcon(gameAssets.getSelectedDaggerCard()),
            new ImageIcon(gameAssets.getSelectedPoisonCard()),
            new ImageIcon(gameAssets.getSelectedRevolverCard()),
            new ImageIcon(gameAssets.getSelectedRopeCard()),
            new ImageIcon(gameAssets.getSelectedWrenchCard()),
    };

    private ImageIcon[] roomIcons = new ImageIcon[]{
            new ImageIcon(gameAssets.getKitchenCard()),
            new ImageIcon(gameAssets.getBallroomCard()),
            new ImageIcon(gameAssets.getConservatoryCard()),
            new ImageIcon(gameAssets.getDiningCard()),
            new ImageIcon(gameAssets.getBilliardCard()),
            new ImageIcon(gameAssets.getLibraryCard()),
            new ImageIcon(gameAssets.getLoungeCard()),
            new ImageIcon(gameAssets.getHallCard()),
            new ImageIcon(gameAssets.getStudyCard()),
    };

    private ImageIcon[] selectedRoomIcons = new ImageIcon[]{
            new ImageIcon(gameAssets.getSelectedKitchenCard()),
            new ImageIcon(gameAssets.getSelectedBallroomCard()),
            new ImageIcon(gameAssets.getSelectedConservatoryCard()),
            new ImageIcon(gameAssets.getSelectedDiningCard()),
            new ImageIcon(gameAssets.getSelectedBilliardCard()),
            new ImageIcon(gameAssets.getSelectedLibraryCard()),
            new ImageIcon(gameAssets.getSelectedLoungeCard()),
            new ImageIcon(gameAssets.getSelectedHallCard()),
            new ImageIcon(gameAssets.getSelectedStudyCard()),
    };

    private T11Label[] suspectLabels, weaponLabels, roomLabels, murderLabels;
    private T11Label selectedSuspect, selectedWeapon, selectedRoom;
    private Timer moveCardTimer;
    private JPanel donePanel;

    private int cardWidth;
    private int suspectLocX;
    private int roomLocX;
    private int suspectTargetX, weaponTargetX, roomTargetX;
    private boolean suspectSelected, weaponSelected, roomSelected, correctGuess = false,
            weaponCorrect = false, suspectCorrect = false, roomCorrect = false;

    public AccusationPanel(Cards gameCards, Resolution resolution) {
        super(null);
        super.setBackground(new Color(0,0,0,156));
        super.setOpaque(false);
        super.setVisible(false);
        this.murderEnvelope = gameCards.getMurderEnvelope();
        this.resolution = resolution;

        setupButtons();
    }

    private void setupCards() {
        Dimension cardSize = new Dimension((int)(suspectIcons[0].getIconWidth()*0.45*resolution.getScalePercentage()),
                (int)(suspectIcons[0].getIconHeight()*0.45*resolution.getScalePercentage()));
        Dimension finalCardSize = new Dimension((int)(suspectIcons[0].getIconWidth()*0.8*resolution.getScalePercentage()),
                (int)(suspectIcons[0].getIconHeight()*0.8*resolution.getScalePercentage()));
        suspectLabels = new T11Label[]{
                new T11Label(suspectIcons[0], suspectData.getSuspectName(0)),
                new T11Label(suspectIcons[1], suspectData.getSuspectName(1)),
                new T11Label(suspectIcons[2], suspectData.getSuspectName(2)),
                new T11Label(suspectIcons[3], suspectData.getSuspectName(3)),
                new T11Label(suspectIcons[4], suspectData.getSuspectName(4)),
                new T11Label(suspectIcons[5], suspectData.getSuspectName(5)),
        };
        weaponLabels = new T11Label[]{
                new T11Label(weaponIcons[0], weaponData.getWeaponName(0)),
                new T11Label(weaponIcons[1], weaponData.getWeaponName(1)),
                new T11Label(weaponIcons[2], weaponData.getWeaponName(2)),
                new T11Label(weaponIcons[3], weaponData.getWeaponName(3)),
                new T11Label(weaponIcons[4], weaponData.getWeaponName(4)),
                new T11Label(weaponIcons[5], weaponData.getWeaponName(5)),
        };
        roomLabels = new T11Label[]{
                new T11Label(roomIcons[0], roomData.getRoomName(0)),
                new T11Label(roomIcons[1], roomData.getRoomName(1)),
                new T11Label(roomIcons[2], roomData.getRoomName(2)),
                new T11Label(roomIcons[3], roomData.getRoomName(3)),
                new T11Label(roomIcons[4], roomData.getRoomName(4)),
                new T11Label(roomIcons[5], roomData.getRoomName(5)),
                new T11Label(roomIcons[6], roomData.getRoomName(6)),
                new T11Label(roomIcons[7], roomData.getRoomName(7)),
                new T11Label(roomIcons[8], roomData.getRoomName(8)),
        };

        for (int i = 0 ; i < suspectLabels.length ; i++) {
            suspectLabels[i].setDisabledIcon(selectedSuspectIcons[i]);
            suspectLabels[i].setSize(cardSize);
            suspectLabels[i].setID(suspectData.getSuspectID(i));
        }
        for (int i = 0 ; i < weaponLabels.length ; i++) {
            weaponLabels[i].setDisabledIcon(selectedWeaponIcons[i]);
            weaponLabels[i].setSize(cardSize);
            weaponLabels[i].setID(weaponData.getWeaponID(i));
        }
        for (int i = 0 ; i < roomLabels.length ; i++) {
            roomLabels[i].setDisabledIcon(roomIcons[i]);
            roomLabels[i].setSize(cardSize);
            roomLabels[i].setID(roomData.getRoomID(i));
        }

        murderLabels = new T11Label[3];
        addMurderCard(0, murderEnvelope.getWeaponCard(), finalCardSize);
        addMurderCard(1, murderEnvelope.getSuspectCard(), finalCardSize);
        addMurderCard(2, murderEnvelope.getRoomCard(), finalCardSize);
    }

    private void addMurderCard(int index, Card murderCard, Dimension cardSize) {
        murderLabels[index] = (new T11Label(
                new ImageIcon(murderCard.getCardImage()), murderCard.getName())
        );
        murderLabels[index].setDisabledIcon(
                new ImageIcon(murderCard.getSelectedCardImage())
        );
        murderLabels[index].setSize(cardSize);
        murderLabels[index].setID(murderCard.getID());
    }

    private void setupButtons() {
        doneButton = new JButton("DONE");
        doneButton.setFont(new Font("Bulky Pixels", Font.BOLD, (int)(30 * resolution.getScalePercentage())));
        doneButton.setBorderPainted(false);
        doneButton.setContentAreaFilled(false);
        doneButton.setFocusPainted(false);
        doneButton.setOpaque(false);
        doneButton.setForeground(Color.WHITE);
        doneButton.setSize(doneButton.getPreferredSize());

        doneButton.addMouseListener(new MouseAdapter() {
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
    }

    private void displayAccusationPanel() {
        removeAll();
        resetBools();
        setupCards();

        int spacing = suspectLabels[0].getWidth() / 6;
        int width = getWidth();
        cardWidth = (spacing *(suspectLabels.length+1))+(suspectLabels[0].getWidth()*suspectLabels.length);
        int xPosSuspect = width / 2 - cardWidth / 2;
        int yPos = getHeight() - (getHeight() / 2);
        suspectLocX = xPosSuspect + spacing;

        int targetWidth = (int) ((suspectIcons[0].getIconWidth() * 0.8 * resolution.getScalePercentage()));
        int half = roomLabels.length/2;
        int roomCardsWidth = (spacing *(half+1))+(roomLabels[0].getWidth()*half);
        int roomCardsWidth2 = (spacing *(roomLabels.length-half-1))+(roomLabels[0].getWidth()*(roomLabels.length-half));
        int xPosRoom1 = width / 2 - roomCardsWidth / 2;
        int xPosRoom2 = width / 2 - roomCardsWidth2 / 2;

        roomLocX = xPosRoom1 + spacing;

        suspectTargetX = (width/2)-(targetWidth/2);
        weaponTargetX = (width/2)-(width/4)-(targetWidth/2);
        roomTargetX = suspectTargetX+(suspectTargetX-weaponTargetX);


        for (int i = 0 ; i < suspectLabels.length ; i++) {
            super.add(suspectLabels[i]);
            suspectLabels[i].setLocation(width + xPosSuspect + (spacing *(i+1))+(suspectLabels[i].getWidth()*(i)), yPos);
            setupActionListener(suspectLabels[i]);
        }

        for (int i = 0 ; i < weaponLabels.length ; i++) {
            super.add(weaponLabels[i]);
            weaponLabels[i].setLocation(width + xPosSuspect + (spacing *(i+1))+(weaponLabels[i].getWidth()*(i)), yPos);
            setupActionListener(weaponLabels[i]);
        }

        for (int i = 0 ; i < roomLabels.length ; i++) {
            super.add(roomLabels[i]);
            if (i < half) {
                roomLabels[i].setLocation(width + xPosRoom1 + (spacing *(i+1))+(roomLabels[i].getWidth()*(i)), yPos);
            } else {
                roomLabels[i].setLocation(width + xPosRoom2 + (spacing *(i-half))+(roomLabels[i].getWidth()*(i-half)), yPos +roomLabels[i].getHeight()+ spacing);
            }
            setupActionListener(roomLabels[i]);
        }
        super.setVisible(true);
        slideCards(suspectLabels, suspectLocX,  suspectLabels[0].getY(),
                suspectLabels[0].getX(), suspectLabels[0].getY());

    }

    private void endAccusationPanel() {
        T11Label[] onScreenCards = {
                selectedWeapon, selectedSuspect, selectedRoom,
                murderLabels[0], murderLabels[1], murderLabels[2]
        };

        moveCardTimer = new Timer(1500, e -> {
            SlideAnimation animate = new SlideAnimation(onScreenCards, false, true);
            animate.setInitialPos(selectedWeapon.getX(), selectedWeapon.getY());
            animate.setTargetPos(selectedWeapon.getX(), getHeight());
            animate.setDelay(0);
            animate.start();
            moveCardTimer.stop();
        });

        JLabel[] text;

        if (correctGuess) {
            text = new JLabel[] {
                    new JLabel("Congratulations!"),
                    new JLabel("You solved the murder!")
            };
            for (JLabel label : text) {
                label.setFont(new Font("Bulky Pixels", Font.BOLD, (int) (20 * resolution.getScalePercentage())));
                label.setForeground(Color.WHITE);
                label.setBackground(Color.BLACK);
            }
        } else {
            text = new JLabel[] {
                    new JLabel("You accused someone of the murder."),
                    new JLabel("But before it could be proven, you were killed.")
            };
            for (JLabel label : text) {
                label.setFont(new Font("Bulky Pixels", Font.BOLD, (int) (20 * resolution.getScalePercentage())));
                label.setForeground(Color.WHITE);
                label.setBackground(Color.BLACK);
            }
        }

        donePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10,10,10,10);
        for (int i = 0 ; i < text.length ; i++) {
            gbc.gridy = i;
            donePanel.add(text[i], gbc);
        }
        gbc.insets = new Insets(50,10,10,10);
        gbc.gridy++;
        donePanel.add(doneButton, gbc);
        doneButton.setVisible(true);
        donePanel.setVisible(false);
        donePanel.setOpaque(false);
        donePanel.setBounds(0,0,getWidth(),getHeight());
        super.add(donePanel);

        moveCardTimer.start();
    }

    public void accuse() {
        displayAccusationPanel();
    }

    public String accuse(String input) {
        boolean doAnimate = false;

        if (!suspectSelected) {
            for (T11Label card : suspectLabels) {
                if (card.getCardID().equals(input)) {
                    doAnimate = true;
                    suspectSelected = true;
                    selectedSuspect = card;
                    SlideAnimation animate = new SlideAnimation(suspectLabels, false, false);
                    animate.setInitialPos(suspectLabels[0].getX(), suspectLabels[0].getY());
                    animate.setTargetPos(0-cardWidth, suspectLabels[0].getY());
                    animate.setDelay(0);
                    moveSelectedCard(card, suspectTargetX, getHeight()/12, card.getX(), card.getY(),
                    (int) ((card.getIcon().getIconWidth() * 0.8 * resolution.getScalePercentage())),
                    (int) ((card.getIcon().getIconHeight() * 0.8 * resolution.getScalePercentage())));
                    card.setSelected(true);
                    animate.start();
                }
            }
        }
        if (!doAnimate && !weaponSelected) {
            for (T11Label card : weaponLabels) {
                if (card.getCardID().equals(input)) {
                    doAnimate = true;
                    weaponSelected = true;
                    selectedWeapon = card;
                    SlideAnimation animate = new SlideAnimation(weaponLabels, false, false);
                    animate.setInitialPos(weaponLabels[0].getX(), weaponLabels[0].getY());
                    animate.setTargetPos(0-cardWidth, weaponLabels[0].getY());
                    animate.setDelay(0);
                    moveSelectedCard(card, weaponTargetX, getHeight()/12, card.getX(), card.getY(),
                            (int) ((card.getIcon().getIconWidth() * 0.8 * resolution.getScalePercentage())),
                            (int) ((card.getIcon().getIconHeight() * 0.8 * resolution.getScalePercentage())));
                    card.setSelected(true);
                    animate.start();
                }
            }
        }
        if (!doAnimate && !roomSelected) {
            for (T11Label card : roomLabels) {
                if (card.getCardID().equals(input)) {
                    doAnimate = true;
                    roomSelected = true;
                    selectedRoom = card;
                    SlideAnimation animate = new SlideAnimation(roomLabels, false, false);
                    animate.setInitialPos(roomLabels[0].getX(), roomLabels[0].getY());
                    animate.setTargetPos(0-cardWidth, roomLabels[0].getY());
                    animate.setDelay(0);
                    moveSelectedCard(card, roomTargetX, getHeight()/12, card.getX(), card.getY(),
                            (int) ((card.getIcon().getIconWidth() * 0.8 * resolution.getScalePercentage())),
                            (int) ((card.getIcon().getIconHeight() * 0.8 * resolution.getScalePercentage())));
                    card.setSelected(true);
                    animate.start();
                }
            }
        }
        if (doAnimate) {
            if (!suspectSelected) {
                slideCards(suspectLabels, suspectLocX,  suspectLabels[0].getY(),
                        suspectLabels[0].getX(), suspectLabels[0].getY());
            } else if (!weaponSelected) {
                slideCards(weaponLabels, suspectLocX,  weaponLabels[0].getY(),
                        weaponLabels[0].getX(), weaponLabels[0].getY());
            } else if (!roomSelected) {
                slideCards(roomLabels, roomLocX,  roomLabels[0].getY(),
                        roomLabels[0].getX(), roomLabels[0].getY());
            }
        }

        return null;
    }

    public void accuse(String[] input) {

    }

    private void displayMurderCards() {
        for (T11Label murderLabel : murderLabels) {
            super.add(murderLabel);
            murderLabel.setVisible(true);
        }

        murderLabels[0].setLocation(weaponTargetX, getHeight());
        murderLabels[1].setLocation(suspectTargetX, getHeight());
        murderLabels[2].setLocation(roomTargetX, getHeight());
        int targetY = getHeight()/2;
        int borderThickness = 7;

        moveCardTimer = new Timer(100, e-> {
            moveCardTimer.setDelay(30);
            boolean moveWeaponCard = true, moveSuspectCard = false, moveRoomCard = false;
            if (murderLabels[0].getY() <= targetY) {
                moveWeaponCard = false;
                moveSuspectCard = true;
                if (weaponSelected) {
                    murderLabels[0].setLocation(murderLabels[0].getX(), targetY);
                    if (murderLabels[0].getCardID().equals(selectedWeapon.getCardID())) {
                        murderLabels[0].setBorder(new LineBorder(Color.GREEN, borderThickness));
                        selectedWeapon.setBorder(new LineBorder(Color.GREEN, borderThickness));
                        weaponCorrect = true;
                    } else {
                        murderLabels[0].setBorder(new LineBorder(Color.RED, borderThickness));
                        selectedWeapon.setBorder(new LineBorder(Color.RED, borderThickness));
                    }
                    weaponSelected = false;
                    moveCardTimer.setDelay(100);
                }
            }
            if (murderLabels[1].getY() <= targetY) {
                moveSuspectCard = false;
                moveRoomCard = true;
                if (suspectSelected) {
                    murderLabels[1].setLocation(murderLabels[1].getX(), targetY);
                    if (murderLabels[1].getCardID().equals(selectedSuspect.getCardID())) {
                        murderLabels[1].setBorder(new LineBorder(Color.GREEN, borderThickness));
                        selectedSuspect.setBorder(new LineBorder(Color.GREEN, borderThickness));
                        suspectCorrect = true;
                    } else {
                        murderLabels[1].setBorder(new LineBorder(Color.RED, borderThickness));
                        selectedSuspect.setBorder(new LineBorder(Color.RED, borderThickness));
                    }
                    suspectSelected = false;
                    moveCardTimer.setDelay(100);
                }
            }
            if (murderLabels[2].getY() <= targetY) {
                moveRoomCard = false;
                murderLabels[2].setLocation(murderLabels[2].getX(), targetY);
                if (roomSelected) {
                    if (murderLabels[2].getCardID().equals(selectedRoom.getCardID())) {
                        murderLabels[2].setBorder(new LineBorder(Color.GREEN, borderThickness));
                        selectedRoom.setBorder(new LineBorder(Color.GREEN, borderThickness));
                        roomCorrect = true;
                    } else {
                        murderLabels[2].setBorder(new LineBorder(Color.RED, borderThickness));
                        selectedRoom.setBorder(new LineBorder(Color.RED, borderThickness));
                    }
                    roomSelected = false;
                    moveCardTimer.setDelay(100);
                }
            }
            if (moveWeaponCard) {
                murderLabels[0].setLocation(murderLabels[0].getX(), murderLabels[0].getY()-((getHeight()-targetY)/20));
            } else if (moveSuspectCard) {
                murderLabels[1].setLocation(murderLabels[1].getX(), murderLabels[1].getY()-((getHeight()-targetY)/20));
            } else if (moveRoomCard) {
                murderLabels[2].setLocation(murderLabels[2].getX(), murderLabels[2].getY()-((getHeight()-targetY)/20));
            }

            if (!moveWeaponCard && !moveSuspectCard && !moveRoomCard) {
                if (weaponCorrect && suspectCorrect && roomCorrect) {
                    System.out.println("CORRECT");
                    correctGuess = true;
                } else {
                    correctGuess = false;
                }
                moveCardTimer.stop();
                endAccusationPanel();
                return;
            }
            repaint();
        });
        moveCardTimer.start();
    }

    private void moveSelectedCard(T11Label card, int targetX, int targetY, int initialX, int initialY, int sizeX, int sizeY) {
        int xDiff = targetX - initialX, direction;
        if (xDiff < 0) {
            direction = -1;
        } else {
            direction = 1;
        }
        int finalDirection = direction;
        // xDiff is minus, direction = left ( - ), else direction = right ( + )
        moveCardTimer = new Timer(0, e -> {
            boolean end = false;
            int moveX = 0;
            if (finalDirection == -1) {
                if (card.getX() <= targetX) {
                    if (card.getY() <= targetY) {
                        end = true;
                    }
                } else {
                    moveX = ((targetX-initialX)/20)+direction;
                }
            }
            else {
                if (card.getX() >= targetX) {
                    if(card.getY() <= targetY) {
                        end = true;
                    }
                } else {
                    moveX = ((targetX-initialX)/20)+direction;
                }
            }
            if (end) {
                card.setLocation(targetX, targetY);
                moveCardTimer.stop();
            } else {
                int moveY = ((targetY-initialY)/20);
                card.setLocation(card.getX()+moveX, card.getY()+moveY);
                if (card.getWidth() != sizeX && card.getHeight() != sizeY) {
                    card.setSize(card.getWidth() + ((sizeX - card.getWidth()) / 5), card.getHeight() + ((sizeY - card.getHeight()) / 5));
                }
                repaint();
            }
        });
        moveCardTimer.start();
    }

    private void slideCards(T11Label[] cards, int targetX, int targetY, int initialX, int initialY){
        SlideAnimation animate = new SlideAnimation(cards, true, false);
        animate.setInitialPos(initialX, initialY);
        animate.setTargetPos(targetX, targetY);
        animate.setDelay(0);
        animate.start();
    }

    public boolean isCorrectGuess() {
        return this.correctGuess;
    }

    public String getAccusation() {
        return "accused " + selectedSuspect.getCardName() + " with the\n" + selectedWeapon.getCardName() + " in the " +
                selectedRoom.getCardName() + ".\n";
    }

    public JButton getDoneButton() {
        return doneButton;
    }

    private void setupActionListener(T11Label card) {
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println("CLICKED");
                if (!card.isSelected()) {
                    accuse(card.getCardID());
                }
            }
        });
    }

    private void resetBools() {
        this.suspectSelected = false;
        this.weaponSelected = false;
        this.roomSelected = false;
        this.correctGuess = false;
        this.weaponCorrect = false;
        this.suspectCorrect = false;
        this.roomCorrect = false;
    }

    public void dispose() {
        removeAll();
        setVisible(false);
        resetBools();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0,0,0, 168));
        g.fillRect(0,0,getWidth(),getHeight());
        g.dispose();
    }

    public class SlideAnimation {
        private Timer timer;

        private T11Label[] cards;

        int delay;
        int initialX, initialY, targetX, targetY;
        private boolean slideIn, vertical;

        private SlideAnimation(T11Label[] cards, boolean slideIn, boolean vertical) {
            this.cards = cards;
            this.slideIn = slideIn;
            this.vertical = vertical;
        }

        private void start(){
            if (vertical) {
                timer = new Timer(delay, e -> {
                    for (T11Label card : cards) {
                        card.setLocation(card.getX(), card.getY() + ((targetY - initialY) / 90));
                        if (slideIn) {
                            if (cards[0].getY() <= targetY) {
                                donePanel.setVisible(true);
                                timer.stop();
                            }
                        } else {
                            if (cards[0].getY() >= targetY) {
                                donePanel.setVisible(true);
                                timer.stop();
                            }
                        }
                    }
                    repaint();
                });
            } else {
                timer = new Timer(delay, e -> {
                    for (T11Label card : cards) {
                        if (!card.isSelected()) {
                            card.setLocation(card.getX() + ((targetX - initialX) / 50), card.getY());
                            if (slideIn) {
                                if (cards[0].getX() <= targetX) {
                                    if (suspectSelected && weaponSelected && roomSelected) {
                                        displayMurderCards();
                                    }
                                    timer.stop();
                                }
                            } else {
                                if (card.getX() <= targetX) {
                                    if (suspectSelected && weaponSelected && roomSelected) {
                                        displayMurderCards();
                                    }
                                    timer.stop();
                                }
                            }
                        }
                    }
                    repaint();
                });
            }
            timer.start();
        }

        private void setDelay(int delay) {
            this.delay = delay;
        }

        private void setTargetPos(int x, int y) {
            this.targetX = x;
            this.targetY = y;
        }

        private void setInitialPos(int x, int y) {
            this.initialX = x;
            this.initialY = y;
        }
    }
}
