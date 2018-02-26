package com.team11.cluedo.weapons;

import com.team11.cluedo.assets.Assets;

import java.awt.*;
import java.util.HashMap;

public class WeaponData {
    private Assets gameAssets;

    private HashMap<Integer, String> weaponName = new HashMap<>();
    private HashMap<Integer, Image> weaponToken = new HashMap<>();
    private HashMap<Integer, Image> weaponCard = new HashMap<>();
    private HashMap<Integer, Image> selectedWeaponCard = new HashMap<>();

    private final int weaponAmount = 6;

    public WeaponData() {
        this.gameAssets = new Assets();
        setWeaponName();
        setWeaponToken();
        setWeaponCard();
        setSelectedWeaponCard();
    }

    private void setWeaponName() {
        weaponName.put(0, "Hatchet");
        weaponName.put(1, "Dagger");
        weaponName.put(2, "Poison");
        weaponName.put(3, "Revolver");
        weaponName.put(4, "Rope");
        weaponName.put(5, "Wrench");
    }

    private void setWeaponToken() {
        weaponToken.put(0, gameAssets.getHatchetToken());
        weaponToken.put(1, gameAssets.getDaggerToken());
        weaponToken.put(2, gameAssets.getPoisonToken());
        weaponToken.put(3, gameAssets.getRevolverToken());
        weaponToken.put(4, gameAssets.getRopeToken());
        weaponToken.put(5, gameAssets.getWrenchToken());
    }

    private void setWeaponCard() {
        weaponCard.put(0, gameAssets.getHatchetCard());
        weaponCard.put(1, gameAssets.getDaggerCard());
        weaponCard.put(2, gameAssets.getPoisonCard());
        weaponCard.put(3, gameAssets.getRevolverCard());
        weaponCard.put(4, gameAssets.getRopeCard());
        weaponCard.put(5, gameAssets.getWrenchCard());
    }

    private void setSelectedWeaponCard() {
        selectedWeaponCard.put(0, gameAssets.getSelectedHatchetCard());
        selectedWeaponCard.put(1, gameAssets.getSelectedDaggerCard());
        selectedWeaponCard.put(2, gameAssets.getSelectedPoisonCard());
        selectedWeaponCard.put(3, gameAssets.getSelectedRevolverCard());
        selectedWeaponCard.put(4, gameAssets.getSelectedRopeCard());
        selectedWeaponCard.put(5, gameAssets.getSelectedWrenchCard());
    }

    public String getWeaponName(int index) {
        return weaponName.getOrDefault(index, null);
    }

    public Image getWeaponToken(int index) {
        return weaponToken.getOrDefault(index, null);
    }

    public Image getWeaponCard(int index) {
        return weaponCard.getOrDefault(index, null);
    }

    public Image getSelectedWeaponCard(int index) {
        return selectedWeaponCard.getOrDefault(index, null);
    }

    public int getWeaponAmount() {
        return weaponAmount;
    }
}
