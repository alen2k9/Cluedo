/*
 * Custom JLabel to allow resizing of the image icon.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.components;

import javax.swing.*;
import java.awt.*;

public class T11Label extends JLabel {

    String name;
    String id;

    public T11Label(ImageIcon icon){
        super(icon);
        super.setDisabledIcon(icon);
    }

    public T11Label() {
        super();
    }

    public T11Label(ImageIcon icon, String name, String ID){
        super(icon);
        super.setDisabledIcon(icon);
        this.name = name;
        this.id = ID;
    }

    public T11Label(ImageIcon icon, String name){
        super(icon);
        super.setDisabledIcon(icon);
        this.name = name;

    }

    public String getCardName(){
        return this.name;
    }

    public String getID(){
        return this.id;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        ImageIcon image;
        if (super.isEnabled())
            image = ((ImageIcon)super.getIcon());
        else
            image = ((ImageIcon)super.getDisabledIcon());
        g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), this);
    }
}
