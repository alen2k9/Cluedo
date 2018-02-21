
package com.team11.cluedo.ui;


import com.team11.cluedo.assets.Assets;
import com.team11.cluedo.board.Board;
import com.team11.cluedo.suspects.Players;
import com.team11.cluedo.controls.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import com.team11.cluedo.ui.Panel.*;

/**
 * The Menu Screen Class
 * Used to handle the title screen, selection of characters and passing through to the Game Screen
 *
 * Main author: Conor Beenham - 16350851
 */
public class MenuScreen implements Screen {
    private JFrame frame;
    private JPanel mainPanel;

    private int numPlayers;
    private int currentPlayer;
    private JTextField nameInput;
    private JButton enterButton;

    private Assets gameAssets;
    private GameScreen gameScreen;
    private CommandInput gameInput;
    private Board gameBoard;

    /**
     * Constructor for the Menu Screen
     * @param gameAssets Handles the importing and accessing of assets.
     * @param gameScreen Handles the creation of the Game Screen
     * @param gameInput Handles the user input for the Game Screen
     */
    public MenuScreen(Assets gameAssets, GameScreen gameScreen, CommandInput gameInput, Board board){
        //  Setting global variables
        this.gameAssets = gameAssets;
        this.gameScreen = gameScreen;
        this.gameInput = gameInput;
        this.gameBoard = board;

        //  Calling functions to create screen
        this.setupScreen(0);
        this.createScreen("cluedo - Title Screen");
        this.displayScreen();
    }

    /**
     *  Method handled to create the screen
     *  @param name Used for the naming of the JPanel
     */
    @Override
    public void createScreen(String name) {
        this.frame = new JFrame(name);
        this.frame.setResizable(false);
        this.frame.getContentPane().add(this.mainPanel);
        this.frame.pack();
    }

    /**
     *  Method handled to set up the screen
     *  @param state Used for the setup of which screen state
     */
    @Override
    public void setupScreen(int state) {
        if (state == 0) {
            mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
            mainPanel.add(getMenuContent());
        }
        else if (state == 1) {
            mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
            mainPanel.add(getPlayerSelection());
        }
    }

    /**
     *  Method handled to display the screen
     */
    @Override
    public void displayScreen() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.frame.setLocation(dimension.width/2 - frame.getSize().width/2, dimension.height/2 - (frame.getSize().height/2));
        this.frame.setVisible(true);
    }

    /**
     *  Method handled to close the screen
     *  Removes everything from the frame closes Frame
     */
    @Override
    public void closeScreen() {
        this.mainPanel.removeAll();
        this.frame.removeAll();
        this.frame.dispose();
    }

    /**
     *  Method handled to re-draw the screen
     */
    @Override
    public void reDraw() {

    }

    /**
     *  Custom JPanel used to draw image on the background of the panel
     */
    public class MenuPanel extends JPanel {
        /**
         * Constructor for the object MenuPanel
         * @param layout Used to set the layout manager for the Panel.
         */
        MenuPanel(LayoutManager layout) {
            super.setLayout(layout);
        }

        @Override
        public void paintComponent(Graphics g) {
            Image splashImage = gameAssets.getSplashImage();
            g.drawImage(splashImage, 0, 0, this);
        }
    }

    /**
     * Setting up and laying out the content of the screen using custom JPanel object.
     * @return MenuPanel to be used in the JFrame
     */
    private MenuPanel getMenuContent() {
        MenuPanel menuPanel = new MenuPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel playerAmount = new JLabel("Select amount of players: ");
        playerAmount.setForeground(Color.WHITE);
        playerAmount.setBackground(Color.BLACK);

        String[] amountChoice = new String[] {"Two", "Three", "Four", "Five", "Six"};
        JComboBox amountList = new JComboBox(amountChoice);
        amountList.setSelectedIndex(0);

        JButton playButton = new JButton("Play");
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5,5, 5, 5);
        menuPanel.add(playerAmount, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 2;
        menuPanel.add(amountList, gbc);
        gbc.gridwidth = 3;
        gbc.gridx = 0; gbc.gridy = 1;
        menuPanel.add(playButton, gbc);

        playButton.addActionListener(e -> {
            this.numPlayers = amountList.getSelectedIndex() + 2;
            this.currentPlayer = 0;

            Players gamePlayers = new Players(numPlayers, this.gameAssets, this.gameBoard);
            gameScreen.setGamePlayers(gamePlayers);
            this.closeScreen();

            this.setupScreen(1);
            this.createScreen("cluedo - Character Selection");
            this.displayScreen();
        });

        Dimension imageSize = new Dimension(600, 400);
        menuPanel.setPreferredSize(imageSize);
        return menuPanel;
    }

    /**
     * Setting up and laying out the content of the screen using custom JPanel object.
     * @return BackgroundPanel to be used in the JFrame
     */
    private BackgroundPanel getPlayerSelection() {
        ImageIcon backgroundTile = this.gameAssets.getBackgroundTile();
        Image backgroundImage = backgroundTile.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage, BackgroundPanel.TILED);
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton[] characterButtons = new JButton[] {
            new JButton(new ImageIcon(this.gameAssets.getWhiteCard())),
            new JButton(new ImageIcon(this.gameAssets.getGreenCard())),
            new JButton(new ImageIcon(this.gameAssets.getPeacockCard())),
            new JButton(new ImageIcon(this.gameAssets.getPlumCard())),
            new JButton(new ImageIcon(this.gameAssets.getScarletCard())),
            new JButton(new ImageIcon(this.gameAssets.getMustardCard()))
        };

        ImageIcon[] selectedCharacters = new ImageIcon[] {
                new ImageIcon(this.gameAssets.getSelectedWhiteCard()),
                new ImageIcon(this.gameAssets.getSelectedGreenCard()),
                new ImageIcon(this.gameAssets.getSelectedPeacockCard()),
                new ImageIcon(this.gameAssets.getSelectedPlumCard()),
                new ImageIcon(this.gameAssets.getSelectedScarletCard()),
                new ImageIcon(this.gameAssets.getSelectedMustardCard())
        };

        for (int i = 0, x = 0, y = 1 ; i < characterButtons.length ; i++, x++) {
            setButtonDisplay(i, characterButtons, selectedCharacters[i]);
            if(i == 3) {
                x = 0;
                y++;
            }
            gbc.gridx = x; gbc.gridy = y;
            backgroundPanel.add(characterButtons[i], gbc);
        }

        gbc.gridx = 1; gbc.gridy = 3;
        this.nameInput = new JTextField(20);
        nameInput.setHorizontalAlignment(JTextField.CENTER);
        backgroundPanel.add(nameInput, gbc);
        this.enterButton = new JButton("Enter");

        //  Action listener for players
        this.enterButton.addActionListener(e -> {
            if(currentPlayer < numPlayers) {
                if(!nameInput.getText().equals("")) {
                    Boolean doContinue = true;
                    String playerName = nameInput.getText();
                    for (int i = 0 ; i < currentPlayer ; i++) {
                        if (playerName.equals(gameScreen.getGamePlayers().getPlayer(i).getPlayerName())) {
                            doContinue = false;
                        }
                    }
                    if(doContinue) {
                        doContinue = false;
                        int index = 0;
                        for (int i = 0; i < characterButtons.length; i++) {
                            if (characterButtons[i].isSelected()) {
                                index = i;
                                characterButtons[i].setSelected(false);
                                characterButtons[i].setEnabled(false);
                                doContinue = true;
                            }
                        }
                        if (doContinue) {
                            gameScreen.getGamePlayers().setPlayer(currentPlayer++, index, playerName);
                            nameInput.setText("");

                            for (JButton charButton : characterButtons) {
                                charButton.setSelected(false);
                                charButton.setBorderPainted(false);
                            }

                            if (currentPlayer == numPlayers) {
                                closeScreen();
                                startGame();
                            }
                        }
                        else {
                        JOptionPane.showMessageDialog(null,"No character selected. Please select a character.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,"Name same as previous players. Please enter a new name.");
                        nameInput.setText("");
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"No name entered. Please enter a name.");
                }
            }
        });

        gbc.gridy += 1;
        backgroundPanel.add(this.enterButton, gbc);
        return backgroundPanel;
    }

    /**
     * Method to add actionlistener to all character buttons
     * @param index Current button being used
     * @param button JButton array of character buttons
     * @param selectedIcon The ImageIcon used when button has been clicked
     */
    private void setButtonDisplay(int index, JButton[] button, ImageIcon selectedIcon) {
        button[index].setBorderPainted(false);
        button[index].setContentAreaFilled(false);
        button[index].setFocusPainted(false);
        button[index].setOpaque(false);
        button[index].setSelectedIcon(selectedIcon);
        button[index].setDisabledIcon(selectedIcon);

        button[index].addActionListener(e -> {
            if (button[index].isSelected()) {
                button[index].setSelected(false);
            } else {
                for (JButton charButton : button) {
                    charButton.setSelected(false);
                    charButton.setBorderPainted(false);
                }
                button[index].setSelected(true);
                button[index].setBorderPainted(true);
            }

            nameInput.requestFocus();
            frame.getRootPane().setDefaultButton(enterButton);
        });
    }

    /**
     * Method to launch game with current players
     */
    private void startGame() {
        gameScreen.createScreen("cluedo");
        gameScreen.setupScreen(1);
        gameScreen.displayScreen();
        gameScreen.getGamePlayers().setSpawnsOccupied(gameBoard);
        gameInput.initialSetup();
        gameInput.introduction();
    }
}