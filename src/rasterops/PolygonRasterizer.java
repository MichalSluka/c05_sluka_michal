package rasterops;

import objectdata.LineSegment;
import objectdata.Polygon;
import rasterdata.Raster;

public class PolygonRasterizer {

    public void draw(final Raster raster,
                     final Polygon polygon,
                     final LineRasterizer lineRasterizer,
                     final int color) {
        for (final LineSegment segment : polygon.getLineSegments()) {
            lineRasterizer.draw(
                    raster,
                    segment.p1().x(),
                    segment.p1().y(),
                    segment.p2().x(),
                    segment.p2().y(),
                    color
            );
        }
    }
}
