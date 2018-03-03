/*
 * Code to handle the creation of the cluedo board.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.board;

import com.team11.cluedo.Pathfinder.Mover;
import com.team11.cluedo.Pathfinder.TileBasedMap;
import com.team11.cluedo.suspects.Direction;
import com.team11.cluedo.suspects.PlayerPoints;
import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Board extends JComponent implements TileBasedMap {

    /*
     * Constants for the board width and height and the size of each tile
     */
    private final int BOARD_WIDTH = 27;
    private final int BOARD_HEIGHT = 26;
    private int tileSize;

    private BoardPos[][] board;

    private boolean[][] visited = new boolean[BOARD_WIDTH][BOARD_HEIGHT];

    private ArrayList<Room> rooms = new ArrayList<>();
    private Room kitchen = new Room();
    private Room ballroom = new Room();
    private Room conservatory = new Room();
    private Room diningRoom = new Room();
    private Room billiardRoom = new Room();
    private Room library = new Room();
    private Room lounge = new Room();
    private Room hall = new Room();
    private Room study = new Room();
    private Room cellar = new Room();

    private WeaponPoints weaponPoints = new WeaponPoints();
    private PlayerPoints playerPoints = new PlayerPoints();

    private Resolution resolution;
    /*
     * Default Constructor
     * @throws IOException : Reads board information from the BoardInfo.txt file
     */
    public Board(Resolution resolution) throws IOException{
        /*
         * Try and read the file and assign the return value to the board variable
         */
        try{
            board = parseBoardFile();
            addRooms();
            addRoomSecretPassages();
            addExitPoints();
            addEntryPoints();
            addAllSpawns();
            this.resolution = resolution;
            this.tileSize = (int)(30 * this.resolution.getScalePercentage());
        }
        catch(IOException ex){
            ex.printStackTrace(System.out);
        }
    }

    public BoardPos[][] getGameBoard() {
        return board;
    }

    private void addRooms(){
        rooms.add(kitchen);
        rooms.add(ballroom);
        rooms.add(conservatory);
        rooms.add(diningRoom);
        rooms.add(billiardRoom);
        rooms.add(library);
        rooms.add(lounge);
        rooms.add(hall);
        rooms.add(study);
        rooms.add(cellar);
    }

    public Room getRoom(int i){
        return rooms.get(i);
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /*
         * Method to parse information from the boardInfo.txt file and setup each of the board positions based off of that information
         * @return : A matrix array of boardPos objects
         * @throws IOException
         */
    private BoardPos[][] parseBoardFile() throws IOException {
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

                /*
                 * Create a blank com.Team11.Cluedo.BoardPosition to work with
                 */
                BoardPos tmp = null;

                /*
                 * Blank Space Tile
                 */
                if (line[j].matches("#")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.BLANK);
                }

                /*
                 * Spawn Tile
                 */
                else if (line[j].matches("1")) {
                    tmp = createTraversal(new Point(i,j), TileType.SPAWN);
                }

                /*
                 * secret Passage Tile
                 */
                else if (line[j].matches("2")) {
                    tmp = new BoardPos(new Point(i,j), true, false, false, TileType.SECRETPASSAGE, tileSize);
                }

                /*
                 * Kitchen Tile
                 */
                else if (line[j].matches("K")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.KITCHEN);
                    kitchen.setRoomType(TileType.KITCHEN);
                    kitchen.getRoomPoints().add(new Point(i,j));
                }

                /*
                 * Ballroom Tile
                 */
                else if (line[j].matches("B")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.BALLROOM);
                    ballroom.setRoomType(TileType.BALLROOM);
                    ballroom.getRoomPoints().add(new Point(i,j));
                }

                /*
                 * Conservatory Tile
                 */
                else if (line[j].matches("C")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.CONSERVATORY);
                    conservatory.setRoomType(TileType.CONSERVATORY);
                    conservatory.getRoomPoints().add(new Point(i,j));
                }

                /*
                 * Dining Tile
                 */
                else if (line[j].matches("I")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.DININGROOM);
                    diningRoom.setRoomType(TileType.DININGROOM);
                    diningRoom.getRoomPoints().add(new Point(i,j));
                }

                /*
                 * Cellar Tile
                 */
                else if (line[j].matches("T")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.CELLAR);
                    cellar.setRoomType(TileType.CELLAR);
                    cellar.getRoomPoints().add(new Point(i,j));
                }

                /*
                 * Billiard WeaponPoints
                 */
                else if (line[j].matches("R")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.BILLIARDROOM);
                    billiardRoom.setRoomType(TileType.BILLIARDROOM);
                    billiardRoom.getRoomPoints().add(new Point(i,j));
                }

                /*
                 * Library Tile
                 */
                else if (line[j].matches("U")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.LOUNGE);
                    lounge.setRoomType(TileType.LOUNGE);
                    lounge.getRoomPoints().add(new Point(i,j));
                }

                /*
                 * Hall Tile
                 */
                else if (line[j].matches("H")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.HALL);
                }

                /*
                 * Study Tile
                 */
                else if (line[j].matches("S")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.STUDY);
                    study.setRoomType(TileType.STUDY);
                    study.getRoomPoints().add(new Point(i,j));
                }

                /*
                 *  Lounge Tile
                 */
                else if (line[j].matches("L")) {
                    tmp = createNonTraversal(new Point(i,j), TileType.LIBRARY);
                    library.setRoomType(TileType.LIBRARY);
                    library.getRoomPoints().add(new Point(i,j));
                }

                /*
                 * Door Tile
                 */
                else if (line[j].matches("D")) {
                    tmp = createTraversal(new Point(i,j), TileType.DOOR);
                    //System.out.println("j : " + j + " i : " + i);
                }

                /*
                 * Hallway Tile
                 */
                else if (line[j].matches("-")) {
                    tmp = createTraversal(new Point(i,j), TileType.HALLWAY);
                }

                else if (line[j].matches("M")){
                    tmp = createTraversal(new Point (i,j), TileType.DOORMAT);
                }
                /*
                 * Error
                 */
                else {
                    System.out.println("Unknown Tile Type, Please Check BoardInfo.txt");
                }
                /*
                 * Assign the value created to the current x,y position on the board
                 */
                tmpBoard[i][j] = tmp;

            }
        }

        //////////////////////////////////////////////////////////////////////////
        /*
        Don't change this line line as otherwise the position is set to lounge for some reason annd
        messes up movement
        Will try to figure out why this is happening but for now tis grand
        */
        //Fix [15][20]
        //tmpBoard[15][20] = createTraversal(new Point(15,20), TileType.DOOR);
        //////////////////////////////////////////////////////////////////////////

        /*
         * Once all of the lines have been read in and all of the tiles created return the matrix
         */
        return tmpBoard;

    }

    private void addRoomSecretPassages(){
        //Add kitchen passage ways
        //Kitchen goes to study
        rooms.get(0).setHasSecretPassage(true);
        rooms.get(0).setSecretPassageIn(new Point(6,2));
        rooms.get(0).setSecretPassageOut(new Point(24,22));

        //Add study passage ways
        //Study goes to kitchen
        rooms.get(8).setHasSecretPassage(true);
        rooms.get(8).setSecretPassageOut(new Point(6,2));
        rooms.get(8).setSecretPassageIn(new Point(24,22));

        //Add conservatory passage ways
        //Conservatory goes to lounge
        rooms.get(2).setHasSecretPassage(true);
        rooms.get(2).setSecretPassageIn(new Point(23,6));
        rooms.get(2).setSecretPassageOut(new Point(1,20));

        //Add lounge passage ways
        //Lounge goes to conservatory
        rooms.get(6).setHasSecretPassage(true);
        rooms.get(6).setSecretPassageOut(new Point(23,6));
        rooms.get(6).setSecretPassageIn(new Point(1,20));
    }

    private void addExitPoints(){
        //Kitchen
        rooms.get(0).addExitPoint(new Point(5,8));
        //Ballroom
        rooms.get(1).addExitPoint(new Point(8,6));
        rooms.get(1).addExitPoint(new Point(10,9));
        rooms.get(1).addExitPoint(new Point(15,9));
        rooms.get(1).addExitPoint(new Point(17,6));
        //Conservatory
        rooms.get(2).addExitPoint(new Point(19,6));
        //Dining Room
        rooms.get(3).addExitPoint(new Point(9,13));
        rooms.get(3).addExitPoint(new Point(7,17));
        //Billiard Room
        rooms.get(4).addExitPoint(new Point(18,10));
        rooms.get(4).addExitPoint(new Point(23,14));
        //Library
        rooms.get(5).addExitPoint(new Point(20,14));
        rooms.get(5).addExitPoint(new Point(17,17));
        //Lounge
        rooms.get(6).addExitPoint(new Point(7, 19));
        //Hall
        rooms.get(7).addExitPoint(new Point(12,18));
        rooms.get(7).addExitPoint(new Point(13,18));
        rooms.get(7).addExitPoint(new Point(16,21));
        //Study
        rooms.get(8).addExitPoint(new Point(18,21));
        //Cellar
        rooms.get(9).addExitPoint(new Point(13,18));
    }

    private void addEntryPoints(){
        //Kitchen
        rooms.get(0).addEntryPoint(new Point(7,5));
        //Ballroom
        rooms.get(1).addEntryPoint(new Point(6,9));
        rooms.get(1).addEntryPoint(new Point(8,10));
        rooms.get(1).addEntryPoint(new Point(8,15));
        rooms.get(1).addEntryPoint(new Point(6,16));
        //Conservatory
        rooms.get(2).addEntryPoint(new Point(5,19));
        //Dining Room
        rooms.get(3).addEntryPoint(new Point(13,8));
        rooms.get(3).addEntryPoint(new Point(16,7));
        //Billiard Room
        rooms.get(4).addEntryPoint(new Point(10,19));
        rooms.get(4).addEntryPoint(new Point(13,23));
        //Library
        rooms.get(5).addEntryPoint(new Point(15,20));
        rooms.get(5).addEntryPoint(new Point(17,18));
        //Lounge
        rooms.get(6).addEntryPoint(new Point(20,7));
        //Hall
        rooms.get(7).addEntryPoint(new Point(19,12));
        rooms.get(7).addEntryPoint(new Point(19,13));
        rooms.get(7).addEntryPoint(new Point(21,15));
        //Study
        rooms.get(8).addEntryPoint(new Point(22,18));
        //Cellar
        rooms.get(9).addEntryPoint(new Point(17,13));
    }

    private void addAllSpawns(){
        for (int i = 0; i < 9; i++){
            rooms.get(i).addPositions(rooms.get(i).getWeaponPositions(), weaponPoints.getWeaponSpawnList().get(i));
        }

        for (int i = 0; i < 10; i++){
            rooms.get(i).addPositions(rooms.get(i).getPlayerPositions(), playerPoints.getPlayerSpawnList().get(i));
        }
    }

    public BoardPos getBoardPos(int x, int y){
        return this.board[x][y];
    }
    /*
     * Method used to create a tile that isn't traversable
     * @param p : The x,y location of that tile
     * @param t : The type of tile that it is
     * @return : The boardPos object created
     */
    private BoardPos createNonTraversal(Point p, TileType t){
        return new BoardPos(p, false, false, false, t, tileSize);
    }

    /*
     * Method to create a tile that is traversable
     * @param p : The x,y location of that tile
     * @param t : The type of tile that it is
     * @return : The boardPos object created
     */
    private BoardPos createTraversal(Point p, TileType t){
        return new BoardPos(p, false, true, false, t, tileSize);
    }

    /*
     * Method to handle the painting of the boardPos matrix
     * @param g : The graphic to draw
     */
    public void paintComponent(Graphics g){
        int top = 0, left = 0;
        //System.out.println("Drawing Board");

        /*
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

    private void getNeighbours(){
        //Get the corner tiles neighbours
        //TopLeft
        board[0][0].addNeighbour(board[0][3].getLocation(), Direction.SOUTH);
        board[0][0].addNeighbour(board[3][0].getLocation(), Direction.EAST);

        //TopRight
        board[BOARD_WIDTH-1][0].addNeighbour(board[BOARD_WIDTH-2][0].getLocation(), Direction.WEST);
        board[BOARD_WIDTH-1][0].addNeighbour(board[BOARD_WIDTH-1][1].getLocation(), Direction.SOUTH);

        //BottomLeft
        board[0][BOARD_HEIGHT-1].addNeighbour(board[0][BOARD_HEIGHT-2].getLocation(), Direction.NORTH);
        board[0][BOARD_HEIGHT-1].addNeighbour(board[1][BOARD_HEIGHT-1].getLocation(), Direction.EAST);

        //BottomRight
        board[BOARD_WIDTH-1][BOARD_HEIGHT-1].addNeighbour(board[BOARD_WIDTH-2][BOARD_HEIGHT-1].getLocation(), Direction.WEST);
        board[BOARD_WIDTH-1][BOARD_HEIGHT-1].addNeighbour(board[BOARD_WIDTH-1][BOARD_HEIGHT-2].getLocation(), Direction.NORTH);

        //Get the tiles that only have three neighbours, ie the top row, bottom row, and side columns less the corner pieces
        for (int i = 1; i < BOARD_WIDTH-1; i++){
            //Top row
            board[i][0].addNeighbour(board[i-1][0].getLocation(), Direction.WEST);
            board[i][0].addNeighbour(board[i][1].getLocation(), Direction.SOUTH);
            board[i][0].addNeighbour(board[i+1][0].getLocation(), Direction.EAST);

            //Bottom Row
            board[i][BOARD_HEIGHT-1].addNeighbour(board[i-1][BOARD_HEIGHT-1].getLocation(), Direction.WEST);
            board[i][BOARD_HEIGHT-1].addNeighbour(board[i][BOARD_HEIGHT-2].getLocation(), Direction.NORTH);
            board[i][BOARD_HEIGHT-1].addNeighbour(board[i+1][BOARD_HEIGHT-1].getLocation(), Direction.EAST);
        }
        for (int i = 1; i < BOARD_HEIGHT-1;i++){
            //Left Column
            board[0][i].addNeighbour(board[0][i-1].getLocation(), Direction.NORTH);
            board[0][i].addNeighbour(board[1][i].getLocation(), Direction.EAST);
            board[0][i].addNeighbour(board[0][i+1].getLocation(), Direction.SOUTH);

            //Right Column
            board[BOARD_WIDTH-1][i].addNeighbour(board[BOARD_WIDTH-1][i-1].getLocation(), Direction.NORTH);
            board[BOARD_WIDTH-1][i].addNeighbour(board[BOARD_WIDTH-2][i].getLocation(), Direction.WEST);
            board[BOARD_WIDTH-1][i].addNeighbour(board[BOARD_WIDTH-1][i+1].getLocation(), Direction.SOUTH);
        }
        //Get the rest of the neighbours, those who have four neighbours
        //TOP, RIGHT, BOTTOM, LEFT
        for (int i = 1; i < BOARD_HEIGHT-1; i++){
            for (int j = 1; j < BOARD_WIDTH-1;j++){
                board[j][i].addNeighbour(board[j][i-1].getLocation(), Direction.NORTH);
                board[j][i].addNeighbour(board[j+1][i].getLocation(), Direction.EAST);
                board[j][i].addNeighbour(board[j][i+1].getLocation(), Direction.SOUTH);
                board[j][i].addNeighbour(board[j-1][i].getLocation(), Direction.WEST);
            }
        }
    }

    public void checkIsOccupied(){
        for (int i = 0; i < 27; i++){
            for (int j = 0; j < 26; j++){
                if (board[i][j].isOccupied()){
                    //System.out.println(board[i][j].getLocation());
                }
            }
        }
    }

    public int getWidthInTiles(){
        return BOARD_WIDTH;
    }

    public int getHeightInTiles(){
        return BOARD_HEIGHT;
    }

    //Determine whether we can move on the tile or not
    public boolean blocked(Mover mover, int x, int y){
        if (board[x][y].isTraversable() && !board[x][y].isOccupied()){
            return false;
        }

        return true;
    }

    //Get the cost of moving
    public float getCost(Mover mover, int sx, int sy, int tx, int ty){
        return 1;
    }

    //Set a tile to have been visited
    public void pathFinderVisited(int x, int y){
        visited[x][y] = true;
    }

    //Clear that all of the tiles have been visited
    public void clearVisited() {
        for (int x=0;x<getWidthInTiles();x++) {
            for (int y=0;y<getHeightInTiles();y++) {
                visited[x][y] = false;
            }
        }
    }

}
