import Board.Board;
import Screen.GameScreen;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException{
        /**
         * Test Code used to test the Drawing of the board
         * NOT ESSENTIAL
         */
        {

/*
            Board mainBoard = new Board();
            mainBoard.parseBoardFile();
            JFrame frame = new JFrame("Test");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            Board board = new Board();
            frame.add(board);
            frame.setSize(1000, 1000);
            frame.setResizable(false);

            frame.setVisible(true);
            */

            GameScreen gameScreen = new GameScreen();
        }
    }
}
