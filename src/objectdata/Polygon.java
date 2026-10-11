package objectdata;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private final List<Point> points;

    public Polygon() {
        points = List.of();
    }

    private Polygon(final List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public Polygon withPoint(final Point point) {
        final List<Point> newPoints = new ArrayList<>(this.points);
        newPoints.add(point);
        return new Polygon(newPoints);
    }

    public Polygon withMovedPoint(final int index, final Point newPoint) {
        if (index < 0 || index >= points.size()) return this;
        final List<Point> newPoints = new ArrayList<>(this.points);
        newPoints.set(index, newPoint);
        return new Polygon(newPoints);
    }

    public Polygon withPointInsertedAfter(final int index, final Point newPoint) {
        if (points.size() < 2 || index < 0 || index >= points.size()) {
            return withPoint(newPoint);
        }
        final List<Point> newPoints = new ArrayList<>(this.points);
        newPoints.add(index + 1, newPoint);
        return new Polygon(newPoints);
    }

    public int findClosestPointIndex(final Point mouse) {
        int closest = -1;
        double minDist = Double.MAX_VALUE;
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            double dist = Math.hypot(mouse.x() - p.x(), mouse.y() - p.y());
            if (dist < minDist) {
                minDist = dist;
                closest = i;
            }
        }
        return closest;
    }

    public int findClosestEdgeIndex(final Point mouse) {
        if (points.size() < 2) return -1;
        int closestEdge = 0;
        double minDist = Double.MAX_VALUE;
        for (int i = 0; i < points.size(); i++) {
            Point a = points.get(i);
            Point b = points.get((i + 1) % points.size());
            double midX = (a.x() + b.x()) / 2.0;
            double midY = (a.y() + b.y()) / 2.0;
            double dist = Math.hypot(mouse.x() - midX, mouse.y() - midY);
            if (dist < minDist) {
                minDist = dist;
                closestEdge = i;
            }
        }
        return closestEdge;
    }

    public List<LineSegment> getLineSegments() {
        final List<LineSegment> lineSegments = new ArrayList<>();
        if (points.size() < 2) return lineSegments;
        for (int i = 0; i < points.size(); i++) {
            final Point p1 = points.get(i);
            final Point p2 = points.get((i + 1) % points.size());
            lineSegments.add(new LineSegment(p1, p2));
        }
        return lineSegments;
    }
}