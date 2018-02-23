/*
 * Resolution Class to scale (better, but not perfectly) on resolutions < 1080p.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.ui;

import java.awt.*;
import java.text.DecimalFormat;

public class Resolution {
    private Dimension screenSize;
    private Double scalePercentage;

    public Resolution() {
        this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.scalePercentage = ((double)this.screenSize.height / getAspectRatio((double)this.getScreenSize().width / (double)this.screenSize.height));
        if (this.scalePercentage > 1) {
            this.scalePercentage = 1d;
        }
    }

    private double getAspectRatio(double ratio) {
        ratio = Double.valueOf(new DecimalFormat("#.###").format(ratio));
        if (ratio == 1.778) {
            return 1080;
        } else if (ratio == 1.6) {
            return 1200;
        } else if (ratio == 1.25){
            return 1536;
        } else if (ratio == 1.779) {
            return 1152;
        } else {
            return 1080;
        }
    }

    public Dimension getScreenSize() {
        return screenSize;
    }

    public Double getScalePercentage() {
        return scalePercentage;
    }
}
