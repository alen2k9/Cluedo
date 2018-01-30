package Screen.Panel;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    public static final int SCALED = 0;
    public static final int TILED = 1;

    private Image image;
    private int style = TILED;

    public BackgroundPanel(Image image, int style) {
        setImage(image);
        setStyle(style);
        setLayout(new BorderLayout());
    }

    private void setImage(Image image) {
        this.image = image;
        repaint();
    }

    private void setStyle(int style) {
        this.style = style;
        repaint();
    }

    public void add(JComponent component) {
        add(component, null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(image == null) {
            return;
        }

        switch (style) {
            case SCALED: {
                drawScaled(g);
                break;
            }
            case TILED: {
                drawTiled(g);
                break;
            }
            default: {
                drawScaled(g);
            }
        }
    }

    private void drawScaled(Graphics g) {
        Dimension d = getSize();
        g.drawImage(this.image, 0, 0, d.width, d.height, null);
    }

    private void drawTiled(Graphics g) {
        Dimension d = getSize();
        int width = this.image.getWidth(null);
        int height = this.image.getHeight(null);

        for (int x = 0 ; x < d.width ; x += width) {
            for (int y = 0 ; y < d.height ; y += height) {
                g.drawImage(this.image, x, y, null, null);
            }
        }
    }

}
