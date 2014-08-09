package both;

import java.awt.Point;

public class Pair {
    private long millis;
    private Point point;

    public Pair ( long _d, Point _p ) {
        this.millis = _d;
        this.point = _p;
    }

    public long getDate () {
        return millis;
    }

    public Point getPoint () {
        return point;
    }
}