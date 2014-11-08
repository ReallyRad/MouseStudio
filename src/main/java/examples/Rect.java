package examples;

import processing.core.PApplet;
import processing.core.PConstants;
import toxi.geom.Vec2D;

/**
 * Created by Marcel on 16.08.2014.
 */
public class Rect {
    Vec2D pos;
    Vec2D size;
    PApplet p;
    int color;
    Vec2D topRight, rightBottom, bottomLeft, leftTop;


    public Rect ( PApplet p, int color ) {
        pos = new Vec2D();
        size = new Vec2D();
        this.p = p;
        this.color = color;
        topRight = new Vec2D();
        rightBottom = new Vec2D();
        bottomLeft = new Vec2D();
        leftTop = new Vec2D();
    }

    void draw () {
        p.fill( color );
        p.noStroke();
        //p.rect( pos.x, pos.y, size.x, size.y );

        p.beginShape();

        p.vertex( topRight.x, topRight.y );
        p.vertex( rightBottom.x, rightBottom.y );
        p.vertex( leftTop.x, leftTop.y);
        p.vertex( bottomLeft.x, bottomLeft.y );

        p.endShape( PConstants.CLOSE );
    }

    void setCorner ( CORNER corner, Vec2D pos ) {
        switch ( corner ) {
            case TOP_RIGHT:
                topRight.x = pos.x;
                topRight.y = pos.y;
                break;
            case RIGHT_BOTTOM:
                rightBottom.x = pos.x;
                rightBottom.y = pos.y;
                break;
            case BOTTOM_LEFT:
                bottomLeft.x = pos.x;
                bottomLeft.y = pos.y;
                break;
            case LEFT_TOP:
                leftTop.x = pos.x;
                leftTop.y = pos.y;
                break;
            default:
                break;
        }
    }

    void setPos ( float _x, float _y ) {
        pos.x = _x;
        pos.y = _y;
    }

    void setPos ( Vec2D _v ) {
        pos.x = _v.x;
        pos.y = _v.y;
    }

    void setSize ( float _x, float _y ) {
        size.x = _x;
        size.y = _y;
    }

    void setSize ( Vec2D _v ) {
        size.x = _v.x;
        size.y = _v.y;
    }
}
