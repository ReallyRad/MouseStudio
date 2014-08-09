package both;

import processing.core.PApplet;
import writing.InputTypeItemListener;

import java.awt.*;
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

    private static final String FILENAME_PREFIX = "saved\\";
    private static final String FILENAME_SUFFIX = ".csv";

    private ArrayList< Pair > points;
    private int index;

    public MousePath () {
        this.startTime = new Date();
        points = new ArrayList<>();
        index = 0;
    }

    public void add ( long milli, Point _p ) {
        if ( points != null ) {
            points.add( new Pair( milli - startTime.getTime(), _p ) );
        }
    }

    public void addRaw ( long milli, Point _p ) {
        if ( points != null ) {
            points.add( new Pair( milli, _p ) );
        }
    }

    public void finish () {
        this.endTime = new Date();
    }

    public Point getStartPos () {
        return points.get( 0 ).getPoint();
    }

    public Point getEndPos () {
        return points.get( points.size() - 1 ).getPoint();
    }

    public long getDuration () {
        return points.get( points.size() - 1 ).getDate();
    }

    public String getStartTime () {
        return dateFormat.format( Calendar.getInstance().getTime() );
    }

    public void export () {
        try {
            String fileName = FILENAME_PREFIX + InputTypeItemListener.CURRENT_INPUT_TYPE + getStartTime() + FILENAME_SUFFIX;
            File file = createFile( fileName );
            BufferedWriter writer = new BufferedWriter( new FileWriter( file.getAbsoluteFile() ) );

            printHeader( writer );

            for ( Pair p : points ) {
                writer.write( index + "," + p.getDate() + "," + ( int ) ( p.getPoint().getX() ) + "," + ( int ) ( p.getPoint().getY() ) + "\n" );
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
        this.points.clear();
        this.startTime = new Date();
        index = 0;
    }

    public String toString () {
        return "Length: " + points.size() + " Duration: " + getDuration();
    }

    public Point getPositionByMillis ( long mil ) {
        mil = mil % getDuration();
        for ( Pair p : points ) {
            long millis = p.getDate();
            if ( mil < millis ) {
                return p.getPoint();
            }
        }
        return getStartPos();
    }

    /**
     * @return
     */
    public boolean isValid ( PApplet p ) {
        return p.dist( getStartPos().x, getStartPos().y, getEndPos().x, getEndPos().y ) > 100;
    }
}
