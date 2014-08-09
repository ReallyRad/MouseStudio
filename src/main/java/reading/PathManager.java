package reading;

import both.MousePath;
import processing.core.PApplet;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Marcel on 08.08.2014.
 */
public class PathManager {
    private PApplet p;
    private int currentPathIndex;
    private ArrayList< PathReader > mousePaths;

    public PathManager ( PApplet p, String directory ) {
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

    public float getProgress ( long millis ) {
        long mil = millis % getCurrentPath().getDuration();
        float progress = mil / ( float ) getCurrentPath().getDuration();

        return progress;
    }
}
