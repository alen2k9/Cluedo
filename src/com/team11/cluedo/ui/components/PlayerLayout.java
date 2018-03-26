/*
 * Code to handle the display of players and current player
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.ui.components;

import com.team11.cluedo.components.T11Label;
import com.team11.cluedo.players.Players;
import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import java.awt.*;

public class PlayerLayout extends JPanel {
    private Players gamePlayers;
    private Resolution resolution;
    private int currentPlayer, targetPlayer;

    public PlayerLayout(Players gamePlayers, Resolution resolution, int currentPlayer) {
        this.gamePlayers = gamePlayers;
        this.resolution = resolution;
        this.currentPlayer = currentPlayer;
        this.targetPlayer = currentPlayer;
        //getCardLayout();
        getCycleLayout();
    }

    private void getCardLayout() {
        this.setLayout(new GridBagLayout());
        currentPlayer = targetPlayer;

        switch (gamePlayers.getPlayerCount()) {
            case 2: {
                getColumnCardsLayout();
                break;
            }
            case 3: {
                getColumnCardsLayout();
                break;
            }
            case 4: {
                getFourCardsLayout();
                break;
            }
            case 5: {
                getFiveCardsLayout();
                break;
            }
            case 6: {
                getSixCardsLayout();
                break;
            }
        }
    }

    private void getColumnCardsLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
        ImageIcon card;
        for(int i = 0  ; i < gamePlayers.getPlayerCount() ; i++) {
            if (i == currentPlayer) {
                card = new ImageIcon(gamePlayers.getPlayer(i).getCardImage().getScaledInstance(
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                        0));
            } else {
                card = new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage().getScaledInstance(
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                        0));
            }

            JLabel playerCard = new JLabel(card);
            gbc.insets = new Insets(5,5,5,5);
            gbc.gridy = i; gbc.ipady = 5;
            this.add(playerCard, gbc);
        }
    }

    private void getFourCardsLayout() {
        GridBagConstraints gbc = new GridBagConstraints();

        ImageIcon card;
        for(int i = 0, x = 0, y = 0 ; i < gamePlayers.getPlayerCount() ; i++, x++) {
            if (i == 0 || i == 3) {
                x = 0;
                y++;
                gbc.gridwidth = 2;
            }
            if (i == 1) {
                x = 0;
                y++;
                gbc.gridwidth = 1;
            }
            if (i == currentPlayer) {
                card = new ImageIcon(gamePlayers.getPlayer(i).getCardImage().getScaledInstance(
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                        0));
            } else {
                card = new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage().getScaledInstance(
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                        0));
            }

            JLabel playerCard = new JLabel(card);
            gbc.insets = new Insets(5,5,5,5);
            gbc.gridx = x; gbc.gridy = y;
            gbc.ipady = 5;
            this.add(playerCard, gbc);
        }
    }

    private void getFiveCardsLayout() {
        GridBagConstraints gbc = new GridBagConstraints();

        ImageIcon card;
        for(int i = 0, x = 0, y = 0 ; i < gamePlayers.getPlayerCount() ; i++, x++) {
            if (i == 2) {
                x = 0;
                y++;
                gbc.gridwidth = 2;
            }
            if (i == 3) {
                x = 0;
                y++;
                gbc.gridwidth = 1;
            }
            if (i == currentPlayer) {
                card = new ImageIcon(gamePlayers.getPlayer(i).getCardImage().getScaledInstance(
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                        0));
            } else {
                card = new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage().getScaledInstance(
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                        0));
            }

            JLabel playerCard = new JLabel(card);
            gbc.insets = new Insets(5,5,5,5);
            gbc.gridx = x; gbc.gridy = y;
            gbc.ipady = 5;
            this.add(playerCard, gbc);
        }
    }

    private void getSixCardsLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
        ImageIcon card;
        for(int i = 0, x = 0, y = 0 ; i < gamePlayers.getPlayerCount() ; i++, x++) {
            if (i % 2 == 0 && i > 0) {
                x = 0;
                y++;
            }
            if (i == currentPlayer) {
                card = new ImageIcon(gamePlayers.getPlayer(i).getCardImage().getScaledInstance(
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                        0));
            } else {
                card = new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage().getScaledInstance(
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                        0));
            }

            JLabel playerCard = new JLabel(card);
            gbc.insets = new Insets(5,5,5,5);
            gbc.gridx = x; gbc.gridy = y;
            gbc.ipady = 5;
            this.add(playerCard, gbc, i);
        }
    }

    private void getCycleLayout() {
        ImageIcon card = new ImageIcon(gamePlayers.getPlayer(currentPlayer).getCardImage());
        double initialScalar = 0.55;

        int width = (int)(card.getIconWidth() * resolution.getScalePercentage() * initialScalar),
                height = (int)(card.getIconHeight() * resolution.getScalePercentage() * initialScalar);
        int nextPlayer, prevPlayer, next2Player;
        double secondScalar = .8;

        if(currentPlayer == gamePlayers.getPlayerCount() - 1) {
            nextPlayer = 0;
            next2Player = nextPlayer + 1;
        } else {
            nextPlayer = currentPlayer + 1;
            if (nextPlayer == gamePlayers.getPlayerCount() - 1)
                next2Player = 0;
                else
                    next2Player = nextPlayer + 1;
        }
        if(currentPlayer == 0)
            prevPlayer = gamePlayers.getPlayerCount() - 1;
            else
                prevPlayer = currentPlayer - 1;


        ImageIcon currentCard = new ImageIcon(gamePlayers.getPlayer(currentPlayer).getCardImage().getScaledInstance(
                width, height,0));

        ImageIcon prevCard = new ImageIcon(gamePlayers.getPlayer(prevPlayer).getSelectedCardImage().getScaledInstance(
                width, height,0));

        ImageIcon nextCard = new ImageIcon(gamePlayers.getPlayer(nextPlayer).getSelectedCardImage().getScaledInstance(
                width, height,0));

        ImageIcon next2Card = new ImageIcon(gamePlayers.getPlayer(next2Player).getSelectedCardImage().getScaledInstance(
                width, height,0));

        T11Label playerCard = new T11Label(currentCard);
        T11Label nextPlayerCard = new T11Label(nextCard);
        T11Label next2PlayerCard = new T11Label(next2Card);
        T11Label prevPlayerCard = new T11Label(prevCard);

        this.setLayout(null);

        this.add(playerCard);
        this.add(prevPlayerCard);
        this.add(nextPlayerCard);
        this.add(next2PlayerCard);

        playerCard.setSize(new Dimension(currentCard.getIconWidth(), currentCard.getIconHeight()));
        prevPlayerCard.setSize(new Dimension((int)(prevCard.getIconWidth() * secondScalar), (int)(prevCard.getIconHeight()*secondScalar)));
        nextPlayerCard.setSize(new Dimension((int)(nextCard.getIconWidth() * secondScalar), (int)(nextCard.getIconHeight()*secondScalar)));
        next2PlayerCard.setSize(new Dimension((int)(nextCard.getIconWidth() * secondScalar), (int)(nextCard.getIconHeight()*secondScalar)));

        int y = (int)(20 * resolution.getScalePercentage());
        this.setPreferredSize(new Dimension(width*3, height+(2*y)));
        int xPosition = this.getPreferredSize().width /2 - playerCard.getWidth()/2;
        int yPosition = (int)((30 * resolution.getScalePercentage()) + y);

        playerCard.setLocation(xPosition, y);
        prevPlayerCard.setLocation(xPosition - prevPlayerCard.getWidth() - (int)(10 * resolution.getScalePercentage()), yPosition);
        nextPlayerCard.setLocation(xPosition + playerCard.getWidth() + (int)(10 * resolution.getScalePercentage()), yPosition);
        next2PlayerCard.setLocation(xPosition + playerCard.getWidth() + (int)(10 * resolution.getScalePercentage()), yPosition);
    }

    public void reDraw(int currentPlayer) {
        this.targetPlayer = currentPlayer;
        //this.removeAll();
        //getCardLayout();
        new playerSwitcher().execute();
        this.revalidate();
    }

    public class playerSwitcher extends SwingWorker<Integer, String> {
        @Override
        protected Integer doInBackground() throws Exception {
            if (currentPlayer != targetPlayer) {
                currentPlayer++;

                if (currentPlayer == gamePlayers.getPlayerCount()) {
                    currentPlayer = 0;
                }
                T11Label currCard = (T11Label) getComponent(0);

                int card;
                if (currentPlayer == 0)
                    card = gamePlayers.getPlayerCount() - 1;
                else
                    card = currentPlayer - 1;
                currCard.setIcon(new ImageIcon(gamePlayers.getPlayer(card).getSelectedCardImage().getScaledInstance(
                        currCard.getWidth(), currCard.getHeight(), 0)));

                T11Label prevCard = (T11Label) getComponent(1);
                T11Label nextCard = (T11Label) getComponent(2);

                int targetX = currCard.getX(), targetY = currCard.getY(),
                        targetW = currCard.getWidth(), targetH = currCard.getHeight();
                int nextX = nextCard.getX(), nextY = nextCard.getY(),
                        nextW = nextCard.getWidth(), nextH = nextCard.getHeight();
                int currX = currCard.getX(), currY = currCard.getY(),
                        currW = currCard.getWidth(), currH = currCard.getHeight();
                int prevX = prevCard.getX(), prevY = prevCard.getY(),
                        prevW = prevCard.getWidth(), prevH = prevCard.getHeight();

                while (nextH != targetH || nextX != targetX || nextW != targetW || nextY != targetY ||
                        currH != prevH || currX != prevX || currW != prevW || currY != prevY) {
                    int sizeModifier = 2;
                    int xModifier = 5;
                    int yModifier = (int) 1.5;

                    // Next Card
                    if (nextH >= targetH)
                        nextH = targetH;
                    else
                        nextH += sizeModifier;

                    if (nextW >= targetW)
                        nextW = targetW;
                    else
                        nextW += sizeModifier;

                    if (nextX <= targetX)
                        nextX = targetX;
                    else
                        nextX -= xModifier;

                    if (nextY <= targetY)
                        nextY = targetY;
                    else
                        nextY -= yModifier;

                    // Curr Card
                    if (currH <= prevH)
                        currH = prevH;
                    else
                        currH -= sizeModifier;

                    if (currW <= prevW)
                        currW = prevW;
                    else
                        currW -= sizeModifier;

                    if (currX <= prevX)
                        currX = prevX;
                    else
                        currX -= xModifier;

                    if (currY >= prevY)
                        currY = prevY;
                    else
                        currY += yModifier;

                    nextCard.setSize(new Dimension(nextW, nextH));
                    nextCard.setLocation(nextX, nextY);

                    currCard.setSize(new Dimension(currW, currH));
                    currCard.setLocation(currX, currY);

                    Thread.sleep(7);
                }
            }
            removeAll();
            getCycleLayout();

            if(currentPlayer != targetPlayer) {
                new playerSwitcher().execute();
            }
            return null;
        }
    }
}
