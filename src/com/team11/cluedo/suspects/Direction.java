/*
 * Main Author : Jack Geraghty
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */
package com.team11.cluedo.suspects;

public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    @Override
    public String toString() {
        switch (this){
            case NORTH:
                return "North";
            case SOUTH:
                return "South";
            case EAST:
                return "East";
            case WEST:
                return "East";
            default:
                return "Unknown";
        }
    }
}
