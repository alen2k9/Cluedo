package com.team11.cluedo.ui.components;

import com.team11.cluedo.assets.Assets;
import com.team11.cluedo.board.Board;
import com.team11.cluedo.components.T11Label;
import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ButtonPanel extends JPanel {
    private Assets gameAssets;
    private Resolution resolution;

    private T11Label rollButton, questionButton, accuseButton, doneButton;

    public ButtonPanel(Assets gameAssets, Resolution resolution) {
        super(new GridBagLayout());
        this.gameAssets = gameAssets;
        this.resolution = resolution;

        //super.setBackground(new Color(0,0,0,56));
        //super.setBorder(new LineBorder(Color.red, 1));
        super.setOpaque(false);


        setupButtons();
        setupPanel();
    }

    private void setupButtons() {
        int buttonSize = (int)(48*resolution.getScalePercentage());
        Dimension size = new Dimension(buttonSize, buttonSize);
        rollButton = new T11Label(new ImageIcon(gameAssets.getRollButton().getScaledInstance(
                buttonSize,buttonSize,0
        )));
        rollButton.setDisabledIcon(new ImageIcon(gameAssets.getRollButtonPressed().getScaledInstance(
                buttonSize,buttonSize,0
        )));
        rollButton.setSize(size);
        rollButton.setPreferredSize(size);
        rollButton.setEnabled(false);

        questionButton = new T11Label(new ImageIcon(gameAssets.getQuestionButton().getScaledInstance(
                buttonSize,buttonSize,0
        )));
        questionButton.setDisabledIcon(new ImageIcon(gameAssets.getQuestionButtonPressed().getScaledInstance(
                buttonSize,buttonSize,0
        )));
        questionButton.setPreferredSize(size);
        questionButton.setSize(size);
        questionButton.setEnabled(false);

        accuseButton = new T11Label(new ImageIcon(gameAssets.getAccuseButton().getScaledInstance(
                buttonSize,buttonSize,0
        )));
        accuseButton.setDisabledIcon(new ImageIcon(gameAssets.getAccuseButtonPressed().getScaledInstance(
                buttonSize,buttonSize,0
        )));
        accuseButton.setPreferredSize(size);
        accuseButton.setSize(size);
        accuseButton.setEnabled(false);

        doneButton = new T11Label(new ImageIcon(gameAssets.getDoneButton().getScaledInstance(
                buttonSize,buttonSize,0
        )));
        doneButton.setDisabledIcon(new ImageIcon(gameAssets.getDoneButtonPressed().getScaledInstance(
                buttonSize,buttonSize,0
        )));
        doneButton.setPreferredSize(size);
        doneButton.setSize(size);
        doneButton.setEnabled(false);
    }

    private void setupPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3,3,3,3);
        gbc.gridx = 0; gbc.gridy = 0;
        super.add(rollButton, gbc);
        gbc.gridx++;
        super.add(questionButton, gbc);
        gbc.gridx++;
        super.add(accuseButton, gbc);
        gbc.gridx++;
        super.add(doneButton, gbc);

        setSize(getPreferredSize());
    }

    public T11Label getAccuseButton() {
        return accuseButton;
    }

    public T11Label getDoneButton() {
        return doneButton;
    }

    public T11Label getQuestionButton() {
        return questionButton;
    }

    public T11Label getRollButton() {
        return rollButton;
    }
}
