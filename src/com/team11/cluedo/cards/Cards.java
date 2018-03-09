/*
 * Code to handle the creation of the cluedo cards.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.cards;

import com.team11.cluedo.board.room.RoomData;
import com.team11.cluedo.players.Players;
import com.team11.cluedo.suspects.SuspectData;
import com.team11.cluedo.weapons.WeaponData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Cards {
    private List<SuspectCard> suspectCards = new ArrayList<>();
    private List<WeaponCard> weaponCards = new ArrayList<>();
    private List<RoomCard> roomCards = new ArrayList<>();
    private List<Card> publicEnvelope = new ArrayList<>();

    private MurderEnvelope murderEnvelope;

    public Cards() {
        setupSuspectCards();
        setupRoomCards();
        setupWeaponCards();
        setupMurderEnvelope();
    }

    private void setupSuspectCards() {
        SuspectData data = new SuspectData();
        for (int i = 0; i < data.getSuspectAmount() ; i++) {
            suspectCards.add(new SuspectCard(data.getSuspectCard(i), data.getSelectedSuspectCard(i), data.getSuspectName(i)));
        }
    }

    private void setupWeaponCards() {
        WeaponData data = new WeaponData();
        for (int i = 0; i < data.getWeaponAmount() ; i++) {
            weaponCards.add(new WeaponCard(data.getWeaponCard(i), data.getSelectedWeaponCard(i), data.getWeaponName(i)));
        }
    }

    private void setupRoomCards() {
        RoomData data = new RoomData();
        for (int i = 0; i < data.getRoomAmount() ; i++) {
            roomCards.add(new RoomCard(data.getRoomCard(i), data.getSelectedRoomCard(i), data.getRoomName(i)));
        }
    }

    private void setupMurderEnvelope() {
        Collections.shuffle(suspectCards);
        Collections.shuffle(weaponCards);
        Collections.shuffle(roomCards);

        murderEnvelope = new MurderEnvelope(suspectCards.remove(0), roomCards.remove(0), weaponCards.remove(0));
    }

    public void dealCards(Players gamePlayers) {
        int remainingCards = suspectCards.size() + weaponCards.size() + roomCards.size();
        int publicCards = remainingCards % gamePlayers.getPlayerCount();
        int dealCards = remainingCards - publicCards;
        Card tmp;

        for (int i = 0, currentPlayer = 0 ; i < dealCards ; i++, currentPlayer++) {
            Random rand = new Random();

            if (currentPlayer == gamePlayers.getPlayerCount()) {
                currentPlayer = 0;
            }
            switch (rand.nextInt(3)) {
                case 0:
                    if(suspectCards.size() > 0) {
                        tmp = suspectCards.remove(0);
                        gamePlayers.getPlayer(currentPlayer).getPlayerHand().addCardToHand(tmp);
                        gamePlayers.getPlayer(currentPlayer).getPlayerNotes().paintCell(currentPlayer+1, tmp.getName());
                    } else {
                        i--;
                        if (currentPlayer == 0)
                            currentPlayer = gamePlayers.getPlayerCount() - 1;
                        else
                            currentPlayer--;
                    }
                    break;
                case 1:
                    if(weaponCards.size() > 0) {
                        tmp = weaponCards.remove(0);
                        gamePlayers.getPlayer(currentPlayer).getPlayerHand().addCardToHand(tmp);
                        gamePlayers.getPlayer(currentPlayer).getPlayerNotes().paintCell(currentPlayer+1, tmp.getName());
                    } else {
                        i--;
                        currentPlayer--;
                    }
                    break;
                case 2:
                    if(roomCards.size() > 0) {
                        tmp = roomCards.remove(0);
                        gamePlayers.getPlayer(currentPlayer).getPlayerHand().addCardToHand(tmp);
                        gamePlayers.getPlayer(currentPlayer).getPlayerNotes().paintCell(currentPlayer+1, tmp.getName());
                    } else {
                        i--;
                        currentPlayer--;
                    }
                    break;
            }
        }

        while(!suspectCards.isEmpty()) {
            publicEnvelope.add(suspectCards.remove(0));
        }
        while(!weaponCards.isEmpty()) {
            publicEnvelope.add(weaponCards.remove(0));
        }
        while(!roomCards.isEmpty()) {
            publicEnvelope.add(roomCards.remove(0));
        }
    }

    public MurderEnvelope getMurderEnvelope() {
        return murderEnvelope;
    }

    public List<Card> getPublicEnvelope() {
        return publicEnvelope;
    }

}
