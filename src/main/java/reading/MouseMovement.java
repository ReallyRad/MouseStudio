package reading;

import both.MousePath;
import both.Pair;
import processing.core.PApplet;
import toxi.geom.Vec2D;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Marcel on 12.08.2014.
 */
public class MouseMovement extends Thread {

    private int wait;
    private boolean running;

    private PApplet p;
    private int currentPathIndex;
    private ArrayList< PathReader > mousePaths;
    private float speed;
    private Dimension resolution;

    public MouseMovement ( PApplet p, String directory, int pathCount ) {
        this.wait = 4;
        this.running = false;
        this.speed = 1.0f;
        this.p = p;
        mousePaths = new ArrayList<>();
        currentPathIndex = 0;
        openDirectory( directory, pathCount );

        resolution = new Dimension( 1920, 1080 );
    }

    public void openDirectory ( String dir, int fileCount ) {
        mousePaths.clear();
        File recordingsPath = new File( p.sketchPath( dir ) );
        String[] files = recordingsPath.list();
        int maxFiles = fileCount;
        for ( int i = 0; i < files.length && i < maxFiles; i++ ) {
            String s = files[ i ];

            PathReader r = new PathReader( p );
            System.out.println( "Loaded " + i + " of " + maxFiles );
            r.load( "saved" + File.separator + s );
            mousePaths.add( r );
        }
    }

    public String getCurrentFileName () {
        return mousePaths.get( currentPathIndex ).getFileName();
    }

    public void deleteCurrent () {
        File fileToDelete = new File( getCurrentFileName() );
        if ( fileToDelete.delete() ) {
            mousePaths.remove( currentPathIndex );
        } else {
            System.out.println( "Not Deleted." );
        }
    }

    public MousePath getCurrentPath () {
        return mousePaths.get( currentPathIndex ).getPath();
    }

    public void nextPath () {
        currentPathIndex++;
        currentPathIndex %= mousePaths.size();

    }

    public void setPathById ( int id ) {
        if ( id < mousePaths.size() ) {
            currentPathIndex = id;
        } else {
            currentPathIndex = mousePaths.size() - 1;
        }
    }

    public MousePath getPathFromId ( int id ) {
        return mousePaths.get( id ).getPath();
    }

    public Vec2D getPositionFromPath ( int pathId ) {
        getPathFromId( pathId ).setCurrentMillis( getPathFromId( pathId ).getCurrentMillis() );
        return getPathFromId( pathId ).getPosition();
    }

    public int getCurrentPathIndex () {
        return currentPathIndex;
    }

    public int getPathCount () {
        return mousePaths.size();
    }

    public float getProgress () {
        long mil = getCurrentPath().getCurrentMillis();
        float progress = mil / ( float ) getCurrentPath().getDuration();

        return progress;
    }

    public void start () {
        running = true;
        super.start();
    }

    public void quit () {
        running = false;
        interrupt();
    }

    public void run () {
        while ( running ) {
            try {
                sleep( wait );
                for ( PathReader p : mousePaths ) {
                    p.getPath().setCurrentMillis( System.currentTimeMillis() );
                    p.getPath().setSpeed( speed );
                }
            } catch ( Exception e ) {

            }
        }
    }

    public void filterByDuration( int min, int max ) {
        Iterator< PathReader > i = mousePaths.iterator();
        while ( i.hasNext() ) {
            PathReader r = i.next();
            if( r.getPath().getDuration() < min || r.getPath().getDuration() > max ) {
                i.remove();
            }
        }
    }

    public void setSpeed ( float _speed ) {
        this.speed = _speed;
    }

    public void setResolution ( int _width, int _height ) {
        for ( PathReader p : mousePaths ) {
            for ( Pair pair : p.getPath().getPairs() ) {
                pair.setResolution( new Vec2D( _width, _height ) );
            }
        }
    }
}
