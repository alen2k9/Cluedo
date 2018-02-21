/**
 * Code to handle the creation of the cluedo board.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.board;

import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Board extends JComponent {

    /**
     * Constants for the board width and height and the size of each tile
     */
    private final int BOARD_WIDTH = 27;
    private final int BOARD_HEIGHT = 26;
    private int tileSize;

    private BoardPos[][] board;

    private Resolution resolution;

    /**
     * Default Constructor
     * @throws IOException : Reads board information from the BoardInfo.txt file
     */
    public Board(Resolution resolution) throws IOException{
        /**
         * Try and read the file and assign the return value to the board variable
         */
        try{
            board = parseBoardFile();
            this.resolution = resolution;
            this.tileSize = (int)(30 * this.resolution.getScalePercentage());
        }
        catch(IOException ex){
            ex.printStackTrace(System.out);
        }
    }

    /**
     * Method to parse information from the boardInfo.txt file and setup each of the board positions based off of that information
     * @return : A matrix array of boardPos objects
     * @throws IOException
     */
    public BoardPos[][] parseBoardFile() throws IOException {
        //Create a boardPos array to assign BoardPos objects to and return
        BoardPos[][] tmpBoard = new BoardPos[BOARD_WIDTH][BOARD_HEIGHT];

        //Find the boardInfo.txt and open it
        InputStream in = getClass().getResourceAsStream("BoardInfo.txt");

        //Open a buffered reader to read each line
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        //Store each line in a string and then split that line into individual characters and create new boardPos objects depending on that character
        String cLine;

        for (int i = 0; (cLine = br.readLine()) != null; i++) {
            String line[] = cLine.split(" ");

            for (int j = 0; j < line.length; j++) {

                /**
                 * Create a blank com.Team11.Cluedo.BoardPosition to work with
                 */
                BoardPos tmp = null;

                /**
                 * Blank Space Tile
                 */
                if (line[j].matches("#")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.BLANK);
                }

                /**
                 * Spawn Tile
                 */
                else if (line[j].matches("1")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.SPAWN);
                }

                /**
                 * Secrete Passage Tile
                 */
                else if (line[j].matches("2")) {
                    tmp = new BoardPos(new Point(i,j), true, false, false, TileType.SECRETPASSAGE, tileSize);
                }

                /**
                 * Kitchen Tile
                 */
                else if (line[j].matches("K")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.KITCHEN);
                }

                /**
                 * Ballroom Tile
                 */
                else if (line[j].matches("B")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.BALLROOM);
                }

                /**
                 * Conservatory Tile
                 */
                else if (line[j].matches("C")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.CONSERVATORY);
                }

                /**
                 * Dining Room Tile
                 */
                else if (line[j].matches("I")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.DININGROOM);
                    //System.out.print("(" + i + "," + j + ") : " + TileType.DININGROOM + " ,");
                }

                /**
                 * Cellar Tile
                 */
                else if (line[j].matches("T")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.CELLAR);
                }

                /**
                 * Billiard Room
                 */
                else if (line[j].matches("R")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.BILLIARDROOM);
                }

                /**
                 * Library Tile
                 */
                else if (line[j].matches("U")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.LIBRARY);
                }

                /**
                 * Hall Tile
                 */
                else if (line[j].matches("H")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.HALL);
                }

                /**
                 * Study Tile
                 */
                else if (line[j].matches("S")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.STUDY);
                }

                /**
                 *  Lounge Tile
                 */
                else if (line[j].matches("L")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.LOUNGE);
                }

                /**
                 * Door Tile
                 */
                else if (line[j].matches("D")) {
                    tmp = createTraversal(new Point(i,j), TileType.DOOR);
                }

                /**
                 * Hallway Tile
                 */
                else if (line[j].matches("-")) {
                    tmp = createTraversal(new Point(i,j), TileType.HALLWAY);
                }

                /**
                 * Error
                 */
                else {
                    System.out.println("Unknown Tile Type, Please Check BoardInfo.txt");
                }
                /**
                 * Assign the value created to the current x,y position on the board
                 */
                tmpBoard[i][j] = tmp;

            }


        }
        /**
         * Once all of the lines have been read in and all of the tiles created return the matrix
         */
        return tmpBoard;

    }

    /**
     * Method used to create a tile that isn't traversable
     * @param p : The x,y location of that tile
     * @param t : The type of tile that it is
     * @return : The boardPos object created
     */
    public BoardPos createNonTraversal(Point p, TileType t){
        return new BoardPos(p, false, false, false, t, tileSize);
    }

    /**
     * Method to create a tile that is traversable
     * @param p : The x,y location of that tile
     * @param t : The type of tile that it is
     * @return : The boardPos object created
     */
    public BoardPos createTraversal(Point p, TileType t){
        return new BoardPos(p, false, true, false, t, tileSize);
    }

    /**
     * Method to handle the painting of the boardPos matrix
     * @param g
     */
    public void paintComponent(Graphics g){
        int top = 0, left = 0;
        //System.out.println("Drawing Board");

        /**
         * Loop through and paint every tile in the board
         */
        for (int i = 0; i < BOARD_WIDTH; i++){
            for (int j = 0; j < BOARD_HEIGHT; j++){
                board[i][j].draw(g, new Point(left, top));
                left += tileSize;
            }
            left = 0;
            top += tileSize;
        }
    }
}
