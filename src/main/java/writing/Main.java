package writing;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import toxi.geom.Vec2D;

/**
 * Created by Marcel on 10.08.2014.
 */
public class Main implements NativeMouseInputListener {
    final TrayMenu tray = new TrayMenu();
    final MouseRecognizer mouseRecognizer = new MouseRecognizer();

    public static void main ( String[] args ) {


        try {
            GlobalScreen.registerNativeHook();
            System.out.println( "Successfully registered native hook." );
        } catch ( NativeHookException ex ) {
            System.err.println( "There was a problem registering the native hook." );
            System.err.println( ex.getMessage() );

            System.exit( 1 );
        }

        //Construct the example object and initialze native hook.
        Main hook = new Main();
        GlobalScreen.getInstance().addNativeMouseListener( hook );
        GlobalScreen.getInstance().addNativeMouseMotionListener( hook );
    }

    @Override
    public void nativeMouseClicked ( NativeMouseEvent nativeMouseEvent ) {
        System.out.println( nativeMouseEvent.getX() + " " + nativeMouseEvent.getY() );

    }

    @Override
    public void nativeMousePressed ( NativeMouseEvent nativeMouseEvent ) {
        System.out.println( nativeMouseEvent.getX() + " " + nativeMouseEvent.getY() );
        mouseRecognizer.down( new Vec2D( nativeMouseEvent.getX(), nativeMouseEvent.getY() ), 0 );
    }

    @Override
    public void nativeMouseReleased ( NativeMouseEvent nativeMouseEvent ) {
        System.out.println( nativeMouseEvent.getX() + " " + nativeMouseEvent.getY() );
        mouseRecognizer.up( new Vec2D( nativeMouseEvent.getX(), nativeMouseEvent.getY() ), 0 );
    }

    @Override
    public void nativeMouseMoved ( NativeMouseEvent nativeMouseEvent ) {
        System.out.println( nativeMouseEvent.getX() + " " + nativeMouseEvent.getY() );
        mouseRecognizer.moved( new Vec2D( nativeMouseEvent.getX(), nativeMouseEvent.getY() ) );
    }

    @Override
    public void nativeMouseDragged ( NativeMouseEvent nativeMouseEvent ) {

    }
}
