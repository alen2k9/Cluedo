package com.team11.cluedo.ui.components;

import com.team11.cluedo.board.Board;
import com.team11.cluedo.components.TokenComponent;
import com.team11.cluedo.players.Player;
import com.team11.cluedo.suspects.Direction;
import com.team11.cluedo.suspects.Suspect;
import com.team11.cluedo.ui.GameScreen;
import com.team11.cluedo.ui.panel.BackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AnimateToken {
    private GameScreen gameScreen;
    private Suspect token;
    private int drawX, drawY, resolutionScalar;
    private Point destination, nextPoint, currentPoint;
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
        while (!moves.isEmpty())
        {
            currentPoint = token.getLocation();
            Direction direction = moves.remove(0);
            drawX = (int)(token.getLocation().getX()*resolutionScalar);
            drawY = (int)(token.getLocation().getY()*resolutionScalar);
            switch (direction) {
                case NORTH:
                    nextPoint = new Point((int)token.getLocation().getX(), (int)token.getLocation().getY()-1);
                    break;
                case EAST:
                    nextPoint = new Point((int)token.getLocation().getX()+1, (int)token.getLocation().getY());
                    break;
                case SOUTH:
                    nextPoint = new Point((int)token.getLocation().getX(), (int)token.getLocation().getY()+1);
                    break;
                case WEST:
                    nextPoint = new Point((int)token.getLocation().getX()-1, (int)token.getLocation().getY());
                    break;
            }
            token.move(gameScreen.getGameBoard(), direction);
            /*
            int minDrawX = (int)(currentPoint.getX()*resolutionScalar), minDrawY = (int)(currentPoint.getY()*resolutionScalar);
            int maxDrawX = (int)(nextPoint.getX()*resolutionScalar), maxDrawY = (int)(nextPoint.getY()*resolutionScalar);
            //System.out.println("X: " + drawX + " Y: " + drawY);
            //System.out.println("X: " + nextPoint.getX()*resolutionScalar + " Y: " + nextPoint.getY()*resolutionScalar);
            */
            while (drawX != (int)(token.getLocation().getX()*resolutionScalar) || drawY != (int)(token.getLocation().getY()*resolutionScalar)){
                if (drawX < token.getLocation().getX()*resolutionScalar)
                    drawX += 3;
                if (drawX > token.getLocation().getX()*resolutionScalar)
                    drawX -= 3;
                if (drawY < token.getLocation().getY()*resolutionScalar)
                    drawY += 3;
                if (drawY > token.getLocation().getY()*resolutionScalar)
                    drawY -= 3;
                //System.out.println("X: " + drawX + " Y: " + drawY);
                token.setDrawX(drawX);
                token.setDrawY(drawY);
                try {
                    Thread.sleep(4);
                    gameScreen.getBoardPanel().paintComponent(gameScreen.getBoardPanel().getGraphics());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            SwingUtilities.invokeLater(() -> {
            });
        }
        gameScreen.getBoardPanel().setPaintParam(0);
    }
}
