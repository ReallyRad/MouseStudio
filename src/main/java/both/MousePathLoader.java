package both;

import org.apache.log4j.Logger;
import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by mar on 22.11.14.
 */
public class MousePathLoader extends Thread {
    private static Logger log = Logger.getLogger( MousePathLoader.class );

    private ArrayList< String > fileNames;
    private String path;
    private PApplet papplet;
    private ArrayList< MousePath > loadedPaths;

    public MousePathLoader( String path, ArrayList< String > fileNames) {
        this.fileNames = fileNames;
        this.path = path;
        this.papplet = new PApplet();

        this.loadedPaths = new ArrayList<>();
    }

    private MousePath load( String fileName ) {

        MousePath path = new MousePath();
        path.clear();

        try {
            String fileToLoad = papplet.sketchPath( fileName );
            log.info( "Trying to load file: " + fileToLoad );
            Table t = papplet.loadTable( fileToLoad, "header" );
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
        path.setOriginalFileName( fileName );

        return path;
    }

    public void start() {
        super.start();
    }

    public void run() {
        for( String s : fileNames ) {
            MousePath p = load( path + File.separator + s );
            p.start();
            loadedPaths.add( p );
        }

        quit();
    }

    public int size() {
        return loadedPaths.size();
    }

    public ArrayList< MousePath > getPaths() {
        return loadedPaths;
    }

    public void quit() {

    }
}
