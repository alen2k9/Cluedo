package com.team11.cluedo.controls;

import com.team11.cluedo.suspects.SuspectData;

import java.util.ArrayList;

public class InputData {

    private ArrayList<String> commandData = new ArrayList<>();

    public InputData(){
        addElement("roll", this.commandData);
        addElement("quit", this.commandData);
        addElement("passage", this.commandData);
        addElement("done", this.commandData);
        addElement("help", this.commandData);
        addElement("exit", this.commandData);
        addElement("weapons", this.commandData);
        addElement("move", this.commandData);

        addElement("white", this.commandData);
        addElement("plum", this.commandData);
        addElement("green", this.commandData);
        addElement("peacock", this.commandData);
        addElement("scarlett", this.commandData);
        addElement("mustard", this.commandData);

    }

    public void addElement(String s, ArrayList<String> list){
        list.add(s);
    }

    public ArrayList<String> getCommandData(){
        return this.commandData;
    }

}
