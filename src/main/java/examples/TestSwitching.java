package examples;

import both.MouseMovement;
import both.MousePath;
import both.MouseVisualizer;
import both.Vec2D;
import processing.core.PApplet;

/**
 * Created by mar on 30.12.14.
 */
public class TestSwitching extends PApplet {

    MouseMovement mm;
    MouseVisualizer mv;

    public void init () {
        frame.removeNotify();
        frame.setUndecorated( true );
        frame.setResizable( true );
        frame.addNotify();
        super.init();
    }

    public void setup() {
        size( 1920, 1080, P2D );
        background( 0 );

        mm = new MouseMovement();
        mm.setDataFolder( "saved" );
        mm.loadRecordings( 100 );
        mm.removeDoubleClicks();

        mv = new MouseVisualizer( this );
    }

    public void draw() {
        println( frameRate );
        background( 0 );
        stroke( 255 );

        MousePath mp1 = mm.getSelectedPath().getPositionsScaledAndRotated( new Vec2D( 200, 150 ), new Vec2D( 200, height - 150 ) );
        MousePath mp2 = mm.getSelectedPath().getPositionsScaledAndRotated( new Vec2D( width - 200, height - 150 ), new Vec2D( width - 200, 150 ) );
        MousePath mMorphed = mp1.getMorphedPath( mp2, map( mouseX, 0, width, 0, 1 ) );
        mv.drawRawPath( mMorphed );
    }

    public void keyPressed() {
        mm.nextPath();
    }

    public static void main( String[] args ) {
        PApplet.main( new String[] { "examples.TestSwitching" } );
    }
}
