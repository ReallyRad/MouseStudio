package examples;

import both.MouseMovement;
import both.MousePath;
import both.MousePath3D;
import both.MouseVisualizer;
import peasy.PeasyCam;
import processing.core.PApplet;
import toxi.geom.Vec3D;

import java.util.ArrayList;

/**
 * Created by mar on 24.11.14.
 */
public class Example3D extends PApplet {

    private PeasyCam cam;
    private MouseMovement mm;
    private MouseVisualizer mv;
    private int selected = 1;

    public void init () {
        frame.removeNotify();
        frame.setUndecorated( true );
        frame.setResizable( true );
        frame.addNotify();
        super.init();
    }

    public void setup() {
        size( 1920, 1080, P3D );

        cam = new PeasyCam( this, 100 );
        mm = new MouseMovement();
        mm.setDataFolder( "saved" );
        mm.loadRecordings( 400 );
        mm.removeDoubleClicks();
        mv = new MouseVisualizer( this );
    }

    public void draw() {
        background( 0 );
        stroke( 255 );

        mv.drawCoordinateSystem();

        MousePath3D path3d1 = mm.get( selected ).get3dPath( mm.get( selected + 1 ), true );
        MousePath3D path3d2 = mm.get( selected ).get3dPath( mm.get( selected + 1 ), false );

        pushMatrix();
        translate( path3d1.getCentroid().x, path3d1.getCentroid().x, path3d1.getCentroid().z );
        sphere( 20 );
        popMatrix();

        pushMatrix();
        translate( path3d2.getCentroid().x, path3d2.getCentroid().x, path3d2.getCentroid().z );
        sphere( 20 );
        popMatrix();

        ArrayList< Vec3D > mapped1 = path3d1.getPositionsMapped( new Vec3D( 0, 0, 0 ), new Vec3D( 200, 200, 200) );
        ArrayList< Vec3D > mapped2 = path3d2.getPositionsMapped( new Vec3D( 0, 0, 0 ), new Vec3D( 200, 200, 200 ) );

        mv.drawPointList3D( mapped1 );
        mv.drawPointList3D( mapped2 );

        mv.drawRawPath( path3d1 );
        mv.drawRawPath( path3d2 );

        mv.drawRawPath( mm.get( selected ) );
        mv.drawRawPath( mm.get( selected + 1 ) );

        ellipse( mm.get( selected ).getCentroid().x, mm.get( selected ).getCentroid().y, 20, 20 );
        ellipse( mm.get( selected + 1 ).getCentroid().x, mm.get( selected + 1 ).getCentroid().y, 20, 20 );


        MousePath normalizedPath = mm.get( selected ).getNormalized();
        MousePath normalizedPath2 = mm.get( selected + 1 ).getNormalized();

        mv.drawCurvedPath( normalizedPath );
        mv.drawCurvedPath( normalizedPath2 );

    }

    public void keyPressed() {
        switch( key ) {
            case 'n':
                selected += 1;
                selected %= mm.size();
                break;
        }
    }

    public static void main( String[] args ) {
        PApplet.main( new String[] { "examples.Example3D" } );
    }
}
