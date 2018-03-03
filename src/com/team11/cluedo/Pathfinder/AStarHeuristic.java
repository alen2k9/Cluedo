/*
    Interface for calculating the heuristic cost

    Authors Team11:    Jack Geraghty - 16384181
                       Conor Beenham - 16350851
                       Alen Thomas   - 16333003
*/

package com.team11.cluedo.Pathfinder;

public interface AStarHeuristic {
       float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty);
}
