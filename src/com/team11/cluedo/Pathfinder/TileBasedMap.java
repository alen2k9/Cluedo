/*
    Interface for the map being used for the pathfinder.
    In terms of cluedo it is the game board

    Authors Team11:    Jack Geraghty - 16384181
                       Conor Beenham - 16350851
                       Alen Thomas   - 16333003
 */

package com.team11.cluedo.Pathfinder;

public interface TileBasedMap {
    int getWidthInTiles();

    int getHeightInTiles();

    void pathFinderVisited(int x, int y);

    boolean blocked(Mover mover, int x, int y);

    float getCost(Mover mover, int sx, int sy, int tx, int ty);
}
