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

    public void addSelf( Vec2D v ) {
        this.x += v.x;
        this.y += v.y;
    }

    public final Vec2D sub(float a, float b) {
        return new Vec2D(x - a, y - b);
    }

    public final Vec2D sub(Vec2D v) {
        return new Vec2D(x - v.x, y - v.y);
    }

    public void subSelf( Vec2D v ) {
        x -= v.x;
        y -= v.y;
    }

    public void divSelf( float fac ) {
        this.x /= fac;
        this.y /= fac;
    }

    public Vec2D div( float fac ) {
        return new Vec2D( this.x / fac, this.y / fac );
    }

    public String toString() {
        return "x: " + x + " y: " + y;
    }

    public void multSelf( Vec2D _v ) {
        this.x *= _v.x;
        this.y *= _v.y;
    }

    public Vec2D mult( Vec2D _v ) {
        return new Vec2D( x * _v.x, y * _v.y );
        //this.x *= _v.x;
        //  this.y *= _v.y;
    }

    public Vec2D rotate(float theta) {
        float co = (float) Math.cos(theta);
        float si = (float) Math.sin(theta);
        float xx = co * x - si * y;
        y = si * x + co * y;
        x = xx;
        return this;
    }

    public final float angleBetween(Vec2D v) {
        return (float) Math.acos(dot(v));
    }

    public final float dot(Vec2D v) {
        return x * v.x + y * v.y;
    }

    public final float magnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public final Vec2D scaleSelf(Vec2D s) {
        x *= s.x;
        y *= s.y;
        return this;
    }

    public final Vec2D scaleSelf(float s) {
        x *= s;
        y *= s;
        return this;
    }
    public final Vec2D normlize() {
        Vec2D returnVec = new Vec2D( x, y );
        float mag = x * x + y * y;
        if (mag > 0) {
            mag = 1f / (float) Math.sqrt(mag);
            returnVec.x *= mag;
            returnVec.y *= mag;
        }
        return returnVec;
    }
}
