/*
    Team11 Authors :    Jack Geraghty - 16384181
                        Conor Beenham -
                        Alen Thomas   -
*/

package bots;

import gameengine.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

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

    private int rollResult;

    public Team11(Player player, PlayersInfo playersInfo, Map map, Dice dice, Log log, Deck deck) {
        this.player = player;
        this.playersInfo = playersInfo;
        this.map = map;
        this.dice = dice;
        this.log = log;
        this.deck = deck;
    }

    public String getName() {
        return "Team11"; // must match the class name
    }

    public String getCommand() {
        // Add your code here
        return "done";
    }

    public String getMove() {
        // Add your code here
        int tarX = 5, tarY = 7;
        ArrayList<String> commands = movementHandling(player.getToken().getPosition().getRow(), player.getToken().getPosition().getCol(), tarX, tarY);
        return "r";
    }

    public String getSuspect() {
        // Add your code here
        return Names.SUSPECT_NAMES[0];
    }

    public String getWeapon() {
        // Add your code here
        return Names.WEAPON_NAMES[0];
    }

    public String getRoom() {
        // Add your code here
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



    /*
        Method to handle the movement of the bot
        CurrX and CurrY are the current location of the bot and the tarX and tarY are determined by which
        room we think the bot should move to.
        TarX and TarY should be set to be the door tile itself not the doorMat

        P.S. I hope this works :)
        It has not been tested
    */
    public ArrayList<String> movementHandling(int currX, int currY, int tarX, int tarY){
        Path path;
        Pathfinding pathfinding = new Pathfinding(map, rollResult, false);
        path = pathfinding.findPath(currX,currY,tarX, tarY);
        ArrayList<String> movementCommands = pathToDirections(path);
        System.out.println(movementCommands);
        System.out.println("Path is " + path);

        return movementCommands;
    }

    private ArrayList<String> pathToDirections(Path path){
        ArrayList<String> directions = new ArrayList<>();

        Point previousPoint = new Point( player.getToken().getPosition().getRow(), player.getToken().getPosition().getCol() );

        Point nextPoint = new Point(path.getStep(0).getY(), path.getStep(0).getX());

        for (int i = 0; i < path.getLength(); i++){
            if (nextPoint.getX() == previousPoint.getX()){
                if (nextPoint.getY() > previousPoint.getY()){
                    directions.add("s");
                }else {
                    directions.add("u");
                }
            }

            else if (nextPoint.getY() == previousPoint.getY()){
                if (nextPoint.getX() > previousPoint.getX()){
                    directions.add("r");
                } else {
                    directions.add("l");
                }
            }

            //Update next and previous
            previousPoint = new Point(path.getStep(i).getY(), path.getStep(i).getX());
            if (i < path.getLength()-1){
                nextPoint = new Point(path.getStep(i+1).getY(), path.getStep(i+1).getX());
            }
        }
        path.getSteps().remove(0);
        return directions;
    }

    private class Pathfinding{
        private HashSet closed = new HashSet();
        private PriorityQueue open = new PriorityQueue();

        private Map tbMap;
        private int maxDistance;

        private Node[][] nodes;
        private boolean allowDiagMovement;

        private AStarHeuristic heuristic;

        private boolean pathFinderVisited[][];

        public Pathfinding(Map tbMap, int maxSearchDistance, boolean allowDiagMovement) {
            this(map, maxSearchDistance, allowDiagMovement, new ClosestHeuristic());
        }

        public Pathfinding(Map tbMap, int maxDistance, boolean allowDiagMovement, AStarHeuristic heuristic){
            this.heuristic = heuristic;
            this.tbMap = tbMap;
            this.maxDistance = maxDistance;
            this.allowDiagMovement = allowDiagMovement;


            nodes = new Node[tbMap.NUM_ROWS][tbMap.NUM_COLS];
            for (int i = 0; i < tbMap.NUM_ROWS; i++){
                for (int j = 0; j < tbMap.NUM_COLS; j++){
                    nodes[i][j] = new Node(i,j);
                }
            }

            this.pathFinderVisited = new boolean[tbMap.NUM_ROWS][tbMap.NUM_COLS];
            for (int i = 0; i < tbMap.NUM_ROWS; i++){
                for (int j = 0; j < tbMap.NUM_COLS; j++){
                    pathFinderVisited[i][j] = false;
                }
            }
        }

        @SuppressWarnings("unchecked")
        public Path findPath(int sx, int sy, int tx, int ty){
            nodes[sx][sy].cost = 0;
            nodes[sx][sy].depth = 0;

            closed.clear();
            open.clear();
            open.add(nodes[sx][sy]);

            nodes[tx][ty].parent = null;

            int maxDepth = 0;
            while ((maxDepth < maxDistance) && open.size() != 0){
                Node current = getFirstInOpen();
                if (current == nodes[tx][ty]){
                    break;
                }

                removeFromOpen(current);
                addToClosed(current);

                for (int x = -1; x < 2; x++) {
                    for (int y = -1; y < 2; y++) {
                        // not a neighbour, it's the current tile
                        if ((x == 0) && (y == 0)) {
                            continue;
                        }

                        // if we're not allowing diagonal movement then only one of x or y can be set
                        if (!allowDiagMovement) {
                            if ((x != 0) && (y != 0)) {
                                continue;
                            }
                        }

                        //Determine the location of the neighbour and evaluate it
                        int xp = x + current.x;
                        int yp = y + current.y;

                        if (isValidLocation(sx,sy,xp,yp)) {
                            // the cost to get to this node is cost the current plus the movement
                            // cost to reach this node. Note that the heuristic value is only used
                            // in the sorted open list

                            float nextStepCost = current.cost + getMovementCost(current.x, current.y, xp, yp);
                            Node neighbour = nodes[xp][yp];
                            this.pathFinderVisited[xp][yp] = true ;
                            if (nextStepCost < 1000){

                                if (nextStepCost < neighbour.cost) {
                                    if (inOpenList(neighbour)) {
                                        removeFromOpen(neighbour);
                                    }
                                    if (inClosedList(neighbour)) {
                                        removeFromClosed(neighbour);
                                    }
                                }
                            }

                            else {
                                return null;
                            }

                            //Check to see if the neighbour has been processed or discarded before now
                            if (!inOpenList(neighbour) && !(inClosedList(neighbour))) {
                                neighbour.cost = nextStepCost;
                                neighbour.heuristic = getHeuristicCost(xp, yp, tx, ty);
                                maxDepth = Math.max(maxDepth, neighbour.setParent(current));
                                addToOpen(neighbour);
                            }
                        }
                    }
                }
            }

            //We've run out of places to search therefore there must be no path
            if (nodes[tx][ty].parent == null) {
                return null;
            }

            //If we get to this point then we have a valid path and by using the parent node of each node we can work back to the original node
            Path path = new Path();
            Node target = nodes[tx][ty];
            while (target != nodes[sx][sy]) {
                path.prependStep(target.x, target.y);
                target = target.parent;
            }

            //Return the valid path found
            return path;
        }

        private float getMovementCost(int sx, int sy, int tx, int ty){
            if (tbMap.isDoor(new Coordinates(sx, sy), new Coordinates(tx,ty)) ||
                    tbMap.isCorridor(new Coordinates(tx,ty))){
                return 1;
            } else{
                return 10;
            }
        }

        private float getHeuristicCost(int x, int y, int tx, int ty){
            return heuristic.getCost(x,y,tx,ty);
        }

        private boolean isValidLocation(int sx, int sy, int xp,int yp){
            return tbMap.isDoor(new Coordinates(sx, sy), new Coordinates(xp, yp)) &&
                    tbMap.isCorridor(new Coordinates(sx, sy));
        }

        private Node getFirstInOpen(){
            return (Node) open.peek();
        }

        @SuppressWarnings("unchecked")
        private void addToOpen(Node node){
            open.add(node);
        }

        private boolean inOpenList(Node node){
            return open.contains(node);
        }

        private void removeFromOpen(Node node){
            open.remove(node);
        }

        @SuppressWarnings("unchecked")
        private void addToClosed(Node node){
            closed.add(node);
        }

        private boolean inClosedList(Node node){
            return closed.contains(node);
        }

        private void removeFromClosed(Node node){
            closed.remove(node);
        }

        private class Node implements Comparable{
            private int x, y;
            private int depth;
            private float cost;
            private float heuristic;
            private Node parent;

            public Node(int x, int y){
                this.x = x;
                this.y = y;
            }

            public int setParent(Node parent){
                depth = parent.depth + 1;
                this.parent = parent;
                return depth;
            }

            @Override
            public int compareTo(Object other){
                Node o = (Node) other;
                float f = heuristic + cost;
                float of = o.heuristic + o.cost;

                if (f < of){
                    return -1;
                } else if (f > of){
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    private class ClosestHeuristic implements AStarHeuristic{
        @Override
        public float getCost(int x, int y, int tx, int ty){
            float dx = tx - x;
            float dy = ty - y;

            float result = (float) (Math.sqrt((dx*dx)+(dy*dy)));

            return result;
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
