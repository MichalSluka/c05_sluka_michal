package rasterops;

import rasterdata.Raster;

public class LineRasterizerTrivial implements LineRasterizer {

    @Override
    public void draw(Raster raster, int c1, int r1, int c2, int r2, int color) {
        if (c1 == c2 && r1 == r2) {
            raster.setColor(c1, r1, color);
            return;
        }

        if (c2 - c1 == 0) {
            if (r1 > r2) {
                int tmpR = r1;
                r1 = r2;
                r2 = tmpR;
            }
            for (int r = r1; r <= r2; r++) {
                raster.setColor(c1, r, color);
            }
            return;
        }

        final double k = (double) (r2 - r1) / (c2 - c1);
        final double q = r1 - k * c1;

        if (Math.abs(k) <= 1) {

            if (c1 > c2) {
                int tmpC = c1; c1 = c2; c2 = tmpC;
                int tmpR = r1; r1 = r2; r2 = tmpR;
            }
            for (int c = c1; c <= c2; c++) {
                final int r = (int) Math.round(k * c + q);
                raster.setColor(c, r, color);
            }
        } else {

            if (r1 > r2) {
                int tmpC = c1; c1 = c2; c2 = tmpC;
                int tmpR = r1; r1 = r2; r2 = tmpR;
            }
            for (int r = r1; r <= r2; r++) {
                final int c = (int) Math.round((r - q) / k);
                raster.setColor(c, r, color);
            }
        }
    }
}