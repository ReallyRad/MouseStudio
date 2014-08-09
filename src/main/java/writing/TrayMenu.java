package writing;

import com.sun.corba.se.spi.orbutil.fsm.Input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created by Marcel on 07.08.2014.
 */
public class TrayMenu {
    public TrayMenu () {
        if ( !SystemTray.isSupported() ) {
            System.out.println( "SystemTray is not supported" );
            return;
        }
        final PopupMenu popup = new PopupMenu();
        //Image img = new ImageIcon(getClass().getResource("/images/bell-icon16.png")).getImage();
        //System.out.println( getClass().getResource( "mouse-icon.png" ).getPath() );
        Image img = Toolkit.getDefaultToolkit().getImage( "mouse-icon.png" );
        final TrayIcon trayIcon =
                new TrayIcon( img, "MouseRecorder", popup );
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a pop-up menu components
        MenuItem aboutItem = new MenuItem( "About" );
        CheckboxMenuItem cb1 = new CheckboxMenuItem( "Set auto size" );
        CheckboxMenuItem cb2 = new CheckboxMenuItem( "Set tooltip" );
        Menu inputTypeMenu = new Menu( "Input Type" );

        CheckboxMenuItem mouseInputItem = new CheckboxMenuItem( InputTypeItemListener.MOUSE_INPUT_TYPE );
        CheckboxMenuItem touchpadInputItem = new CheckboxMenuItem( InputTypeItemListener.TOUCHPAD_INPUT_TYPE );
        CheckboxMenuItem trackpointInputItem = new CheckboxMenuItem( InputTypeItemListener.TRACKPOINT_INPUT_TYPE );
        CheckboxMenuItem touchscreenInputItem = new CheckboxMenuItem( InputTypeItemListener.TOUCHPAD_INPUT_TYPE );
        mouseInputItem.setState( true );

        ArrayList< CheckboxMenuItem > inputItems = new ArrayList<>();
        inputItems.add( mouseInputItem );
        inputItems.add( touchpadInputItem );
        inputItems.add( trackpointInputItem );
        inputItems.add( touchscreenInputItem );
        mouseInputItem.addItemListener( new InputTypeItemListener( inputItems ) );
        touchpadInputItem.addItemListener( new InputTypeItemListener( inputItems ) );
        trackpointInputItem.addItemListener( new InputTypeItemListener( inputItems ) );
        touchpadInputItem.addItemListener( new InputTypeItemListener( inputItems ) );

        MenuItem exitItem = new MenuItem( "Exit" );
        exitItem.addActionListener( e -> System.exit( 0 ) );

        //Add components to pop-up menu
        popup.add( aboutItem );
        popup.addSeparator();
        popup.add( cb1 );
        popup.add( cb2 );
        popup.addSeparator();
        popup.add( inputTypeMenu );
        inputTypeMenu.add( mouseInputItem );
        inputTypeMenu.add( touchpadInputItem );
        inputTypeMenu.add( trackpointInputItem );
        inputTypeMenu.add( touchscreenInputItem );
        popup.add( exitItem );

        trayIcon.setPopupMenu( popup );

        try {
            tray.add( trayIcon );
        } catch ( AWTException e ) {
            System.out.println( "TrayIcon could not be added." );
        }
    }
}
