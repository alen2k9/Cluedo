/*
 * Code to handle the dice and rolling of dice.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.components;

import com.team11.cluedo.assets.Assets;
import com.team11.cluedo.board.Board;
import com.team11.cluedo.ui.Resolution;

import java.awt.*;
import javax.swing.*;
import java.util.Random;

public class Dice extends JComponent {
    private boolean doAnimation = false;
    private Die leftDice;
    private Die rightDice;

    private Assets gameAssets;
    private double resolutionScalar;

    public Dice(Assets gameAssets, Resolution resolution) {
        this.gameAssets = gameAssets;
        this.resolutionScalar = resolution.getScalePercentage();
        this.leftDice = new Die();
        this.rightDice = new Die();

        int x = (int)(Board.TILE_SIZE * resolutionScalar);
        int y = (int)(5 * resolutionScalar);
        int spacing = (int)(10 * resolutionScalar);

        leftDice.setBounds(x, y, leftDice.getPreferredSize().width, leftDice.getPreferredSize().height);
        rightDice.setBounds(x + leftDice.getPreferredSize().width + spacing, y,
                leftDice.getPreferredSize().width, leftDice.getPreferredSize().height);

        super.add(leftDice);
        super.add(rightDice);
    }

    public int rollDice() {
        if (doAnimation) {
            int leftValue = leftDice.roll(), rightValue = rightDice.roll();
            new DiceAnimation(leftDice, rightDice, leftValue, rightValue, getSize()).execute();
            leftDice.setRoll(leftValue); rightDice.setRoll(rightValue);
            return leftDice.getValue() + rightDice.getValue();
        } else {
            return leftDice.roll() + rightDice.roll();
        }
    }

    public void setDoAnimation(boolean doAnimation) {
        this.doAnimation = doAnimation;
    }

    private class Die extends T11Label {
        private int value;
        private final int initialPreferredSize = (int)(48*resolutionScalar);

        private Random random = new Random();

        public Die() {
            setPreferredSize(new Dimension(initialPreferredSize,initialPreferredSize));
            roll();
        }

        public int roll() {
            this.value = random.nextInt(6) + 1; // Range 1-6
            setIcon();
            return value;
        }
        
        public void setRoll(int value) {
            this.value = value;
            setIcon();
        }
        
        private void setIcon(){
            switch (value) {
                case 1:
                    super.setIcon(new ImageIcon(gameAssets.getDice1()));
                    break;
                case 2:
                    super.setIcon(new ImageIcon(gameAssets.getDice2()));
                    break;
                case 3:
                    super.setIcon(new ImageIcon(gameAssets.getDice3()));
                    break;
                case 4:
                    super.setIcon(new ImageIcon(gameAssets.getDice4()));
                    break;
                case 5:
                    super.setIcon(new ImageIcon(gameAssets.getDice5()));
                    break;
                case 6:
                    super.setIcon(new ImageIcon(gameAssets.getDice6()));
                    break;
            }
        }

        public int getValue() {
            return value;
        }
    }

    private class DiceAnimation extends SwingWorker<Integer, String> {
        private Die leftDice, rightDice;
        private int leftValue, rightValue;
        private Dimension dimension;

        private int initialX, initialY;
        private int targetX, targetY, spacing;

        public DiceAnimation(Die leftDice, Die rightDice, int leftValue, int rightValue, Dimension dimension) {
            this.leftDice = leftDice;
            this.rightDice = rightDice;
            this.leftValue = leftValue;
            this.rightValue = rightValue;
            this.dimension = dimension;

            this.targetX = (int)(Board.TILE_SIZE * resolutionScalar);
            this.targetY = (int)(5 * resolutionScalar);
            this.spacing = (int)(10 * resolutionScalar);
        }

        @Override
        protected Integer doInBackground() throws Exception {
            getInitialPosition();
            leftDice.setLocation(initialX, initialY);
            rightDice.setLocation(initialX+leftDice.getPreferredSize().width+(int)(40*resolutionScalar), initialY);

            int count = 10;
            while (count > 0) {
                Thread.sleep(100 - (9*count--));
                leftDice.roll();
                rightDice.roll();
                leftDice.setLocation(leftDice.getX()-leftValue*2,
                        leftDice.getY()-(int)((15+count*5+leftValue*2)*resolutionScalar));
                rightDice.setLocation(rightDice.getX()+rightValue*2,
                        rightDice.getY()-(int)((15+count*5+rightValue*2)*resolutionScalar));
            }
            leftDice.setRoll(leftValue);
            rightDice.setRoll(rightValue);

            Thread.sleep(500);

            int size = (int)(48 * resolutionScalar);
            Dimension targetSize = new Dimension(size, size);
            int leftTargetX = targetX, rightTargetX = targetX + size + spacing;
            int leftMoveX, leftMoveY, rightMoveX, rightMoveY, divisor = (int)(20*resolutionScalar);

            leftMoveX = (int)(((leftTargetX - leftDice.getX())/divisor)*resolutionScalar);
            leftMoveY = (int)(((targetY - leftDice.getY())/divisor)*resolutionScalar);
            rightMoveX = (int)(((rightTargetX - rightDice.getX())/divisor)*resolutionScalar);
            rightMoveY = (int)(((targetY - rightDice.getY())/divisor)*resolutionScalar);

            while(leftDice.getX() != leftTargetX || leftDice.getY() != targetY ||
                    rightDice.getX() != rightTargetX || rightDice.getY() != targetY) {
                if (leftDice.getPreferredSize().height > targetSize.height && rightDice.getPreferredSize().height > targetSize.height) {
                    int sizeScalar = (int)(4 * resolutionScalar);
                    Dimension newSize = new Dimension(leftDice.getSize().width - sizeScalar,
                            leftDice.getSize().height - sizeScalar);
                    leftDice.setPreferredSize(newSize);
                    leftDice.setSize(newSize);
                    rightDice.setPreferredSize(newSize);
                    rightDice.setSize(newSize);
                }
                if (leftDice.getX() > leftTargetX || leftDice.getY() > targetY)
                    leftDice.setLocation(leftDice.getX()+leftMoveX, leftDice.getY()+leftMoveY);
                if (leftDice.getX() < leftTargetX || leftDice.getY() < targetY)
                    leftDice.setLocation(leftTargetX, targetY);
                if (rightDice.getX() > rightTargetX || rightDice.getY() > targetY)
                    rightDice.setLocation(rightDice.getX()+rightMoveX, rightDice.getY()+rightMoveY);
                if (rightDice.getX() < rightTargetX || rightDice.getY() < targetY)
                    rightDice.setLocation(rightTargetX, targetY);

                Thread.sleep(15);
            }

            return null;
        }

        private void getInitialPosition() {
            leftDice.setPreferredSize(new Dimension((int)(80*resolutionScalar),(int)(80*resolutionScalar)));
            rightDice.setPreferredSize(new Dimension((int)(80*resolutionScalar),(int)(80*resolutionScalar)));

            leftDice.setSize(leftDice.getPreferredSize().width, leftDice.getPreferredSize().height);
            rightDice.setSize(rightDice.getPreferredSize().width, rightDice.getPreferredSize().height);

            int width = (leftDice.getPreferredSize().width*2 + (int)(40*resolutionScalar)),
                    height = leftDice.getPreferredSize().height;
            initialX = (int)(dimension.getWidth()/2 - width/2);
            initialY = (int)(dimension.getHeight()/2 + height*3);
        }
    }
}
