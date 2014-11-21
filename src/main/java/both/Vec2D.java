package both;

/**
 * Created by mar on 21.11.14.
 */
public class Vec2D {
    public float x, y;

    public Vec2D() {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    public Vec2D( float x, float y ) {
        this.x = x;
        this.y = y;
    }

    public Vec2D( Vec2D v ) {
        this.x = v.x;
        this.y = v.y;
    }

    public boolean equalsWithTolerance( Vec2D v, float tolerance ) {
        if ( Math.abs( x - v.x ) < tolerance ) {
            if ( Math.abs( y - v.y ) < tolerance ) {
                return true;
            }
        }
        return false;
    }

    public final float distanceTo( Vec2D v ) {
        if ( v != null ) {
            float dx = x - v.x;
            float dy = y - v.y;
            return ( float ) Math.sqrt( dx * dx + dy * dy );
        } else {
            return Float.NaN;
        }
    }

    public final Vec2D add( float a, float b ) {
        return new Vec2D( x + a, y + b );
    }

    public final Vec2D add( Vec2D v ) {
        return new Vec2D( x + v.x, y + v.y );
    }

    public final Vec2D sub(float a, float b) {
        return new Vec2D(x - a, y - b);
    }

    public final Vec2D sub(Vec2D v) {
        return new Vec2D(x - v.x, y - v.y);
    }
}
