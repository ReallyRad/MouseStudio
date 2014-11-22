package examples;

import processing.core.PApplet;
import both.MouseMovement;

import java.util.ArrayList;

/**
 * Created by Marcel on 16.08.2014.
 */
public class SquareMouseExample extends PApplet {

    private MouseMovement mouse;
    ArrayList< Rect > rects;


    public void init () {
        frame.removeNotify();
        frame.setUndecorated( true );
        frame.setResizable( true );
        frame.addNotify();
        super.init();
    }

    public void setup () {
        size( 1920, 1080, P2D );
        mouse = new MouseMovement();
        mouse.setDataFolder( "saved" );
        mouse.loadRecordings( 200 );

        rects = new ArrayList<>();

        for ( int i = 0; i < mouse.size(); i++ ) {
            rects.add( new Rect( this, color( 255, 10 ) ) );
        }
    }

    public void draw () {
        println(frameRate);
        //mouse.setSpeed( map( mouseX, 0, width, 0.1f, 2 ) );

        background( 15 );
        for ( int i = 0; i < rects.size(); i += 4 ) {
            Rect r = rects.get( i );
            //r.setPos( mouse.getPositionFromPath( i ) );
            //r.setSize( mouse.getPositionFromPath( i + rects.size() ).sub( mouse.getPositionFromPath(  i ) ) );
            r.setCorner( examples.CORNER.TOP_RIGHT, mouse.getPositionFromPath( i ) );
            r.setCorner( examples.CORNER.RIGHT_BOTTOM, mouse.getPositionFromPath( i + 1 ) );
            r.setCorner( examples.CORNER.BOTTOM_LEFT, mouse.getPositionFromPath( i + 2 ) );
            r.setCorner( examples.CORNER.LEFT_TOP, mouse.getPositionFromPath( i + 3 ) );
            r.draw();
        }

    }

    public static void main ( String[] args ) {
        PApplet.main( new String[]{ "examples.SquareMouseExample" } );
    }
}
