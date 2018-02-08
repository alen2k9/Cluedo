package com.Team11.Cluedo.UI;

import com.Team11.Cluedo.Suspects.Players;
import com.Team11.Cluedo.UI.Panel.BackgroundPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.KeyEvent;

public class GameScreen implements Screen {
    private JFrame frame;
    private JPanel mainPanel;
    public JTextArea infoOutput;
    public JButton testButton;
    public String input;
    public JTextField commandInput;



    private Players gamePlayers;

    public GameScreen(Players gamePlayers) throws IOException{
        this.gamePlayers = gamePlayers;

        this.createScreen();
        this.setupScreen();
        this.displayScreen();
        frame.getRootPane().setDefaultButton(testButton);
    }

    @Override
    public void createScreen() {
        this.frame = new JFrame("Cluedo");
        //this.frame.setSize(1280,720);
        this.frame.setResizable(true);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public void setupScreen() {
        this.mainPanel = new JPanel(new BorderLayout());
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);

        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("..\\Assets\\backgroundTile.PNG"));
        Image bgroundImage = backgroundImage.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(bgroundImage,BackgroundPanel.TILED);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3,3,1,3);

        JPanel cardPanel = setupCardPanel();
        gbc.gridx = 0; gbc.gridy = 11;
        gbc.gridwidth = 2; gbc.gridheight = 2;
        contentPanel.add(cardPanel, gbc);

        JPanel commandPanel = setupCommandPanel();
        gbc.gridx = 11; gbc.gridy = 4;
        gbc.gridwidth = 1; gbc.gridheight = 2;
        contentPanel.add(commandPanel, gbc);


        JPanel infoPanel = setupInfoPanel();
        gbc.gridx = 11; gbc.gridy = 0;
        gbc.gridwidth = 2; gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        contentPanel.add(infoPanel, gbc);


        Players boardPanel = setupBoardPanel();
        //JPanel boardPanel = setupBoardPanel();

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 10; gbc.gridheight = 10;
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(boardPanel, gbc);

        backgroundPanel.add(contentPanel);
        mainPanel.add(backgroundPanel);
        this.frame.getContentPane().add(this.mainPanel);

        this.frame.pack();
    }

    @Override
    public void displayScreen() {
        this.frame.setVisible(true);
    }

    @Override
    public void closeScreen() {
        this.mainPanel.removeAll();
        this.frame.dispose();
    }

    private Players setupBoardPanel() {

        try {
<<<<<<< HEAD
            JPanel boardPanel = new JPanel();
=======
            Board boardPanel = new Board();
            boardPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
            ImageIcon boardImage = new ImageIcon(getClass().getResource("..\\Assets\\Board4.PNG"));
            JLabel boardLabel = new JLabel("", boardImage, JLabel.CENTER);
            boardPanel.add(boardLabel, BorderLayout.CENTER);
>>>>>>> adf0b6d7d8ac1a0cf2b87acbef460a79ca7ca2d6

            Players players = this.gamePlayers;

            ImageIcon boardImage = new ImageIcon(getClass().getResource("..\\Assets\\Board4.PNG"));
            JLabel boardLabel = new JLabel(boardImage);
            boardLabel.setBounds(0,0,boardImage.getIconWidth(), boardImage.getIconHeight());
            boardPanel.add(boardLabel);

            players.add(boardLabel);
            return players;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error. com.Team11.Cluedo.Board Layout not found.");
            return null;
        }
    }

    private JPanel setupCommandPanel() {
        JPanel commandPanel = new JPanel(new BorderLayout());
        commandPanel.setBorder(new TitledBorder("Command Entry"));
        commandInput = new JTextField(30);

        testButton = new JButton("Input");
        commandPanel.add(commandInput, BorderLayout.LINE_START);
        commandPanel.add(testButton, BorderLayout.LINE_END);

        return commandPanel;
    }

    private JPanel setupInfoPanel() {
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoOutput = new JTextArea(10, 30);
        infoOutput.setEditable(false); infoOutput.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(infoOutput);
        infoPanel.add(scrollPane, BorderLayout.CENTER);


        return infoPanel;
    }

    private JPanel setupCardPanel() {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(Color.DARK_GRAY);

        return cardPanel;
    }
}
