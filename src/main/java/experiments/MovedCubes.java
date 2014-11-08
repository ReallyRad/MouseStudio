package experiments;

import both.MousePath;
import processing.core.PApplet;
import processing.core.PVector;
import reading.MouseMovement;
import toxi.geom.Vec2D;

/**
 * Created by mar on 14.09.14.
 */
public class MovedCubes extends PApplet {
    private PVector[][] boxes;
    private MouseMovement mouse;
    public void setup() {
        size( 800, 600 );
        boxes = new PVector[ 8 ][ 6 ];
        for( int i = 0; i < 6; i++ ) {
            for( int j = 0; j < 8; j++ ) {
                boxes[ j ][ i ] = new PVector( j * 100, i * 100 );
            }
        }

        mouse = new MouseMovement( this, "saved", 20 );
        mouse.setSpeed( 1 );
        mouse.start();
        mouse.setPathById( 10 );
        mouse.setResolution( 1920, 1080 );

        noStroke();
        for( int i = 0; i < 10; i++ ) {
            MousePath p = mouse.getPathFromId(i);
            Vec2D start = p.getStartPos();
            Vec2D end = p.getEndPos();
            Vec2D diff = end.sub(start);
            // TODO: move cubes around
        }
    }

    public void draw() {
        for( int i = 0; i < 6; i++ ){
            for (int j = 0; j < 8; j++) {
                int count = i * 8 + j;
                fill( (int)(count/(8*6.0)*255) );
                PVector pos = boxes[ j ][ i ];
                rect( pos.x, pos.y, 100, 100 );
            }
        }
    }
}
