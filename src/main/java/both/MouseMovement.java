package both;

import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

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
    private ArrayList< MousePath > mousePaths;
    private float speed;
    public static Dimension resolution;
    private String path = "saved";

    public MouseMovement ( PApplet p) {
        this.wait = 4;
        this.running = false;
        this.speed = 1.0f;
        this.p = p;
        mousePaths = new ArrayList<>();
        currentPathIndex = 0;

        resolution = new Dimension( 1920, 1080 );
        setResolution( ( int )( resolution.getWidth()), ( int )( resolution.getHeight() ) );
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
        int maxFiles = fileCount;
        for ( int i = 0; i < files.length && i < maxFiles; i++ ) {
            String s = files[ i ];

            System.out.println( "Loaded " + i + " of " + maxFiles );
            MousePath finishedPath = load( recordingsPath + File.separator + s );
            mousePaths.add( finishedPath );
        }
    }

    private MousePath load ( String fileName ) {
        MousePath path = new MousePath();
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
        path.setOriginalFileName( fileName );

        return path;
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

    public MousePath getCurrentPath () {
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

    public MousePath getPathById( int id ) {
        return mousePaths.get( id );
    }

    public Vec2D getPositionFromPath ( int pathId ) {
        getPathById( pathId ).setCurrentMillis( getPathById( pathId ).getCurrentMillis() );
        return getPathById( pathId ).getPosition();
    }

    public int getCurrentPathIndex () {

        return currentPathIndex;
    }

    public int size() {

        return mousePaths.size();
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
                for ( MousePath p : mousePaths ) {
                    p.setCurrentMillis( System.currentTimeMillis() );
                }
            } catch ( Exception e ) {

            }
        }
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
}
