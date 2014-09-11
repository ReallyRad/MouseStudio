package reading;

import processing.core.PApplet;

import java.util.ArrayList;

/**
 * Created by Marcel on 16.08.2014.
 */
public class SquareMouseExample extends PApplet {

    private MouseMovement mouse;
    ArrayList< Rect > rects;

    /*
    1. Punkte zur Delaunay Triangulatio nutzen
    2. Interpolation zeischen zwei Punkten bei getPoint(), damit langsamer playback nicht 'ruckelt'
    3. refactoring
     */
    public void setup () {
        size( 1920, 1080 );
        mouse = new MouseMovement( this, "saved", 20 );
        mouse.start();

        rects = new ArrayList<>();
        int count = 20;
        for ( int i = 0; i < count; i++ ) {
            rects.add( new Rect( this, color( 255, 255 / count * i ) ) );
        }
    }

    public void draw () {
        mouse.setSpeed( map( mouseX, 0, width, 0.1f, 2 ) );

        background( 15 );
        for ( int i = 0; i < rects.size(); i += 4 ) {
            Rect r = rects.get( i );
            //r.setPos( mouse.getPositionFromPath( i ) );
            //r.setSize( mouse.getPositionFromPath( i + rects.size() ).sub( mouse.getPositionFromPath(  i ) ) );
            r.setCorner( reading.CORNER.TOP_RIGHT, mouse.getPositionFromPath( i ) );
            r.setCorner( reading.CORNER.RIGHT_BOTTOM, mouse.getPositionFromPath( i + 1 ) );
            r.setCorner( reading.CORNER.BOTTOM_LEFT, mouse.getPositionFromPath( i + 2 ) );
            r.setCorner( reading.CORNER.LEFT_TOP, mouse.getPositionFromPath( i + 3 ) );
            r.draw();
        }

    }

    public static void main ( String[] args ) {
        PApplet.main( new String[]{ "reading.SquareMouseExample" } );
    }
}
