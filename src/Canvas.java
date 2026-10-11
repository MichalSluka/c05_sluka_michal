import objectdata.LineSegment;
import objectdata.Point;
import objectdata.Polygon;
import rasterdata.RasterBI;
import rasterops.LineRasterizer;
import rasterops.LineRasterizerTrivial;
import rasterops.PolygonRasterizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Canvas {

    private final JFrame frame;
    private final JPanel panel;
    private final RasterBI image;

    private int centerC, centerR;

    private final LineRasterizer lineRasterizer = new LineRasterizerTrivial();
    private final PolygonRasterizer polygonRasterizer = new PolygonRasterizer();

    private final List<LineSegment> lineSegments = new ArrayList<>();
    private Polygon polygon = new Polygon();

    private boolean drawingLines = true;

    private int selectedPointIndex = -1;
    private int targetEdgeIndex = -1;

    public Canvas(final int width, final int height) {
        frame = new JFrame();
        frame.setTitle("PGRF1 Úloha 1 | Režim: ÚSEČKY (M = režim, C = zmazať)");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        image = new RasterBI(width, height);
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                image.present(g);
            }
        };

        panel.setFocusable(true);
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    lineSegments.clear();
                    polygon = new Polygon();
                    image.clear(0);
                    panel.repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_M) {
                    drawingLines = !drawingLines;
                    frame.setTitle("PGRF1 Úloha 1 | Režim: " +
                            (drawingLines ? "ÚSEČKY" : "POLYGÓN") + " (M = režim, C = zmazať)");
                }
            }
        });

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point mouse = new Point(e.getX(), e.getY());

                if (SwingUtilities.isLeftMouseButton(e)) {
                    centerC = e.getX();
                    centerR = e.getY();
                    if (!drawingLines) {
                        targetEdgeIndex = polygon.findClosestEdgeIndex(mouse);
                        image.clear(0);
                        render();
                        drawPolygonRubberBand(e.getX(), e.getY());
                        panel.repaint();
                    }
                } else if (SwingUtilities.isRightMouseButton(e) && !drawingLines) {
                    selectedPointIndex = polygon.findClosestPointIndex(mouse);
                }
                else if (SwingUtilities.isMiddleMouseButton(e) && !drawingLines) {
                    int closest = polygon.findClosestPointIndex(mouse);
                    polygon = polygon.withoutPoint(closest);
                    image.clear(0);
                    render();
                    panel.repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point mouse = new Point(e.getX(), e.getY());

                if (SwingUtilities.isLeftMouseButton(e)) {
                    image.clear(0);
                    render();
                    if (drawingLines) {
                        lineRasterizer.draw(image, centerC, centerR, e.getX(), e.getY(), 0xffff00);
                    } else {
                        drawPolygonRubberBand(e.getX(), e.getY());
                    }
                    panel.repaint();
                } else if (SwingUtilities.isRightMouseButton(e) && !drawingLines && selectedPointIndex != -1) {
                    polygon = polygon.withMovedPoint(selectedPointIndex, mouse);
                    image.clear(0);
                    render();
                    panel.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point mouse = new Point(e.getX(), e.getY());

                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (drawingLines) {
                        lineSegments.add(new LineSegment(new Point(centerC, centerR), mouse));
                    } else {
                        polygon = polygon.withPointInsertedAfter(targetEdgeIndex, mouse);
                        targetEdgeIndex = -1;
                    }
                    image.clear(0);
                    render();
                    panel.repaint();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    selectedPointIndex = -1;
                }
            }
        };

        panel.addMouseListener(mouseAdapter);
        panel.addMouseMotionListener(mouseAdapter);

        panel.setPreferredSize(new Dimension(width, height));
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        panel.requestFocusInWindow();
    }

    private void drawPolygonRubberBand(int mouseX, int mouseY) {
        List<Point> pts = polygon.getPoints();
        if (pts.isEmpty()) return;

        if (pts.size() == 1) {
            lineRasterizer.draw(image, pts.get(0).x(), pts.get(0).y(), mouseX, mouseY, 0xffff00);
        } else {
            int i1 = (targetEdgeIndex != -1) ? targetEdgeIndex : pts.size() - 1;
            int i2 = (i1 + 1) % pts.size();
            lineRasterizer.draw(image, pts.get(i1).x(), pts.get(i1).y(), mouseX, mouseY, 0xffff00);
            lineRasterizer.draw(image, pts.get(i2).x(), pts.get(i2).y(), mouseX, mouseY, 0xffff00);
        }
    }

    private void render() {
        for (final LineSegment segment : lineSegments) {
            lineRasterizer.draw(image, segment.p1().x(), segment.p1().y(), segment.p2().x(), segment.p2().y(), 0xff0000);
        }
        polygonRasterizer.draw(image, polygon, lineRasterizer, 0x00ff00);
    }

    static void main() {
        SwingUtilities.invokeLater(() -> new Canvas(500, 500));
    }
}