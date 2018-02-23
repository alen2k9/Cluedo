/**
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */
package com.team11.cluedo.controls;


import com.team11.cluedo.suspects.Direction;
import com.team11.cluedo.ui.GameScreen;
import com.team11.cluedo.weapons.WeaponData;

import javax.swing.*;
import java.util.ArrayList;



public class CommandInput {
    private GameScreen gameScreen;

    private int currentPlayer;

    public CommandInput(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        currentPlayer = 0;
    }

    public void initialSetup()
    {
        gameScreen.getInfoOutput().append("\nPlease enter 1 to move player\n");
        gameScreen.getInfoOutput().append("Please enter 2 to move Weapons\n");
    }

    public void introduction()
    {
        boolean doOnce = false;

        gameScreen.getCommandInput().addActionListener(e -> {
            gameScreen.setInput(gameScreen.getCommandInput().getText() + '\n');
            //initialSetup();
                if (gameScreen.getCommandInput().getText().equals("1")) {
                    gameScreen.reDraw(currentPlayer);
                    gameScreen.getInfoOutput().append(gameScreen.getInput());
                    //gameScreen.getInfoOutput().append("Welcome\n");
                    gameScreen.getInfoOutput().append("Please enter the following\n ");
                    gameScreen.getInfoOutput().append("l - Left\n r - Right\n u - Up\n d - Down\n 'back' - Return to Options ");
                    playerMovement();
                    gameScreen.getCommandInput().setText("");

                } else if (gameScreen.getCommandInput().getText().equals("2")) {
                    gameScreen.reDraw(currentPlayer);
                    gameScreen.getInfoOutput().append(gameScreen.getInput());
                    //gameScreen.getInfoOutput().append("Welcome\n");
                    gameScreen.getInfoOutput().append("Please enter the following\n ");
                    gameScreen.getInfoOutput().append("Rooms to choose from:\n- Kitchen\n- Ballroom\n- Conservatory\n- Biliard WeaponPoints\n- Library\n" +
                            "- Study\n- Hall \n- Lounge\n- Dining WeaponPoints \n- Cellar\n\n");
                    gameScreen.getInfoOutput().append("Weapons to choose from\n");
                    gameScreen.getInfoOutput().append("- Candlestick\n- Dagger\n- Lead Pipe\n- Revolver\n- Rope\n- Spanner");
                    weaponMovement();
                } else if (gameScreen.getCommandInput().getText().equals("4")){
                    gameScreen.reDraw(currentPlayer);
                    gameScreen.getInfoOutput().append((gameScreen.getInput()));
                    playerMovement();
                }

                else if(gameScreen.getCommandInput().getText().equals("")){
                    gameScreen.reDraw(currentPlayer);
                }

                gameScreen.getCommandInput().setText("");
        });

        if (!doOnce){
            //gameScreen.getInfoOutput().act
            doOnce = true;
        }

    }


    public void playerTurn(int currentUser)
    {
        Boolean endTurn = false;
        while (!endTurn) {
            if(/* SOME CONDITION FROM COMPLETING MOVE this.gameScreen.getGameSuspects().getRemainingPlayers() < 2 */true) {
                endTurn = true;
            }
            /*
            TO DO command input, then push into each options below
             */
        }
        this.gameScreen.reDraw(currentUser);
    }

    public /*int*/ void Diceroll(){
        //Roll dice and animate using Dice class
        //return number of dice roll for playerMovement function
    }

    private class ChoiceOption {

        private String weapon;
        private String room;

        private ChoiceOption()
        {
            makeChoice();
        }

        private void makeChoice()
        {
            WeaponData weaponData = new WeaponData();
            String[] weaponChoice = new String[gameScreen.getGameWeapons().getNumWeapons()];
            String[] roomChoice = { "Kitchen", "Ballroom", "Conservatory", "Billiard", "Library", "Study", "Hall", "Lounge", "Dining WeaponPoints", "Cellar" };

            for (int i = 0; i < gameScreen.getGameWeapons().getNumWeapons() ; i++) {
                weaponChoice[i] = weaponData.getWeaponName(i);
                System.out.print(weaponChoice[i]);
            }

            weapon = (String) JOptionPane.showInputDialog(null, "Choose the Weapon you want to move",
                    "Weapon Movement", JOptionPane.QUESTION_MESSAGE, null, weaponChoice, weaponChoice[0]);

            if (weapon != null){
                room = (String) JOptionPane.showInputDialog(null, "Choose the WeaponPoints you want to move it into",
                        "Weapon Movement", JOptionPane.QUESTION_MESSAGE, null, roomChoice, roomChoice[0]);
            }

            else{
                System.out.println("Cancelling weapon movement");
            }

        }

        private String getRoom() {
            return room;
        }

        private String getWeapon() {
            return weapon;
        }

    }

    private void weaponMovement()
    {
        ChoiceOption choice = new ChoiceOption();
        int weapon = 0;
        int room = 0;

        if (choice.getWeapon() != null){
            /*
             * Moving Weapon
             */
            if(choice.getRoom().equals("Kitchen"))      //move weapon based on user choice as chosen on joption Pane
            {
                room = 0;
            }
            else if (choice.getRoom().equals("Ballroom"))
            {
                room = 1;
            }
            else if (choice.getRoom().equals("Conservatory"))
            {
                room = 2;
            }
            else if (choice.getRoom().equals("Dining"))
            {
                room = 3;
            }
            else if (choice.getRoom().equals("Billiard"))
            {
                room = 4;
            }
            else if (choice.getRoom().equals("Library"))
            {
                room = 5;
            }
            else if (choice.getRoom().equals("Lounge"))
            {
                room = 6;
            }
            else if (choice.getRoom().equals("Hall"))
            {
                room = 7;
            }
            else if (choice.getRoom().equals("Study"))
            {
                room = 8;
            }

            //private String[] weaponName = {"Candlestick", "Dagger", "Lead Pipe", "Revolver", "Rope", "Spanner"};
            WeaponData weaponData = new WeaponData();

            if (choice.getWeapon().equals(weaponData.getWeaponName(0)))
            {
                System.out.println("Found " + choice.getWeapon());
                weapon = 0;
            }
            else if(choice.getWeapon().equals(weaponData.getWeaponName(1)))
            {
                System.out.println("Found " + choice.getWeapon());
                weapon = 1;
            }
            else if(choice.getWeapon().equals(weaponData.getWeaponName(2)))
            {
                System.out.println("Found " + choice.getWeapon());
                weapon = 2;
            }
            else if(choice.getWeapon().equals(weaponData.getWeaponName(3)))
            {
                System.out.println("Found " + choice.getWeapon());
                weapon = 3;
            }
            else if(choice.getWeapon().equals(weaponData.getWeaponName(4)))
            {
                System.out.println("Found " + choice.getWeapon());
                weapon = 4;
            }
            else if(choice.getWeapon().equals(weaponData.getWeaponName(5)))
            {
                System.out.println("Found " + choice.getWeapon());
                weapon = 5;
            }

            System.out.println("Moving "+ weapon + choice.getWeapon() + " to " + room +choice.getRoom());
            gameScreen.getGameWeapons().moveWeaponToRoom(weapon, room);
            gameScreen.getInfoOutput().append("\n\n" + choice.getWeapon() + " has been moved to " + choice.getRoom() + "\n\n");
            gameScreen.reDraw(currentPlayer);

        }

        else{
            gameScreen.getInfoOutput().append("\nReturning to Main Menu\n");
            initialSetup();
        }

    }

    private void playerMovement()
    {

        ArrayList<Direction> list = new ArrayList<>();
        ArrayList<Direction> tList = new ArrayList<>();

        list.add(Direction.WEST);
        list.add(Direction.WEST);
        list.add(Direction.WEST);
        list.add(Direction.WEST);
        list.add(Direction.WEST);
        list.add(Direction.WEST);
        list.add(Direction.SOUTH);
        list.add(Direction.SOUTH);

        tList.add(Direction.NORTH);




        /*
        Player movement function found in: this.gameScreen.getGameSuspects().playerMove(PLAYER, DIRECTION, AMUONT);
         */
        gameScreen.getCommandInput().addActionListener(e -> {
                if(gameScreen.getCommandInput().getText().equals("u"))
                {
                    gameScreen.getInfoOutput().append("\nUp");
                    //playerMove character up
                    //gameScreen.getGameSuspects().playerMove(gameBoard, currentPlayer, Direction.NORTH);
                    gameScreen.reDraw(currentPlayer);
                    gameScreen.getCommandInput().setText("");
                    playerMovement();
                }
                else if(gameScreen.getCommandInput().getText().equals("d"))
                {
                    gameScreen.getInfoOutput().append("\nDown");
                    //playerMove character down
                    //gameScreen.getGameSuspects().playerMove(gameBoard,currentPlayer, Direction.SOUTH, 1);
                    gameScreen.reDraw(currentPlayer);
                    gameScreen.getCommandInput().setText("");
                    playerMovement();
                }
                else if(gameScreen.getCommandInput().getText().equals("l"))
                {
                    gameScreen.getInfoOutput().append("\nLeft");
                    //playerMove character left
                    //gameScreen.getGameSuspects().playerMove(gameBoard, currentPlayer, Direction.WEST);
                    gameScreen.reDraw(currentPlayer);
                    gameScreen.getCommandInput().setText("");
                    playerMovement();
                }
                else if(gameScreen.getCommandInput().getText().equals("r"))
                {
                    gameScreen.getInfoOutput().append("\nRight");
                    //playerMove character right
                    //gameScreen.getGameSuspects().playerMove(gameBoard, currentPlayer, Direction.EAST, 1);
                    gameScreen.reDraw(currentPlayer);
                    gameScreen.getCommandInput().setText("");
                    playerMovement();
                }
                else if (gameScreen.getCommandInput().getText().equals("back")){
                    gameScreen.getInfoOutput().append("\n\nReturning to Main Menu\nEnter 1 to Move\nEnter 2 to move a weapon\n");
                    gameScreen.getCommandInput().setText("");
                    System.out.println("Returning to main menu");
                    introduction();
                }
                else if (gameScreen.getCommandInput().getText().equals("test")){
                    gameScreen.getGamePlayers().playerMove(gameScreen.getGameBoard(), currentPlayer, list);
                }

                else if (gameScreen.getCommandInput().getText().equals("room")){
                    gameScreen.getGamePlayers().moveOutOfRoom(gameScreen.getGameBoard(), currentPlayer, 2);
                }

                else if (gameScreen.getCommandInput().getText().equals("secret")){
                    if (gameScreen.getGamePlayers().useSecretPassageWay(gameScreen.getGameBoard(), currentPlayer)){
                        gameScreen.getInfoOutput().append("Used secret passageway");
                    }

                    else{
                        gameScreen.getInfoOutput().append("No secret passageway to use in this room");
                    }

                }
                else if(gameScreen.getCommandInput().getText().equals("reverse")){
                    gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().reverseMoves();
                }

                else if (gameScreen.getCommandInput().getText().equals("testtwo")){
                    gameScreen.getGamePlayers().playerMove(gameScreen.getGameBoard(), currentPlayer, tList);
                }
            gameScreen.getCommandInput().setText("");
        });
    }
}
