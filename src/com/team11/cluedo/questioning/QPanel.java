/*
 * Code to handle the questioning mechanics of the game.
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

public class QPanel extends JPanel {

    //Game data
    private Assets gameAssets = new Assets();
    private SuspectData suspectData = new SuspectData();
    private WeaponData weaponData = new WeaponData();
    private RoomData roomData = new RoomData();

    //Labels, Icons and Selected Icons for each of the suspect, weapon and room labels
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

    private T11Label[] playerLabels;

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

    private T11Label[] weaponLabels;

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

    private T11Label[] roomLabels = new T11Label[]{
            new T11Label(roomIcons[0], roomData.getRoomName(0), roomData.getRoomID(0)),
            new T11Label(roomIcons[1], roomData.getRoomName(1), roomData.getRoomID(1)),
            new T11Label(roomIcons[2], roomData.getRoomName(2), roomData.getRoomID(2)),
            new T11Label(roomIcons[3], roomData.getRoomName(3), roomData.getRoomID(3)),
            new T11Label(roomIcons[4], roomData.getRoomName(4), roomData.getRoomID(4)),
            new T11Label(roomIcons[5], roomData.getRoomName(5), roomData.getRoomID(5)),
            new T11Label(roomIcons[6], roomData.getRoomName(6), roomData.getRoomID(6)),
            new T11Label(roomIcons[7], roomData.getRoomName(7), roomData.getRoomID(7)),
            new T11Label(roomIcons[8], roomData.getRoomName(8), roomData.getRoomID(8))
    };

    //Array used to store the three questioning cards
    private T11Label[] selectedCards = new T11Label[3];
    private T11Label[] prevSelectedCards = new T11Label[3];

    //Individual labels for the selected card
    private T11Label selectedPlayer;
    private T11Label selectedWeapon;
    private T11Label selectedRoom;

    //Array lists of the valid cards found when checking each players hand
    private ArrayList<T11Label> validCards = new ArrayList<>();
    private ArrayList<ImageIcon> validCardIcons = new ArrayList<>();

    private int currentRoom;
    private int currentPlayer;
    private int nextPlayer;

    private Timer  moveCardTimer;

    private int cardWidth;
    private int labelSlideX;
    private int spacing;
    private int weaponTargetX;
    private int playerTargetX;

    //Booleans used to control flow
    private boolean hasSelectedPlayer = false;
    private boolean hasSelectedWeapon = false;
    private boolean inPlayerState = false;
    private boolean showingNextPlayer = false;
    private boolean hasLooped = false;
    private boolean doneQuestioning;
    private boolean hasShownCard = false;
    private boolean isDoneShowing = false;
    private boolean doneSelecting;

    private int questionState;

    private JLabel selectLabel;
    private JLabel noCardsLabel;
    private JLabel doneButton;

    private String shower;
    private String selectedCardName;

    private GameScreen gameScreen;
    private Resolution resolution;

    private T11Label selectedCard;

    public QPanel(GameScreen gameScreen, Resolution resolution) {
        super(null);
        super.setBackground(new Color(0,0,0,156));
        super.setOpaque(false);
        super.setVisible(false);
        this.gameScreen = gameScreen;
        this.resolution = resolution;
        this.setupLabels();
        this.setupButtonsAndLabels();
    }

    public boolean displaying(){
        return this.isVisible();
    }

    public T11Label getSelectedCard() {
        selectedCard.setEnabled(true);
        return selectedCard;
    }

    public void setSelectedCard(T11Label label){
        this.selectedCard = label;
    }

    public int getNextPlayer(){
        return this.nextPlayer;
    }

    private void setupButtonsAndLabels(){
        doneButton = new JLabel("DONE");
        doneButton.setFont(new Font("Bulky Pixels", Font.BOLD, (int)(30 * resolution.getScalePercentage())));
        doneButton.setBackground(new Color(0,0,0,156));
        doneButton.setForeground(Color.WHITE);
        doneButton.setVerticalAlignment(SwingConstants.CENTER);
        doneButton.setHorizontalAlignment(SwingConstants.CENTER);
        doneButton.setSize(new Dimension((int)(resolution.getScalePercentage() * 150),
                (int)(resolution.getScalePercentage() * 100)));

        doneButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                doneButton.setForeground(Color.WHITE);
                if (!hasLooped){
                    showNextPlayer();
                    questionState = 1;
                } else{
                    questionState = 4;
                    showNextPlayer();
                }
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

        noCardsLabel = new JLabel("NO CARDS TO SHOW");
        noCardsLabel.setFont(new Font("Bulky Pixels", Font.BOLD, (int)(25 * resolution.getScalePercentage())));
        noCardsLabel.setForeground(Color.WHITE);
        noCardsLabel.setSize(new Dimension((int)(resolution.getScalePercentage() * 400),
                (int)(resolution.getScalePercentage() * 100)));

        selectLabel = new JLabel("SELECT A CARD TO SHOW");
        selectLabel.setFont(new Font("Bulky Pixels", Font.BOLD, (int)(25 * resolution.getScalePercentage())));
        selectLabel.setForeground(Color.WHITE);
        selectLabel.setSize(new Dimension((int)(resolution.getScalePercentage() * 500),
                (int)(resolution.getScalePercentage() * 100)));

    }

    public void setupLabels(){
        playerLabels = new T11Label[]{
                new T11Label(playerIcons[0], suspectData.getSuspectName(0),suspectData.getSuspectID(0)),
                new T11Label(playerIcons[1], suspectData.getSuspectName(1),suspectData.getSuspectID(1)),
                new T11Label(playerIcons[2], suspectData.getSuspectName(2),suspectData.getSuspectID(2)),
                new T11Label(playerIcons[3], suspectData.getSuspectName(3),suspectData.getSuspectID(3)),
                new T11Label(playerIcons[4], suspectData.getSuspectName(4),suspectData.getSuspectID(4)),
                new T11Label(playerIcons[5], suspectData.getSuspectName(5),suspectData.getSuspectID(5)),
        };

        weaponLabels = new T11Label[]{
                new T11Label(weaponIcons[0], weaponData.getWeaponName(0), weaponData.getWeaponID(0)),
                new T11Label(weaponIcons[1], weaponData.getWeaponName(1), weaponData.getWeaponID(1)),
                new T11Label(weaponIcons[2], weaponData.getWeaponName(2),weaponData.getWeaponID(2)),
                new T11Label(weaponIcons[3], weaponData.getWeaponName(3),weaponData.getWeaponID(3)),
                new T11Label(weaponIcons[4], weaponData.getWeaponName(4),weaponData.getWeaponID(4)),
                new T11Label(weaponIcons[5], weaponData.getWeaponName(5),weaponData.getWeaponID(5)),
        };
    }



    public void displayQuestionPanel(int currentRoom, int currentPlayer){
        gameScreen.getInfoOutput().append("Select who you might think it could be.\n");
        this.setupLabels();
        prevSelectedCards = new T11Label[3];
        removeAll();
        resetAllBoolean();
        this.questionState = 0;
        this.currentRoom = currentRoom;
        this.currentPlayer = currentPlayer;
        this.nextPlayer = currentPlayer;
        setRoom(roomLabels[currentRoom]);
        setupCards();
        setupSelectedCards();

        if (!hasSelectedPlayer){
            inPlayerState = true;
            slideCards(playerLabels, labelSlideX, playerLabels[0].getY(),playerLabels[0].getX(), playerLabels[0].getY());

        }
        else if (!hasSelectedWeapon ){
            slideCards(weaponLabels, labelSlideX, weaponLabels[0].getY(),weaponLabels[0].getX(), weaponLabels[0].getY());
        }

        super.setVisible(true);

    }

    public void hideQuestionPanel(){
        setRoom(null);
        setPlayer(null);
        setWeapon(null);
        this.selectedCards = new T11Label[3];
        //resetAllBoolean();
        this.setVisible(false);
        this.removeAll();
    }

    public void showQuestionPanel(){
        super.setVisible(true);
        repaint();
    }

    public void question(String input) {
        boolean found = false;

        if (!hasSelectedPlayer || !hasSelectedWeapon)
            textSelectCard(input);
        if (questionState == 3) {
            for (T11Label card : playerLabels) {
                if (card.getCardID().equals(input)) {
                    for (T11Label card2 : validCards) {
                        if (selectedCards[0] == card && card2.getCardName().equals(card.getCardName())) {
                            found = true;
                            showCard(card.getCardName());
                        }
                    }
                }
            }
            if (!found) {
                for (T11Label card : weaponLabels) {
                    if (card.getCardID().equals(input)) {
                        for (T11Label card2 : validCards) {
                                if (selectedCards[2] == card && card2.getCardName().equals(card.getCardName())) {
                                found = true;
                                showCard(card.getCardName());
                            }
                        }
                    }
                }
            }
            if (!found) {
                for (T11Label card : roomLabels) {
                    if (card.getCardID().equals(input)) {
                        for (T11Label card2 : validCards) {
                            if (selectedCards[1] == card && card2.getCardName().equals(card.getCardName())) {
                                found = true;
                                showCard(card.getCardName());
                            }
                        }
                    }
                }
            }
            if (!found) {
                gameScreen.getInfoOutput().append("You don't have this card!\n");
            }
        }
    }
    //*/

    private void showCard(String cardName) {
        setSelectedCardName(cardName);
        setHasShownCard(true);
        selectCard();
        fillNotes(cardName);
    }

    public void tempHideQuestionPanel(){
        super.setVisible(false);
    }

    public void setShowingNextPlayer(boolean b){
        this.showingNextPlayer = b;
    }

    public void setSelectedPlayer(String player){

        for (T11Label label : playerLabels){
            if (label.getCardID().matches(player)){
                setPlayer(label);
            }
        }

        selectedCards[0] = selectedPlayer;
    }

    public void setSelectedWeapon(String weapon){
        for (T11Label label : weaponLabels){
            if (label.getCardID().matches(weapon)) {
                setWeapon(label);
            }}
        selectedCards[2] = selectedWeapon;
    }

    public void setQuestionState(int state){
        this.questionState = state;
    }

    public int validCardSize(){
        return this.validCards.size();
    }

    private void setPlayer(T11Label label){
        this.selectedPlayer = label;
    }

    private void setWeapon(T11Label label){
        this.selectedWeapon = label;
    }

    private void setRoom(T11Label label){
        this.selectedRoom = label;
    }

    public int getQuestionState(){
        return this.questionState;
    }

    public boolean getLooped(){
        return hasLooped;
    }

    public boolean hasShownCard(){
        return hasShownCard;
    }

    private void setHasShownCard(boolean shown){
        this.hasShownCard = shown;
    }

    private void resetAllBoolean(){
        this.hasSelectedWeapon = false;
        this.hasSelectedPlayer = false;
        this.inPlayerState = false;
        this.showingNextPlayer = false;
        this.hasLooped = false;
        this.doneQuestioning = false;
        this.doneSelecting = false;
        this.hasShownCard = false;
    }

    private void removeNoCardLabels() {
        if (isDoneShowing) {
            remove(doneButton);
            remove(noCardsLabel);
            gameScreen.repaint();
        }
    }

    private void incrementNextPlayer(){
        nextPlayer++;
        if (nextPlayer == gameScreen.getGamePlayers().getPlayerCount()) {
            nextPlayer = 0;
        }

        if (nextPlayer == currentPlayer) {
            hasLooped = true;
            questionState = 4;
        }
    }

    private void setupCards(){
        Dimension cardSize = new Dimension((int)(playerIcons[0].getIconWidth()*0.45*resolution.getScalePercentage()),
                (int)(playerIcons[0].getIconHeight()*0.45*resolution.getScalePercentage()));

        for (int i = 0; i < playerLabels.length; i++){
            playerLabels[i].setSize(cardSize);
            playerLabels[i].setDisabledIcon(selectedPlayerIcons[i]);
        }

        for (int i = 0; i < weaponLabels.length; i++){
            weaponLabels[i].setSize(cardSize);
            weaponLabels[i].setDisabledIcon(selectedWeaponIcons[i]);
        }

        for (int i = 0; i < roomLabels.length; i++){
            roomLabels[i].setSize(cardSize);
            roomLabels[i].setDisabledIcon(selectedRoomIcons[i]);
        }
    }

    private void setupSelectedCards(){

        Dimension finalCardSize = new Dimension((int)(playerIcons[0].getIconWidth()*0.8*resolution.getScalePercentage()),
                (int)(playerIcons[0].getIconHeight()*0.8*resolution.getScalePercentage()));

        this.spacing = playerLabels[0].getWidth()/6;
        this.cardWidth = (this.spacing * (playerLabels.length + 1) + (playerLabels[0].getWidth() * playerLabels.length));
        int targetY = getHeight() / 12;
        int cardY  = getHeight()-(getHeight()/2);

        int targetWidth = (int) ((playerIcons[0].getIconWidth() * 0.8 * resolution.getScalePercentage()));
        int xPosSuspect = getWidth()/2 - cardWidth/2;
        this.labelSlideX = xPosSuspect + this.spacing;

        int roomTargetX = (getWidth() / 2 - targetWidth / 2);
        this.weaponTargetX = (getWidth()/2 + targetWidth/2 + this.spacing);
        this.playerTargetX = (roomTargetX - this.spacing - targetWidth);

        //Have to set up room card anyway
        selectedRoom.setSize(finalCardSize);
        selectedRoom.setLocation(roomTargetX, targetY);

        selectedRoom.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        this.add(selectedRoom);
        //Update the array
        selectedCards[1] = selectedRoom;

        //if we have a selected player then add it
        if (selectedPlayer != null){
            selectedPlayer.setSize(finalCardSize);
            selectedPlayer.setLocation(this.playerTargetX, targetY);

            selectedPlayer.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            this.add(selectedPlayer);

            hasSelectedPlayer = true;
            selectedCards[0] = selectedPlayer;
        }

        //if we hae a selected weapon then add it
        if (selectedWeapon != null){
            selectedWeapon.setSize(finalCardSize);
            selectedWeapon.setLocation(this.weaponTargetX , targetY);
            selectedWeapon.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            this.add(selectedWeapon);
            hasSelectedWeapon = true;
            selectedCards[2] = selectedWeapon;
        }


        //Assign the card points array as this method must be called
        if (!hasSelectedPlayer){
            for (int i = 0; i < playerLabels.length; i++){
                super.add(playerLabels[i]);
                playerLabels[i].setLocation(getWidth() + (this.spacing*(i+1))+(playerLabels[i].getWidth()*(i)), cardY);
            }
            setupActionListener(playerLabels);
        }

        setupActionListener(playerLabels);
        if (!hasSelectedWeapon){
            for (int i = 0; i < weaponLabels.length; i++){
                super.add(weaponLabels[i]);
                weaponLabels[i].setLocation(getWidth() + (this.spacing*(i+1))+(weaponLabels[i].getWidth()*(i)), cardY);
            }
            setupActionListener(weaponLabels);
        }

        if (hasSelectedPlayer && hasSelectedWeapon){
            question();
        }
    }

    private void addCards(String input){
        boolean doAnimate = false;

        if (!hasSelectedPlayer) {
            for (T11Label card : playerLabels) {
                if (card.getCardName().equals(input)) {
                    doAnimate = true;
                    hasSelectedPlayer = true;
                    selectedPlayer = card;
                    SlideAnimation animate = new SlideAnimation(playerLabels, false, false);
                    animate.setInitialPos(playerLabels[0].getX(), playerLabels[0].getY());
                    animate.setTargetPos(0-cardWidth, playerLabels[0].getY());
                    animate.setDelay(0);
                    moveAbove(card, playerTargetX, getHeight()/12, card.getX(), card.getY(),
                            (int) ((card.getIcon().getIconWidth() * 0.8 * resolution.getScalePercentage())),
                            (int) ((card.getIcon().getIconHeight() * 0.8 * resolution.getScalePercentage())));
                    card.setSelected(true);
                    animate.start();
                    inPlayerState = false;
                }
            }
        }
        if (!doAnimate && !hasSelectedWeapon) {
            for (T11Label card : weaponLabels) {
                if (card.getCardName().equals(input)) {
                    doAnimate = true;
                    hasSelectedWeapon = true;
                    selectedWeapon = card;
                    SlideAnimation animate = new SlideAnimation(weaponLabels, false, false);
                    animate.setInitialPos(weaponLabels[0].getX(), weaponLabels[0].getY());
                    animate.setTargetPos(0-cardWidth, weaponLabels[0].getY());
                    animate.setDelay(0);
                    moveAbove(card, weaponTargetX, getHeight()/12, card.getX(), card.getY(),
                            (int) ((card.getIcon().getIconWidth() * 0.8 * resolution.getScalePercentage())),
                            (int) ((card.getIcon().getIconHeight() * 0.8 * resolution.getScalePercentage())));
                    card.setSelected(true);
                    animate.start();
                }
            }
        }
        if (doAnimate) {
            if (!hasSelectedPlayer) {
                slideCards(playerLabels, labelSlideX,  playerLabels[0].getY(),
                        playerLabels[0].getX(), playerLabels[0].getY());
            } else if (!hasSelectedWeapon) {
                slideCards(weaponLabels, labelSlideX,  weaponLabels[0].getY(),
                        weaponLabels[0].getX(), weaponLabels[0].getY());
            }
        }
    }

    private void setupActionListener(T11Label[] labels) {
        for (int i = 0; i < labels.length; i++){
            int finalI = i;
            labels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (!labels[finalI].isSelected()) {
                        addCards(labels[finalI].getCardName());
                        gameScreen.getInfoOutput().append("> " + labels[finalI].getCardName() + "\n");
                        if (!hasSelectedWeapon) {
                            gameScreen.getInfoOutput().append("Select the suspects weapon of choice.\n");
                        }
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    for (int j = 0; j < labels.length; j++){
                        if (j != finalI){
                            labels[j].setEnabled(false);
                        }
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    for (T11Label label : labels) {
                        label.setEnabled(true);
                    }
                }
            });
        }
    }

    private void moveAbove(T11Label label, int targetX, int targetY, int startX, int startY, int targetW, int targetH){
        int xDiff = targetX - startX, direction;
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
                if (label.getX() <= targetX) {
                    if (label.getY() <= targetY) {
                        end = true;
                    }
                } else {
                    moveX = ((targetX-startX)/20)+direction;
                }
            }
            else {
                if (label.getX() >= targetX) {
                    if(label.getY() <= targetY) {
                        end = true;
                    }
                } else {
                    moveX = ((targetX-startX)/20)+direction;
                }
            }
            if (end) {
                label.setLocation(targetX, targetY);
                moveCardTimer.stop();
            } else {
                int moveY = ((targetY-startY)/20);
                label.setLocation(label.getX() + moveX, label.getY() + moveY);
                if (label.getWidth() != targetW && label.getHeight() != targetH) {
                    label.setSize(label.getWidth() + ((targetW - label.getWidth()) / 5), label.getHeight() + ((targetH - label.getHeight()) / 5));
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

    public T11Label[] getPrevSelectedCards(){
        return this.prevSelectedCards;
    }

    private void question(){
        //Move the player and weapon to the room
        doneSelecting = true;
        selectedCards[0] = selectedPlayer;
        selectedCards[1] = selectedRoom;
        selectedCards[2] = selectedWeapon;
        prevSelectedCards = selectedCards.clone();
        int questionedPlayer = findLabelID(playerLabels, selectedPlayer);
        int questionedWeapon = findLabelID(weaponLabels, selectedWeapon);

        //Move the player and weapon
        gameScreen.getGameSuspects().getSuspect(questionedPlayer).moveToRoom(currentRoom, gameScreen.getGameBoard(), gameScreen.getGameSuspects().getSuspect(questionedPlayer));
        gameScreen.getGameWeapons().moveWeaponToRoom(questionedWeapon, currentRoom);
        gameScreen.repaint();

        showNextPlayer();
    }

    public void showNextPlayer(){
        if (isDoneShowing){
            removeNoCardLabels();
        }

        this.questionState = 1;
        if (!hasLooped) {
            incrementNextPlayer();
            gameScreen.getInfoOutput().setText("");
            gameScreen.getInfoOutput().setText("Pass to " + gameScreen.getGamePlayers().getPlayer(nextPlayer).getPlayerName() + "\nand click done or enter 'done'\n");

            gameScreen.getPlayerChange().setPlayerCard(gameScreen.getGamePlayers().getPlayer(nextPlayer));
            gameScreen.getPlayerChange().setVisible(true);
            this.showingNextPlayer = true;
            gameScreen.reDraw(nextPlayer);
        } else{
            gameScreen.getInfoOutput().setText("");
            gameScreen.getInfoOutput().setText("Pass to " + gameScreen.getGamePlayers().getPlayer(currentPlayer).getPlayerName() + "\nand click done or enter 'done'\n");
            questionState = 4;
        }
    }

    public void findValidCards(){
        PlayerHand playerHand = gameScreen.getGamePlayers().getPlayer(nextPlayer).getPlayerHand();
        this.validCards = new ArrayList<>();
        this.validCardIcons = new ArrayList<>();

        for (T11Label selectedCard : selectedCards) {
            for (Card card : playerHand.getPlayerHand()) {
                if (card.getName().matches(selectedCard.getCardName())) {
                    validCards.add(new T11Label(new ImageIcon(card.getSelectedCardImage()), card.getName()));
                    validCardIcons.add(new ImageIcon(card.getCardImage()));
                }
            }

            for (Card card : playerHand.getPublicHand()) {
                if (card.getName().matches(selectedCard.getCardName())) {
                    validCards.add(new T11Label(new ImageIcon(card.getSelectedCardImage()), card.getName()));
                    validCardIcons.add(new ImageIcon(card.getCardImage()));
                }
            }
        }
    }

    private int findLabelID(T11Label[] labels, T11Label card){
        int index = -1;
        int i = 0;

        for (T11Label label : labels){
            if (label.getCardName().matches(card.getCardName())){
                index = i;
            }
            i++;
        }

        return index;
    }

    public boolean containsCard(String cardID){
        for (int i = 0; i < validCards.size();i++){
            if (validCards.get(i).getCardName().matches(cardID)){
                //new T11Label(roomIcons[0], roomData.getRoomName(0), roomData.getRoomID(0)),

                T11Label label = new T11Label(validCardIcons.get(i), validCards.get(i).getCardName());
                selectedCard = label;
                return true;
            }
        }
        return false;
    }

    public void setupValidCards() {
        this.validCards = new ArrayList<>();
        findValidCards();

        int cardY  = getHeight()-(getHeight()/2);

        T11Label[] validCardArray = this.validCards.toArray(new T11Label[0]);
        for (int i = 0; i < validCardArray.length; i++){
            validCardArray[i].setSize((int)(playerIcons[0].getIconWidth()*0.8*resolution.getScalePercentage()),
                    (int)(playerIcons[0].getIconHeight()*0.8*resolution.getScalePercentage()));
            validCardArray[i].setDisabledIcon(validCardIcons.get(i));
            validCardArray[i].setLocation(getWidth() + (this.spacing*(i+1))+(selectedCards[i].getWidth()*(i)), cardY);
            setupValidListeners(validCardArray);
            super.add(validCardArray[i]);
        }

        int targetX = 0;
        if (validCardArray.length == 0){
            isDoneShowing = true;
            questionState = 2;
            if (selectLabel.isVisible()){
                super.remove(selectLabel);
            }
            this.doneButton.setLocation(getWidth()/2 - selectedRoom.getWidth()/3 , getHeight()/2);
            this.noCardsLabel.setLocation(doneButton.getX() - doneButton.getWidth()/2 - (int)(resolution.getScalePercentage() * 25), doneButton.getY() - doneButton.getHeight()/2 );
            super.add(noCardsLabel);
            super.add(doneButton);
            repaint();
        }
        else if (validCardArray.length == 1){
            targetX = selectedRoom.getX();
        } else if (validCardArray.length == 2){
            targetX = getWidth()/2 - selectedRoom.getWidth() -(int)(resolution.getScalePercentage() * 15);
        } else if (validCardArray.length == 3){
            targetX = selectedPlayer.getX();
        }

        if (validCardArray.length != 0){
            questionState = 3;
            this.selectLabel.setLocation(selectedRoom.getX() - selectLabel.getWidth()/4, selectedRoom.getY() + selectedRoom.getHeight() );
            super.add(selectLabel);
            SlideAnimation animation = new SlideAnimation(validCardArray, true, false);
            animation.setTargetPos(targetX, cardY);
            animation.setInitialPos(validCardArray[0].getX(), validCardArray[0].getY());
            animation.setDelay(0);
            animation.start();
        }

    }

    private void setupValidListeners(T11Label[] labels){
        for (T11Label label : labels){
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    selectedCard = new T11Label(validCardIcons.get(validCards.indexOf(label)), label.getCardName());

                    shower = gameScreen.getGamePlayers().getPlayer(nextPlayer).getPlayerName();
                    setSelectedCardName(label.getCardName());
                    hasShownCard = true;
                    selectCard();
                    fillNotes(label.getCardName());
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    label.setEnabled(false);
                    label.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    label.setEnabled(true);
                    label.repaint();
                }
            });
        }
    }

    public void fillNotes(String selectedCard){
        gameScreen.getGamePlayers().getPlayer(currentPlayer).getPlayerNotes().paintCell(nextPlayer+1, selectedCard, 1);
        for (int i = 0; i < 8; i++){
            if (i != nextPlayer + 1) {
                gameScreen.getGamePlayers().getPlayer(currentPlayer).getPlayerNotes().paintCell(i, selectedCard, 2);
            }
        }
    }

    private void selectCard(){
        this.shower = gameScreen.getGamePlayers().getPlayer(nextPlayer).getPlayerName();
        gameScreen.getInfoOutput().setText("");
        gameScreen.getInfoOutput().setText("Pass to " + gameScreen.getGamePlayers().getPlayer(currentPlayer).getPlayerName() + ",\nand enter 'done'.\n\n");
        hideQuestionPanel();
        gameScreen.reDraw(currentPlayer);
        gameScreen.getPlayerChange().setPlayerCard(gameScreen.getGamePlayers().getPlayer(currentPlayer));
        gameScreen.getPlayerChange().setVisible(true);
        showingNextPlayer = true;
        doneQuestioning = true;
        questionState = 4;
    }

    private void setSelectedCardName(String name){
        this.selectedCardName = name;
    }

    public String getShower(){
        return this.shower;
    }

    public void printShower(){
        gameScreen.getInfoOutput().setText("");
        gameScreen.getInfoOutput().append(gameScreen.getGamePlayers().getPlayer(nextPlayer).getPlayerName() + " showed you the " + selectedCardName + " card.\n\n");
    }

    private void textSelectCard(String card){
        boolean found = false;
        if (inPlayerState){
            for (T11Label sCard : playerLabels) {
                if (sCard.getCardID().equals(card)) {
                    addCards(sCard.getCardName());
                    selectedPlayer = sCard;
                    selectedCards[0] = selectedPlayer;
                    inPlayerState = false;
                    hasSelectedPlayer = true;
                    found = true;
                    gameScreen.getInfoOutput().append("Select the suspects weapon of choice.\n");
                    break;
                }
            }
            if (!found) {
                gameScreen.getInfoOutput().append("This person didn't attend the party.\n");
            }
        } else{
            for (T11Label wCard : weaponLabels) {
                if (wCard.getCardID().equals(card)) {
                    addCards(wCard.getCardName());
                    selectedWeapon = wCard;
                    selectedCards[2] = selectedWeapon;
                    hasSelectedWeapon = true;
                    found = true;
                    break;
                }
            }
            if (!found) {
                gameScreen.getInfoOutput().append("There was no evidence of this lying around!\n");
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(new Color(0,0,0,156));
        g.fillRect(0,0, getWidth(), getHeight());
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
                                timer.stop();
                            }
                        } else {
                            if (cards[0].getY() >= targetY) {

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
                                    if (hasSelectedPlayer && hasSelectedWeapon && !doneSelecting) {
                                        question();
                                    }
                                    timer.stop();
                                }
                            } else {
                                if (card.getX() <= targetX) {
                                    if (hasSelectedPlayer && hasSelectedWeapon && !doneSelecting) {
                                        question();
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

