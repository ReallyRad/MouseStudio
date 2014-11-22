package examples;

import both.MouseMovement;
import both.MousePath;
import both.MouseVisualizer;
import both.Vec2D;
import processing.core.PApplet;

import java.util.ArrayList;

/**
 * Created by mar on 22.11.14.
 */
public class TestTriangle2 extends PApplet {

    private MouseMovement mm;
    private ArrayList< MousePath > filtered;
    private MouseVisualizer mv;
    private int selectedPath = 0;

    private Vec2D edgeA = new Vec2D( width / 2, 200 );
    private Vec2D edgeB = new Vec2D( width / 4, 800 );
    private Vec2D edgeC = new Vec2D( width / 4 * 3, 800 );

    public void init() {
        frame.removeNotify();
        frame.setUndecorated( true );
        frame.setResizable( true );
        frame.addNotify();
        super.init();
    }

    public void setup() {
        size( 1920, 1080, P3D );
        frameRate( 500 );
        noCursor();

        mm = new MouseMovement();
        mm.setDataFolder( "saved" );
        mm.loadRecordings( 800 );
        filtered = mm.filterByTravelDistance( 0, 1000 );
        mm.removeDoubleClicks();

        System.out.println( "Filtered " + mm.size() + " to " + filtered.size() + " paths." );

        mv = new MouseVisualizer( this );
    }

    public void draw() {
        background( 0 );
        println( frameRate );

        stroke( 255, 90 );
        strokeWeight( 3 );

        int displayAtOnce = (int)( map( mouseX, 0, width, 1, 30 ) );
        int frameRateToSet = (int)( map( mouseY, 0, height, 1, 60 ) );
        frameRate( frameRateToSet );
        for( int i = 0; i < displayAtOnce; i++ ) {
            ArrayList< Vec2D > morphedA = mm.get( selectedPath ).getPositionsMapped( edgeA, edgeB );
            ArrayList< Vec2D > morphedB = mm.get( selectedPath + 1 ).getPositionsMapped( edgeB, edgeC );
            ArrayList< Vec2D > morphedC = mm.get( selectedPath + 2 ).getPositionsMapped( edgeC, edgeA );


            mv.drawPointList( morphedA );
            mv.drawPointList( morphedB );
            mv.drawPointList( morphedC );

            selectedPath++;
            selectedPath %= mm.size() - 3;
        }
    }

    public void keyPressed() {
        if ( key == 'n' ) {
            selectedPath++;
            selectedPath %= mm.size() - 3;
        }
        if ( key == '1' ) {
            edgeA = new Vec2D( mouseX, mouseY );
        }
        if ( key == '2' ) {
            edgeB = new Vec2D( mouseX, mouseY );
        }
        if ( key == '3' ) {
            edgeC = new Vec2D( mouseX, mouseY );
        }
    }

    public static void main( String[] args ) {
        PApplet.main( new String[]{ "examples.TestTriangle2" } );
    }
}
