package com.Team11.Cluedo.Controls;

import com.Team11.Cluedo.UI.GameScreen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class Command {


    private GameScreen main_panel;

    public Command(GameScreen Main_Panel) {


       main_panel = Main_Panel;


       command();
    }


    private void command()
    {
       // JOptionPane.showMessageDialog(null, "obi wan kenobi","show Message" ,JOptionPane.INFORMATION_MESSAGE, hello);

        main_panel.infoOutput.append("Welcome to Cluedo\n");
        main_panel.infoOutput.append("Please enter 1 to continue\n");

        main_panel.testButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                main_panel.input = main_panel.commandInput.getText() + '\n';


                if(main_panel.commandInput.getText().equals("1"))
                {
                    main_panel.infoOutput.append(main_panel.input);
                    main_panel.infoOutput.append("Welcome\n");
                    main_panel.infoOutput.append("Please enter the following\n ");
                    main_panel.infoOutput.append("l - Left\n r - Right\n u - Up\n d - Down\n ");
                    movement();
                    main_panel.commandInput.setText("");

                }

                main_panel.commandInput.setText("");
                 }
        });
    }

    public void turn()
    {
        //User turn starts
    }


    public void character_setup(int num){
        //set up character info and choose character for number of players imputed 'num'
        command();
    }

    public /*int*/ void Diceroll(){
        //Roll dice and animate using Dice class
        //reurn number of dice roll for movement function

    }

    public void movement()
    {

        main_panel.testButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                main_panel.input = main_panel.commandInput.getText() + '\n';
                main_panel.infoOutput.append(main_panel.input);

                while(main_panel.commandInput.getText().equals("u") || main_panel.commandInput.getText().equals("d")
                        || main_panel.commandInput.getText().equals("l") || main_panel.commandInput.getText().equals("r"))
                {
                    if(main_panel.commandInput.getText().equals("u"))
                    {
                        main_panel.infoOutput.append("Up");
                        //move character up
                        main_panel.commandInput.setText("");
                        movement();
                    }
                    else if(main_panel.commandInput.getText().equals("d"))
                    {
                        main_panel.infoOutput.append("Down");
                        //move character down
                        main_panel.commandInput.setText("");
                        movement();
                    }
                    else if(main_panel.commandInput.getText().equals("l"))
                    {
                        main_panel.infoOutput.append("Left");
                        //move character left
                        main_panel.commandInput.setText("");
                        movement();
                    }
                    else if(main_panel.commandInput.getText().equals("r"))
                    {
                        main_panel.infoOutput.append("Right");
                        //move character right
                        main_panel.commandInput.setText("");
                        movement();
                    }
                }

                main_panel.commandInput.setText("");
            }
        });

    }

}
