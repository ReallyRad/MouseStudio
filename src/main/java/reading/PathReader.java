package reading;

import processing.core.PApplet;
import processing.core.PVector;
import processing.data.Table;
import processing.data.TableRow;
import both.MousePath;

import java.awt.*;

/**
 * Created by Marcel on 07.08.2014.
 */
public class PathReader {
    private PApplet p;
    private MousePath path;

    public PathReader ( PApplet p ) {
        this.p = p;
        path = new MousePath();
    }

    public void load ( String fileName ) {
        path.clear();
        try {
            Table t = p.loadTable( p.sketchPath( fileName ), "header" );
            for ( TableRow r : t.rows() ) {
                long milli = r.getLong( "millis" );
                int x = r.getInt( "x" );
                int y = r.getInt( "y" );
                path.addRaw( milli, new Point( x, y ) );
            }
        } catch( NullPointerException e ) {
            System.out.println( e );
            e.printStackTrace();
        }

        path.finish();
    }

    public MousePath getPath() {
        return path;
    }


}
