package both;

import org.apache.log4j.Logger;
import processing.core.PApplet;
import toxi.geom.AABB;
import toxi.geom.Rect;
import toxi.geom.Vec3D;
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
public class MousePath {
    private static Logger log = Logger.getLogger( MousePath.class );

    private static final DateFormat dateFormat = new SimpleDateFormat( "_dd_MM_yyyy__HH_mm_ss_SSS" );
    private static final String FILENAME_SUFFIX = ".csv";

    private ArrayList< Pair > pairs;
    private int currentlySelectedPathIndex;

    private Date startTime, endTime;



    private long currentMillis;
    private Vec2D lastPoint;
    private Vec2D acceleration;

    private float playbackSpeed;
    private String originalFileName;

    public MousePath() {
        this.startTime = new Date();
        this.endTime = new Date();
        this.pairs = new ArrayList<>();
        this.currentlySelectedPathIndex = 0;
        this.currentMillis = 0;
        this.lastPoint = new Vec2D();
        this.acceleration = new Vec2D();
        this.playbackSpeed = 1.0f;
        this.originalFileName = "no name";
    }

    public void update() {
        this.setCurrentMillis( System.currentTimeMillis() );
    }

    public String getOriginalFileName() {
        return this.originalFileName;
    }

    public void setOriginalFileName( String originalFileName ) {
        this.originalFileName = originalFileName;
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
        return pairs.get( pairs.size() - 1 ).getTime();
    }

    public String getCreationTime() {
        return dateFormat.format( Calendar.getInstance().getTime() );
    }

    public void setStartTime( long _startTime ) {
        startTime.setTime( _startTime );
    }

    public float getDistance() {
        return PApplet.dist( getStartPos().x, getStartPos().y, getEndPos().x, getEndPos().y );
    }

    public float getProgress() {
        float returnProgress = ( float ) getCurrentMillis() / ( float ) getDuration();
        return returnProgress;
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

    public void export( String folderToSaveTo ) {
        try {
            String fileName = folderToSaveTo + File.separator + InputTypeItemListener.CURRENT_INPUT_TYPE + getCreationTime() + FILENAME_SUFFIX;
            File folder = new File( folderToSaveTo );
            folder.mkdirs();
            File file = createFile( fileName );
            BufferedWriter writer = new BufferedWriter( new FileWriter( file.getAbsoluteFile() ) );

            printHeader( writer );

            for ( Pair p : pairs ) {
                writer.write( currentlySelectedPathIndex + "," + p.getTime() + "," + ( int ) ( p.getPosition().x ) + "," + ( int ) ( p.getPosition().y ) + "\n" );
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
        currentlySelectedPathIndex = 0;
    }

    /**
     * TODO
     *
     * @return
     */
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
    public Vec2D getCurrentPosition() {
        int pairIndex = 0;

        for ( Pair p : pairs ) {
            long millisCurrentPoint = ( long ) ( p.getTime() * playbackSpeed );
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


    public MousePath getPositionsScaledAndRotated( Vec2D newStart, Vec2D newEnd ) {
        MousePath mp = new MousePath();

        // 1. scale
        for ( Pair p : getPairs() ) {
            Vec2D v = p.getPosition();
            long time = p.getTime();

            Vec2D generalDirectionOld = getEndPos().sub( getStartPos() );
            Vec2D generalDirectionNew = newEnd.sub( newStart );
            float magDifference = generalDirectionNew.magnitude() / generalDirectionOld.magnitude();
            v.scaleSelf( magDifference );

            mp.addRaw( time, v );
        }
        mp.update();

        Vec2D currentStartToEnd = mp.getEndPos().sub( mp.getStartPos() );
        Vec2D newStartToEnd = newEnd.sub( newStart );
        currentStartToEnd = currentStartToEnd.normlize();
        newStartToEnd = newStartToEnd.normlize();
        float angle = currentStartToEnd.angleBetween( newStartToEnd );

        // acos can't properly calculate angle more than 180°.
        // solution taken from here: http://www.gamedev.net/topic/556500-angle-between-vectors/
        if ( currentStartToEnd.x * newStartToEnd.y < currentStartToEnd.y * newStartToEnd.x ) {
            angle = 2 * PApplet.PI - angle;
        }

        // 3. rotate
        for ( Pair p : mp.getPairs() ) {
            Vec2D v = p.getPosition();
            v.rotate( angle );
            p.setPosition( v );
        }
// 2. translate
        Vec2D translation = newStart.sub( mp.getStartPos() );
        for ( Pair p : mp.getPairs() ) {
            Vec2D v = p.getPosition();
            v.addSelf( translation );
            p.setPosition( v );
        }
        return mp;
    }

    public int size() {
        return this.getPoints().size();
    }

    public MousePath getMorphedPath( MousePath toMorphTo, float percentage ) {
        MousePath returnPath = new MousePath();

        int maxPoints = PApplet.max( this.size(), toMorphTo.size() );

        for ( int i = 0; i < maxPoints; i++ ) {
            // calculating the individual current indices. necessary when mapping one path to another
            int thisIndex = ( int ) ( ( ( float ) ( i ) / maxPoints ) * this.size() );
            int toMorphToIndex = ( int ) ( ( ( float ) ( i ) / maxPoints ) * toMorphTo.size() );
            Vec2D morphedPosition = new Vec2D();

            morphedPosition.x = mixValues( this.getPairs().get( thisIndex ).getPosition().x, toMorphTo.getPairs().get( toMorphToIndex ).getPosition().x, percentage );
            morphedPosition.y = mixValues( this.getPairs().get( thisIndex ).getPosition().y, toMorphTo.getPairs().get( toMorphToIndex ).getPosition().y, percentage );

            long thisTime = getPairs().get( thisIndex ).getTime();
            long toMorphToTime = toMorphTo.getPairs().get( toMorphToIndex ).getTime();

            long morphedTime = ( long ) mixValues( ( float ) ( thisTime ), ( float ) ( toMorphToTime ), percentage );

            returnPath.addRaw( morphedTime, morphedPosition );
        }

        returnPath.setOriginalFileName( "GENERATED ON THE FLY" );
        returnPath.update();
        return returnPath;
    }

    public MousePath getMapped( Vec2D artificialStart, Vec2D artificialEnd ) {
        MousePath returnPath = new MousePath();

        for ( Pair p : getPairs() ) {
            returnPath.addRaw( p.getTime(), getPositionMapped( p.getPosition(), artificialStart, artificialEnd ) );
        }
        returnPath.setOriginalFileName( "GENERATED ON THE FLY" );
        returnPath.update();

        return returnPath;
    }

    public MousePath3D get3dPath( MousePath additionalPath, boolean xOrY ) {
        MousePath3D returnPath = new MousePath3D();

        ArrayList< Vec2D > pointsFrom = getPoints();
        MousePath toPathNormalized = additionalPath.getNormalized();
        ArrayList< Vec2D > pointsTo = toPathNormalized.getPoints();

        int maxPoints = PApplet.max( pointsFrom.size(), pointsTo.size() );

        for ( int i = 0; i < maxPoints; i++ ) {
            // calculating the individual current indices. necessary when mapping one path to another
            int thisIndex = ( int ) ( ( ( float ) ( i ) / maxPoints ) * pointsFrom.size() );
            int additionalIndex = ( int ) ( ( ( float ) ( i ) / maxPoints ) * pointsTo.size() );

            Vec3D toAdd = new Vec3D();
            toAdd.x = pointsFrom.get( thisIndex ).x;
            toAdd.y = pointsFrom.get( thisIndex ).y;
            if ( xOrY ) {
                toAdd.z = pointsTo.get( additionalIndex ).x;
            } else {
                toAdd.z = pointsTo.get( additionalIndex ).y;
            }
            returnPath.add( toAdd );
        }

        return returnPath;
    }

    public double getShannonEntropyX() {
        double entropyX;

        ArrayList< Integer > xTranslation = new ArrayList<>();
        for ( int i = 0; i < getPoints().size() - 2; i++ ) {
            Vec2D from = getPoints().get( i );
            Vec2D to = getPoints().get( i + 1 );
            Vec2D translation = to.sub( from );
            xTranslation.add( ( int ) ( Math.abs( translation.x ) ) );
        }
        entropyX = getShannonEntropy( xTranslation );

        return entropyX;
    }

    public double getShannonEntropyY() {
        double entropyY;

        ArrayList< Integer > yTranslation = new ArrayList<>();
        for ( int i = 0; i < getPoints().size() - 2; i++ ) {
            Vec2D from = getPoints().get( i );
            Vec2D to = getPoints().get( i + 1 );
            Vec2D translation = to.sub( from );
            yTranslation.add( ( int ) ( Math.abs( translation.y ) ) );
        }
        entropyY = getShannonEntropy( yTranslation );

        return entropyY;
    }

    private double getShannonEntropy( ArrayList< Integer > s ) {
        int n = 0;
        Map< Integer, Integer > occ = new HashMap<>();

        for ( int c_ = 0; c_ < s.size(); ++c_ ) {
            Integer cx = s.get( c_ );
            if ( occ.containsKey( cx ) ) {
                occ.put( cx, occ.get( cx ) + 1 );
            } else {
                occ.put( cx, 1 );
            }
            ++n;
        }

        double e = 0.0;
        for ( Map.Entry< Integer, Integer > entry : occ.entrySet() ) {
            Integer cx = entry.getKey();
            double p = ( double ) entry.getValue() / n;
            e += p * log2( p );
        }
        return -e;
    }

    private double log2( double a ) {
        return Math.log( a ) / Math.log( 2 );
    }

    public long getCurrentMillis() {
        return currentMillis;
    }

    public void setCurrentMillis( long _millis ) {
        long duration = ( long ) ( getDuration() / playbackSpeed );
        long mil = _millis % duration;
        mil *= playbackSpeed;
        this.currentMillis = mil;
    }

    public void setSpeed( float _speed ) {
        playbackSpeed = _speed;
    }

    public Vec2D getCentroid() {
        Vec2D centroid = new Vec2D();

        for ( Vec2D v : getPoints() ) {
            centroid.addSelf( v );
        }

        centroid.divSelf( getPoints().size() );
        return centroid;
    }

    public MousePath getNormalized() {
        MousePath returnPath = new MousePath();

        for ( int i = 0; i < getPairs().size(); i++ ) {
            Vec2D morphedPosition = getPairs().get( i ).getPosition().sub( getCentroid() );
            long time = getPairs().get( i ).getTime();
            returnPath.addRaw( time, morphedPosition );
        }

        returnPath.setOriginalFileName( "GENERATED ON THE FLY" );
        returnPath.update();
        return returnPath;
    }

    public void translateSelf( Vec2D _v ) {
        for ( Pair p : getPairs() ) {
            Vec2D pos = p.getPosition();
            pos.addSelf( _v );
            p.setPosition( pos );
        }
    }

    public void scaleSelf( Vec2D _v ) {
        for ( Pair p : getPairs() ) {
            Vec2D pos = p.getPosition();
            pos.multSelf( _v );
            p.setPosition( pos );
        }
    }

    public boolean isInBoundingBox( Rect bb ) {
        for ( Pair p : getPairs() ) {
            Vec2D pos = p.getPosition();
            if ( pos.x < bb.getLeft() || pos.x > bb.getRight() || pos.y < bb.getTop() || pos.y > bb.getBottom() ) {
                return false;
            }
        }
        return true;
    }

    public AABB getBoundingBox() {
        //Rect bb = new Rect( getCentroid().x, getCentroid().y, 10, 10 );
        AABB bb = new AABB( new Vec3D( getStartPos().x, getStartPos().y, 0 ), 1.0f );
        // use Collections.min() / Collections.max() on the ArrayList. Though I'm not sure
        // how to compare the vectors and if it's even possible with non-basic types. cya later aligator

        for( Pair p : getPairs() ) {
            bb.includePoint( new Vec3D( p.getPosition().x, p.getPosition().y, 0 ) );
        }

        bb.updateBounds();
        return bb;
    }
}
