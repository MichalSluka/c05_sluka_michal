package rasterdata;

import java.util.Optional;

/**
 * Represent a two-dimensional grid containing addressable pixels.
 * Each pixel is represented as 32-bit integers
 */
public interface Raster {

    /**
     * Returns the number of columns in this Raster
     * @return number of columns
     */
    int getWidth();

    /**
     * Returns the number of rows in this Raster
     * @return number of rows
     */
    int getHeight();

    /**
     * Replaces the color at the specified address with the given color
     * if the given pixel address is valid.
     * @param c column address
     * @param r row address
     * @param color new color
     */
    void setColor(int c, int r, int color);

    /**
     * Returns an Optional of the color at the specified pixel address
     * @param c column address
     * @param r row address
     * @return Optional of color if the given address is valid; empty Optional otherwise
     */
    Optional<Integer> getColor(int c, int r);

    /**
     * Tests weather the given pixel address is valid for this Raster.
     * @param c column address
     * @param r row address
     * @return true in case of a valid address
     */
    default boolean isValid(int c, int r) {
        return
                (c >= 0 && c < getWidth() && r >= 0 && r < getHeight());
    }

}
