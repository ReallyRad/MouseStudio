package both;

import processing.core.PApplet;
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

    public Vec3D getPositionMapped( Vec3D currentOriginalPoint, Vec3D newStartPoint, Vec3D newEndPoint ) {
        Vec3D originalStart = getStartPos();
        Vec3D originalEnd = getEndPos();

        float newX = PApplet.map( currentOriginalPoint.x, originalStart.x, originalEnd.x, newStartPoint.x, newEndPoint.x );
        float newY = PApplet.map( currentOriginalPoint.y, originalStart.y, originalEnd.y, newStartPoint.y, newEndPoint.y );
        float newZ = PApplet.map( currentOriginalPoint.z, originalStart.z, originalEnd.z, newStartPoint.z, newEndPoint.z );

        return new Vec3D( ( int ) newX, ( int ) newY, ( int ) newZ );
    }

    public ArrayList< Vec3D > getPositionsMapped( Vec3D artificialStart, Vec3D artificialEnd ) {
        ArrayList< Vec3D > returnPoints = new ArrayList<>();

        for ( Vec3D p : getPoints() ) {
            Vec3D mapped = getPositionMapped( p, artificialStart, artificialEnd );
            returnPoints.add( mapped );
        }

        return returnPoints;
    }

    public Vec3D getCentroid() {
        Vec3D returnPoint = new Vec3D();

        for( Vec3D v : getPoints() ) {
            returnPoint.addSelf( v );
        }

        returnPoint.x /= getPoints().size();
        returnPoint.y /= getPoints().size();
        returnPoint.z /= getPoints().size();

        return returnPoint;
    }
}
