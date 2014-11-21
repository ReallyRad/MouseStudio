package examples;

import both.MousePath;
import both.MouseVisualizer;
import processing.core.PApplet;
import both.MouseMovement;

import java.util.ArrayList;

/**
 * Created by mar on 15.11.14.
 */
public class ExampleEntropy extends PApplet {

    private MouseVisualizer mv;
    private ArrayList< MousePath > filtered;

    public void setup() {
        size( 1920, 1080 );

        mv = new MouseVisualizer( this );

        MouseMovement mm = new MouseMovement( this );
        mm.setDataFolder( "saved" );
        mm.loadRecordings( 100 );

        filtered = mm.filterByShannonEntropyY( 1.0f, 3.5f );
        System.out.println( "Filtered " + mm.size() + " to " + filtered.size() + " paths." );
    }

    public void draw() {
        background( 0 );
        stroke( 255 );

        mv.drawPaths( filtered );
    }

    public static void main( String[] args ) {
        PApplet.main( new String[] { "examples.ExampleEntropy" } );
    }
}
