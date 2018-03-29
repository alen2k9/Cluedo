/*
 * Code to handle the creation of the murder envelope.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.cards;

import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MurderEnvelope extends JComponent {
    private SuspectCard suspectCard;
    private RoomCard roomCard;
    private WeaponCard weaponCard;

    private double resolutionScalar;

    public MurderEnvelope(SuspectCard suspectCard, RoomCard roomCard, WeaponCard weaponCard, Resolution resolution) {
        this.suspectCard = suspectCard;
        this.roomCard = roomCard;
        this.weaponCard = weaponCard;
        this.resolutionScalar = resolution.getScalePercentage();
        createMurderEnvelope();
    }

    public void displayMurderEnvelope() {
        this.setVisible(true);
    }

    public void createMurderEnvelope() {
        ImageIcon card = new ImageIcon(weaponCard.getCardImage());
        int cardWidth = (int)(card.getIconWidth() * resolutionScalar * .80);
        int cardHeight = (int)(card.getIconHeight() * resolutionScalar * .80);

        JLabel weapon = new JLabel(new ImageIcon(weaponCard.getCardImage().getScaledInstance(cardWidth,cardHeight, 0))),
        suspect = new JLabel(new ImageIcon(suspectCard.getCardImage().getScaledInstance(cardWidth,cardHeight, 0))),
        room = new JLabel(new ImageIcon(roomCard.getCardImage().getScaledInstance(cardWidth,cardHeight, 0)));

        weapon.setBorder(new LineBorder(Color.BLACK, 2));
        room.setBorder(new LineBorder(Color.BLACK, 2));
        suspect.setBorder(new LineBorder(Color.BLACK, 2));

        add(weapon);
        add(suspect);
        add(room);

        int x = (int)(30 * resolutionScalar), y = 0, space = (int)(10 * resolutionScalar);
        weapon.setLocation(new Point(x, y));
        suspect.setLocation(new Point(x + cardWidth + space, y));
        room.setLocation(new Point(x + 2*(cardWidth + space), y));

        weapon.setSize(cardWidth, cardHeight);
        suspect.setSize(cardWidth, cardHeight);
        room.setSize(cardWidth, cardHeight);

        int width = (x*2) + (cardWidth*3) + (space*2);
        super.setSize(new Dimension(width, cardHeight));

        this.setVisible(false);
    }

    public void disposeMurderEnvelope() {
        this.setVisible(false);
    }

    public SuspectCard getSuspectCard() {
        return suspectCard;
    }

    public RoomCard getRoomCard() {
        return roomCard;
    }

    public WeaponCard getWeaponCard() {
        return weaponCard;
    }
}
