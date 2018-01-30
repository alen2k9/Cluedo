package Board;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class Board extends JPanel{

    public final int BOARD_WIDTH = 27;
    public final int BOARD_HEIGHT = 26;
    public BoardPos[][] board;

   //Playable board is 25 rows and 24 columns

    public Board() throws IOException{
        try{
            board = parseBoardFile();
        }
        catch(IOException ex){
            ex.printStackTrace(System.out);
        }
    }

    /**
     * Takes in the board information from a .txt file and parses it into an array consisting of BoardPos objects
     * @return: Returns an array consisting of all the board positions
     * @throws IOException
     */
    public BoardPos[][] parseBoardFile() throws IOException{

        BoardPos[][] tmpBoard = new BoardPos[BOARD_WIDTH][BOARD_HEIGHT];
        URL url = getClass().getResource("BoardInfo.txt");
        File boardFile = new File(url.getPath());

        BufferedReader br = new BufferedReader(new FileReader(boardFile));
        String cLine;

        for (int i = 0; (cLine = br.readLine()) != null; i++){
            String line[] = cLine.split(" ");

            for (int j = 0; j < line.length; j++){

                /**
                 * Create a blank Board Position to work with
                 */
                BoardPos tmp = null;

                /**
                 * Blank Space Tile
                 */
                if (line[j].matches("#")){
                    tmp = createNonTraversal(i, j, TileType.BLANK);
                    //System.out.println("X: " + i + ", Y: " + j + ", TileType: " + TileType.BLANK);
                    System.out.print("(" + i + "," + j + ") : " + TileType.BLANK + " ,");
                }

                /**
                 * Spawn Tile
                 */
                else if (line[j].matches("1")){
                    tmp = createNonTraversal(i,j, TileType.SPAWN);
                    System.out.print("(" + i + "," + j + ") : " + TileType.SPAWN + " ,");
                }

                /**
                 * Secrete Passage Tile
                 */
                else if (line[j].matches("2")){
                    tmp = new BoardPos(i, j, false, true,TileType.SECRETPASSAGE, false);
                    System.out.print("(" + i + "," + j + ") : " + TileType.SECRETPASSAGE + " ,");
                }

                /**
                 * Kitchen Tile
                 */
                else if (line[j].matches("K")){
                    tmp = createNonTraversal(i,j, TileType.KITCHEN);
                    System.out.print("(" + i + "," + j + ") : " + TileType.KITCHEN + " ,");
                }

                /**
                 * Ballroom Tile
                 */
                else if (line[j].matches("B")){
                    tmp = createNonTraversal(i,j, TileType.BALLROOM);
                    System.out.print("(" + i + "," + j + ") : " + TileType.BALLROOM + " ,");
                }

                /**
                 * Conservatory Tile
                 */
                else if (line[j].matches("C")){
                    tmp = createNonTraversal(i,j, TileType.CONSERVATORY);
                    System.out.print("(" + i + "," + j + ") : " + TileType.CONSERVATORY + " ,");
                }

                /**
                 * Dining Room Tile
                 */
                else if (line[j].matches("I")){
                    tmp = createNonTraversal(i,j, TileType.DININGROOM);
                    System.out.print("(" + i + "," + j + ") : " + TileType.DININGROOM + " ,");
                }

                /**
                 * Cellar Tile
                 */
                else if (line[j].matches("T")){
                    tmp = createNonTraversal(i,j, TileType.CELLAR);
                    System.out.print("(" + i + "," + j + ") : " + TileType.CELLAR + " ,");
                }

                /**
                 * Billiard Room
                 */
                else if (line[j].matches("R")){
                    tmp = createNonTraversal(i,j, TileType.BILLIARDROOM);
                    System.out.print("(" + i + "," + j + ") : " + TileType.BILLIARDROOM + " ,");
                }

                /**
                 * Library Tile
                 */
                else if (line[j].matches("U")){
                    tmp = createNonTraversal(i,j, TileType.LIBRARY);
                    System.out.print("(" + i + "," + j + ") : " + TileType.LIBRARY + " ,");
                }

                /**
                 * Hall Tile
                 */
                else if (line[j].matches("H")){
                    tmp = createNonTraversal(i,j, TileType.HALL);
                    System.out.print("(" + i + "," + j + ") : " + TileType.HALL + " ,");
                }

                /**
                 * Study Tile
                 */
                else if (line[j].matches("S")){
                    tmp = createNonTraversal(i,j, TileType.STUDY);
                    System.out.print("(" + i + "," + j + ") : " + TileType.STUDY + " ,");
                }

                /**
                 *  Lounge Tile
                 */
                else if (line[j].matches("L")){
                    tmp = createNonTraversal(i,j, TileType.LOUNGE);
                    System.out.print("(" + i + "," + j + ") : " + TileType.LOUNGE + " ,");
                }

                /**
                 * Door Tile
                 */
                else if (line[j].matches("D")){
                    tmp = createTraversal(i,j, TileType.DOOR);
                    System.out.print("(" + i + "," + j + ") : " + TileType.DOOR + " ,");
                }

                else if (line[j].matches("-")){
                    tmp = createTraversal(i, j, TileType.HALLWAY);
                    System.out.print("(" + i + "," + j + ") : " + TileType.HALLWAY + " ,");
                }
                else{
                    System.out.println("Unknown Tile Type, Please Check BoardInfo.txt");
                }
                /**
                 * Assign the value created to the current x,y position on the board
                 */
                tmpBoard[i][j] = tmp;

            }
            System.out.println();
        }

        return tmpBoard;
    }

    /**
     * Method used to create a non traversable tile such as a room tile or blank tile
     * @param a: Signifies the x coordinate
     * @param b: Signifies the y coordinate
     * @param type: Signifies the type of the tile, enum from TileType.java
     * @return : Returns the boardPos created as a result of the above information
     */
    public BoardPos createNonTraversal(int a, int b, TileType type){
        return new BoardPos(a, b, false, false, type, false);
    }
    /**
     * Method used to create a traversable tile such as a hall tile
     * @param a: Signifies the x coordinate
     * @param b: Signifies the y coordinate
     * @param type: Signifies the type of the tile, enum from TileType.java
     * @return : Returns the boardPos created as a result of the above information
     */
    public BoardPos createTraversal(int a, int b, TileType type){
        return new BoardPos(a, b, true,false, type, false);
    }

    /**
     * Method which handles the painting of the Board on a panel / in a frame
     */
    public void paintComponent(Graphics g){
        int top = 0, left = 0;

        /**
         * Loop through and paint every tile in the board
         */
        for (int i = 0; i < BOARD_WIDTH; i++){
            for (int j = 0; j < BOARD_HEIGHT; j++){
                board[i][j].draw(g, left, top);
                left += 40;
            }
            left = 0;
            top += 40;
        }
    }
}
