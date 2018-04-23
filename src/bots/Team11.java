/*
    Team11 Authors :    Jack Geraghty - 16384181
                        Conor Beenham -
                        Alen Thomas   - 16333003
*/

package bots;

import gameengine.*;
import gameengine.Map;

import java.awt.*;
import java.util.*;


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

    ArrayList<String> moveList;
    LinkedList<String> targetRooms;


    int targetX, targetY;

    private int rollResult;
    private QuestioningLogic questioningLogic = new QuestioningLogic();
    private String currentRoom;
    private boolean inRoom, inCellar, rollDone, questionDone, questioning, accusing, moveDone, moving, doOnce;



    public Team11(Player player, PlayersInfo playersInfo, Map map, Dice dice, Log log, Deck deck) {
        this.player = player;
        this.playersInfo = playersInfo;
        this.map = map;
        this.dice = dice;
        this.log = log;
        this.deck = deck;

        inRoom = false;
        doOnce = true;
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

        if(player.getToken().isInRoom()) {
            currentRoom = player.getToken().getRoom().toString();
            inRoom = true;
            if (moving) {
                moveDone = true;
                moving = false;
            }
        } else {
            currentRoom = null;
            inRoom = false;
        }

        System.out.println("question done: " + questionDone  +
                "\ninRoom: " + inRoom  + "\nshouldQ:" + questioningLogic.shouldQuestion());

        if (!questionDone && inRoom && questioningLogic.shouldQuestion()) {
            System.out.println("SHOULD QUESTION");
            return doQuestion();
        } else if (questionDone && inRoom && !rollDone) {
            return doRoll();
        } else if (!rollDone) {
            return doRoll();
        } else if (inRoom && moving) {
            return doExit();
        } else if (moveDone && inRoom && !questionDone) {
            return doQuestion();
        } else if (questioningLogic.readyToAccuse() && inCellar) {
            return doAccuse();
        }
        return endTurn();
    }

    public String getMove() {
        // Add your code here
        System.out.println(questioningLogic.roomCards);
        String targetRoom = getTargetRoom();
        System.out.println(targetRoom);
        getTargetRoomDoor(targetRoom);
        System.out.println("Target X " + targetX + " Target Y " + targetY);
        if (doOnce) {
            System.out.println(targetX + "  " + targetY);
            moveList = movementHandling(player.getToken().getPosition().getRow(), player.getToken().getPosition().getCol(), targetX, targetY);
            System.out.println(moveList);
            doOnce = false;
        }
        if (moveList.size() > 0){
            return moveList.remove(0);
        }
        else {
            int row = player.getToken().getPosition().getRow();
            int col = player.getToken().getPosition().getCol();

            //If we are accusing prioritise moving up so that when we land outside the cellar we will move into it
            if (accusing){
                if (map.isDoor(player.getToken().getPosition(), new Coordinates(col,row-1 )) ){
                    return "u";
                }
                else if (map.isDoor(player.getToken().getPosition(), new Coordinates(col, row+1 ))){
                    return "d";
                }
            } else{
                if (map.isDoor(player.getToken().getPosition(), new Coordinates(col, row+1 ))){
                    return "d";
                }
                else if (map.isDoor(player.getToken().getPosition(), new Coordinates(col,row-1 )) ){
                    return "u";
                }
            }

            if (map.isDoor(player.getToken().getPosition(), new Coordinates(col-1, row))){
                return "l";
            } else{
                return "r";
            }
        }
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
        questioningLogic.analyseLatestQuery();
    }

    @Override
    public void notifyQuery(String playerName, String query) {
        System.out.println("Player:" + playerName + " : " + query);
        String suspect = null, weapon = null, room = null;
        for (String suspectName : Names.SUSPECT_NAMES) {
            if (query.contains(suspectName)) {
                suspect = suspectName;
                break;
            }
        }
        for (String weaponName : Names.WEAPON_NAMES) {
            if (query.contains(weaponName)) {
                weapon = weaponName;
                break;
            }
        }
        for (String roomName : Names.ROOM_CARD_NAMES) {
            if (query.contains(roomName)) {
                room = roomName;
                break;
            }
        }

        questioningLogic.getLatestQuery().setQueryingPlayer(playerName);
        questioningLogic.getLatestQuery().setQuery(suspect, weapon, room);
    }

    @Override
    public void notifyReply(String playerName, boolean cardShown) {
        if (cardShown) {
            questioningLogic.getLatestQuery().setQueriedPlayer(playerName);
        }
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
        if (inRoom){
            System.out.println("Exiting");
            targetRooms.addLast(targetRooms.removeFirst());
            System.out.println("Target Rooms: " + targetRooms);
        }
        rollDone = true;
        moving = true;
        moveList = new ArrayList<>();

        System.out.println("Move list is: " + moveList);
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
        doOnce = true;
    }

    public class QuestioningLogic {
        private final HashSet<String> roomCards = new HashSet<>(), suspectCards = new HashSet<>(), weaponCards = new HashSet<>();
        private final HashSet<String> myRoomCards = new HashSet<>(), mySuspectCards = new HashSet<>(), myWeaponCards = new HashSet<>();
        private final HashSet<String > knownCards = new HashSet<>();
        private final HashSet<String > publicCards = new HashSet<>();

        private LatestQuery latestQuery = new LatestQuery();

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
            System.out.println("Rooms " + roomCards);
            targetRooms = new LinkedList<>(roomCards);

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
            System.out.println((!knownCards.contains(currentRoom) && !publicCards.contains(currentRoom)) ||
                    (foundRoom && (accusedRoom.equals(currentRoom) || myRoomCards.contains(currentRoom))));
            return (!knownCards.contains(currentRoom) && !publicCards.contains(currentRoom)) ||
                    (foundRoom && (accusedRoom.equals(currentRoom) || myRoomCards.contains(currentRoom)));
        }

        public boolean readyToAccuse() {
            return foundSuspect && foundWeapon && foundRoom;
        }

        public void analyseLatestQuery() {
            boolean knowSus = false, knowWeapon = false, knowRoom = false;
            if (mySuspectCards.contains(latestQuery.getQuery().getSuspect())
                    || knownCards.contains(latestQuery.getQuery().getSuspect())) {
                knowSus = true;
            }
            if (myWeaponCards.contains(latestQuery.getQuery().getWeapon())
                    || knownCards.contains(latestQuery.getQuery().getWeapon())) {
                knowWeapon = true;
            }
            if (myRoomCards.contains(latestQuery.getQuery().getRoom())
                    || knownCards.contains(latestQuery.getQuery().getRoom())) {
                knowRoom = true;
            }

            if ((knowSus && knowWeapon) || (knowSus && knowRoom) || (knowWeapon && knowRoom)) {
                if (knowSus && knowWeapon) {
                    knownCards.add(latestQuery.getQuery().getRoom());
                } else if (knowSus) {
                    knownCards.add(latestQuery.getQuery().getWeapon());
                } else {
                    knownCards.add(latestQuery.getQuery().getSuspect());
                }
            }
        }

        public LatestQuery getLatestQuery() {
            return latestQuery;
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


    public class LatestQuery {
        private Query query;
        private String queryingPlayer, queriedPlayer;

        public void setQuery(String suspect, String weapon, String room) {
            this.query = new Query(suspect, weapon, room);
        }

        public void setQueryingPlayer(String queryingPlayer) {
            this.queryingPlayer = queryingPlayer;
        }

        public void setQueriedPlayer(String queriedPlayer) {
            this.queriedPlayer = queriedPlayer;
        }

        public Query getQuery() {
            return this.query;
        }

        public String getQueryingPlayer() {
            return queryingPlayer;
        }

        public String getQueriedPlayer() {
            return queriedPlayer;
        }
    }

    private String getTargetRoom(){
        if (targetRooms.size() > 0) {
            return targetRooms.peek();
        }
        return null;
    }

    private void getTargetRoomDoor(String roomName){
        System.out.println("Room Name: " + roomName);
        ArrayList<Coordinates> doors = new ArrayList<>();
        int index;
        switch (roomName){
            case "Kitchen":
                //System.out.println("In case kitchen");
                this.targetX = 7;
                this.targetY = 4;
                break;

            case "Ballroom":
                //System.out.println("In case ballroom");
                doors.add(new Coordinates(9,8));
                doors.add(new Coordinates(7,5));
                doors.add(new Coordinates(14,8));
                doors.add(new Coordinates(16,5));

                index = getClosestDoor(doors);
                this.targetX = doors.get(index).getRow();
                this.targetY = doors.get(index).getCol();
                break;

            case "Conservatory":
                //System.out.println("In case conservatory");
                this.targetX = 5;
                this.targetY = 18;

                break;

            case "Billiard":
                //System.out.println("In case billiard");
                this.targetX = 9;
                this.targetY = 17;
                break;

            case "Library":
                //System.out.println("In case library");
                doors.add(new Coordinates(16,16));
                doors.add(new Coordinates(20,13));

                index = getClosestDoor(doors);
                //System.out.println("Closest door is " + doors.get(index));

                this.targetX = doors.get(index).getRow();
                this.targetY = doors.get(index).getCol();
                break;

            case "Study":
                //System.out.println("In case study");
                this.targetX = 20;
                this.targetY = 16;
                break;

            case "Hall":
                //System.out.println("In case hall");
                doors.add(new Coordinates(11,17));
                doors.add(new Coordinates(12,17));
                doors.add(new Coordinates(15,20));

                index = getClosestDoor(doors);
                this.targetX = doors.get(index).getRow();
                this.targetY = doors.get(index).getCol();
                break;

            case "Lounge":
                //System.out.println("In case lounge");
                this.targetX = 18;
                this.targetY = 6;
                break;

            case "Dining":
                //System.out.println("In case dining");
                doors.add(new Coordinates(6,16));
                doors.add(new Coordinates(8,12));

                index = getClosestDoor(doors);
                this.targetX = doors.get(index).getRow();
                this.targetY = doors.get(index).getCol();
                break;
        }

        //System.out.println("Target X " + targetX + " TargetY " + targetY);

    }

    private int getClosestDoor(ArrayList<Coordinates> list){
        int minDistance = 1000;
        int index = 0;

        for (Coordinates coordinates : list){
            if (minDistance > (Math.abs(player.getToken().getPosition().getRow() - coordinates.getRow() ) +
                    Math.abs(player.getToken().getPosition().getCol() - coordinates.getCol()))) {

                minDistance = (Math.abs(player.getToken().getPosition().getRow() - coordinates.getRow()) +
                        Math.abs(player.getToken().getPosition().getCol() - coordinates.getCol()));
                index = list.indexOf(coordinates);
            }
        }

        return index;
    }

    /*
        Method to handle the movement of the bot
        CurrX and CurrY are the current location of the bot and the tarX and tarY are determined by which
        room we think the bot should move to.
        TarX and TarY should be set to be the door tile itself not the doorMat

        P.S. I hope this works :)
        It has not been tested
    */
    private ArrayList<String> movementHandling(int currX, int currY, int tarX, int tarY){
        Path path;
        Pathfinding pathfinding = new Pathfinding(map, 100, false);
        path = pathfinding.findPath(currX,currY,tarX, tarY);
        ArrayList<String> movementCommands = pathToDirections(path);
        //System.out.println("Path is " + path);
        //System.out.println(movementCommands);

        return movementCommands;
    }

    private ArrayList<String> pathToDirections(Path path){
        ArrayList<String> directions = new ArrayList<>();

        Point previousPoint = new Point( player.getToken().getPosition().getRow(), player.getToken().getPosition().getCol() );
        //System.out.println(previousPoint);
        System.out.println(path);

        Point nextPoint = new Point(path.getStep(0).getY(), path.getStep(0).getX());

        for (int i = 0; i < path.getLength(); i++){
            if (nextPoint.getX() == previousPoint.getX()){
                if (nextPoint.getY() < previousPoint.getY()){
                    directions.add("u");
                }else {
                    directions.add("d");
                }
            }

            else if (nextPoint.getY() == previousPoint.getY()){
                if (nextPoint.getX() < previousPoint.getX()){
                    //System.out.println(nextPoint.getX() + " < " + previousPoint.getX());
                    directions.add("l");
                } else {
                    //System.out.println(nextPoint.getX() + " > " + previousPoint.getX());
                    directions.add("r");
                }
            }

            //Update next and previous
            previousPoint = new Point(path.getStep(i).getY(), path.getStep(i).getX());
            if (i < path.getLength()-1){
                nextPoint = new Point(path.getStep(i+1).getY(), path.getStep(i+1).getX());
            }
        }

        //path.getSteps().remove(0);
        //System.out.println("Directions:" + directions);
        return directions;
    }

    private class Pathfinding {
        private HashSet closed = new HashSet<>();
        private PriorityQueue open = new PriorityQueue();

        private Map tbMap;
        private int maxSearchDistance;

        private Node[][] nodes;
        private boolean allowDiagMovement;

        private AStarHeuristic heuristic;

        private boolean pathFinderVisited[][];

        public Pathfinding(Map map, int maxSearchDistance, boolean allowDiagMovement) {
            this(map, maxSearchDistance, allowDiagMovement, new ClosestHeuristic());
        }


        public Pathfinding(Map map, int maxSearchDistance,
                               boolean allowDiagMovement, AStarHeuristic heuristic) {
            this.heuristic = heuristic;
            this.tbMap = map;
            this.maxSearchDistance = maxSearchDistance;
            this.allowDiagMovement = allowDiagMovement;

            nodes = new Node[Map.NUM_ROWS][Map.NUM_COLS];
            for (int x=0;x<Map.NUM_ROWS;x++) {
                for (int y=0;y< Map.NUM_COLS;y++) {
                    nodes[x][y] = new Node(x,y);
                }
            }
            this.pathFinderVisited = new boolean[Map.NUM_ROWS][Map.NUM_COLS];
        }

        private Path findPath(int sx, int sy, int tx, int ty) {
            //System.out.println("tx " + tx + " ty " + ty);

            nodes[sx][sy].cost = 0;
            nodes[sx][sy].depth = 0;
            closed.clear();
            open.clear();
            addToOpen(nodes[sx][sy]);

            nodes[tx][ty].parent = null;

            int maxDepth = 0;
            while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {
                // pull out the first node in our open list, this is determined to

                // be the most likely to be the next step based on our heuristic

                Node current = getFirstInOpen();
                if (current == nodes[tx][ty]) {
                    break;
                }

                removeFromOpen(current);
                addToClosed(current);

                for (int x=-1;x<2;x++) {
                    for (int y=-1;y<2;y++) {
                        // not a neighbour, its the current tile

                        if ((x == 0) && (y == 0)) {
                            continue;
                        }

                        // if we're not allowing diaganol movement then only
                        // one of x or y can be set
                        if (!allowDiagMovement) {
                            if ((x != 0) && (y != 0)) {
                                continue;
                            }
                        }

                        // determine the location of the neighbour and evaluate it

                        int xp = x + current.x;
                        int yp = y + current.y;

                        if (isValidLocation(sx,sy,xp,yp)) {

                            float nextStepCost = current.cost + getMovementCost(current.x, current.y, xp, yp);
                            Node neighbour = nodes[xp][yp];
                            pathFinderVisited[xp][yp] = true;

                            if (nextStepCost < neighbour.cost) {
                                if (inOpenList(neighbour)) {
                                    removeFromOpen(neighbour);
                                }
                                if (inClosedList(neighbour)) {
                                    removeFromClosed(neighbour);
                                }
                            }

                            if (!inOpenList(neighbour) && !(inClosedList(neighbour))) {
                                //System.out.println("HERE");
                                neighbour.cost = nextStepCost;
                                neighbour.heuristic = heuristic.getCost(xp, yp, tx, ty);
                                maxDepth = Math.max(maxDepth, neighbour.setParent(current));
                                addToOpen(neighbour);
                            }
                        }
                    }
                }
            }

            Path path = new Path();
            Node target = nodes[tx][ty];
            while (target != nodes[sx][sy]) {
                path.prependStep(target.x, target.y);
                target = target.parent;
            }
            path.prependStep(sx,sy);

            return path;
    }

        public float getMovementCost(int x, int y, int xp, int xy){
            return 1;
        }

        private boolean isValidLocation(int sx, int sy, int xp, int yp){
            boolean inBounds = !( ((xp < 0) || xp >= (Map.NUM_ROWS))  || ((yp < 0) || (yp >= Map.NUM_COLS)));

            if (inBounds){
                inBounds = (tbMap.isCorridor(new Coordinates(yp,xp))  || tbMap.isDoor(new Coordinates(sy,sx), new Coordinates(yp,xp)));
            }

            return inBounds;
        }


        private Node getFirstInOpen() {
            return (Node) open.peek();
        }

        @SuppressWarnings("unchecked")
        private void addToOpen(Node node) {
            open.add(node);
        }

        private boolean inOpenList(Node node) {
            return open.contains(node);
        }

        private void removeFromOpen(Node node) {
            open.remove(node);
        }

        @SuppressWarnings("unchecked")
        private void addToClosed(Node node) {
            closed.add(node);
        }

        private boolean inClosedList(Node node) {
            return closed.contains(node);
        }

        private void removeFromClosed(Node node) {
            closed.remove(node);
        }
    }

    private class Node implements Comparable {
        //The x coordinate of the node
        private int x;
        //The y coordinate of the node
        private int y;
        //The path cost for this node
        private float cost;
        //The parent of this node, how we reached the current node
        private Node parent;
        // The heuristic cost of this node
        private float heuristic;
        // The search depth of this node
        private int depth;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private int setParent(Node parent) {
            depth = parent.depth + 1;
            this.parent = parent;

            return depth;
        }

        public int compareTo(Object other) {
            Node o = (Node) other;

            float f = heuristic + cost;
            float of = o.heuristic + o.cost;

            return Float.compare(f, of);
        }
    }

    private class ClosestHeuristic implements AStarHeuristic{
        @Override
        public float getCost(int x, int y, int tx, int ty){
            return Math.abs( (x - tx) + (y-ty));
        }
    }

    private class Path{
        private ArrayList<Step> steps = new ArrayList<>();

        public Path() {}

        public int getLength() {
            return steps.size();
        }

        public Step getStep(int index) {
            return steps.get(index);
        }

        public ArrayList getSteps() {
            return steps;
        }

        public int getX(int index) {
            return getStep(index).x;
        }

        public int getY(int index) {
            return getStep(index).y;
        }

        public void appendStep(int x, int y) {
            steps.add(new Step(x,y));
        }

        public void prependStep(int x, int y) {
            steps.add(0, new Step(x, y));
        }

        public boolean contains(int x, int y) {
            return steps.contains(new Step(x,y));
        }

        public Point getStepAsPoint(int index){
            return new Point(this.getX(index),this.getY(index));
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();

            for (Step step : steps){
                sb.append(step);
            }
            return sb.toString();
        }

        public class Step {
            // The x coordinate at the given step
            private int x;
            // The y coordinate at the given step
            private int y;

            public Step(int x, int y) {
                this.x = x;
                this.y = y;
            }

            public int getX() {
                return x;
            }

            public int getY() {
                return y;
            }

            public int hashCode() {
                return x*y;
            }

            public boolean equals(Object other) {
                if (other instanceof Step) {
                    Step o = (Step) other;

                    return (o.x == x) && (o.y == y);
                }
                return false;
            }

            public String toString(){
                return ("(" + this.getY() + ", " + this.getX() + ")");
            }
        }
    }

    public interface AStarHeuristic{
        float getCost(int x, int y, int tx, int ty);
    }
}
