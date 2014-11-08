package both;

import processing.core.PApplet;
import toxi.geom.Vec2D;
import writing.InputTypeItemListener;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Marcel on 05.08.2014.
 */
public class MousePath {
    private Date startTime, endTime;

    private DateFormat dateFormat = new SimpleDateFormat( "HHmmssSSS" );

    private static final String FILENAME_PREFIX = "saved" + File.separator;
    private static final String FILENAME_SUFFIX = ".csv";

    private ArrayList< Pair > pairs;
    private int index;
    private String fileName;
    private long currentMillis;
    private Vec2D lastPoint;
    private Vec2D acceleration;
    private float playbackSpeed;

    public MousePath () {
        this.startTime = new Date();
        pairs = new ArrayList<>();
        index = 0;
        currentMillis = 0;
        lastPoint = new Vec2D();
        acceleration = new Vec2D();
        playbackSpeed = 1.0f;
    }

    public ArrayList< Vec2D > getPoints () {
        ArrayList< Vec2D > ps = new ArrayList<>();
        for ( Pair p : pairs ) {
            ps.add( p.getPosition() );
        }
        return ps;
    }

    public ArrayList< Pair > getPairs () {
        return pairs;
    }

    public void add ( long milli, Vec2D _p ) {
        if ( pairs != null ) {
            pairs.add( new Pair( milli - startTime.getTime(), _p ) );
        }
    }

    public void addRaw ( long milli, Vec2D _p ) {
        if ( pairs != null ) {
            pairs.add( new Pair( milli, _p ) );
        }
    }

    public void finish () {
        this.endTime = new Date();
    }

    public Vec2D getStartPos () {
        return pairs.get( 0 ).getPosition();
    }

    public Vec2D getEndPos () {
        return pairs.get( pairs.size() - 1 ).getPosition();
    }

    public long getDuration () {
        return pairs.get( pairs.size() - 1 ).getDate();
    }

    public String getStartTime () {
        return dateFormat.format( Calendar.getInstance().getTime() );
    }

    public float getDistance () {
        return PApplet.dist( getStartPos().x, getStartPos().y, getEndPos().x, getEndPos().y );
    }

    public float getTravelDistance () {
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

    public void export () {
        try {
            String fileName = FILENAME_PREFIX + InputTypeItemListener.CURRENT_INPUT_TYPE + getStartTime() + FILENAME_SUFFIX;
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

    private File createFile ( String fileName ) {
        File file = new File( fileName );
        try {
            if ( !file.exists() ) {
                file.createNewFile();
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        return file;
    }

    private void printHeader ( BufferedWriter writer ) {
        try {
            writer.write( "index,millis,x,y\n" );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void clear () {
        this.pairs.clear();
        this.startTime = new Date();
        index = 0;
    }

    public String toString () {
        return "Length: " + pairs.size() + " Duration: " + getDuration();
    }

    public Vec2D getPosition () {

        int pairIndex = 0;
        for ( Pair p : pairs ) {
            long millis = ( long ) (p.getDate() * playbackSpeed);
            if ( getCurrentMillis() < millis && pairIndex > 0 ) {
                calculateAcceleration( p );

                Vec2D returnVec = p.getPosition();

                //Pair lastPair = pairs.get( pairIndex - 1 );
                // long normalizedCurrentMillis = getCurrentMillis() - lastPair.getDate();
                // long normalizedMillis = millis - lastPair.getDate();
                // float factor = normalizedCurrentMillis / (float)normalizedMillis;
                //Vec2D subbed = p.getPosition().sub( lastPair.getPosition() );
                //subbed.limit( subbed.magnitude() * factor );
                //returnVec.addSelf( subbed );

                lastPoint = p.getPosition();
                return returnVec;
            }
            pairIndex++;
        }
        return getStartPos();
    }

    private void calculateAcceleration ( Pair _pair ) {
        // acceleration should not be calculated when jumpinmg from end to beginning of a mouse path
        if ( !( lastPoint.equalsWithTolerance( this.getEndPos(), 10.0f ) && _pair.getPosition().equalsWithTolerance( this.getStartPos(), 10.0f ) ) ) {

            Vec2D acc = new Vec2D( _pair.getPosition().x - lastPoint.x, _pair.getPosition().y - lastPoint.y );

            // positions are being updated too rapidly, so the acceleration is always 0
            if ( acc.x != 0.0 && acc.y != 0.0 ) {
                acceleration = acc;
            }
        }
    }

    public Vec2D getPositionMapped ( Vec2D originalPoint, Vec2D newStartPoint, Vec2D newEndPoint ) {
        Vec2D currentOriginalPoint = originalPoint;
        Vec2D originalStart = getStartPos();
        Vec2D originalEnd = getEndPos();

        float newX = PApplet.map( currentOriginalPoint.x, originalStart.x, originalEnd.x, newStartPoint.x, newEndPoint.x );
        float newY = PApplet.map( currentOriginalPoint.y, originalStart.y, originalEnd.y, newStartPoint.y, newEndPoint.y );
        return new Vec2D( ( int ) newX, ( int ) newY );
    }

    public Vec2D getPositionScaledAndRotated ( Vec2D originalPoint, Vec2D newStartPoint, Vec2D newEndPoint ) {
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

    public Vec2D getAcceleration () {
        return acceleration;
    }

    /**
     * @return
     */
    public boolean isValid ( float minDistance, long maxDurationMillis ) {
        boolean isDistanceValid = getStartPos().distanceTo( getEndPos() ) > minDistance;
        boolean isDurationValid = getDuration() < maxDurationMillis;
        return isDistanceValid && isDurationValid;
    }

    public ArrayList< Vec2D > getPositionsMapped ( Vec2D artificialStart, Vec2D artificialEnd ) {
        ArrayList< Vec2D > returnPoints = new ArrayList<>();

        for ( Vec2D p : getPoints() ) {
            Vec2D mapped = getPositionMapped( p, artificialStart, artificialEnd );
            returnPoints.add( mapped );
        }

        return returnPoints;
    }

    public ArrayList< Vec2D > getPositionsScaledAndRotated ( Vec2D newStart, Vec2D newEnd ) {
        ArrayList< Vec2D > returnPoints = new ArrayList<>();

        for ( Vec2D p : getPoints() ) {
            Vec2D mapped = getPositionScaledAndRotated( p, newStart, newEnd );
            returnPoints.add( mapped );
        }

        return returnPoints;
    }

    public void setCurrentMillis ( long _millis ) {
        long duration =  ( long )( getDuration() / playbackSpeed );
        long mil = _millis % duration;
        mil *= playbackSpeed;

        this.currentMillis = mil;
    }

    public long getCurrentMillis() {
        return currentMillis;
    }

    public void setSpeed ( float _speed ) {
        playbackSpeed = _speed;
    }
}
