import Board.Board;
import Screen.Screen;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException{
        //Screen gameScreen = new Screen();

        /**
         * Test Code used to test the Drawing of the board
         * NOT ESSENTIAL
         */
        {
            Board mainBoard = new Board();
            mainBoard.parseBoardFile();
            JFrame frame = new JFrame("Cluedo");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            Board board = new Board();
            frame.add(board);
            frame.setSize(780, 840);
            frame.setResizable(false);

            frame.setVisible(true);
        }
    }
}
