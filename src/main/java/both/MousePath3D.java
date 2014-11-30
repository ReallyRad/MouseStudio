package both;

import toxi.geom.Vec3D;

import java.util.ArrayList;

/**
 * Created by mar on 24.11.14.
 */
public class MousePath3D {
    private ArrayList< Vec3D > points;

    public MousePath3D() {
        points = new ArrayList<>(  );
    }

    public void add( Vec3D p ) {
        points.add( p );
    }

    public ArrayList< Vec3D > getPoints() {
        return points;
    }

    public Vec3D getStartPos() {
        return points.get( 0 );
    }

    public Vec3D getEndPos() {
        return points.get( points.size() - 1 );
    }
}
