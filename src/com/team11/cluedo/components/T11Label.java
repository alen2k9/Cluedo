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
    public T11Label(ImageIcon icon){
        super(icon);
    }

    public T11Label() {
        super();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        ImageIcon image = ((ImageIcon)super.getIcon());
        g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), this);
    }
}
