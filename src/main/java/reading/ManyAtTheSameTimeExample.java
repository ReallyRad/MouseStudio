package reading;

import processing.core.PApplet;
import toxi.geom.Vec2D;

import java.util.ArrayList;

/**
 * Created by Marcel on 16.08.2014.
 */
public class ManyAtTheSameTimeExample extends PApplet {
    private MouseMovement mm;
    int mousePathCount = 20;

    int selectedIndex = 1; // ( int ) map( mouseX, 0, width, 0, mousePathCount );
    int size = 2;

    public void init () {
        frame.removeNotify();
        frame.setUndecorated( true );
        frame.addNotify();
        super.init();
    }
    
    public void setup () {
        size( 1920, 1080 );
        mm = new MouseMovement( this, "saved", mousePathCount );
        mm.start();
    }

    public void draw () {
        background( 0 );
        stroke( 255, 90 );
        for ( int i = 1; i < mousePathCount; i++ ) {


            fill( 255 );

            // selectedIndex = 1; // ( int ) map( mouseX, 0, width, 0, mousePathCount );
            // if ( i == selectedIndex ) {
            // size = 8;
            // fill( 255, 100, 0 );
            // }

            for ( Vec2D p : mm.getPathFromId( i ).getPoints() ) {
                // ellipse( p.x, p.y, size, size );
            }

            // Vec2D v = mm.getPositionFromPath( i );
            // text( i + "", v.x + 5, v.y );
            // ellipse( v.x, v.y, size*3, size*3 );


            // for( Vec2D mapped : mm.getPathFromId( i ).getPositionsMapped( mm.getPathFromId( i ).getStartPos(), new Vec2D( mouseX, mouseY ) ) ) {

        }

        drawSelected();
    }

    private void drawSelected () {
        ArrayList< Vec2D > recordedPointsFromIndex = mm.getPathFromId( this.selectedIndex ).getPoints();
        for ( Vec2D p : recordedPointsFromIndex ) {
            ellipse( p.x, p.y, size, size );

            if ( recordedPointsFromIndex.indexOf( p ) > 0 ) {
                Vec2D last = recordedPointsFromIndex.get( recordedPointsFromIndex.indexOf( p ) - 1 );
                line( last.x, last.y, p.x, p.y );
            }
        }

        ArrayList< Vec2D > mappedPointsFromIndex = mm.getPathFromId( this.selectedIndex ).getPositionsMapped( mm.getPathFromId( this.selectedIndex ).getStartPos(), new Vec2D( mouseX, mouseY ) );
        for ( Vec2D mapped : mappedPointsFromIndex ) {
            ellipse( mapped.x, mapped.y, 3, 3 );
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
        PApplet.main( new String[]{ "reading.ManyAtTheSameTimeExample" } );
    }
}
