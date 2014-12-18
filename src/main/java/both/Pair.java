package both;

import processing.core.PApplet;

public class Pair {
    private long millis;
    private Vec2D point;
    private Vec2D resolution;

    public Pair ( long _d, Vec2D _p ) {
        this.millis = _d;
        this.point = _p;
        this.resolution = new Vec2D( 1920, 1080 );
    }

    public long getTime() {
        return millis;
    }

    public Vec2D getPosition () {
        Vec2D returnPoint = new Vec2D( point );
        returnPoint.x = PApplet.map( point.x, 0, 1920, 0, resolution.x );
        returnPoint.y = PApplet.map( point.y, 0, 1080, 0, resolution.y );
        return returnPoint;
    }

    public void setPosition( Vec2D _p ) {
        this.point = _p;
    }

    public void setResolution( Vec2D _res ) {
        this.resolution = _res;
    }
}