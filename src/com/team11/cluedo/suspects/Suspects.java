/*
 * Code to handle the suspects in the game.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */


package com.team11.cluedo.suspects;

import com.team11.cluedo.ui.Resolution;
import com.team11.cluedo.board.Board;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;

public class Suspects extends JComponent implements Iterable<Suspect>, Iterator<Suspect>{
    private final int numSuspects = 6;
    private Iterator<Suspect> iterator;
    private final HashSet<Suspect> suspects = new HashSet<>();

    public Suspects(Resolution resolution) {
        this.setupSuspects(resolution);
    }

    public void setupSuspects(Resolution resolution) {
        SuspectData suspectData = new SuspectData();
        for(int i = 0 ; i < 6 ; i++) {
            suspects.add(new Suspect(i, suspectData.getSuspectName(i),
                    suspectData.getSuspectSpawn(i), suspectData.getSuspectToken(i), resolution));
        }
    }

    public void setSpawnsOccupied(Board gameBoard){
        for(Suspect suspect : suspects) {
            gameBoard.getBoardPos(suspect.getY(), suspect.getX()).setOccupied(true);
        }
    }

    public int getNumSuspects() {
        return numSuspects;
    }

    public Suspect getSuspect(int index) {
        for(Suspect suspect : suspects) {
            if (suspect.getSuspectID() == index) {
                return suspect;
            }
        }
        return null;
    }

    @Override
    public void paintComponent(Graphics g){
        for (Suspect suspect : suspects) {
            suspect.draw(g);
        }
    }

    @Override
    public Iterator<Suspect> iterator() {
        iterator = suspects.iterator();
        return iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Suspect next() {
        return iterator.next();
    }
}