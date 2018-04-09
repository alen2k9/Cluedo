/*
  Code to handle the creation of the gamescreen and UI.

  Authors Team11:  Jack Geraghty - 16384181
                   Conor Beenham - 16350851
                   Alen Thomas   - 16333003
 */

package com.team11.cluedo.ui;

import com.team11.cluedo.accusations.AccusationPanel;
import com.team11.cluedo.assets.Assets;
import com.team11.cluedo.cards.CardsPanel;
import com.team11.cluedo.components.Autocomplete;
import com.team11.cluedo.components.Dice;
import com.team11.cluedo.components.InputData;

import com.team11.cluedo.board.Board;
import com.team11.cluedo.players.NotesPanel;
import com.team11.cluedo.players.NotesTable;
import com.team11.cluedo.players.Players;

import com.team11.cluedo.questioning.QPanel;

import com.team11.cluedo.suspects.Suspects;
import com.team11.cluedo.ui.components.*;
import com.team11.cluedo.ui.panel.BackgroundPanel;
import com.team11.cluedo.weapons.Weapons;
import com.team11.cluedo.cards.Cards;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class GameScreen extends JFrame implements Screen {
    private static final String COMMIT_ACTION = "commit";

    private BoardUI boardPanel;
    private JPanel playerPanel;
    private PlayerHandLayout playerHandPanel;
    private PlayerChange playerChange;
    private NotesTable notesTable;
    private Dice gameDice;
    private CardsPanel cardsPanel;
    private NotesPanel notesPanel;
    private ButtonPanel buttonPanel;

    private QPanel qPanel;
    private AccusationPanel aPanel;

    private MoveOverlay moveOverlay;
    private DoorOverlay doorOverlay;

    private JTextArea infoOutput;
    private JTextArea personal;
    private JTabbedPane infoTabs;
    private JTextField commandInput;

    private final Board gameBoard;
    private final Suspects gameSuspects;
    private final Weapons gameWeapons;
    private final Players gamePlayers;
    private final Assets gameAssets;
    private final Cards gameCards;

    private final Resolution resolution;
    private Dimension currSize;

    private boolean gameEnabled;

    public GameScreen(Board gameBoard, Suspects gameSuspects, Weapons gameWeapons, Players gamePlayers, Assets gameAssets, Resolution resolution, String name) throws IOException{
        super(name);
        super.setIconImage(gameAssets.getIcon());
        this.gameBoard = gameBoard;
        this.gameSuspects = gameSuspects;
        this.gameWeapons = gameWeapons;
        this.gamePlayers = gamePlayers;
        this.gameAssets = gameAssets;
        this.resolution = resolution;
        this.gameCards = new Cards(resolution);
        this.moveOverlay = new MoveOverlay(this.getGamePlayers().getPlayer(0), this.resolution);
        this.doorOverlay = new DoorOverlay(this.getGamePlayers().getPlayer(0), this.resolution);
        this.qPanel = new QPanel(this, this.resolution);
        this.gameDice = new Dice(gameAssets, resolution);
        this.playerChange = new PlayerChange(resolution);
        this.cardsPanel = new CardsPanel(gameAssets, resolution, "Your Cards");
        this.notesPanel = new NotesPanel(gameAssets,resolution);
    }

    @Override
    public void createScreen(String name) {
        super.setResizable(false);
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public void setupScreen(int state) {
        ImageIcon backgroundTile = this.gameAssets.getBackgroundTile();
        Image backgroundImage = backgroundTile.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage, BackgroundPanel.TILED);

        this.playerPanel = setupPlayerPanel();
        this.playerHandPanel = setupCardPanel();
        this.notesTable = new NotesTable(gamePlayers);
        this.notesPanel.updatePlayerNotes(notesTable);
        this.buttonPanel = new ButtonPanel(gameAssets, resolution);
        this.aPanel = new AccusationPanel(gameCards,resolution);
        this.boardPanel = new BoardUI();
        JPanel infoPanel = setupInfoPanel();

        int index = 0;
        //backgroundPanel.add(playerPanel, BorderLayout.WEST, index++);
        backgroundPanel.add(boardPanel, BorderLayout.CENTER, index++);
        backgroundPanel.add(infoPanel, BorderLayout.EAST, index);

        this.add(backgroundPanel);
        this.pack();
    }
    @Override
    public void displayScreen() {
        setLocation(resolution.getScreenSize().width/2 - getSize().width/2, resolution.getScreenSize().height/2 - (getSize().height/2));
        setVisible(true);
        currSize = getSize();
    }

    @Override
    public void closeScreen() {
        removeAll();
        dispose();
        System.exit(0);
    }

    @Override
    public void reDraw(int currentPlayer) {
        //((PlayerLayout)this.playerPanel.getComponent(0)).reDraw(currentPlayer);
        this.cardsPanel.updatePlayerHand(getGamePlayers().getPlayer(currentPlayer).getPlayerHand());
        ((PlayerLayout)playerPanel).reDraw(currentPlayer);
        this.playerHandPanel.reDraw(currentPlayer);
        this.notesTable.reDraw(currentPlayer);
        this.notesPanel.updatePlayerNotes(notesTable);
    }

    public void reDrawFrame() {
        if (new Dimension(getPreferredSize().width, getSize().height) != currSize) {
            setSize(new Dimension(getPreferredSize().width, getSize().height));
            currSize = new Dimension(getPreferredSize().width, getSize().height);
        }
        repaint();
    }

    private JPanel setupCommandPanel() {
        JPanel commandPanel = new JPanel(new BorderLayout());
        Font f = new Font("Orange Kid",Font.BOLD, (int)(25*resolution.getScalePercentage()));

        commandInput = new JTextField(15);
        commandInput.setFont(f);
        commandInput.setBackground(Color.WHITE);
        commandInput.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        commandInput.setForeground(Color.DARK_GRAY);

        //////////////////////////////////////////////////////////////////////////////
        /*
        Code for auto-completing commands
        Only add or remove commands to the commandData arrayList in InputData class
        Don't change any other code in this section
        */
        commandInput.setFocusTraversalKeysEnabled(false);
        InputData inputData = new InputData();
        Autocomplete autoComplete = new Autocomplete(commandInput, inputData.getCommandData());
        commandInput.getDocument().addDocumentListener(autoComplete);
        commandInput.getInputMap().put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION);
        commandInput.getActionMap().put(COMMIT_ACTION, autoComplete.new CommitAction());
        ////////////////////////////////////////////////////////////////////////////////

        commandPanel.add(commandInput, BorderLayout.CENTER);
        commandPanel.setOpaque(false);

        return commandPanel;
    }

    private JTextArea setupHelpPanel() {
        JTextArea helpOutput = new JTextArea(15, 25);
        int fontSize = (int)(18 * resolution.getScalePercentage());
        helpOutput.setFont(new Font("Orange Kid",Font.BOLD, fontSize));
        helpOutput.setEditable(false); infoOutput.setLineWrap(true);
        helpOutput.setBackground(Color.DARK_GRAY);
        helpOutput.setForeground(Color.WHITE);
        helpOutput.setBorder(null);

        try {
            InputStream in = getClass().getResourceAsStream("helpPanel.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null)   {

                helpOutput.append(strLine + "\n");
            }
            in.close();

        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }

        return helpOutput;
    }

    private JTextArea setupPersonalPanel() {
        personal = new JTextArea(15, 25);
        int fontSize = (int)(25 * resolution.getScalePercentage());
        personal.setFont(new Font("Orange Kid",Font.BOLD, fontSize));
        personal.setEditable(true); infoOutput.setLineWrap(true);
        personal.setBackground(Color.DARK_GRAY);
        personal.setForeground(Color.WHITE);
        personal.setBorder(null);

        return personal;
    }

    private JPanel setupInfoPanel() {
        UIManager.put("TabbedPane.selected", gameAssets.getDarkerGrey());
        UIManager.put("TabbedPane.contentAreaColor", gameAssets.getDarkerGrey());

        int fontSize = (int)(25 * resolution.getScalePercentage());
        Font f = new Font("Orange Kid", Font.BOLD, fontSize);

        this.infoTabs = new JTabbedPane();
        infoTabs.setUI(new BasicTabbedPaneUI(){
            @Override
            protected void installDefaults() {
                super.installDefaults();
                Color alpha = new Color(0,0,0,0);
                highlight = Color.BLACK;
                lightHighlight = alpha;
                shadow = alpha;
                darkShadow = alpha;
                focus = alpha;
            }
        });
        infoTabs.setBackground(Color.BLACK);
        infoTabs.setForeground(Color.WHITE);
        infoOutput = new JTextArea(15, 25);
        DefaultCaret caret = (DefaultCaret)infoOutput.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        infoOutput.setFont(f);
        infoOutput.setEditable(false); infoOutput.setLineWrap(true);
        infoOutput.setBackground(Color.DARK_GRAY);
        infoOutput.setForeground(Color.WHITE);
        infoOutput.setBorder(null);

        JScrollPane[] scrollPane = new JScrollPane[] {new JScrollPane(infoOutput), new JScrollPane(setupHelpPanel()), new JScrollPane(setupPersonalPanel())};

        for (JScrollPane pane : scrollPane) {
            pane.setBorder(null);
            pane.getVerticalScrollBar().setUnitIncrement(15);
        }

        infoTabs.addTab("Game Log", null, scrollPane[0], "Game Log - Forget what's happened so far?");
        infoTabs.addTab("Help Panel", null, scrollPane[1], "Help Panel - List of all commands");
        infoTabs.addTab("personal log", null, scrollPane[2], "Personal Log - Add personal info");

        JPanel infoPanel = new JPanel(new BorderLayout());

        infoPanel.setOpaque(false);
        infoPanel.add(playerPanel, BorderLayout.NORTH);
        infoPanel.add(infoTabs, BorderLayout.CENTER);
        infoPanel.add(setupCommandPanel(), BorderLayout.SOUTH);
        infoPanel.setPreferredSize(new Dimension(infoPanel.getPreferredSize().width + 3, infoOutput.getPreferredSize().height));

        return infoPanel;


    }

    private PlayerHandLayout setupCardPanel() {
        return new PlayerHandLayout(gameCards, gamePlayers, resolution);
    }

    private JPanel setupPlayerPanel() {
        JPanel panel = new JPanel(null);

        PlayerLayout playerPanel = new PlayerLayout(gamePlayers, resolution, 0);
        playerPanel.setOpaque(false);
        TitledBorder border = new TitledBorder(new LineBorder(Color.BLACK,3), "PLAYERS");
        border.setTitleFont(new Font("Orange Kid",Font.BOLD, (int)(20*resolution.getScalePercentage())));
        border.setTitleColor(Color.WHITE);
        playerPanel.setBorder(border);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.CENTER;// gbc.fill = GridBagConstraints.BOTH;
        panel.add(playerPanel);
        panel.setOpaque(false);
        return playerPanel;
    }

    //  Getters //

    public JTextArea getInfoOutput() {
        return infoOutput;
    }

    public JTextField getCommandInput() {
        return commandInput;
    }

    public JTextArea getPersonal(){return personal;}

    public Board getGameBoard() {
        return gameBoard;
    }

    public Suspects getGameSuspects() {
        return gameSuspects;
    }

    public Weapons getGameWeapons() {
        return gameWeapons;
    }

    public Players getGamePlayers() {
        return gamePlayers;
    }

    public Cards getGameCards() {
        return gameCards;
    }

    public MoveOverlay getMoveOverlay() {
        return this.moveOverlay;
    }

    public DoorOverlay getDoorOverlay() {
        return this.doorOverlay;
    }

    public PlayerChange getPlayerChange() {
        return playerChange;
    }

    public CardsPanel getCardsPanel() {
        return cardsPanel;
    }

    public NotesPanel getNotesPanel() {
        return notesPanel;
    }

    public ButtonPanel getButtonPanel() {
        return buttonPanel;
    }

    public Dice getGameDice() {
        return gameDice;
    }

    public BoardUI getBoardPanel() {
        return this.boardPanel;
    }

    public Resolution getResolution(){
        return this.resolution;
    }

    public QPanel getQuestionPanel() {
        return qPanel;
    }

    public QPanel getqPanel() {
        return qPanel;
    }

    public Assets getGameAssets() {
        return gameAssets;
    }

    public void setTab(int i) {
        infoTabs.setSelectedIndex(i);
    }

    public void setGameEnabled(boolean gameEnabled) {
        this.gameEnabled = gameEnabled;
    }

    public AccusationPanel getAccusations(){
        return aPanel;
    }

    public class BoardUI extends JLayeredPane {
        public BoardUI() {

            setBackground(new Color(69,136,100));
            setBorder(new LineBorder(new Color(108,13,13), 5));
            setOpaque(true);

            int index = 0;
            add(gameCards.getMurderEnvelope(), index++);
            add(aPanel, index++);
            add(playerChange, index++);
            add(qPanel, index++);
            add(buttonPanel, index++);
            add(cardsPanel, index++);
            add(notesPanel, index++);
            add(gameDice,index++);
            add(gameSuspects,index++);
            add(gameWeapons,index++);
            add(doorOverlay,index++);
            add(moveOverlay,index++);
            add(gameBoard,index);


            qPanel.hideQuestionPanel();
//            accusations.setVisible(false);

            ImageIcon board = new ImageIcon(gameAssets.getBoardImage());
            int size = (int)(60 * resolution.getScalePercentage());
            int translate = size/2;
            Dimension componentSize = new Dimension((int)(board.getIconWidth()*resolution.getScalePercentage()),
                    (int)(board.getIconHeight()*resolution.getScalePercentage()));
            Dimension panelSize = new Dimension(componentSize.width + size,
                    componentSize.height + size);
            this.setPreferredSize(panelSize);

            this.getComponent(0).setLocation(0,0);
            for (int i = 1 ; i < getComponentCount() ; i++) {
                if (i < 4) {
                    this.getComponent(i).setSize(panelSize);
                    this.getComponent(i).setLocation(0,0);
                } else if (i >= 7) {
                    this.getComponent(i).setSize(componentSize);
                    this.getComponent(i).setLocation(translate, translate);
                }
            }
            setupMouse();
            cardsPanel.setPosY((int)(Board.TILE_SIZE*3*resolution.getScalePercentage()));
            notesPanel.setLocation((int)(Board.TILE_SIZE*2*resolution.getScalePercentage()),getPreferredSize().height-notesPanel.getTabHeight());
            notesPanel.setMaxY(getPreferredSize().height);
            buttonPanel.setLocation(getPreferredSize().width-buttonPanel.getPreferredSize().width-size,
                    (int)(Board.TILE_SIZE*0.5*resolution.getScalePercentage()));
        }

        private void setupMouse() {
           cardsPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (gameEnabled) {
                        cardsPanel.animate();
                        if (!notesPanel.isEnabled())
                            notesPanel.animate();
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    if (!cardsPanel.isAnimating())
                        if (!cardsPanel.isEnabled()) {
                            cardsPanel.setAlpha(false);
                            GameScreen.this.repaint();
                        }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    if (!cardsPanel.isAnimating())
                        if (!cardsPanel.isEnabled()) {
                            cardsPanel.setAlpha(true);
                            GameScreen.this.repaint();
                        }
                }
            });

           notesPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (gameEnabled) {
                        notesPanel.animate();
                        if (!cardsPanel.isEnabled())
                            cardsPanel.animate();
                    }
                }
            });
        }

        public boolean checkPoint(int x, int y){
            OverlayTile clickedPoint = new OverlayTile(x,y);
            HashSet<OverlayTile> validMoves = moveOverlay.getValidMoves();

            if (!validMoves.isEmpty()){
                for (OverlayTile overlayTile : validMoves){
                    if (overlayTile.getLocation().equals(clickedPoint.getLocation())){
                        return true;
                    }
                }
            }
            return false;
        }

        public int checkMoveOut(int x, int y){
            OverlayTile clickedPoint = new OverlayTile(y,x);
            ArrayList<OverlayTile> validDoors = doorOverlay.getValidExits();

            if (!validDoors.isEmpty()){
                for (OverlayTile overlayTile : validDoors){
                    if (overlayTile.getLocation().equals(clickedPoint.getLocation())){
                        return validDoors.indexOf(overlayTile);
                    }
                }
            }
            return -1;
        }
    }
}
