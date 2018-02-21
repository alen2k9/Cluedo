package com.team11.cluedo.ui;

import java.awt.*;

public class Resolution {
    private Dimension screenSize;
    private Double scalePercentage;

    public Resolution() {
        this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.scalePercentage = ((double)this.screenSize.height / (double)1080);
        if (this.scalePercentage > 1) {
            this.scalePercentage = 1d;
        }
    }

    public Dimension getScreenSize() {
        return screenSize;
    }

    public Double getScalePercentage() {
        return scalePercentage;
    }
}
