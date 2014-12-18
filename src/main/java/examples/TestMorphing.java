package examples;

import both.MouseMovement;
import both.MousePath;
import both.MouseVisualizer;
import both.Vec2D;
import processing.core.PApplet;

import java.util.ArrayList;

/**
 * Created by mar on 16.12.14.
 */
public class TestMorphing extends PApplet {

    private MouseMovement mm;
    private ArrayList< MousePath > filtered;
    private MouseVisualizer mv;

    public void setup() {
        size( 1920, 1080, P3D );
        frameRate( 500 );

        mm = new MouseMovement();
        mm.setDataFolder( "new_stuff" );
        mm.loadRecordings( 800 );
        mm.removeDoubleClicks();

        filtered = mm.filterByDuration( 0, 2000 );

        mv = new MouseVisualizer( this );
    }

    public void draw() {
        background( 0 );
        mm.update();
        stroke( 255 );
        //mv.drawCurvedPaths( filtered );

        MousePath p1 = mm.getSelectedPath().getMapped( new Vec2D( 100, 100 ), new Vec2D( 100, height - 100) );

        mv.drawCurvedPath( p1 );
        mv.drawCurvedPath( mm.getSelectedPath() );
    }

    public void keyPressed() {
        switch( key ) {
            case 'n':
                mm.nextPath();
                break;
        }
    }

    public static void main( String[] args ) {
        PApplet.main( new String[]{ "examples.TestMorphing" } );
    }

}
