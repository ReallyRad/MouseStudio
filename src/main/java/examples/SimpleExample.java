package examples;

import both.MouseVisualizer;
import processing.core.PApplet;
import reading.MouseMovement;
import toxi.geom.Vec2D;

/**
 * Created by mar on 15.11.14.
 */
public class SimpleExample extends PApplet {

   private MouseMovement mm;
    private MouseVisualizer mv;

    public void setup() {
        size( 1920, 1080 );

        mm = new MouseMovement( this, "saved", 20 );
        //mm.filterByDuration( 0, 1000 );

        mm.setSpeed( 1.0f );
        mm.start();

        mv = new MouseVisualizer( this );

        noStroke();
    }

    public void draw() {
        background( 0 );
        fill( 255 );

        Vec2D current = mm.getCurrentPath().getPoints().get( ( int )map( mouseX, 0, width, 0, mm.getCurrentPath().getPoints().size() ) );
        Vec2D experimental = mm.getCurrentPath().getPosition( map( mouseX, 0, width, 0, 1 ) );
        ellipse( current.x, current.y, 20, 20 );
        ellipse( experimental.x, experimental.y, 20, 20 );

        for( Vec2D v : mm.getCurrentPath().getPoints() ) {
            ellipse( v.x, v.y, 5, 5 );
        }

        fill( 255, 0, 0 );
        ellipse( mm.getCurrentPath().getPosition().x, mm.getCurrentPath().getPosition().y, 5, 5 );

        mv.drawPathCurved( mm.getCurrentPath() );
    }

    public void keyPressed () {
        switch ( key ) {
            case 'n':
                mm.nextPath();
                break;
        }
    }

    public static void main( String[] args ) {
        PApplet.main( new String[] { "examples.SimpleExample" } );
    }
}
