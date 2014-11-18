package both;

import processing.core.PApplet;
import toxi.geom.Vec2D;

/**
 * Created by mar on 15.11.14.
 */
public class MouseVisualizer {
    private PApplet p;

    public MouseVisualizer( PApplet _p ) {
        this.p = _p;
    }

    public void drawPathCurved( MousePath path ) {
        p.pushStyle();
        p.noFill();
        p.stroke( 255 );

        p.beginShape();
        p.curveVertex( path.getStartPos().x, path.getStartPos().y );
        for( Vec2D v : path.getPoints() ) {
            p.curveVertex( v.x, v.y );
        }
        p.curveVertex( path.getEndPos().x, path.getEndPos().y );
        p.endShape();

        p.popStyle();
    }
}
