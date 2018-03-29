package com.team11.cluedo.questioning;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class QuestionMouseListener implements MouseListener {

    private final JComponent component;

    public QuestionMouseListener(JComponent component){
        this.component = component;
    }

    @Override
    public void mouseClicked(MouseEvent e){
        this.component.requestFocus();
    }

    @Override
    public void mousePressed(MouseEvent e){}
    @Override
    public void mouseReleased(MouseEvent e){}
    @Override
    public void mouseEntered(MouseEvent e){}
    @Override
    public void mouseExited(MouseEvent e){}
}
