/*
    Interface for the pathfinder

    Authors Team11:    Jack Geraghty - 16384181
                       Conor Beenham - 16350851
                       Alen Thomas   - 16333003
*/

package com.team11.cluedo.pathfinder;

public interface PathFinder {
    Path findPath(Mover mover, int sx, int sy, int tx, int ty);
}
