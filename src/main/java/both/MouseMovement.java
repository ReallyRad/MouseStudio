package both;

import org.apache.log4j.Logger;
import processing.core.PApplet;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Marcel on 12.08.2014.
 *
 * TODO:
 * 1. fix the getMapped functions to return valid MousePaths
 * 2. create method to generate 3D paths from two 2d paths
 *
 */
public class MouseMovement {
    private static Logger log = Logger.getLogger( MouseMovement.class );

    private PApplet p;
    private int currentPathIndex;
    private ArrayList< MousePath > mousePaths;
    private float speed;
    public static Dimension resolution;
    private String path = "new_beginning";

    public MouseMovement () {
        this.speed = 1.0f;
        this.p = new PApplet();
        this.mousePaths = new ArrayList<>();
        this.currentPathIndex = 0;

        this.resolution = new Dimension( 1920, 1080 );
        setResolution( ( int )( resolution.getWidth()), ( int )( resolution.getHeight() ) );
    }

    public void update() {
        for( MousePath mp : mousePaths ) {
            mp.update();
        }
    }

    public void setDataFolder( String folder ) {
        this.path = folder;
    }

    public void loadRecordings( int fileCount ) {
        mousePaths.clear();
        File recordingsPath = new File( p.sketchPath( this.path ) );
        if ( !recordingsPath.exists() ) {
            System.out.println( "There is no folder with the name " + this.path + " specified." );
            return;
        }
        String[] files = recordingsPath.list();

        int maxFilesToLoad = Math.min( fileCount, files.length );

        int numberOfLoaderThreads = Math.min(16, fileCount);
        ExecutorService executor = Executors.newFixedThreadPool( numberOfLoaderThreads );
        ArrayList< Runnable > loaders = new ArrayList<>(  );
        for( int i = 0; i < numberOfLoaderThreads; i++ ) {
            int from = maxFilesToLoad  / numberOfLoaderThreads * i;
            int to = maxFilesToLoad / numberOfLoaderThreads * ( i + 1 );
            ArrayList< String > fileNames = new ArrayList<>(  );
            for( int j = from; j < to; j++ ) {
                fileNames.add( files[ j ] );
            }
            Runnable worker = new MousePathLoader( recordingsPath.toString(), fileNames );
            executor.execute( worker );
            loaders.add( worker );
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        for( int i = 0; i < numberOfLoaderThreads; i++ ) {
            MousePathLoader loader = ( MousePathLoader ) loaders.get( i );
            for( MousePath p : loader.getPaths() ) {
                if( p.getPairs().size() > 2 ) {
                    mousePaths.add( p );
                } else {
                    log.info( "Not adding MousePath " + p.getOriginalFileName() + " due to small point set." );
                }
            }
        }
    }

    public int getAvailableRecordingCount() {
        File recordingsPath = new File( p.sketchPath( this.path ) );
        if ( !recordingsPath.exists() ) {
            System.out.println( "There is no folder with the name " + this.path + " specified." );
            return 0;
        }
        return recordingsPath.list().length;
    }

    public void removeDoubleClicks() {
        Iterator< MousePath > i = mousePaths.iterator();
        while ( i.hasNext() ) {
            MousePath p = i.next();
            // there are usually many paths who only consist of two points at the same location ( a double click )
            if( p.getPoints().size() < 3 ) {
                i.remove();
            }
        }
    }
    public String getCurrentFileName () {
        return mousePaths.get( currentPathIndex ).getOriginalFileName();
    }

    public void deleteCurrent () {
        File fileToDelete = new File( getCurrentFileName() );
        if ( fileToDelete.delete() ) {
            mousePaths.remove( currentPathIndex );
        } else {
            System.out.println( "Not Deleted." );
        }
    }

    public MousePath getSelectedPath() {
        return mousePaths.get( currentPathIndex );
    }

    public ArrayList< MousePath > getPaths() {
        return mousePaths;
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

    public MousePath get( int id ) {
        return mousePaths.get( id );
    }

    public Vec2D getPositionFromPath ( int pathId ) {
        get( pathId ).setCurrentMillis( get( pathId ).getCurrentMillis() );
        return get( pathId ).getCurrentPosition();
    }

    public int getCurrentPathIndex () {

        return currentPathIndex;
    }

    public int size() {

        return mousePaths.size();
    }

    public ArrayList< MousePath > filterByDuration( int min, int max ) {
        ArrayList< MousePath > filteredPaths = new ArrayList<>(  );
        Iterator< MousePath > i = mousePaths.iterator();
        while ( i.hasNext() ) {
            MousePath p = i.next();
            if( p.getDuration() >= min && p.getDuration() <= max ) {
                filteredPaths.add ( p );
            }
        }

        return filteredPaths;
    }

    public void filterSelfByDuration( int min, int max ) {
        Iterator< MousePath > i = mousePaths.iterator();
        while ( i.hasNext() ) {
            MousePath p = i.next();
            if( p.getDuration() <= min || p.getDuration() >= max ) {
                i.remove();
            }
        }
    }

    public ArrayList< MousePath > filterByShannonEntropyX( float min, float max ) {
        ArrayList< MousePath > filteredPaths = new ArrayList<>();

        Iterator< MousePath > i = mousePaths.iterator();
        while ( i.hasNext() ) {
            MousePath p = i.next();
            if( p.getShannonEntropyX() >= min && p.getShannonEntropyX() <= max ) {
                filteredPaths.add( p );
            }
        }

        return filteredPaths;
    }

    public ArrayList< MousePath > filterByShannonEntropyY( float min, float max ) {
        ArrayList< MousePath > filteredPaths = new ArrayList<>();

        Iterator< MousePath > i = mousePaths.iterator();
        while ( i.hasNext() ) {
            MousePath p = i.next();
            if( p.getShannonEntropyY() >= min && p.getShannonEntropyY() <= max ) {
                filteredPaths.add( p );
            }
        }

        return filteredPaths;
    }

    public ArrayList< MousePath > filterByShannonEntropy( float minX, float maxX, float minY, float maxY ) {
        ArrayList< MousePath > filteredPaths = filterByShannonEntropyX( minX, maxX  );

        Iterator< MousePath > i = filteredPaths.iterator();
        while ( i.hasNext() ) {
            MousePath p = i.next();
            if( p.getShannonEntropyY() >= minX && p.getShannonEntropyY() <= maxX ) {
                i.remove();
            }
        }

        return filteredPaths;
    }

    public ArrayList< MousePath > filterByDistance( float min, float max ) {
        ArrayList< MousePath > filteredPaths = new ArrayList<>();

        Iterator< MousePath > i = mousePaths.iterator();
        while ( i.hasNext() ) {
            MousePath p = i.next();
            if( p.getDistance() >= min && p.getDistance() <= max ) {
                filteredPaths.add( p );
            }
        }

        return filteredPaths;
    }

    public ArrayList< MousePath > filterByTravelDistance( float min, float max ) {
        ArrayList< MousePath > filteredPaths = new ArrayList<>();

        Iterator< MousePath > i = mousePaths.iterator();
        while ( i.hasNext() ) {
            MousePath p = i.next();
            if( p.getTravelDistance() >= min && p.getTravelDistance() <= max ) {
                filteredPaths.add( p );
            }
        }

        return filteredPaths;
    }

    public void filterSelfByTravelDistance( float min, float max ) {
        Iterator< MousePath > i = mousePaths.iterator();
        while ( i.hasNext() ) {
            MousePath p = i.next();
            if( p.getTravelDistance() <= min || p.getTravelDistance() >= max ) {
                i.remove();
            }
        }
    }

    public void setSpeed ( float _speed ) {
        this.speed = _speed;
        for ( MousePath p : mousePaths ) {
            p.setSpeed( this.speed );
        }
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setResolution ( int _width, int _height ) {
        for ( MousePath p : mousePaths ) {
            for ( Pair pair : p.getPairs() ) {
                pair.setResolution( new Vec2D( _width, _height ) );
            }
        }
    }

    public void selectPath( int _selectedPath ) {
        currentPathIndex = _selectedPath;
    }
}
