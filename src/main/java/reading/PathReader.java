package reading;

import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;
import both.MousePath;
import toxi.geom.Vec2D;

import java.awt.*;

/**
 * Created by Marcel on 07.08.2014.
 */
public class PathReader {
    private PApplet p;
    private MousePath path;
    private String fileName;

    public PathReader ( PApplet p ) {
        this.p = p;
        path = new MousePath();
    }

    public void load ( String fileName ) {
        this.fileName = fileName;
        path.clear();
        try {
            String fileToLoad = p.sketchPath( fileName );
            System.out.println( "Trying to load file: " + fileToLoad );
            Table t = p.loadTable( fileToLoad, "header" );
            for ( TableRow r : t.rows() ) {
                long milli = r.getLong( "millis" );
                int x = r.getInt( "x" );
                int y = r.getInt( "y" );
                path.addRaw( milli, new Vec2D( x, y ) );
            }
        } catch ( NullPointerException e ) {
            System.out.println( e );
            e.printStackTrace();
        }

        path.finish();
    }

    public MousePath getPath () {
        return path;
    }

    public String getFileName () {
        return fileName;
    }


}
