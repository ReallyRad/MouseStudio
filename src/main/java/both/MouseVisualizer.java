package both;

import processing.core.PApplet;
import toxi.geom.Vec2D;

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
            this.dawCurvedPath( p );
        }
    }

    public void dawCurvedPath( MousePath path ) {
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

    public void drawPaths( ArrayList< MousePath > paths ) {
        for( MousePath p : paths ) {
            this.drawRawPath( p );
        }
    }
}
