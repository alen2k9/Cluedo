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

public class QPanel extends JPanel {

    private Assets gameAssets = new Assets();
    private SuspectData suspectData = new SuspectData();
    private WeaponData weaponData = new WeaponData();
    private RoomData roomData = new RoomData();

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

    private T11Label[] playerLabels = new T11Label[]{
        new T11Label(playerIcons[0], suspectData.getSuspectID(0)),
        new T11Label(playerIcons[1], suspectData.getSuspectID(1)),
        new T11Label(playerIcons[2], suspectData.getSuspectID(2)),
        new T11Label(playerIcons[3], suspectData.getSuspectID(3)),
        new T11Label(playerIcons[4], suspectData.getSuspectID(4)),
        new T11Label(playerIcons[5], suspectData.getSuspectID(5)),
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

    private T11Label[] weaponLabels = new T11Label[]{
            new T11Label(weaponIcons[0], weaponData.getWeaponID(0)),
            new T11Label(weaponIcons[1], weaponData.getWeaponID(1)),
            new T11Label(weaponIcons[2], weaponData.getWeaponID(2)),
            new T11Label(weaponIcons[3], weaponData.getWeaponID(3)),
            new T11Label(weaponIcons[4], weaponData.getWeaponID(4)),
            new T11Label(weaponIcons[5], weaponData.getWeaponID(5)),
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

    private T11Label[] roomLabels = new T11Label[]{
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

    private T11Label[] selectedCards = new T11Label[3];

    private T11Label selectedPlayer;
    private T11Label selectedWeapon;
    private T11Label selectedRoom;

    private Point[] labelPoints = new Point[6];
    private Point[] translatedLabelPointsRight = new Point[6];
    private Point[] translatedLabelPointsLeft = new Point[6];

    private ArrayList<T11Label> validCards = new ArrayList<>();


    private int currentRoom;
    private int currentPlayer;
    private int nextPlayer;

    private int playerTargetX;
    private int weaponTargetX;

    private boolean moving;
    private boolean hasSelectedPlayer = false;
    private boolean hasSelectedWeapon = false;
    private boolean inPlayerState = false;
    private boolean inWeaponState = false;
    private boolean showingNextPlayer = false;
    private boolean hasResized = false;
    private boolean hasLooped = false;

    private boolean isFinished;
    private boolean doneShowing = false;

    private GameScreen gameScreen;
    private Resolution resolution;

    private Timer timer;

    public QPanel(GameScreen gameScreen, Resolution resolution) {
        this.gameScreen = gameScreen;
        this.resolution = resolution;

        this.setBackground(new Color(0,0,0, 156));

        this.setLayout(null);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
    }

    public void displayQuestionPanel(int currentRoom, int currentPlayer){
        this.currentRoom = currentRoom;
        this.currentPlayer = currentPlayer;
        this.nextPlayer = currentPlayer;
        this.isFinished = false;

        System.out.println("Current Player :" + this.currentPlayer);
        //System.out.println("Next Player : " + nextPlayer);

        this.selectedRoom = roomLabels[currentRoom];
        setupSelectedCards();

        if (!hasSelectedPlayer){
            inPlayerState = true;
            addCards(playerLabels, 0);
        }
        else if (!hasSelectedWeapon){
            addCards(weaponLabels, 1);
        } else {
            //question();
        }

        this.setVisible(true);
        this.setFocusable(true);
    }

    public void hideQuestionPanel(){
        this.selectedRoom = null;
        this.selectedPlayer = null;
        this.selectedWeapon = null;
        this.selectedCards = new T11Label[3];
        resetAllBoolean();
        this.setVisible(false);
        this.removeAll();
    }

    public boolean getResized(){
        return hasResized;
    }

    private void resetAllBoolean(){
        this.hasSelectedWeapon = false;
        this.hasSelectedPlayer = false;
        this.inWeaponState = false;
        this.inPlayerState = false;
        this.showingNextPlayer = false;
        this.hasResized = false;
        this.hasLooped = false;
        this.isFinished = false;
        this.doneShowing = false;
    }

    public boolean isDoneShowing(){
        return doneShowing;
    }

    public void setDoneShowing(boolean b){
        this.doneShowing = b;
    }

    public boolean isShowingNextPlayer(){
        return showingNextPlayer;
    }

    public void setShowingNextPlayer(boolean b){
        this.showingNextPlayer = b;
    }

    public void setSelectedPlayer(String player){

        for (T11Label label : playerLabels){
            //System.out.println("Comparing " + label.getCardName() + "  and  " + player);
            if (label.getCardName().matches(player)){
                this.selectedPlayer = label;
                System.out.println("Selected player is " + selectedPlayer.getCardName());
            }
        }

        selectedCards[0] = selectedPlayer;
    }

    public void setSelectedWeapon(String weapon){
        for (T11Label label : weaponLabels){
            if (label.getCardName().matches(weapon)){
                this.selectedWeapon = label;
                System.out.println("Selected player is " + selectedWeapon.getCardName());
            }
        }

        selectedCards[2] = selectedWeapon;
    }

    public void setFinished(boolean b){
        this.isFinished = b;
    }

    public boolean isFinished(){
        return isFinished;
    }

    public void incrementNextPlayer(){
        System.out.println("Next Player " + nextPlayer + "  player count "  + gameScreen.getGamePlayers().getPlayerCount());
        if (nextPlayer + 1 == gameScreen.getGamePlayers().getPlayerCount()){
            nextPlayer = 0;
        } else{
            nextPlayer++;
        }
        System.out.println("Next Player: " + nextPlayer);

        if (nextPlayer == currentPlayer){
            hasLooped = true;
            System.out.println("\nHas Looped\n");
        }
    }

    private void setupSelectedCards(){

        if ((this.getWidth()/6 - (int)(resolution.getScalePercentage() * 25)) % 2 == 0){
            playerTargetX = (this.getWidth()/6 - (int)(resolution.getScalePercentage() * 25));
        } else {
            playerTargetX = (this.getWidth()/6 - (int)(resolution.getScalePercentage() * 25) - 1);
        }

        //Have to set up room card anyway
        selectedRoom.setSize(new Dimension((int)(selectedRoom.getIcon().getIconWidth() * (resolution.getScalePercentage() * 0.66)),
                (int)(selectedRoom.getIcon().getIconHeight() *(resolution.getScalePercentage() * 0.66))));
        selectedRoom.setLocation(this.getWidth()/2 - selectedRoom.getWidth()/2, (int)(resolution.getScalePercentage() * 20));

        selectedRoom.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        this.add(selectedRoom);
        //Update the array
        selectedCards[1] = selectedRoom;

        //if we have a selected player then add it
        if (selectedPlayer != null){
            selectedPlayer.setSize(new Dimension((int)(selectedPlayer.getIcon().getIconWidth() * (resolution.getScalePercentage() * 0.66)),
                    (int)(selectedPlayer.getIcon().getIconHeight() *(resolution.getScalePercentage() * 0.66))));

            selectedPlayer.setLocation(playerTargetX, (int)(resolution.getScalePercentage() * 20));

            selectedPlayer.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            this.add(selectedPlayer);

            hasSelectedPlayer = true;

            selectedCards[1] = selectedPlayer;
        }

        if ((((getWidth()/3)*2) -(int)(resolution.getScalePercentage() * 10) ) % 2 == 0){
            weaponTargetX = ((((getWidth()/3)*2) - (int)(resolution.getScalePercentage() * 10)));
        } else {
            weaponTargetX = ((((getWidth()/3)*2) - (int)(resolution.getScalePercentage() * 10)) - 1);
        }

        //if we hae a selected weapon then add it
        if (selectedWeapon != null){
            selectedWeapon.setSize(new Dimension((int)(selectedWeapon.getIcon().getIconWidth() * (resolution.getScalePercentage() * 0.66)),
                    (int)(selectedWeapon.getIcon().getIconHeight() *(resolution.getScalePercentage() * 0.66))));

            selectedWeapon.setLocation((weaponTargetX) , (int)(resolution.getScalePercentage() * 20));

            selectedWeapon.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            this.add(selectedWeapon);

            hasSelectedWeapon = true;

            selectedCards[2] = selectedWeapon;
        }

        //Assign the card points array as this method must be called
        for (int i = 0; i < 6; i++){
            //Setup the first point
            if (i == 0){
                labelPoints[i] = new Point((int)(resolution.getScalePercentage() * 25), selectedRoom.getY() + (selectedRoom.getHeight())+ (int)(resolution.getScalePercentage() * 150));
            } else {
                labelPoints[i] = new Point(sumWidths(i) + (int)(resolution.getScalePercentage() * 7 ) ,selectedRoom.getY()+ (selectedRoom.getHeight()) + (int)(resolution.getScalePercentage() * 150));
            }
        }

        for (int i = 0; i < 6; i++){
            translatedLabelPointsRight[i] = new Point((int)labelPoints[i].getX() + this.getWidth(), (int)labelPoints[i].getY());
            translatedLabelPointsLeft[i] = new Point((int)labelPoints[i].getX() - this.getWidth(), (int)labelPoints[i].getY());
        }

        if (hasSelectedPlayer && hasSelectedWeapon){
            question();
        }

    }

    private int sumWidths(int index){
        int sum = (int)(resolution.getScalePercentage() * 15);
        T11Label tmpLabel = new T11Label(playerIcons[1], "tmp");
        tmpLabel.setSize((int)(playerIcons[1].getIconWidth() * (resolution.getScalePercentage() * 0.42)), (int)(playerIcons[1].getIconHeight() * (resolution.getScalePercentage() * 0.42)) );
        sum += (index * tmpLabel.getWidth());
        sum += index * (int)(resolution.getScalePercentage() * 15);

        return sum;
    }

    private void addCards(T11Label[] labels, int flag){

        int targetUp =(int) (translatedLabelPointsLeft[1].getY() - (int)(resolution.getScalePercentage() * 40));
        int targetDown = (int)(translatedLabelPointsLeft[1].getY());

        int i = 0;
        for (T11Label label : labels){
            label.setSize(new Dimension((int) (label.getIcon().getIconWidth() * (resolution.getScalePercentage() * 0.42)),
                    (int)(label.getIcon().getIconHeight() * (resolution.getScalePercentage() * 0.42))));

            label.setLocation(translatedLabelPointsRight[i]);
            i++;

            this.add(label);
        }

        new AnimateMoveIn(labels, labelPoints).execute();

        if (flag == 0){
            inPlayerState = true;
        } else{
            inWeaponState = true;
        }

        for (int j = 0; j < 6; j++){

            int finalJ = j;
            labels[j].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (flag == 0){
                        hasSelectedPlayer = true;
                        inPlayerState = false;
                        selectedPlayer = labels[finalJ];
                        selectedCards[0] = selectedPlayer;
                        labels[finalJ].setIcon(playerIcons[finalJ]);
                    } else{
                        hasSelectedWeapon = true;
                        inWeaponState = false;
                        selectedWeapon = labels[finalJ];
                        selectedCards[2] = selectedWeapon;
                        labels[finalJ].setIcon(weaponIcons[finalJ]);
                    }

                    new AnimateCorner(labels, finalJ, flag).execute();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    if (!moving){
                        for (int j = 0; j < 6; j++){
                            if (j != finalJ){
                                if (flag == 0){
                                    labels[j].setIcon(selectedPlayerIcons[j]);
                                    new AnimateMoveUpDown(labels[finalJ], 0, targetUp, targetDown, hasSelectedPlayer ).execute();
                                } else{
                                    labels[j].setIcon(selectedWeaponIcons[j]);
                                    new AnimateMoveUpDown(labels[finalJ], 0, targetUp, targetDown, hasSelectedWeapon ).execute();
                                }
                            }
                        }
                        labels[finalJ].revalidate();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    if (!moving){
                        for (int j = 0; j < 6; j++){
                            if (flag == 0){
                                labels[j].setIcon(playerIcons[j]);
                                new AnimateMoveUpDown(labels[finalJ], 1, targetUp, targetDown, hasSelectedPlayer ).execute();

                            } else{
                                labels[j].setIcon(weaponIcons[j]);
                                new AnimateMoveUpDown(labels[finalJ], 1, targetUp, targetDown, hasSelectedWeapon ).execute();

                            }
                        }
                        labels[finalJ].revalidate();
                    }
                }
            });
        }
    }

    private void question(){
        //Move the player and weapon to the room
        int questionedPlayer = findLabelID(playerLabels, selectedPlayer);

        if (questionedPlayer == -1){
            System.out.println("Error finding player");
        }

        int questionedWeapon = findLabelID(weaponLabels, selectedWeapon);
        if (questionedPlayer == -1){
            System.out.println("Error finding weapon");
        }

        //Move the player and weapon

        //gameScreen.getGamePlayers().getPlayer(questionedPlayer).getSuspectToken().moveToRoom(currentRoom, gameScreen.getGameBoard());
        //gameScreen.getGameWeapons().moveWeaponToRoom(questionedWeapon, currentRoom);

        ////////////////////////////

        //Pass to the next player


        PlayerHand playerHand = gameScreen.getGamePlayers().getPlayer(nextPlayer).getPlayerHand();

        for (Card card : playerHand.getPlayerHand()){
            if ( (card.getName().matches(selectedPlayer.getCardName()))
                    || (card.getName().matches(selectedWeapon.getCardName()))
                    || (card.getName().matches(selectedRoom.getCardName()))){
                validCards.add(new T11Label(new ImageIcon(card.getCardImage()), card.getName()));
                //System.out.println("Added " + card.getName() + " to valid cards");
            }
        }

        for (Card card : playerHand.getPublicHand()){
            if ( (card.getName().matches(selectedPlayer.getCardName()))
                    || (card.getName().matches(selectedWeapon.getCardName()))
                    || (card.getName().matches(selectedRoom.getCardName()))){
                validCards.add(new T11Label(new ImageIcon(card.getCardImage()), card.getName()));
                //System.out.println("Added " + card.getName() + " to valid cards");
            }
        }

        //System.out.println("Size of valid cards : " + validCards.size());
        incrementNextPlayer();

        gameScreen.getPlayerChange().setPlayerCard(gameScreen.getGamePlayers().getPlayer(nextPlayer));
        gameScreen.getPlayerChange().setVisible(true);
        this.showingNextPlayer = true;

        gameScreen.reDraw(nextPlayer);
    }

    public void showNextPlayer(){
        if (!hasLooped) {
            incrementNextPlayer();
            gameScreen.getPlayerChange().setPlayerCard(gameScreen.getGamePlayers().getPlayer(nextPlayer));
            gameScreen.getPlayerChange().setVisible(true);
            this.showingNextPlayer = true;
            gameScreen.reDraw(nextPlayer);
        } else{
            doneShowing = false;
            hideQuestionPanel();
        }
    }

    public void resizeSelected(){
        final int distance = 2;
        int targetW = (int)(selectedRoom.getWidth() * (resolution.getScalePercentage() * 0.9));
        int targetH = (int)(selectedRoom.getHeight() * (resolution.getScalePercentage() * 0.9));
        int roomTarget = getWidth()/2 - targetW/2;
        int playerTarget = (getWidth()/6);
        int weaponTarget = ((getWidth()/3)*2 - (int)(resolution.getScalePercentage()*20));

        timer = new Timer(4, e->{
            if (selectedPlayer.getWidth() == targetW && selectedPlayer.getHeight() == targetH
                    && selectedRoom.getWidth() == targetW && selectedRoom.getHeight() == targetH
                    && selectedWeapon.getWidth() == targetW && selectedWeapon.getHeight() == targetH
                    && selectedPlayer.getX() == playerTarget
                    && selectedWeapon.getX() == weaponTarget
                    && selectedRoom.getX() == roomTarget ){
                timer.stop();
            } else{
                //System.out.println("player current location " + selectedPlayer.getLocation());
                if (selectedPlayer.getWidth() <= targetW){
                    selectedPlayer.setSize(new Dimension(targetW, selectedPlayer.getHeight()));
                } else{
                    selectedPlayer.setSize(new Dimension(selectedPlayer.getWidth() - distance, selectedPlayer.getHeight()));
                }
                if (selectedPlayer.getHeight() <= targetH){
                    selectedPlayer.setSize(new Dimension(selectedPlayer.getWidth(), targetH));
                } else{
                    selectedPlayer.setSize(new Dimension(selectedPlayer.getWidth(), selectedPlayer.getHeight()-distance));
                }

                if (selectedPlayer.getX() >= playerTarget){
                    selectedPlayer.setLocation(playerTarget, selectedPlayer.getY());
                } else{
                    selectedPlayer.setLocation(selectedPlayer.getX() + distance, selectedPlayer.getY());
                }

                if (selectedRoom.getWidth() <= targetW){
                    selectedRoom.setSize(new Dimension(targetW, selectedRoom.getHeight()));
                } else{
                    selectedRoom.setSize(new Dimension(selectedRoom.getWidth() - distance, selectedRoom.getHeight()));
                }
                if (selectedRoom.getHeight() <= targetH){
                    selectedRoom.setSize(new Dimension(selectedRoom.getWidth(), targetH));
                } else{
                    selectedRoom.setSize(new Dimension(selectedRoom.getWidth(), selectedRoom.getHeight()-distance));
                }

                if (selectedRoom.getX() >= roomTarget){
                    selectedRoom.setLocation(roomTarget, selectedRoom.getY());
                } else{
                    selectedRoom.setLocation(selectedRoom.getX() + distance, selectedRoom.getY());
                }

                if (selectedWeapon.getWidth() <= targetW){
                    selectedWeapon.setSize(new Dimension(targetW, selectedWeapon.getHeight()));
                } else{
                    selectedWeapon.setSize(new Dimension(selectedWeapon.getWidth() - distance, selectedWeapon.getHeight()));
                }
                if (selectedWeapon.getHeight() <= targetH){
                    selectedWeapon.setSize(new Dimension(selectedWeapon.getWidth(), targetH));
                } else{
                    selectedWeapon.setSize(new Dimension(selectedWeapon.getWidth(), selectedWeapon.getHeight() - distance));
                }

                if (selectedWeapon.getX() >= weaponTarget){
                    selectedWeapon.setLocation(weaponTarget, selectedWeapon.getY());
                } else{
                    selectedWeapon.setLocation(selectedWeapon.getX() + distance, selectedWeapon.getY());
                }

                gameScreen.repaint();
            }
        });

        timer.start();
        hasResized = true;
    }

    public boolean getLooped(){
        return hasLooped;
    }

    public void setLooped(boolean b){
        this.hasLooped = b;
    }

    public void addValidCards(){

        //System.out.println("In add valid cards");
        ArrayList<T11Label> validCards = new ArrayList<>(0);

        if (validCards.size() == 0){

            //add in a done button
            JLabel doneLabel = new JLabel("DONE");
            doneLabel.setFont(new Font("Bulky Pixels", Font.BOLD, (int)(40 * resolution.getScalePercentage())));
            doneLabel.setSize((int)(resolution.getScalePercentage() * 200), (int)(resolution.getScalePercentage() * 70));
            doneLabel.setLocation(getWidth()/2 - (int)(resolution.getScalePercentage() * 65), getHeight()/2);
            doneLabel.setForeground(Color.WHITE);
            doneLabel.setBackground(new Color(0, 0, 0, 156));

            JLabel infoLabel = new JLabel("YOU HAVE NO CARDS TO SHOW");
            infoLabel.setFont(new Font("Bulky Pixels", Font.BOLD, (int)(20 * resolution.getScalePercentage())));
            infoLabel.setSize((int)(resolution.getScalePercentage() * 600), (int)(resolution.getScalePercentage() * 100));
            infoLabel.setLocation(doneLabel.getX() - (int)(resolution.getScalePercentage() * 150), doneLabel.getY() - (int)(resolution.getScalePercentage() * 100));
            infoLabel.setForeground(Color.white);
            infoLabel.setBackground(new Color(0,0,0, 156));
            add(infoLabel);

            doneShowing = true;
            doneLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);

                    //Increment first
                    showNextPlayer();
                    System.out.println("showing next player");
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    doneLabel.setForeground(Color.RED);
                    gameScreen.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    doneLabel.setForeground(Color.WHITE);
                    gameScreen.repaint();
                }
            });

            this.add(doneLabel);
            gameScreen.repaint();
        }

        else if (validCards.size() == 1){
            validCards.get(0).setSize(new Dimension(selectedRoom.getWidth() * (int)(resolution.getScalePercentage() * 1.5),
                    selectedRoom.getWidth() * (int)(resolution.getScalePercentage() * 1.5) ));
            validCards.get(0).setLocation(getWidth(), selectedRoom.getY() + selectedRoom.getHeight() + (int) (resolution.getScalePercentage() * 50));
            add(validCards.get(0));


        }
         else if (validCards.size() == 2){

        }

        else if (validCards.size() == 3){

        } else {
            System.out.println("More than 3 cards");
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

    public void textSelectCard(String card){
        if (inPlayerState){
            switch (card){
                case "white":
                    new AnimateCorner(playerLabels, 0, 0).execute();
                    selectedPlayer = playerLabels[0];
                    selectedCards[0] = selectedPlayer;
                    inPlayerState = false;
                    hasSelectedPlayer = true;
                    break;
                case "green":
                    new AnimateCorner(playerLabels, 1, 0).execute();
                    selectedPlayer = playerLabels[1];
                    selectedCards[0] = selectedPlayer;
                    inPlayerState = false;
                    hasSelectedPlayer = true;
                    break;
                case "peacock":
                    new AnimateCorner(playerLabels, 2, 0).execute();
                    selectedPlayer = playerLabels[2];
                    selectedCards[0] = selectedPlayer;
                    inPlayerState = false;
                    hasSelectedPlayer = true;
                    break;
                case "plum":
                    new AnimateCorner(playerLabels, 3, 0).execute();
                    selectedPlayer = playerLabels[3];
                    selectedCards[0] = selectedPlayer;
                    inPlayerState = false;
                    hasSelectedPlayer = true;
                    break;
                case "scarlett":
                    new AnimateCorner(playerLabels, 4, 0).execute();
                    selectedPlayer = playerLabels[4];
                    selectedCards[0] = selectedPlayer;
                    inPlayerState = false;
                    hasSelectedPlayer = true;
                    break;
                case "mustard":
                    new AnimateCorner(playerLabels, 5, 0).execute();
                    selectedPlayer = playerLabels[5];
                    selectedCards[0] = selectedPlayer;
                    inPlayerState = false;
                    hasSelectedPlayer = true;
                    break;
                default:
                    break;
            }
        } else{
            switch (card){
                case "hatchet":
                    new AnimateCorner(weaponLabels, 0, 1).execute();
                    selectedWeapon = weaponLabels[0];
                    selectedCards[2] = selectedWeapon;
                    inWeaponState = false;
                    hasSelectedWeapon = true;
                    break;
                case "dagger":
                    new AnimateCorner(weaponLabels, 1, 1).execute();
                    selectedWeapon = weaponLabels[1];
                    selectedCards[2] = selectedWeapon;
                    inWeaponState = false;
                    hasSelectedWeapon = true;
                    break;
                case "poison":
                    new AnimateCorner(weaponLabels, 2, 1).execute();
                    selectedWeapon = weaponLabels[2];
                    selectedCards[2] = selectedWeapon;
                    inWeaponState = false;
                    hasSelectedWeapon = true;
                    break;
                case "revolver":
                    new AnimateCorner(weaponLabels, 3, 1).execute();
                    selectedWeapon = weaponLabels[3];
                    selectedCards[2] = selectedWeapon;
                    inWeaponState = false;
                    hasSelectedWeapon = true;
                    break;
                case "rope":
                    new AnimateCorner(weaponLabels, 4, 1).execute();
                    selectedWeapon = weaponLabels[4];
                    selectedCards[2] = selectedWeapon;
                    inWeaponState = false;
                    hasSelectedWeapon = true;
                    break;
                case "wrench":
                    new AnimateCorner(weaponLabels, 5, 1).execute();
                    selectedWeapon = weaponLabels[5];
                    selectedCards[2] = selectedWeapon;
                    inWeaponState = false;
                    hasSelectedWeapon = true;
                    break;
                default:
                    break;
            }
        }
    }

    public class AnimateMoveIn extends SwingWorker<Integer, String>{

        private final int distance = 6, delay = 1;
        private T11Label[] labels;
        private Point[] points;

        private ArrayList<T11Label> cards;

        public AnimateMoveIn(T11Label[] labels, Point[] points) {
            this.labels = labels;
            this.points = points;

        }


        @Override
        protected Integer doInBackground() throws Exception{
            moving = true;

            process(new ArrayList<>());
            while (labels[0].getX() != points[0].getX() ){


                if (labels[0].getX() <= points[0].getX()){
                    labels[0].setLocation((int)points[0].getX(), labels[0].getY());
                } else{
                    labels[0].setLocation( (labels[0].getX() - distance), labels[0].getY());
                }

                if (labels[1].getX() <= points[1].getX()){
                    labels[1].setLocation((int)points[1].getX(), labels[1].getY());
                } else{
                    labels[1].setLocation( (labels[1].getX() - distance), labels[1].getY());
                }

                if (labels[2].getX() <= points[2].getX()){
                    labels[2].setLocation((int)points[2].getX(), labels[2].getY());
                } else{
                    labels[2].setLocation( (labels[2].getX() - distance), labels[2].getY());
                }
                if (labels[3].getX() <= points[3].getX()){
                    labels[3].setLocation((int)points[3].getX(), labels[3].getY());
                } else{
                    labels[3].setLocation( (labels[3].getX() - distance), labels[3].getY());
                }
                if (labels[4].getX() <= points[4].getX()){
                    labels[4].setLocation((int)points[4].getX(), labels[4].getY());
                } else{
                    labels[4].setLocation( (labels[4].getX() - distance), labels[4].getY());
                }
                if (labels[5].getX() <= points[5].getX()){
                    labels[5].setLocation((int)points[5].getX(), labels[5].getY());
                } else{
                    labels[5].setLocation( (labels[5].getX() - distance), labels[5].getY());
                }


                process(new ArrayList<>());
                Thread.sleep(delay);
            }
            moving = false;
            process(new ArrayList<>());
            return null;
        }

        @Override
        protected void process(List<String> chunks){
            gameScreen.repaint();
        }
    }

    public class AnimateMoveOut extends SwingWorker<Integer, String>{

        private final int distance = 6, delay = 1;

        private ArrayList<T11Label> labels;

        public AnimateMoveOut(ArrayList<T11Label> labels){
            this.labels = labels;
        }

        @Override
        protected Integer doInBackground() throws Exception{
            process(new ArrayList<>());
            moving = true;
            while (labels.get(0).getX() != translatedLabelPointsLeft[0].getX()){

                if (labels.get(0).getX() <= translatedLabelPointsLeft[0].getX()){
                    labels.get(0).setLocation(translatedLabelPointsLeft[0]);
                } else{
                    labels.get(0).setLocation(labels.get(0).getX() - distance, labels.get(0).getY());
                }

                if (labels.get(1).getX() <= translatedLabelPointsLeft[1].getX()){
                    labels.get(1).setLocation(translatedLabelPointsLeft[1]);
                } else{
                    labels.get(1).setLocation(labels.get(1).getX() - distance, labels.get(1).getY());
                }

                if (labels.get(2).getX() <= translatedLabelPointsLeft[2].getX()){
                    labels.get(2).setLocation(translatedLabelPointsLeft[2]);
                } else{
                    labels.get(2).setLocation(labels.get(2).getX() - distance, labels.get(2).getY());
                }

                if (labels.get(3).getX() <= translatedLabelPointsLeft[3].getX()){
                    labels.get(3).setLocation(translatedLabelPointsLeft[3]);
                } else{
                    labels.get(3).setLocation(labels.get(3).getX() - distance, labels.get(3).getY());
                }

                if (labels.get(4).getX() <= translatedLabelPointsLeft[0].getX()){
                    labels.get(4).setLocation(translatedLabelPointsLeft[0]);
                } else{
                    labels.get(4).setLocation(labels.get(4).getX() - distance, labels.get(4).getY());
                }

                process(new ArrayList<>());
                Thread.sleep(delay);
            }
            moving = false;

            Thread.sleep(250);
            if (!hasSelectedWeapon){
                addCards(weaponLabels, 1);
            }

            if (hasSelectedPlayer && hasSelectedWeapon){
                question();
            }

            process(new ArrayList<>());
            return null;
        }

        @Override
        protected void process(List<String> chunks){
            gameScreen.repaint();
        }
    }

    public class AnimateCorner extends SwingWorker<Integer, String>{

        private final int distance = 6, delay = 1;
        private T11Label[] labels;
        private int index;
        private int flag;

        private final int targetH = selectedRoom.getHeight();
        private final int targetW = selectedRoom.getWidth();

        public AnimateCorner(T11Label[] labels , int index, int flag){
            this.labels = labels;
            this.index = index;
            this.flag = flag;
        }

        @Override
        protected Integer doInBackground() throws Exception{
            process(new ArrayList<>());

            if (this.flag == 0){
                //Will be moving right
                if (labels[index].getX() < playerTargetX){
                    while (labels[index].getX() != playerTargetX || labels[index].getY() != (int)(resolution.getScalePercentage() * 20)
                            || labels[index].getHeight() != targetH || labels[index].getWidth() != targetW ){

                        //System.out.println(labels[index].getLocation());

                        if (labels[index].getX() >= playerTargetX){
                            labels[index].setLocation(playerTargetX, labels[index].getY());
                        } else{
                            labels[index].setLocation(labels[index].getX() + distance, labels[index].getY());
                        }
                        if (labels[index].getY() <= (int)(resolution.getScalePercentage() * 20)){
                            labels[index].setLocation(labels[index].getX(), (int)(resolution.getScalePercentage() * 20));
                        } else {
                            labels[index].setLocation(labels[index].getX(), labels[index].getY() - distance);
                        }


                        if (labels[index].getHeight() >= targetH){
                            labels[index].setSize(new Dimension(labels[index].getWidth(), targetH));
                        } else{
                            labels[index].setSize(new Dimension(labels[index].getWidth(), labels[index].getHeight() + 1));
                        }

                        if (labels[index].getWidth() >= targetW){
                            labels[index].setSize(new Dimension(targetW, labels[index].getHeight()));
                        } else{
                            labels[index].setSize(new Dimension(labels[index].getWidth() + 1, labels[index].getHeight()));
                        }
                        process(new ArrayList<>());
                        Thread.sleep(delay);
                    }
                } else{
                    while (labels[index].getX() != playerTargetX || labels[index].getY() != (int)(resolution.getScalePercentage() * 20)
                            || labels[index].getHeight() != targetH || labels[index].getWidth() != targetW){

                        if (labels[index].getX() <= playerTargetX){
                            labels[index].setLocation(playerTargetX, labels[index].getY());
                        } else{
                            labels[index].setLocation(labels[index].getX() - distance, labels[index].getY());
                        }

                        if (labels[index].getY() <= (int)(resolution.getScalePercentage() * 20)){
                            labels[index].setLocation(labels[index].getX(), (int)(resolution.getScalePercentage() * 20));
                        } else {
                            labels[index].setLocation(labels[index].getX(), labels[index].getY() - distance);
                        }

                        if (labels[index].getHeight() >= targetH){
                            labels[index].setSize(new Dimension(labels[index].getWidth(), targetH));
                        } else{
                            labels[index].setSize(new Dimension(labels[index].getWidth(), labels[index].getHeight() + 1));
                        }

                        if (labels[index].getWidth() >= targetW){
                            labels[index].setSize(new Dimension(targetW, labels[index].getHeight()));
                        } else{
                            labels[index].setSize(new Dimension(labels[index].getWidth() + 1, labels[index].getHeight()));
                        }

                        process(new ArrayList<>());
                        Thread.sleep(delay);
                    }
                }

                hasSelectedPlayer = true;
            }

            else if (this.flag == 1){
                if (labels[index].getX() < weaponTargetX){
                    while (labels[index].getX() != weaponTargetX || labels[index].getY() != (int)(resolution.getScalePercentage() * 20)
                            || labels[index].getHeight() != targetH || labels[index].getWidth() != targetW){


                        if (labels[index].getX() >= weaponTargetX){
                            labels[index].setLocation(weaponTargetX, labels[index].getY());
                        } else{
                            labels[index].setLocation(labels[index].getX() + distance, labels[index].getY());
                        }

                        if (labels[index].getY() <= (int)(resolution.getScalePercentage() * 20)){
                            labels[index].setLocation(labels[index].getX(), (int)(resolution.getScalePercentage() * 20));
                        } else {
                            labels[index].setLocation(labels[index].getX(), labels[index].getY() - distance);
                        }

                        if (labels[index].getHeight() >= targetH){
                            labels[index].setSize(new Dimension(labels[index].getWidth(), targetH));
                        } else{
                            labels[index].setSize(new Dimension(labels[index].getWidth(), labels[index].getHeight() + 1));
                        }

                        if (labels[index].getWidth() >= targetW){
                            labels[index].setSize(new Dimension(targetW, labels[index].getHeight()));
                        } else{
                            labels[index].setSize(new Dimension(labels[index].getWidth() + 1, labels[index].getHeight()));
                        }
                        process(new ArrayList<>());
                        Thread.sleep(delay);
                    }
                } else{
                    while (labels[index].getX() != weaponTargetX || labels[index].getY() != (int)(resolution.getScalePercentage() * 20)
                            || labels[index].getHeight() != targetH || labels[index].getWidth() != targetW){

                        if (labels[index].getX() <= weaponTargetX){
                            labels[index].setLocation(weaponTargetX, labels[index].getY());
                        } else{
                            labels[index].setLocation(labels[index].getX() - distance, labels[index].getY());
                        }

                        if (labels[index].getY() <= (int)(resolution.getScalePercentage() * 20)){
                            labels[index].setLocation(labels[index].getX(), (int)(resolution.getScalePercentage() * 20));
                        } else {
                            labels[index].setLocation(labels[index].getX(), labels[index].getY() - distance);
                        }

                        if (labels[index].getHeight() >= targetH){
                            labels[index].setSize(new Dimension(labels[index].getWidth(), targetH));
                        } else{
                            labels[index].setSize(new Dimension(labels[index].getWidth(), labels[index].getHeight() + 1));
                        }

                        if (labels[index].getWidth() >= targetW){
                            labels[index].setSize(new Dimension(targetW, labels[index].getHeight()));
                        } else{
                            labels[index].setSize(new Dimension(labels[index].getWidth() + 1, labels[index].getHeight()));
                        }

                        process(new ArrayList<>());
                        Thread.sleep(delay);
                    }

                    hasSelectedWeapon = true;
                }
            }

            //Get Remaining cards
            ArrayList<T11Label> remainingCards = new ArrayList<>(5);
            for (int i = 0; i < 6; i++){
                if (i != index){
                    remainingCards.add(labels[i]);
                }
            }

            new AnimateMoveOut(remainingCards).execute();

            process(new ArrayList<>());
            return null;
        }

        @Override
        protected void process(List<String> chunks){
            gameScreen.repaint();
        }
    }

    public class AnimateMoveUpDown extends SwingWorker<Integer, String>{

        private final int distance = 4, delay = 4;

        private int targetUp;
        private int targetDown;

        private int flag;
        private T11Label label;

        private boolean hasSelected;

        public AnimateMoveUpDown(T11Label label, int flag, int targetUp, int targetDown, boolean hasSelected){
            this.label = label;
            this.flag = flag;
            this.targetUp = targetUp;
            this.targetDown = targetDown;
            this.hasSelected = hasSelected;
        }

        @Override
        protected Integer doInBackground() throws Exception{

            process(new ArrayList<>());
            //Moving up
            if (flag == 0 && (!hasSelected)){
                while (label.getY() != targetUp){
                    if (label.getY() <= targetUp){
                        label.setLocation(label.getX(), targetUp);
                    } else{
                        label.setLocation(label.getX(), label.getY() - distance);
                    }

                    process(new ArrayList<>());
                    Thread.sleep(delay);
                }
            }

            //Moving down
            else if  (flag == 1 && (!hasSelected)){
                Thread.sleep(100);

                while (label.getY() != targetDown){
                    //System.out.println(label.getLocation());
                    if (label.getY() >= targetDown){
                        label.setLocation(label.getX(), targetDown);
                    } else{
                        label.setLocation(label.getX(), label.getY() + distance);
                    }

                    process(new ArrayList<>());
                    Thread.sleep(delay);
                }
            }

            process(new ArrayList<>());
            return null;
        }

        @Override
        protected void process(List<String> chunks){
            gameScreen.repaint();
        }
    }

}
