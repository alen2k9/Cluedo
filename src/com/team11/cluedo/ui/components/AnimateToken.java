package com.team11.cluedo.ui.components;

import com.sun.org.apache.regexp.internal.RE;
import com.team11.cluedo.board.Board;
import com.team11.cluedo.suspects.Direction;
import com.team11.cluedo.suspects.Suspect;
import com.team11.cluedo.ui.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AnimateToken {
    private GameScreen gameScreen;
    private Suspect token;
    private int drawX, drawY, resolutionScalar;
    private ArrayList<Direction> moves;

    public AnimateToken(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.resolutionScalar = (int)(gameScreen.getResolution().getScalePercentage() * Board.TILE_SIZE);
    }

    public void setToken(Suspect token) {
        this.token = token;
    }

    public void setMoves(ArrayList<Direction> moves){
        this.moves = moves;
    }

    public void moveIt()
    {
        gameScreen.getBoardPanel().setPaintParam(1);
        gameScreen.getGameSuspects().setDraw(1);
        while (!moves.isEmpty())
        {
            Direction direction = moves.remove(0);

            drawX = (int)(token.getBoardLocation().getX()*resolutionScalar);
            drawY = (int)(token.getBoardLocation().getY()*resolutionScalar);

            token.move(gameScreen.getGameBoard(), direction);

            while (drawX != (int)(token.getBoardLocation().getX()*resolutionScalar) || drawY != (int)(token.getBoardLocation().getY()*resolutionScalar)){
                int distance = 3;
                if (drawX < token.getBoardLocation().getX()*resolutionScalar) {
                    gameScreen.getBoardPanel().setDrawBounds(drawX-6, drawY, 6, resolutionScalar*2);
                    drawX += distance;
                }
                if (drawX > token.getBoardLocation().getX()*resolutionScalar) {
                    gameScreen.getBoardPanel().setDrawBounds(drawX+resolutionScalar, drawY, 6, resolutionScalar*2);
                    drawX -= distance;
                }
                if (drawY < token.getBoardLocation().getY()*resolutionScalar) {
                    gameScreen.getBoardPanel().setDrawBounds(drawX, (drawY-6), resolutionScalar*2, 6);
                    drawY += distance;
                }
                if (drawY > token.getBoardLocation().getY()*resolutionScalar) {
                    gameScreen.getBoardPanel().setDrawBounds(drawX, (drawY+resolutionScalar), resolutionScalar*2, 6);
                    drawY -= distance;
                }

                token.setDrawX(drawX);
                token.setDrawY(drawY);

                try {
                    Thread.sleep(2);
                    gameScreen.getGameSuspects().setDrawBounds(drawX, drawY, resolutionScalar, resolutionScalar);
                    gameScreen.getGameSuspects().paintComponent(gameScreen.getGameSuspects().getGraphics());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        gameScreen.getBoardPanel().setPaintParam(0);
        gameScreen.getGameSuspects().setDraw(0);
    }
}
