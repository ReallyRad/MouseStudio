package reading;

import processing.core.PApplet;

import java.awt.*;

/**
 * Created by Marcel on 07.08.2014.
 */
public class Reconstructor extends PApplet {
    private PathManager paths;

    public void init () {
        frame.removeNotify();
        frame.setUndecorated( true );
        frame.addNotify();
        super.init();
    }

    public void setup () {
        size( 1920, 1080 );

        paths = new PathManager( this, "saved" );
    }


    public void draw () {
        background( 0 );

        Point startPos = paths.getCurrentPath().getStartPos();
        Point endPos = paths.getCurrentPath().getEndPos();
        Point currentPos = paths.getCurrentPath().getPositionByMillis( millis() );

        fill( 255 );
        ellipse( ( float ) ( currentPos.getX() ), ( float ) ( currentPos.getY() ), 15, 15 );
        fill( 0, 255, 0 );
        ellipse( ( float ) ( startPos.getX() ), ( float ) ( startPos.getY() ), 20, 20 );
        fill( 0, 0, 255 );
        ellipse( ( float ) ( endPos.getX() ), ( float ) ( endPos.getY() ), 20, 20 );

        fill( 255 );
        text( "Progress: " + paths.getProgress( millis() ), 10, 15 );
        text( "Duration: " + paths.getCurrentPath().getDuration() + "ms", 10, 30 );
        text( "Path N°: " + paths.getCurrentPathIndex() + " / " + paths.getPathCount(), 10, 45 );
        text( "Valid: " + paths.getCurrentPath().isValid( this ), 10, 60 );
    }


    public void keyPressed () {
        switch ( key ) {
            case 'n':
                paths.nextPath();
                break;
        }
    }

    public static void main ( String[] args ) {
        PApplet.main( new String[]{ "reading.Reconstructor" } );
    }
}
