package com.team11.cluedo.recursiveMove;

import com.team11.cluedo.board.Board;
import com.team11.cluedo.board.room.TileType;
import com.team11.cluedo.ui.components.OverlayTile;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class RecursiveMove {

    private HashSet<Point> moveList = new HashSet<>();
    private Board gameBoard;
    private int counter = 0;
    private boolean visited[][] = new boolean[27][26];

    public RecursiveMove(Board gameBoard){
        this.gameBoard = gameBoard;
    }

    public HashSet<Point> getMoveList() {
        return this.moveList;
    }

    public void clearVisited(){
        for (int i = 0; i < 27; i++){
            for (int j = 0; j < 26; j++){
                visited[i][j] = false;
            }
        }
    }

    public ArrayList<OverlayTile> pointToOverlayTiles(){
        ArrayList<OverlayTile> moves = new ArrayList<>();
        for (Point point : moveList){
            moves.add(new OverlayTile((int)point.getY(), (int)point.getX()));
        }

        return moves;
    }

    public void clearMoveList(){
        moveList.clear();
    }

    public void printMap(){
        System.out.println("Map: " + moveList);
    }

    public void findMoves(int numMoves, Point currentPoint, Stack<Point> moveStack){

        //Right
        if (this.gameBoard.getBoardPos( (int)(currentPoint.getX() + 1), (int)(currentPoint.getY())).isTraversable() && !(this.gameBoard.getBoardPos( (int)(currentPoint.getX() + 1), (int)(currentPoint.getY())).isOccupied())
               /* && !this.visited[(int)(currentPoint.getX() + 1)][(int)(currentPoint.getY())]*/){

            if (this.gameBoard.getBoardPos((int)(currentPoint.getX() + 1), (int)(currentPoint.getY())).getType() == TileType.DOOR && this.gameBoard.getBoardPos((int)(currentPoint.getX()), (int)(currentPoint.getY())).getType() == TileType.DOORMAT){
                moveList.add(new Point((int)(currentPoint.getX() + 1), (int)(currentPoint.getY())));
                //this.visited[(int)(currentPoint.getX() + 1)][(int)(currentPoint.getY())] = true;
                System.out.println("Added : " + (int)(currentPoint.getX() + 1) + " " +  (int)(currentPoint.getY()));
            }


            else if (this.gameBoard.getBoardPos((int)(currentPoint.getX() + 1), (int)(currentPoint.getY())).getType() == TileType.DOOR && this.gameBoard.getBoardPos((int)(currentPoint.getX()), (int)(currentPoint.getY())).getType() != TileType.DOORMAT){
                System.out.println("Cannot enter a door unless you come from a doormat");
            }
            else{
                moveStack.push(new Point((int)(currentPoint.getX() + 1), (int)(currentPoint.getY())));
                //this.visited[(int)(currentPoint.getX() + 1)][(int)(currentPoint.getY())] = true;
                if (numMoves > 0){
                    System.out.println(numMoves);
                    findMoves(numMoves-1, moveStack.pop(), moveStack);
                    System.out.println("Added : " + (int)(currentPoint.getX() + 1) + " " +  (int)(currentPoint.getY()));
                }
            }

        }

        //Down
        if (this.gameBoard.getBoardPos( (int)(currentPoint.getX()), (int)(currentPoint.getY() + 1)).isTraversable() && !(this.gameBoard.getBoardPos( (int)(currentPoint.getX()), (int)(currentPoint.getY() + 1)).isOccupied())
                /*&& !this.visited[(int)(currentPoint.getX())][(int)(currentPoint.getY() + 1)]*/){
            if (this.gameBoard.getBoardPos((int)(currentPoint.getX()), (int)(currentPoint.getY() + 1)).getType() == TileType.DOOR && this.gameBoard.getBoardPos((int)(currentPoint.getX()), (int)(currentPoint.getY())).getType() == TileType.DOORMAT){
                moveList.add(new Point((int)(currentPoint.getX()), (int)(currentPoint.getY()) + 1));
                //this.visited[(int)(currentPoint.getX())][(int)(currentPoint.getY() + 1)] = true;
                System.out.println("Added : " + (int)(currentPoint.getX() ) + " " +  (int)(currentPoint.getY()+1));
            }


            else if (this.gameBoard.getBoardPos((int)(currentPoint.getX() ), (int)(currentPoint.getY() + 1)).getType() == TileType.DOOR && this.gameBoard.getBoardPos((int)(currentPoint.getX()), (int)(currentPoint.getY())).getType() != TileType.DOORMAT){
                System.out.println("Cannot enter a door unless you come from a doormat");
            }
            else{
                moveStack.push(new Point((int)(currentPoint.getX()), (int)(currentPoint.getY() + 1)));
                //this.visited[(int)(currentPoint.getX())][(int)(currentPoint.getY() + 1)] = true;
                if (numMoves > 0){
                    System.out.println(numMoves);
                    findMoves(numMoves-1, moveStack.pop(), moveStack);
                    System.out.println("Added : " + (int)(currentPoint.getX()) + " " +  (int)(currentPoint.getY() + 1));
                }
            }
        }
        //Left
        if (this.gameBoard.getBoardPos( (int)(currentPoint.getX() - 1), (int)(currentPoint.getY())).isTraversable() && !(this.gameBoard.getBoardPos( (int)(currentPoint.getX() - 1), (int)(currentPoint.getY())).isOccupied())
                /*&& !this.visited[(int)(currentPoint.getX() - 1)][(int)(currentPoint.getY())]*/){
            if (this.gameBoard.getBoardPos((int)(currentPoint.getX() - 1), (int)(currentPoint.getY())).getType() == TileType.DOOR && this.gameBoard.getBoardPos((int)(currentPoint.getX()), (int)(currentPoint.getY())).getType() == TileType.DOORMAT){
                moveList.add(new Point((int)(currentPoint.getX() - 1), (int)(currentPoint.getY())));
                //this.visited[(int)(currentPoint.getX() - 1)][(int)(currentPoint.getY())] = true;
                System.out.println("Added : " + (int)(currentPoint.getX() - 1) + " " +  (int)(currentPoint.getY()));
            }


            else if (this.gameBoard.getBoardPos((int)(currentPoint.getX() - 1), (int)(currentPoint.getY())).getType() == TileType.DOOR && this.gameBoard.getBoardPos((int)(currentPoint.getX()), (int)(currentPoint.getY())).getType() != TileType.DOORMAT){
                System.out.println("Cannot enter a door unless you come from a doormat");
            }
            else{
                moveStack.push(new Point((int)(currentPoint.getX() - 1), (int)(currentPoint.getY())));
               // this.visited[(int)(currentPoint.getX() - 1)][(int)(currentPoint.getY())] = true;
                if (numMoves > 0){
                    System.out.println(numMoves);
                    findMoves(numMoves-1, moveStack.pop(), moveStack);
                    System.out.println("Added : " + (int)(currentPoint.getX() - 1) + " " +  (int)(currentPoint.getY()));
                }
            }
        }
        //Up
        if (this.gameBoard.getBoardPos( (int)(currentPoint.getX()), (int)(currentPoint.getY() - 1)).isTraversable() && !(this.gameBoard.getBoardPos( (int)(currentPoint.getX()), (int)(currentPoint.getY() - 1)).isOccupied())
                /*&& !this.visited[(int)(currentPoint.getX())][(int)(currentPoint.getY() - 1)]*/){
            if (this.gameBoard.getBoardPos((int)(currentPoint.getX()), (int)(currentPoint.getY() - 1)).getType() == TileType.DOOR && this.gameBoard.getBoardPos((int)(currentPoint.getX()), (int)(currentPoint.getY())).getType() == TileType.DOORMAT){
                moveList.add(new Point((int)(currentPoint.getX()), (int)(currentPoint.getY() - 1)));
                //this.visited[(int)(currentPoint.getX())][(int)(currentPoint.getY() - 1)] = true;
                System.out.println("Added : " + (int)(currentPoint.getX() ) + " " +  (int)(currentPoint.getY() - 1));
            }


            else if (this.gameBoard.getBoardPos((int)(currentPoint.getX() ), (int)(currentPoint.getY() - 1)).getType() == TileType.DOOR && this.gameBoard.getBoardPos((int)(currentPoint.getX()), (int)(currentPoint.getY())).getType() != TileType.DOORMAT){
                System.out.println("Cannot enter a door unless you come from a doormat");
            }
            else{
                moveStack.push(new Point((int)(currentPoint.getX()), (int)(currentPoint.getY() - 1)));
                //this.visited[(int)(currentPoint.getX())][(int)(currentPoint.getY() - 1)] = true;
                if (numMoves > 0){
                    System.out.println(numMoves);
                    findMoves(numMoves-1, moveStack.pop(), moveStack);
                    System.out.println("Added : " + (int)(currentPoint.getX()) + " " +  (int)(currentPoint.getY() - 1));
                }
            }
        }

        moveList.add(currentPoint);


    }
}
