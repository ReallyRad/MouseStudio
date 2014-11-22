package both;

import org.apache.log4j.Logger;
import processing.core.PApplet;
import writing.InputTypeItemListener;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Marcel on 05.08.2014.
 * <p/>
 * The MousePath class, which holds all information about a path. A path can be drawn with any kind of input, e.g.
 * mouse, touch pad, graphics tablet and any other device that enables you to record discrete data of the timings
 * and positions of small parts of the movement.
 */
public class MousePath extends Thread {
    private static Logger log = Logger.getLogger( MousePath.class );

    @SuppressWarnings( "unused" )
    private Date startTime, endTime;

    private DateFormat dateFormat = new SimpleDateFormat( "HHmmssSSS" );

    private static final String FILENAME_PREFIX = "saved";
    private static final String FILENAME_SUFFIX = ".csv";

    private ArrayList< Pair > pairs;
    private int index;
    private long currentMillis;
    private Vec2D lastPoint;
    private Vec2D acceleration;
    private float playbackSpeed;
    private String originalFileName;

    private boolean running;
    private int wait;

    public MousePath() {
        this.startTime = new Date();
        this.endTime = new Date();
        this.pairs = new ArrayList<>();
        this.index = 0;
        this.currentMillis = 0;
        this.lastPoint = new Vec2D();
        this.acceleration = new Vec2D();
        this.playbackSpeed = 1.0f;
        this.originalFileName = "no name";
        this.running = false;
        this.wait = 4;

        File newFolder = new File( FILENAME_PREFIX );
        if ( !newFolder.exists() ) {
            try {
                boolean wasCreated = newFolder.mkdir();
                log.info( "Created folder to store mouse paths into. The path was " + newFolder.toString() + ". Returned was " + wasCreated + "." );
            } catch ( SecurityException se ) {
                log.error( "Could not create folder to store mouse paths into. The path was" + newFolder.toString() );
            }
        }
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
                this.setCurrentMillis( System.currentTimeMillis() );
            } catch ( Exception e ) {

            }
        }
    }

    public void setOriginalFileName( String originalFileName ) {
        this.originalFileName = originalFileName;
    }

    public String getOriginalFileName() {
        return this.originalFileName;
    }

    public ArrayList< Vec2D > getPoints() {
        ArrayList< Vec2D > ps = new ArrayList<>();
        for ( Pair p : pairs ) {
            ps.add( p.getPosition() );
        }
        return ps;
    }

    public ArrayList< Pair > getPairs() {
        return pairs;
    }

    public void add( long milli, Vec2D _p ) {
        pairs.add( new Pair( milli - startTime.getTime(), _p ) );
    }

    public void addRaw( long milli, Vec2D _p ) {
        pairs.add( new Pair( milli, _p ) );
    }

    public void finish() {
        this.endTime = new Date();
    }

    public Vec2D getStartPos() {
        return pairs.get( 0 ).getPosition();
    }

    public Vec2D getEndPos() {
        return pairs.get( pairs.size() - 1 ).getPosition();
    }

    public long getDuration() {
        return pairs.get( pairs.size() - 1 ).getDate();
    }

    public String getStartTime() {
        return dateFormat.format( Calendar.getInstance().getTime() );
    }

    public float getDistance() {
        return PApplet.dist( getStartPos().x, getStartPos().y, getEndPos().x, getEndPos().y );
    }

    public float getProgress() {
        return getCurrentMillis() / ( float ) getDuration();
    }

    public float getTravelDistance() {
        float dist = 0.0f;

        int index = 0;
        for ( Vec2D p : getPoints() ) {
            if ( index < getPoints().size() - 1 ) {
                Vec2D nextPoint = getPoints().get( index + 1 );
                float d = PApplet.dist( p.x, p.y, nextPoint.x, nextPoint.y );
                dist += d;
                index++;
            }
        }

        return dist;
    }

    public void export() {
        try {
            String fileName = FILENAME_PREFIX + File.separator + InputTypeItemListener.CURRENT_INPUT_TYPE + getStartTime() + FILENAME_SUFFIX;
            File file = createFile( fileName );
            BufferedWriter writer = new BufferedWriter( new FileWriter( file.getAbsoluteFile() ) );

            printHeader( writer );

            for ( Pair p : pairs ) {
                writer.write( index + "," + p.getDate() + "," + ( int ) ( p.getPosition().x ) + "," + ( int ) ( p.getPosition().y ) + "\n" );
            }
            writer.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private File createFile( String fileName ) {
        File file = new File( fileName );
        if ( !file.exists() ) {
            try {
                boolean wasCreated = file.createNewFile();
                log.debug( "Created mouse path file " + file + ". " + wasCreated );
            } catch ( IOException e ) {
                log.error( "Could not create mouse path file: " + file );
            }
        }

        return file;
    }

    private void printHeader( BufferedWriter writer ) {
        try {
            writer.write( "index,millis,x,y" + System.lineSeparator() );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void clear() {
        this.pairs.clear();
        this.startTime = new Date();
        index = 0;
    }

    public String toString() {
        return "Length: " + pairs.size() + " Duration: " + getDuration();
    }

    /**
     * Interpolates in between points and returns the absolute position of the mouse path indicated by the progress parameter
     *
     * @param progress percentage of the entire mouse path ( normalized
     * @return the interpolated point
     */
    public Vec2D getPosition( float progress ) {
        float prog = progress * ( getPoints().size() - 1 );
        int startIndex = PApplet.floor( prog );
        int endIndex = startIndex + 1;
        float normalizedPercentageInBetween = prog - startIndex;

        Vec2D from = getPoints().get( startIndex );
        Vec2D to = getPoints().get( endIndex );

        Vec2D returnVector = new Vec2D();

        returnVector.x = mixValues( from.x, to.x, normalizedPercentageInBetween );
        returnVector.y = mixValues( from.y, to.y, normalizedPercentageInBetween );
        return returnVector;
    }

    /**
     * Gives you the current position on the mouse path. the mouse path is being played back in the background.
     * The position returned by this method is timed according to the real recording.
     * <p/>
     * See {@link #getPosition(float)} or {@link #getPoints()} for a different approach of accessing the data
     * of a mouse path.
     *
     * @return the current position
     */
    public Vec2D getPosition() {
        int pairIndex = 0;
        for ( Pair p : pairs ) {
            long millisCurrentPoint = ( long ) ( p.getDate() * playbackSpeed );
            if ( getCurrentMillis() < millisCurrentPoint && pairIndex > 0 ) {
                calculateAcceleration( p );

                Vec2D returnVec = p.getPosition();

                lastPoint = p.getPosition();
                return returnVec;
            }
            pairIndex++;
        }
        return getStartPos();
    }

    private float mixValues( float begin, float end, float percentage ) {
        return ( ( end - begin ) * percentage ) + begin;
    }

    private void calculateAcceleration( Pair _pair ) {
        // acceleration should not be calculated when jumpinmg from end to beginning of a mouse path
        if ( !( lastPoint.equalsWithTolerance( this.getEndPos(), 10.0f ) && _pair.getPosition().equalsWithTolerance( this.getStartPos(), 10.0f ) ) ) {

            Vec2D acc = new Vec2D( _pair.getPosition().x - lastPoint.x, _pair.getPosition().y - lastPoint.y );

            // positions are being updated too rapidly, so the acceleration is always 0
            if ( acc.x != 0.0 && acc.y != 0.0 ) {
                acceleration = acc;
            }
        }
    }

    public Vec2D getPositionMapped( Vec2D currentOriginalPoint, Vec2D newStartPoint, Vec2D newEndPoint ) {
        Vec2D originalStart = getStartPos();
        Vec2D originalEnd = getEndPos();

        float newX = PApplet.map( currentOriginalPoint.x, originalStart.x, originalEnd.x, newStartPoint.x, newEndPoint.x );
        float newY = PApplet.map( currentOriginalPoint.y, originalStart.y, originalEnd.y, newStartPoint.y, newEndPoint.y );
        return new Vec2D( ( int ) newX, ( int ) newY );
    }

    public Vec2D getCurrentAcceleration() {
        return acceleration;
    }

    /**
     * @return boolean indicating if the path is valid according to the passed parameters
     */
    public boolean isValid( float minDistance, long maxDurationMillis ) {
        boolean isDistanceValid = getStartPos().distanceTo( getEndPos() ) > minDistance;
        boolean isDurationValid = getDuration() < maxDurationMillis;
        return isDistanceValid && isDurationValid;
    }

    public ArrayList< Vec2D > getPositionsMapped( Vec2D artificialStart, Vec2D artificialEnd ) {
        ArrayList< Vec2D > returnPoints = new ArrayList<>();

        for ( Vec2D p : getPoints() ) {
            Vec2D mapped = getPositionMapped( p, artificialStart, artificialEnd );
            returnPoints.add( mapped );
        }

        return returnPoints;
    }

    /*
    public ArrayList< Vec2D > getPositionsScaledAndRotated( Vec2D newStart, Vec2D newEnd ) {
        ArrayList< Vec2D > returnPoints = new ArrayList< >();

        for ( Vec2D p : getPoints() ) {
            Vec2D mapped = getPositionScaledAndRotated( p, newStart, newEnd );
            returnPoints.add( mapped );
        }

        return returnPoints;
    }

    public Vec2D getPositionScaledAndRotated( Vec2D originalPoint, Vec2D newStartPoint, Vec2D newEndPoint ) {
        Vec2D returnPoint = new Vec2D( originalPoint.x, originalPoint.y );

        // 1. move the point towards the new starting position start point
        Vec2D distanceDiffStart = new Vec2D( newStartPoint.x - getStartPos().x, newStartPoint.y - getStartPos().y );
        returnPoint.add( distanceDiffStart );

        // 2. calculate the difference of angle rotate the new vector accordingly
        float angleDiff =
                new Vec2D( newStartPoint.x - returnPoint.x, newStartPoint.y - returnPoint.y ).angleBetween(
                        new Vec2D( newStartPoint.x - newEndPoint.x, newStartPoint.y - newEndPoint.y ) );

        returnPoint.rotate( angleDiff );

        // 3. calc magnitude difference and add it to the new vector
        float magnitudeDiff = new Vec2D( newStartPoint.x - newEndPoint.x, newStartPoint.y - newEndPoint.y ).magnitude() -
                new Vec2D( newStartPoint.x - returnPoint.x, newStartPoint.y - returnPoint.y ).magnitude();
        //returnPoint.setMagnitude( returnPoint.magnitude() - magnitudeDiff );

        return returnPoint;
    }

    */

    public void setCurrentMillis( long _millis ) {
        long duration = ( long ) ( getDuration() / playbackSpeed );
        long mil = _millis % duration;
        mil *= playbackSpeed;

        this.currentMillis = mil;
    }

    public ArrayList< Vec2D > getMorphed( MousePath toMorphTo, float percentage ) {
        ArrayList< Vec2D > morphedPoints = new ArrayList<>();

        for ( float i = 0.0f; i < 1.0f; i += 0.001 ) {
            Vec2D startPosition = getPosition( i );
            Vec2D endPosition = toMorphTo.getPosition( i );

            Vec2D morphed = new Vec2D();
            morphed.x = mixValues( startPosition.x, endPosition.x, percentage );
            morphed.y = mixValues( startPosition.y, endPosition.y, percentage );
            morphedPoints.add( morphed );
        }

        return morphedPoints;
    }

    public MousePath getMorphedPath( MousePath toMorphTo, float percentage ) {
        MousePath returnPath = new MousePath();

        int max = PApplet.max( this.getPoints().size(), toMorphTo.getPoints().size() );
        for( int i = 0; i < max; i++ ) {

        }

        for( Pair p : getPairs() ) {
           // p.
        }
        // todo

        return returnPath;
    }


    public double getShannonEntropyX() {
        double entropyX;

        ArrayList< Integer > xTranslation = new ArrayList<>(  );
        for( int i = 0; i < getPoints().size() - 2; i++ ) {
            Vec2D from = getPoints().get( i );
            Vec2D to = getPoints().get( i + 1 );
            Vec2D translation = to.sub( from );
            xTranslation.add( ( int )( Math.abs( translation.x ) ) );
        }
        entropyX = getShannonEntropy( xTranslation );

        return entropyX;
    }

    public double getShannonEntropyY() {
        double entropyY;

        ArrayList< Integer > yTranslation = new ArrayList<>(  );
        for( int i = 0; i < getPoints().size() - 2; i++ ) {
            Vec2D from = getPoints().get( i );
            Vec2D to = getPoints().get( i + 1 );
            Vec2D translation = to.sub( from );
            yTranslation.add( ( int ) (Math.abs( translation.y ) ) );
        }
        entropyY = getShannonEntropy( yTranslation );

        return entropyY;
    }

    private double getShannonEntropy( ArrayList< Integer > s ) {
        int n = 0;
        Map<Integer, Integer> occ = new HashMap<>();

        for (int c_ = 0; c_ < s.size(); ++c_) {
            Integer cx = s.get( c_ );
            if (occ.containsKey(cx)) {
                occ.put(cx, occ.get(cx) + 1);
            } else {
                occ.put(cx, 1);
            }
            ++n;
        }

        double e = 0.0;
        for (Map.Entry<Integer, Integer> entry : occ.entrySet()) {
            Integer cx = entry.getKey();
            double p = (double) entry.getValue() / n;
            e += p * log2(p);
        }
        return -e;
    }

    private double log2(double a) {
        return Math.log(a) / Math.log(2);
    }

    public long getCurrentMillis() {
        return currentMillis;
    }

    public void setSpeed( float _speed ) {
        playbackSpeed = _speed;
    }
}
