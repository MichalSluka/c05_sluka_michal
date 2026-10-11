package rasterops;

import rasterdata.Raster;

/**
 * Represents an algorithm for drawing straight line segments.
 */
public interface LineRasterizer {

    /**
     * Draws a straight line segment into the given Raster using the given color.
     * Line segment is defined by 2 pixel addresses.
     * @param raster Raster to draw onto
     * @param c1 column address of the starting pixel
     * @param r1 row address of the starting pixel
     * @param c2 column address of the ending pixel
     * @param r2 row address of the ending pixel
     * @param color color of the line
     */
    void draw(Raster raster, int c1, int r1, int c2, int r2, int color);
}
