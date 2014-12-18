package examples;

import both.MouseMovement;
import both.MousePath;
import both.Vec2D;
import deadpixel.keystone.CornerPinSurface;
import deadpixel.keystone.Keystone;
import processing.core.PApplet;
import processing.core.PGraphics;
import toxi.geom.Rect;

/**
 * Created by mar on 02.12.14.
 */
public class ExampleKeystone extends PApplet {

    private MouseMovement mm;
    private MousePath currentPath;

    Keystone ks;
    CornerPinSurface surface;

    PGraphics offscreen;

    public void init () {
        frame.removeNotify();
        frame.setUndecorated( true );
        frame.setResizable( true );
        frame.addNotify();
        super.init();
    }

    public void setup() {
        size( 1920, 1080, P2D );

        mm = new MouseMovement();
        mm.setDataFolder( "saved" );
        mm.removeDoubleClicks();
        mm.loadRecordings( 1500 );
        mm.filterSelfByTravelDistance( 1000, 100000 );
        mm.filterSelfByDuration( 0, 5000 );
        System.out.println( mm.getPaths().size() );
        currentPath = mm.getSelectedPath();

        ks = new Keystone(this);
        surface = ks.createCornerPinSurface(width, height, 20);

        // We need an offscreen buffer to draw the surface we
        // want projected
        // note that we're matching the resolution of the
        // CornerPinSurface.
        // (The offscreen buffer can be P2D or P3D)
        offscreen = createGraphics(width, height, P2D);
    }

    public void draw() {
        println( frameRate );
        offscreen.beginDraw();
        offscreen.background( 0 );
        offscreen.endDraw();
        mm.update();
        offscreen.strokeWeight( 8 );
        if( frameCount % 2 == 0 ) {
            offscreen.stroke( 255 );
        } else {
            offscreen.stroke( 255 );
        }
        offscreen.pushMatrix();

        drawCurvedPath( currentPath, offscreen );
        offscreen.popMatrix();

        offscreen.fill( 255, 0, 0 );
        draw( currentPath, offscreen );

        background( 0 );
        surface.render(offscreen);

    }

    public void keyPressed () {
        switch ( key ) {
            case 'n':
                calcNext();
                break;
            case 'c':
                ks.toggleCalibration();
                break;
            case 'l':
                ks.load();
                break;
            case 's':
                ks.save();
                break;
        }

        if ( key == CODED ) {
            if ( keyCode == UP ) {
                currentPath.translateSelf( new Vec2D( 0, -10 ) );
            } else if ( keyCode == DOWN ) {
                currentPath.translateSelf( new Vec2D( 0, 10 ) );
            } else if ( keyCode == LEFT ) {
                currentPath.translateSelf( new Vec2D( -10, 0 ) );
            } else if ( keyCode == RIGHT ) {
                currentPath.translateSelf( new Vec2D( 10, 0 ) );
            }
        }
    }

    private void calcNext() {
        mm.selectPath( (int)( random( mm.size() ) ) );
        currentPath = mm.getSelectedPath();
        currentPath.scaleSelf( new Vec2D( 10, 10 ) );
        Vec2D centerOfPath = currentPath.getCentroid();
        Vec2D translationVec = new Vec2D( width / 2, height / 2 ).sub( centerOfPath );
        currentPath.translateSelf( translationVec );
        while( !currentPath.isInBoundingBox( new Rect( 0, 0, width, height ) ) ) {
            centerOfPath = currentPath.getCentroid();
            translationVec = new Vec2D( width / 2, height / 2 ).sub( centerOfPath );
            currentPath.translateSelf( translationVec );
            currentPath.scaleSelf( new Vec2D( 0.9f, 0.9f ) );
        }

    }

    public void drawCurvedPath( MousePath path, PGraphics p ) {
        p.beginDraw();
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
        p.endDraw();
    }

    public void draw( MousePath path, PGraphics p ) {
        p.beginDraw();
        p.pushStyle();
        p.noStroke();
        p.ellipse( path.getCurrentPosition().x, path.getCurrentPosition().y, 30, 30 );
        p.popStyle();
        p.endDraw();
    }

    public static void main( String[] args ) {
        PApplet.main( new String[] { "examples.ExampleKeystone" } );
    }
}
