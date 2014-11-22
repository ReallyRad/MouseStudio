package examples;

import processing.core.PApplet;
import both.MouseMovement;
import both.Vec2D;

/**
 * Created by Marcel on 07.08.2014.
 */
public class MainExample extends PApplet {
    private MouseMovement mouse;
    private int mousePathsToLoad = 200;

    public void init () {
        frame.removeNotify();
        frame.setUndecorated( true );
        frame.setResizable( true );
        frame.addNotify();
        super.init();
    }

    public void setup () {
        size( 1920, 1080, P2D );

        mouse = new MouseMovement();
        mouse.setDataFolder( "saved" );
        mouse.loadRecordings( mousePathsToLoad );
        mouse.filterByDuration(1000, 5000 );
        mouse.setSpeed( 1 );
        mouse.setPathById( 59 );
        mouse.setResolution( 1920, 1080 );
    }


    public void draw () {
        background( 0 );

        Vec2D startPos = mouse.getSelectedPath().getStartPos();
        Vec2D endPos = mouse.getSelectedPath().getEndPos();
        Vec2D currentPos = mouse.getSelectedPath().getPosition();
        noStroke();
        fill( 255 );
        ellipse( currentPos.x, currentPos.y, 15, 15 );
        fill( 0, 255, 0 );
        ellipse( startPos.x, startPos.y, 20, 20 );
        fill( 0, 0, 255 );
        ellipse( endPos.x, endPos.y, 20, 20 );

        Vec2D artificialStart = new Vec2D( 400, 400 );
        Vec2D artificialEnd = new Vec2D( mouseX, mouseY );
        Vec2D currArtificialPos = mouse.getSelectedPath().getPositionMapped( mouse.getSelectedPath().getPosition(), artificialStart, artificialEnd );

        fill( 0, 255, 255 );
        ellipse( currArtificialPos.x, currArtificialPos.y, 20, 20 );

        for ( Vec2D p : mouse.getSelectedPath().getPoints() ) {
            fill( 255, 180 );
            noStroke();
            ellipse( p.x, p.y, 3, 3 );
        }

        for ( Vec2D p : mouse.getSelectedPath().getPositionsMapped( artificialStart, artificialEnd ) ) {
            fill( 255, 180 );
            noStroke();
            ellipse( p.x, p.y, 3, 3 );
        }

        stroke( 255, 255 );
        strokeWeight( 1.5f );
        line( currentPos.x, currentPos.y, currentPos.x + mouse.getSelectedPath().getCurrentAcceleration().x, currentPos.y + mouse.getSelectedPath().getCurrentAcceleration().y );
/*
        for ( Vec2D p : mouse.getSelectedPath().getPositionsScaledAndRotated( new Vec2D( mouseX, mouseY ), new Vec2D( mouseX + 300, mouseY + 200 ) ) ) {
            fill( 255, 180 );
            noStroke();
            ellipse( p.x, p.y, 3, 3 );
        }
*/
        fill( 255 );
        text( "Progress: " + mouse.getSelectedPath().getProgress(), 10, 15 );
        text( "Duration: " + mouse.getSelectedPath().getDuration() + "ms", 10, 30 );
        text( "Path N°: " + mouse.getCurrentPathIndex() + " / " + mouse.size(), 10, 45 );
        text( "Valid: " + mouse.getSelectedPath().isValid( 100, 2000 ), 10, 60 );
        text( "Filename: " + mouse.getCurrentFileName(), 10, 75 );
        text( "Acceleration: " + mouse.getSelectedPath().getCurrentAcceleration(), 10, 90 );
        text( "Distance: " + mouse.getSelectedPath().getDistance(), 10, 105 );
        text( "Travel distance :" + mouse.getSelectedPath().getTravelDistance(), 10, 120 );
    }


    public void keyPressed () {
        switch ( key ) {
            case 'n':
                mouse.nextPath();
                break;
            case 'd':
                mouse.deleteCurrent();
                break;
        }
    }

    public static void main ( String[] args ) {
        PApplet.main( new String[]{ "examples.MainExample" } );
    }
}
