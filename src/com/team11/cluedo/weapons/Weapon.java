/*
 * Code to handle the weapon objects and tokens
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */
package com.team11.cluedo.weapons;

import com.team11.cluedo.components.TokenComponent;
import com.team11.cluedo.ui.Resolution;

import java.awt.*;

public class Weapon extends TokenComponent {
    public Weapon(int weaponID, String name, Point tokenLocation,Image tokenImage, Resolution resolution){
        super(weaponID, name, tokenLocation, tokenImage, resolution );
    }

    public int getWeaponID(){
        return tokenID;
    }
}

