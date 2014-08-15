package both;

import toxi.geom.Vec2D;

public class Pair {
    private long millis;
    private Vec2D point;

    public Pair ( long _d, Vec2D _p ) {
        this.millis = _d;
        this.point = _p;
    }

    public long getDate () {
        return millis;
    }

    public Vec2D getPosition () {
        return point;
    }
}