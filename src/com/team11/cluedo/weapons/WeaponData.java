package com.team11.cluedo.weapons;

import com.team11.cluedo.assets.Assets;

import java.awt.*;
import java.util.HashMap;

public class WeaponData {
    private HashMap<Integer, String> weaponName = new HashMap<>();
    private HashMap<Integer, Image> weaponToken = new HashMap<>();

    public WeaponData() {
        setWeaponName();
        setWeaponToken();
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
        Assets gameAssets = new Assets();
        weaponToken.put(0, gameAssets.getHatchetToken());
        weaponToken.put(1, gameAssets.getDaggerToken());
        weaponToken.put(2, gameAssets.getPoisonToken());
        weaponToken.put(3, gameAssets.getRevolverToken());
        weaponToken.put(4, gameAssets.getRopeToken());
        weaponToken.put(5, gameAssets.getWrenchToken());
    }

    public String getWeaponName(int index) {
        return weaponName.getOrDefault(index, null);
    }

    public Image getWeaponToken(int index) {
        return weaponToken.getOrDefault(index, null);
    }
}
