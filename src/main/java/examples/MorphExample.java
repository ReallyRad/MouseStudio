package examples;

import both.MousePath;
import processing.core.PApplet;
import reading.MouseMovement;
import toxi.geom.Vec2D;

import java.util.ArrayList;

/**
 * Created by mar on 13.11.14.
 */
public class MorphExample extends PApplet {

    private MouseMovement mm;
    private int currentPath;
    private float currentMorphPercentage;

    public static void main ( String[] args ) {
        PApplet.main( new String[]{ "examples.MorphExample" } );
    }

    public void init() {
        frame.removeNotify();
        frame.setUndecorated( true );
        frame.setResizable( true );
        frame.addNotify();
        super.init();
    }

    public void setup() {
        size( displayWidth, displayHeight, P2D );

        currentPath = 1;

        mm = new MouseMovement( this, "saved", 30 );
        mm.start();
        mm.setResolution( displayWidth, displayHeight );
        mm.setPathById( currentPath );

        System.out.println( "Duration: " + mm.getCurrentPath().getDuration() );
    }

    public void draw() {
        background( 0 );
        stroke( 255 );

        Vec2D debugStart = new Vec2D( width - 300, height / 2 - 200 );
        Vec2D debugEnd = new Vec2D( 300, height / 2 + 200 );
        ArrayList< Vec2D > mapped = mm.getCurrentPath().getPositionsMapped( debugStart , debugEnd );
        fill( 255, 0, 0 );
        for( Vec2D p : mapped ) {
            //ellipse( p.x, p.y, 5, 5 );
        }

        ellipse( mm.getCurrentPath().getPositionMapped( mm.getCurrentPath().getPosition(), debugStart, debugEnd ).x, mm.getCurrentPath().getPositionMapped( mm.getCurrentPath().getPosition(), debugStart, debugEnd ).y, 20, 20 );

        noStroke();
        ArrayList< Vec2D > morped = mm.getCurrentPath().getMorphed( mm.getPathFromId( 7 ), map(mouseX, 0, width, 0, 1 ) );
        for( Vec2D v : morped ) {
            ellipse( v.x, v.y, 30, 30 );
        }

        fill( 0, 0, 255 );
        //Vec2D gg = mm.getCurrentPath().getMorphed( mm.getPathFromId( 7 ), mm.getCurrentPath().getProgress() );
        //ellipse(gg.x, gg.y, 20, 20);

        mm.setPathById( 7 );
        drawCurrent();
        mm.setPathById( currentPath );
        drawCurrent();
    }

    private void drawCurrent() {
        fill( 255 );
        for( Vec2D p : mm.getCurrentPath().getPoints() ) {
            ellipse( p.x, p.y, 5, 5 );
        }

        fill( 0, 255, 0 );
        ellipse( mm.getCurrentPath().getPosition().x, mm.getCurrentPath().getPosition().y, 30, 30 );
    }

    public void mouseMoved() {
        currentMorphPercentage = map( mouseX, 0, width, 0, 1 );
    }

    public void keyPressed() {
        if( key == '+' ) {
            currentPath++;
            currentPath = min( currentPath, mm.getPathCount() );
        }
        if( key == '-' ) {
            currentPath--;
            currentPath = max( 0, currentPath );

        }
        println( "path now " + currentPath );
    }
}
