package writing;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Marcel on 07.08.2014.
 */
public class TrayMenu {
    private static Logger log = Logger.getLogger( TrayMenu.class );
    private Frame debugFrame;
    private DebugFIFO<String> debugOutput = new DebugFIFO<String>( 20 );

    public TextArea getDebugTextArea() {
        return debugTextArea;
    }

    public void updateDebug( String newMessage ) {
        debugOutput.add( newMessage );
        //getDebugTextArea().set
    }

    private TextArea debugTextArea = new TextArea("Debug", 5, 30 );

    public TrayMenu () {
        log.setLevel( Level.OFF );

        if ( !SystemTray.isSupported() ) {
            log.fatal( "SystemTray is not supported" );
            return;
        }
        final PopupMenu popup = new PopupMenu();
        Image img = Toolkit.getDefaultToolkit().getImage( "mouse-icon.gif" );
        final TrayIcon trayIcon =
                new TrayIcon( img, "Mouse Studio", popup );
        trayIcon.setImageAutoSize( true );
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a pop-up menu components
        MenuItem aboutItem = new MenuItem( "About" );
        MenuItem debugToggleItem = new MenuItem( "Debug" );
        debugToggleItem.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                debugFrame.setVisible( !debugFrame.isVisible() );
            }
        } );
        Menu inputTypeMenu = new Menu( "Input Type" );

        CheckboxMenuItem mouseInputItem = new CheckboxMenuItem( InputTypeItemListener.MOUSE_INPUT_TYPE );
        CheckboxMenuItem touchpadInputItem = new CheckboxMenuItem( InputTypeItemListener.TOUCHPAD_INPUT_TYPE );
        CheckboxMenuItem trackpointInputItem = new CheckboxMenuItem( InputTypeItemListener.TRACKPOINT_INPUT_TYPE );
        CheckboxMenuItem touchscreenInputItem = new CheckboxMenuItem( InputTypeItemListener.TOUCHPAD_INPUT_TYPE );
        mouseInputItem.setState( true );

        ArrayList< CheckboxMenuItem > inputItems = new ArrayList< CheckboxMenuItem>();
        inputItems.add( mouseInputItem );
        inputItems.add( touchpadInputItem );
        inputItems.add( trackpointInputItem );
        inputItems.add( touchscreenInputItem );
        mouseInputItem.addItemListener( new InputTypeItemListener( inputItems ) );
        touchpadInputItem.addItemListener( new InputTypeItemListener( inputItems ) );
        trackpointInputItem.addItemListener( new InputTypeItemListener( inputItems ) );
        touchpadInputItem.addItemListener( new InputTypeItemListener( inputItems ) );

        MenuItem exitItem = new MenuItem( "Exit" );
        exitItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit( 0 );
                    }
                });

        //Add components to pop-up menu
        popup.add( aboutItem );
        popup.add( debugToggleItem );
        popup.add( inputTypeMenu );
        inputTypeMenu.add( mouseInputItem );
        inputTypeMenu.add( touchpadInputItem );
        inputTypeMenu.add( trackpointInputItem );
        inputTypeMenu.add( touchscreenInputItem );
        popup.add( exitItem );

        trayIcon.setPopupMenu( popup );

        debugFrame = new Frame("Debug");
        debugFrame.setSize( 400, 500 );
        debugFrame.add( debugTextArea );
        debugFrame.pack();
        debugFrame.setVisible( true );

        try {
            tray.add( trayIcon );
        } catch ( AWTException e ) {
            System.out.println( "TrayIcon could not be added." );
        }
    }
}
