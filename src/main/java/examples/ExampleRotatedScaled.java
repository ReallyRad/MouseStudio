package examples;

import both.MousePath;
import both.MouseVisualizer;
import both.Vec2D;
import peasy.PeasyCam;
import processing.core.PApplet;
import both.MouseMovement;

import java.util.ArrayList;

/**
 * Created by mar on 10.12.14.
 */
public class ExampleRotatedScaled extends PApplet {

    private MouseMovement mm;
    private MouseVisualizer mv;
    private PeasyCam cam;

    public void setup() {
        size( 1920, 1080, P3D );

        cam = new PeasyCam( this, 1000 );

        mm = new MouseMovement();
        mm.setDataFolder( "saved" );
        mm.loadRecordings( 10 );

        mv = new MouseVisualizer( this );
    }

    public void draw() {
        background( 0 );

        mv.drawCoordinateSystem();

        mm.update();

        stroke( 255 );

        mv.drawRawPath( mm.getSelectedPath() );

        MousePath rotatedAndScaled = mm.getSelectedPath().getPositionsScaledAndRotated( new Vec2D( 200, 200 ), new Vec2D( mouseX - width /2 , mouseY - height / 2 ) );

        stroke( 0, 0, 255 );
        mv.drawRawPath( rotatedAndScaled );
    }

    public void keyPressed () {
        switch ( key ) {
            case 'n':
                mm.nextPath();
                break;
        }
    }

    public static void main( String[] args ) {
        PApplet.main( new String[] { "examples.ExampleRotatedScaled" } );
    }
}
