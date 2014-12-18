package examples;

import both.MouseMovement;
import both.MousePath;
import both.MouseVisualizer;
import processing.core.PApplet;

/**
 * Created by mar on 23.11.14.
 */
public class ExampleSimpleMorph extends PApplet {

    private MouseMovement mm;
    private MouseVisualizer mv;
    private int selected = 0;

    public void setup() {
        size( 1920, 1080 );

        mm = new MouseMovement();
        mm.setDataFolder( "saved" );
        mm.loadRecordings( 20 );
        mm.removeDoubleClicks();

        mv = new MouseVisualizer( this );
    }

    public void draw() {
        background( 0 );
        MousePath p1 = mm.get( selected );
        MousePath p2 = mm.get( selected + 1 );
        MousePath pMorphed = p1.getMorphedPath( p2, map( mouseX, 0, width, 0, 1 ) );

        stroke( 255 );
        mv.drawCurvedPath( pMorphed );
        mv.draw( pMorphed );

        stroke( 255, 90 );
        mv.drawCurvedPath( p1 );
        mv.drawCurvedPath( p2 );
        mv.draw( p1 );
        mv.draw( p2 );
    }

    public void keyPressed () {
        switch( key ) {
            case 'n':
                selected++;
                selected %= mm.size() - 1;
                break;
        }
    }

    public static void main( String[] args ) {
        PApplet.main( new String[]{ "examples.ExampleSimpleMorph" } );
    }
}
