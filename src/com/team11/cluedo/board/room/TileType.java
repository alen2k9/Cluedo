/*
 * Enum for types of tiles
 * Main Author : Jack Geraghty
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.board.room;

public enum TileType {
    SPAWN, KITCHEN, BALLROOM, HALLWAY,LOUNGE, CONSERVATORY, DININGROOM, CELLAR, BILLIARDROOM,
    LIBRARY, HALL,STUDY, SECRETPASSAGE, DOOR, BLANK, DOORMAT, PREFER, AVOID, ROOM;

    @Override
    public String toString(){
        switch (this){
            case SPAWN:
                return "Spawn";
            case KITCHEN:
                return "Kitchen";
            case BALLROOM:
                return "Ballroom";
            case HALLWAY:
                return "Hallway";
            case LOUNGE:
                return "Lounge";
            case CONSERVATORY:
                return "Conservatory";
            case DININGROOM:
                return "Dining Room";
            case CELLAR:
                return "Cellar";
            case BILLIARDROOM:
                return "Billiard Room";
            case LIBRARY:
                return "Library";
            case HALL:
                return "Hall";
            case STUDY:
                return "Study";
            case SECRETPASSAGE:
                return "Secret Passageway";
            case BLANK:
                return "Blank";
            case DOOR:
                return "Door";
            case DOORMAT:
                return  "Doormat";
            case PREFER:
                return "Preffer";
            case AVOID:
                return "Avoid";
            default:
                return "";
        }
    }
}
