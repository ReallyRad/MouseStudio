package examples;

import both.MouseVisualizer;
import processing.core.PApplet;
import both.MouseMovement;
import both.Vec2D;

/**
 * Created by mar on 15.11.14.
 */
public class ExampleSimple extends PApplet {

    private MouseMovement mm;
    private MouseVisualizer mv;

    public void setup() {
        size( 1920, 1080 );

        mm = new MouseMovement();
        mm.setDataFolder( "saved" );
        mm.loadRecordings( 10 );

        mv = new MouseVisualizer( this );

    }

    public void draw() {
        background( 0 );

        stroke( 255 );
        mv.dawCurvedPath( mm.getSelectedPath() );

        fill( 255, 0, 0 );
        mv.draw( mm.getSelectedPath() );

        fill( 255 );
        text( "Progress: " + mm.getSelectedPath().getProgress(), 10, 15 );
        text( "Duration: " + mm.getSelectedPath().getDuration() + "ms", 10, 30 );
        text( "Path N°: " + mm.getCurrentPathIndex() + " / " + mm.size(), 10, 45 );
        text( "Valid: " + mm.getSelectedPath().isValid( 100, 2000 ), 10, 60 );
        text( "Filename: " + mm.getCurrentFileName(), 10, 75 );
        text( "Acceleration: " + mm.getSelectedPath().getCurrentAcceleration(), 10, 90 );
        text( "Distance: " + mm.getSelectedPath().getDistance(), 10, 105 );
        text( "Travel distance :" + mm.getSelectedPath().getTravelDistance(), 10, 120 );
        text( "Entropy: x:" + mm.getSelectedPath().getShannonEntropyX() + " y:" + mm.getSelectedPath().getShannonEntropyY(), 10, 135 );

        text( "Press 'n' for the next recording", 10, 155 );
    }

    public void keyPressed () {
        switch ( key ) {
            case 'n':
                mm.nextPath();
                break;
        }
    }

    public static void main( String[] args ) {
        PApplet.main( new String[] { "examples.ExampleSimple" } );
    }
}
