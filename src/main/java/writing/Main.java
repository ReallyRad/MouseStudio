package writing;

import de.ksquared.system.mouse.GlobalMouseListener;
import de.ksquared.system.mouse.MouseAdapter;
import de.ksquared.system.mouse.MouseEvent;

import java.awt.*;

/**
 * Created by Marcel on 05.08.2014.
 */
public class Main {
    public static void main ( String[] args ) {

        final TrayMenu tray = new TrayMenu();
        final MouseRecognizer mouseRecognizer = new MouseRecognizer();
        new GlobalMouseListener().addMouseListener( new MouseAdapter() {
            @Override
            public void mousePressed ( MouseEvent event ) {
                mouseRecognizer.down( new Point( event.getX(), event.getY() ), event.getButton() );
            }

            @Override
            public void mouseReleased ( MouseEvent event ) {
                mouseRecognizer.up( new Point( event.getX(), event.getY() ), event.getButton() );
            }

            @Override
            public void mouseMoved ( MouseEvent event ) {
                mouseRecognizer.moved( new Point( event.getX(), event.getY() ) );

                /*
                if ( ( event.getButtons() & MouseEvent.BUTTON_LEFT ) != MouseEvent.BUTTON_NO
                        && ( event.getButtons() & MouseEvent.BUTTON_RIGHT ) != MouseEvent.BUTTON_NO )
                    System.out.println( "Both mouse buttons are currenlty pressed!" );
                */
            }
        } );
        while ( true )
            try {
                Thread.sleep( 100 );
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
    }
}
