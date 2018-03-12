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


    private int drawX, drawY, drawW, drawH, draw;

    public Suspects(Resolution resolution) {
        setLayout(null);
        this.setupSuspects(resolution);
    }

    private void setupSuspects(Resolution resolution) {
        SuspectData suspectData = new SuspectData();
        for(int i = 0 ; i < numSuspects ; i++) {
            Suspect suspect = (new Suspect(i, suspectData.getSuspectName(i),
                    suspectData.getSuspectSpawn(i), suspectData.getSuspectToken(i), resolution));
            suspects.add(suspect);

            super.add(suspect, i);
            suspect.setLocation(suspect.getLocation());
            suspect.setSize((int)(resolution.getScalePercentage()*Board.TILE_SIZE),
                    (int)(resolution.getScalePercentage()*Board.TILE_SIZE));
        }
    }

    public void setSpawnsOccupied(Board gameBoard){
        for(Suspect suspect : suspects) {
            gameBoard.getBoardPos(suspect.getBoardY(), suspect.getBoardX()).setOccupied(true);
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
    public Iterator<Suspect> iterator() {
        iterator = suspects.iterator();
        return iterator;
    }


    public void setDrawBounds(int x, int y, int w, int h) {
        drawX = x;
        drawY = y;
        drawW = w;
        drawH = h;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }


    @Override
    public void paintComponent(Graphics gr)
    {
        if (draw == 1) {
            gr.setClip(drawX, drawY, drawW, drawH);
            gr.setColor(super.getBackground());
            super.paintComponents(gr);
        } else {
            super.paintComponents(gr);
        }

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