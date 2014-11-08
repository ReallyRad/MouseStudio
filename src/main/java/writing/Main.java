package writing;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import toxi.geom.Vec2D;


/**
 * Created by Marcel on 10.08.2014.
 */
public class Main implements NativeMouseInputListener {
    private static Logger log = Logger.getLogger( Main.class );

    final MouseRecognizer mouseRecognizer = new MouseRecognizer();

    @Override
    public void nativeMouseClicked ( NativeMouseEvent nativeMouseEvent ) {
        log.debug( "nativeMouseClicked -> " + nativeMouseEvent.getX() + " " + nativeMouseEvent.getY() );
    }

    @Override
    public void nativeMousePressed ( NativeMouseEvent nativeMouseEvent ) {
        log.debug( "nativeMousePressed -> " + nativeMouseEvent.getX() + " " + nativeMouseEvent.getY() );
        mouseRecognizer.down( new Vec2D( nativeMouseEvent.getX(), nativeMouseEvent.getY() ), 0 );
    }

    @Override
    public void nativeMouseReleased ( NativeMouseEvent nativeMouseEvent ) {
        log.debug( "nativeMouseReleased -> " + nativeMouseEvent.getX() + " " + nativeMouseEvent.getY() );
        mouseRecognizer.up( new Vec2D( nativeMouseEvent.getX(), nativeMouseEvent.getY() ), 0 );
    }

    @Override
    public void nativeMouseMoved ( NativeMouseEvent nativeMouseEvent ) {
        log.debug( "nativeMouseMoved -> " + nativeMouseEvent.getX() + " " + nativeMouseEvent.getY() );
        mouseRecognizer.moved( new Vec2D( nativeMouseEvent.getX(), nativeMouseEvent.getY() ) );
    }

    @Override
    public void nativeMouseDragged ( NativeMouseEvent nativeMouseEvent ) {
        log.debug( "nativeMouseDragged -> " + nativeMouseEvent.getX() + " " + nativeMouseEvent.getY() );
    }

    /**
     *
     * @param args
     */
    public static void main ( String[] args ) {
            log.setLevel( Level.OFF );

        try {
            GlobalScreen.registerNativeHook();
            log.debug( "Successfully registered native mouse hook." );
        } catch ( NativeHookException ex ) {
            log.fatal( "Could not register native mouse hook." );
            log.fatal( ex.getMessage() );

            System.exit( 1 );
        }

        // initialize the native hook.
        Main hook = new Main();
        new TrayMenu();

        GlobalScreen.getInstance().addNativeMouseListener( hook );
        GlobalScreen.getInstance().addNativeMouseMotionListener( hook );
    }
}
