/*
    Team11 Authors :    Jack Geraghty - 16384181
                        Conor Beenham -
                        Alen Thomas   - 16333003
*/

package bots;

import gameengine.*;

import javax.annotation.processing.SupportedSourceVersion;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class Team11 implements BotAPI {

    // The public API of Bot must not change
    // This is ONLY class that you can edit in the program
    // Rename Bot to the name of your team. Use camel case.
    // Bot may not alter the state of the board or the player objects
    // It may only inspect the state of the board and the player objects

    private Player player;
    private PlayersInfo playersInfo;
    private Map map;
    private Dice dice;
    private Log log;
    private Deck deck;

    private QuestioningLogic questioningLogic = new QuestioningLogic();

    private String currentRoom;
    private boolean inRoom, inCellar, rollDone, questionDone, questioning, accusing, moveDone, moving;

    public Team11(Player player, PlayersInfo playersInfo, Map map, Dice dice, Log log, Deck deck) {
        this.player = player;
        this.playersInfo = playersInfo;
        this.map = map;
        this.dice = dice;
        this.log = log;
        this.deck = deck;

        inRoom = false;
        inCellar = false;
        resetBools();
    }

    public String getName() {
        return "Team11"; // must match the class name
    }

    @Override
    public String getVersion() {
        return null;
    }

    public String getCommand() {
        if (!questioningLogic.isInitialised())
            questioningLogic.initialiseCards();

        if (!questionDone && inRoom && questioningLogic.shouldQuestion())
            return doQuestion();
        if (questionDone && inRoom && !rollDone)
            return doRoll();
        if (!rollDone)
            return doRoll();
        if (inRoom && moving)
            return doExit();
        if (moveDone && inRoom && !questionDone)
            return doQuestion();
        if (questioningLogic.readyToAccuse() && inCellar)
            return doAccuse();
        return endTurn();
    }

    public String getMove() {
        // Add your code here
        return "r";
    }

    public String getSuspect() {
        // Add your code here
        if (questioning)
            return questioningLogic.getCardToQuestion(0);
        if (accusing)
            return questioningLogic.getCardToAccuse(0);
        return Names.SUSPECT_NAMES[0];
    }

    public String getWeapon() {
        // Add your code here
        if (questioning) {
            questioning = false;
            questionDone = true;
            return questioningLogic.getCardToQuestion(1);
        }
        if (accusing)
            return questioningLogic.getCardToAccuse(1);
        return Names.WEAPON_NAMES[0];
    }

    public String getRoom() {
        // Add your code here
        if (accusing)
            return questioningLogic.getCardToAccuse(2);
        return Names.ROOM_NAMES[0];
    }

    public String getDoor() {
        // Add your code here
        return "1";
    }

    public String getCard(Cards matchingCards) {
        // Add your code here
        return matchingCards.get().toString();
    }

    public void notifyResponse(Log response) {
        // Add your code here
    }

    @Override
    public void notifyPlayerName(String playerName) {

    }

    @Override
    public void notifyTurnOver(String playerName, String position) {

    }

    @Override
    public void notifyQuery(String playerName, String query) {

    }

    @Override
    public void notifyReply(String playerName, boolean cardShown) {

    }

    private String doExit() {
        inRoom = false;
        currentRoom = null;
        return "exit";
    }

    private String doQuestion() {
        questioning = true;
        return "question";
    }

    private String doRoll() {
        rollDone = true;
        moving = true;
        return "roll";
    }

    private String doAccuse() {
        accusing = true;
        return "accuse";
    }

    private String endTurn() {
        resetBools();
        return "done";
    }

    private void resetBools() {
        questioning = false;
        questionDone = false;
        moveDone = false;
        moving = false;
        rollDone = false;
        accusing = false;
    }


    public class QuestioningLogic {
        private final HashSet<String> roomCards = new HashSet<>(), suspectCards = new HashSet<>(), weaponCards = new HashSet<>();
        private final HashSet<String> myRoomCards = new HashSet<>(), mySuspectCards = new HashSet<>(), myWeaponCards = new HashSet<>();
        private final HashSet<String > knownCards = new HashSet<>();
        private final HashSet<String > publicCards = new HashSet<>();

        private String accusedSuspect, accusedWeapon, accusedRoom;
        private boolean foundSuspect = false, foundWeapon = false, foundRoom = false;
        private boolean initialised = false;

        public void initialiseCards() {
            roomCards.addAll(Arrays.asList(Names.ROOM_CARD_NAMES));
            suspectCards.addAll(Arrays.asList(Names.SUSPECT_NAMES));
            weaponCards.addAll(Arrays.asList(Names.WEAPON_NAMES));

            System.out.println("PLAYER: ");
            for (Object o : player.getCards()) {
                System.out.println(o.toString());
                if (roomCards.contains(o.toString())) {
                    myRoomCards.add(o.toString());
                    roomCards.remove(o.toString());
                } else if (weaponCards.contains(o.toString())) {
                    myWeaponCards.add(o.toString());
                    weaponCards.remove(o.toString());
                } else if (suspectCards.contains(o.toString())) {
                    mySuspectCards.add(o.toString());
                    suspectCards.remove(o.toString());
                }
                knownCards.add(o.toString());
            }

            System.out.println("SHARED: ");
            for (Object o : deck.getSharedCards()) {
                if (roomCards.contains(o.toString())) {
                    publicCards.add(o.toString());
                    roomCards.remove(o.toString());
                } else if (weaponCards.contains(o.toString())) {
                    publicCards.add(o.toString());
                    weaponCards.remove(o.toString());
                } else if (suspectCards.contains(o.toString())) {
                    publicCards.add(o.toString());
                    suspectCards.remove(o.toString());
                }
                knownCards.add(o.toString());
                System.out.println(o.toString());
            }

            initialised = true;
        }

        public boolean isInitialised() {
            return initialised;
        }

        public String getCardToQuestion(int cardType) {
            switch (cardType) {
                case 0:
                    return questionCard(foundSuspect, suspectCards, mySuspectCards, accusedSuspect);
                case 1:
                    return questionCard(foundWeapon, weaponCards, myWeaponCards, accusedWeapon);
                case 2:
                    return questionCard(foundRoom, roomCards, myRoomCards, accusedRoom);
                default:
                    return null;
            }
        }

        public String getCardToAccuse(int cardType) {
            switch (cardType) {
                case 0:
                    return accusedSuspect;
                case 1:
                    return accusedWeapon;
                case 2:
                    return accusedRoom;
                default:
                    return null;
            }
        }

        public String questionCard(boolean found, HashSet<String> cards, HashSet<String> myCards, String accusedCard) {
            if (!found)
                return cards.stream().findFirst().orElse(null);
            else if (!myCards.isEmpty())
                return myCards.stream().findFirst().orElse(null);
            else
                return accusedCard;
        }

        public boolean shouldQuestion() {
            return (!knownCards.contains(currentRoom) && !publicCards.contains(currentRoom)) ||
                    (foundRoom && (accusedRoom.equals(currentRoom) || myRoomCards.contains(currentRoom)));
        }

        public boolean readyToAccuse() {
            return foundSuspect && foundWeapon && foundRoom;
        }

        public void printKnownCards() {
            if (publicCards.size() > 0) {
                StringBuilder s = new StringBuilder();
                s.append("Shared cards: ");
                for (String name : publicCards) {
                    s.append(name).append(", ");
                }
                System.out.println(s.toString());
            }
            if (knownCards.size() > 0) {
                StringBuilder s = new StringBuilder();
                s.append("Known cards: ");
                for (String name : knownCards) {
                    s.append(name).append(", ");
                }
                System.out.println(s.toString());
            }
        }
    }

}
