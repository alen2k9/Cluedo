package com.team11.cluedo.cards;

public class MurderEnvelope {
    private SuspectCard suspectCard;
    private RoomCard roomCard;
    private WeaponCard weaponCard;

    public MurderEnvelope(SuspectCard suspectCard, RoomCard roomCard, WeaponCard weaponCard) {
        this.suspectCard = suspectCard;
        this.roomCard = roomCard;
        this.weaponCard = weaponCard;
    }

    public SuspectCard getSuspectCard() {
        return suspectCard;
    }

    public RoomCard getRoomCard() {
        return roomCard;
    }

    public WeaponCard getWeaponCard() {
        return weaponCard;
    }
}
