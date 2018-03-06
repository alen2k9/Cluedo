/*
    Main class of the AStar pathfinding algorithm implementation

    Authors Team11:    Jack Geraghty - 16384181
                       Conor Beenham - 16350851
                       Alen Thomas   - 16333003
*/

package com.team11.cluedo.pathfinder;

import java.util.ArrayList;
import java.util.Collections;

public class AStarFinder implements PathFinder {
    // The set of nodes that have been searched through
    private ArrayList closed = new ArrayList();

    // The set of nodes that we do not yet consider fully searched
    private SortedList open = new SortedList();

    //The Board being searched
    private TileBasedMap map;

    // The maximum depth of search we're willing to accept before giving up
    private int maxSearchDistance;

    // The complete set of nodes across the map
    private Node[][] nodes;

    // True if we allow diaganol movement
    private boolean allowDiagMovement;

    // The heuristic we're applying to determine which nodes to search first
    private AStarHeuristic heuristic;

    public AStarFinder(TileBasedMap map, int maxSearchDistance, boolean allowDiagMovement) {
        this(map, maxSearchDistance, allowDiagMovement, new ClosestHeuristic());
    }

    public AStarFinder(TileBasedMap map, int maxSearchDistance,
                           boolean allowDiagMovement, AStarHeuristic heuristic) {
        this.heuristic = heuristic;
        this.map = map;
        this.maxSearchDistance = maxSearchDistance;
        this.allowDiagMovement = allowDiagMovement;

        nodes = new Node[map.getWidthInTiles()][map.getHeightInTiles()];
        for (int x = 0; x < map.getWidthInTiles(); x++) {
            for (int y = 0; y < map.getHeightInTiles(); y++) {
                nodes[x][y] = new Node(x,y);
            }
        }
    }

    public Path findPath(Mover mover, int sx, int sy, int tx, int ty) {
        // Check first to see if the destination is blocked
        if (map.blocked(mover, tx, ty)) {
            return null;
        }

        nodes[sx][sy].cost = 0;
        nodes[sx][sy].depth = 0;
        closed.clear();
        open.clear();
        open.add(nodes[sx][sy]);

        nodes[tx][ty].parent = null;

        // while we haven'n't exceeded our max search depth
        int maxDepth = 0;
        while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {
            // pull out the first node in our open list, this is determined to be the most likely to be the next step based on our heuristic

            Node current = getFirstInOpen();
            if (current == nodes[tx][ty]) {
                break;
            }

            removeFromOpen(current);
            addToClosed(current);

            // search through all the neighbours of the current node evaluating them as next steps
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

                    if (isValidLocation(mover,sx,sy,xp,yp)) {
                        // the cost to get to this node is cost the current plus the movement
                        // cost to reach this node. Note that the heuristic value is only used
                        // in the sorted open list

                        float nextStepCost = current.cost + getMovementCost(mover, current.x, current.y, xp, yp);
                        Node neighbour = nodes[xp][yp];
                        map.pathFinderVisited(xp, yp);

                        if (nextStepCost < neighbour.cost) {
                            if (inOpenList(neighbour)) {
                                removeFromOpen(neighbour);
                            }
                            if (inClosedList(neighbour)) {
                                removeFromClosed(neighbour);
                            }
                        }


                        //Check to see if the neighbour has been processed or discarded before now
                        if (!inOpenList(neighbour) && !(inClosedList(neighbour))) {
                            neighbour.cost = nextStepCost;
                            neighbour.heuristic = getHeuristicCost(mover, xp, yp, tx, ty);
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



    private Node getFirstInOpen() {
        return (Node) open.first();
    }


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

    //Check to see if the location we are trying to move to is valid or not
    private boolean isValidLocation(Mover mover, int sx, int sy, int x, int y) {
        boolean invalid = (x < 0) || (y < 0) || (x >= map.getWidthInTiles()) || (y >= map.getHeightInTiles());

        if ((!invalid) && ((sx != x) || (sy != y))) {
            invalid = map.blocked(mover, x, y);
        }

        return !invalid;
    }

    private float getMovementCost(Mover mover, int sx, int sy, int tx, int ty) {
        return map.getCost(mover, sx, sy, tx, ty);
    }

    private float getHeuristicCost(Mover mover, int x, int y, int tx, int ty) {
        return heuristic.getCost(map, mover, x, y, tx, ty);
    }

    private class SortedList {
        // The list of elements
        private ArrayList list = new ArrayList();

        public Object first() {
            return list.get(0);
        }

        public void clear() {
            list.clear();
        }

        @SuppressWarnings("unchecked")
        public void add(Object o) {
            list.add(o);
            Collections.sort(list);
        }

        public void remove(Object o) {
            list.remove(o);
        }

        public int size() {
            return list.size();
        }

        public boolean contains(Object o) {
            return list.contains(o);
        }
    }

    //Class to represent a node on the map
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

        public int setParent(Node parent) {
            depth = parent.depth + 1;
            this.parent = parent;

            return depth;
        }

        public int compareTo(Object other) {
            Node o = (Node) other;

            float f = heuristic + cost;
            float of = o.heuristic + o.cost;

            if (f < of) {
                return -1;
            } else if (f > of) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}