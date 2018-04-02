package com.team11.cluedo.questioning;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class QuestionListener implements KeyListener {

    private final QuestionPanel panel;

    public QuestionListener(QuestionPanel panel){
        this.panel = panel;
    }

    @Override
    public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            System.out.println("Right");
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT){
            System.out.println("left");
        }
    }

    @Override
    public void keyTyped(KeyEvent e){}

    @Override
    public void keyReleased(KeyEvent e){}
}
