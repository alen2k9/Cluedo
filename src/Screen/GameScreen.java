package Screen;

import Board.Board;
import Characters.Players;
import Screen.Panel.BackgroundPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GameScreen implements Screen {
    private JFrame frame;
    private JPanel mainPanel;
    private JTextArea infoOutput;

    public GameScreen() throws IOException{
        this.createScreen();
        this.setupScreen();
        this.displayScreen();
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

        ImageIcon backgroundImage = new ImageIcon(GameScreen.class.getResource("../assets/backgroundTile.png"));
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


        Board mapPanel = setupBoardPanel();

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 10; gbc.gridheight = 10;
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(mapPanel, gbc);

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

    private Board setupBoardPanel() {

        try {
            Board boardPanel = new Board(25);
            boardPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
            ImageIcon boardImage = new ImageIcon(GameScreen.class.getResource("../Assets/Board4.png"));
            JLabel boardLabel = new JLabel("", boardImage, JLabel.CENTER);
            boardPanel.add(boardLabel, BorderLayout.CENTER);

            return boardPanel;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error. Board Layout not found.");
            return null;
        }
    }

    private JPanel setupCommandPanel() {
        JPanel commandPanel = new JPanel(new BorderLayout());
        commandPanel.setBorder(new TitledBorder("Command Entry"));
        JTextField commandInput = new JTextField(30);


        JButton testButton = new JButton("Input");
        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = commandInput.getText() + '\n';
                infoOutput.append(input);
                commandInput.setText("");
            }
        });

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
