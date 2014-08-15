package reading;

import both.MousePath;
import processing.core.PApplet;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Marcel on 12.08.2014.
 */
public class MouseMovementParser extends Thread {

    private int wait;
    private boolean running;
    private long currentMillis;

    private PApplet p;
    private int currentPathIndex;
    private ArrayList< PathReader > mousePaths;
    private float speed;

    public MouseMovementParser ( PApplet p, String directory ) {
        this.wait = 10;
        this.running = false;
        this.speed = 1.0f;
        this.p = p;
        mousePaths = new ArrayList<>();
        currentPathIndex = 0;
        openDirectory( directory );
    }

    public void openDirectory ( String dir ) {
        mousePaths.clear();
        File recordingsPath = new File( p.sketchPath( dir ) );
        String[] files = recordingsPath.list();
        for ( String s : files ) {
            PathReader r = new PathReader( p );
            r.load( "saved\\" + s );
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

    public int getCurrentPathIndex () {
        return currentPathIndex;
    }

    public int getPathCount () {
        return mousePaths.size();
    }

    public float getProgress () {
        long mil = currentMillis;
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
                long dur = ( long ) ( getCurrentPath().getDuration() / speed );
                currentMillis = System.currentTimeMillis() % ( dur );
                currentMillis *= speed;
                this.getCurrentPath().setCurrentMillis( currentMillis );
            } catch ( Exception e ) {

            }
        }
    }

    public void setSpeed ( float _speed ) {
        this.speed = _speed;
    }
}
