package com.team11.cluedo.components;

import com.team11.cluedo.board.RoomData;
import com.team11.cluedo.suspects.SuspectData;
import com.team11.cluedo.weapons.WeaponData;

import java.util.ArrayList;

public class InputData {

    private ArrayList<String> commandData = new ArrayList<>();
    private SuspectData suspectData = new SuspectData();
    private RoomData roomData = new RoomData();
    private WeaponData weaponData = new WeaponData();

    public InputData(){
        addElement("roll", this.commandData);
        addElement("quit", this.commandData);
        addElement("passage", this.commandData);
        addElement("done", this.commandData);
        addElement("help", this.commandData);
        addElement("exit", this.commandData);
        addElement("weapon", this.commandData);
        addElement("move", this.commandData);
        addElement("cheat", this.commandData);

        for (int i = 0; i < suspectData.getSuspectAmount(); i++){
            addElement(suspectData.getSuspectID(i), this.commandData);
        }
        for (int i = 0; i < roomData.getRoomAmount(); i++){
            addElement(roomData.getRoomID(i), this.commandData);
        }

        for (int i = 0; i < weaponData.getWeaponAmount(); i++){
            addElement(weaponData.getWeaponID(i), this.commandData);
        }
    }

    public void addElement(String s, ArrayList<String> list){
        list.add(s);
    }

    public ArrayList<String> getCommandData(){
        return this.commandData;
    }

}
