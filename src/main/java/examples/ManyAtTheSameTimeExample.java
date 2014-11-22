package examples;

import both.Vec2D;
import processing.core.PApplet;
import both.MouseMovement;

import java.util.ArrayList;

/**
 * Created by Marcel on 16.08.2014.
 */
public class ManyAtTheSameTimeExample extends PApplet {
    private MouseMovement mm;
    int mousePathCount = 4900;

    int selectedIndex = 1; // ( int ) map( mouseX, 0, width, 0, mousePathCount );
    int size = 2;

    public void init () {
        frame.removeNotify();
        frame.setUndecorated( true );
        frame.setResizable( true );
        frame.addNotify();
        super.init();
    }

    public void setup () {
        size( 1920, 1080, P2D );
        println(sketchFullScreen());

        mm = new MouseMovement();
        mm.setDataFolder( "saved" );
        mm.loadRecordings( mousePathCount );
    }

    public void draw () {
        println( frameRate );
        background( 0 );
        stroke( 255, 90 );
        for ( int i = 1; i < mousePathCount; i++ ) {


            fill( 255 );

            // selectedIndex = 1; // ( int ) map( mouseX, 0, width, 0, mousePathCount );
            // if ( i == selectedIndex ) {
            // size = 8;
            // fill( 255, 100, 0 );
            // }
            //fill( 255, 255, 0 );
            for ( Vec2D p : mm.get( i ).getPoints() ) {
                 ellipse( p.x, p.y, size, size );
            }

            // Vec2D v = mm.getPositionFromPath( i );
            // text( i + "", v.x + 5, v.y );
            // ellipse( v.x, v.y, size*3, size*3 );


            // for( Vec2D mapped : mm.get( i ).getPositionsMapped( mm.get( i ).getStartPos(), new Vec2D( mouseX, mouseY ) ) ) {
//drawSelected( i );
        }

        drawSelected( this.selectedIndex );
    }

    private void drawSelected ( int selected ) {
        stroke( 255, 255, 0 );
        fill( 255, 255, 0 );
        ArrayList< Vec2D > recordedPointsFromIndex = mm.get( selected ).getPoints();
        for ( Vec2D p : recordedPointsFromIndex ) {
            ellipse( p.x, p.y, size, size );

            if ( recordedPointsFromIndex.indexOf( p ) > 0 ) {
                Vec2D last = recordedPointsFromIndex.get( recordedPointsFromIndex.indexOf( p ) - 1 );
                line( last.x, last.y, p.x, p.y );
            }
        }

        stroke( 0, 0, 255 );
        fill( 0, 0, 255 );
        ArrayList< Vec2D > mappedPointsFromIndex = mm.get( selected ).getPositionsMapped( mm.get( selected ).getStartPos(), new Vec2D( mouseX, mouseY ) );
        for ( Vec2D mapped : mappedPointsFromIndex ) {
            ellipse( mapped.x, mapped.y, 5, 5 );
            if ( mappedPointsFromIndex.indexOf( mapped ) > 0 ) {
                Vec2D last = mappedPointsFromIndex.get( mappedPointsFromIndex.indexOf( mapped ) - 1 );
                line( last.x, last.y, mapped.x, mapped.y );
            }
        }
    }

    public void mouseReleased () {
        selectedIndex++;
        selectedIndex %= mousePathCount;
    }

    public static void main ( String[] args ) {
        PApplet.main( new String[]{ "examples.ManyAtTheSameTimeExample" } );
    }
}
