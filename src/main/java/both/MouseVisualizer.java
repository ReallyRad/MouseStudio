package both;

import processing.core.PApplet;
import toxi.geom.Vec3D;

import java.util.ArrayList;

/**
 * Created by mar on 15.11.14.
 */
public class MouseVisualizer {
    private PApplet p;

    public MouseVisualizer( PApplet _p ) {
        this.p = _p;
    }

    public void drawCurvedPaths( ArrayList< MousePath > paths ) {
        for( MousePath p : paths ) {
            this.drawCurvedPath( p );
        }
    }

    public void drawCoordinateSystem() {
        int length = 2000;

        p.pushStyle();
        p.strokeWeight( 3 );
        p.stroke( 255, 0, 0 );
        p.line( 0, 0, 0, length, 0, 0 );
        p.stroke( 0, 255, 0 );
        p.line( 0, 0, 0, 0, length, 0 );
        p.stroke( 0, 0, 255 );
        p.line( 0, 0, 0, 0, 0, length );

        int steps = 10;
        int stepLength = length / steps;
        for( int i = 0; i < steps; i++ ) {
            p.pushMatrix();
            p.translate( i * stepLength, 0, 0 );
            p.text( i * stepLength + "", 0, 0 );
            p.popMatrix();

            p.pushMatrix();
            p.translate( 0, i * stepLength, 0 );
            p.rotateZ( PApplet.radians( 90 ) );
            p.text( i * stepLength + "", 0, 0 );
            p.popMatrix();

            p.pushMatrix();

            p.translate( 0, 0, i * stepLength );
            //p.rotateX( PApplet.radians( 90 ) );
            p.rotateY( PApplet.radians( 90 ) );
            p.text( i * stepLength + "", 0, 0 );
            p.popMatrix();
        }
        p.popStyle();

    }

    public void drawCurvedPath( MousePath path ) {
        p.pushStyle();
        p.noFill();

        p.beginShape();
        p.curveVertex( path.getStartPos().x, path.getStartPos().y );
        for( Vec2D v : path.getPoints() ) {
            p.curveVertex( v.x, v.y );
        }
        p.curveVertex( path.getEndPos().x, path.getEndPos().y );
        p.endShape();

        p.popStyle();
    }

    public void drawRawPath( MousePath path ) {
        p.pushStyle();
        p.noFill();

        p.beginShape();
        p.vertex( path.getStartPos().x, path.getStartPos().y );
        for( Vec2D v : path.getPoints() ) {
            p.vertex( v.x, v.y );
        }
        p.vertex( path.getEndPos().x, path.getEndPos().y );
        p.endShape();

        p.popStyle();
    }

    public void drawRawPath( MousePath3D path ) {
        p.pushStyle();
        p.noFill();

        p.beginShape();
        p.vertex( path.getStartPos().x, path.getStartPos().y, path.getStartPos().z );
        for( Vec3D v : path.getPoints() ) {
            p.vertex( v.x, v.y, v.z );
        }
        p.vertex( path.getEndPos().x, path.getEndPos().y, path.getEndPos().z );
        p.endShape();

        p.popStyle();
    }

    public void draw( MousePath path ) {
        p.pushStyle();
        p.noStroke();
        p.ellipse( path.getCurrentPosition().x, path.getCurrentPosition().y, 20, 20 );
        p.popStyle();
    }

    public void drawPaths( ArrayList< MousePath > paths ) {
        for( MousePath p : paths ) {
                this.drawRawPath( p );
        }
    }

    public void drawPointList( ArrayList< Vec2D > points ) {
        p.pushStyle();
        p.noFill();

        p.beginShape();
        p.curveVertex( points.get( 0 ).x, points.get( 0 ).y, 0 );
        for( Vec2D v : points ) {
            p.curveVertex( v.x, v.y, 0 );
        }
        p.curveVertex( points.get( points.size() - 1 ).x, points.get( points.size() - 1 ).y, 0 );
        p.endShape();

        p.popStyle();
    }

    public void drawPointList3D( ArrayList< Vec3D > points ) {
        p.pushStyle();
        p.noFill();

        p.beginShape();
        p.curveVertex( points.get( 0 ).x, points.get( 0 ).y, points.get( 0 ).z );
        for( Vec3D v : points ) {
            p.curveVertex( v.x, v.y, v.z );
        }
        p.curveVertex( points.get( points.size() - 1 ).x, points.get( points.size() - 1 ).y, points.get( points.size() - 1 ).z );
        p.endShape();

        p.popStyle();
    }

}
