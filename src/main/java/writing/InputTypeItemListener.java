package writing;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created by Marcel on 09.08.2014.
 */
public class InputTypeItemListener implements ItemListener {

    public static final String MOUSE_INPUT_TYPE = "MOUSE";
    public static final String TOUCHPAD_INPUT_TYPE = "TOUCHPAD";
    public static final String TRACKPOINT_INPUT_TYPE = "TRACKPOINT";
    public static final String TOUCHSCREEN_INPUT_TYPE = "TOUCHSCREEN";
    public static String CURRENT_INPUT_TYPE = "MOUSE";

    private ArrayList< CheckboxMenuItem > items;

    public InputTypeItemListener ( ArrayList< CheckboxMenuItem > inputItems ) {
        this.items = inputItems;
    }

    @Override
    public void itemStateChanged ( ItemEvent e ) {
        String item = ( String ) e.getItem();
        switch ( item ) {
            case MOUSE_INPUT_TYPE:
                items.get( 0 ).setState( true );
                items.get( 1 ).setState( false );
                items.get( 2 ).setState( false );
                items.get( 3 ).setState( false );
                break;
            case TOUCHPAD_INPUT_TYPE:
                items.get( 0 ).setState( false );
                items.get( 1 ).setState( true );
                items.get( 2 ).setState( false );
                items.get( 3 ).setState( false );
                break;
            case TRACKPOINT_INPUT_TYPE:
                items.get( 0 ).setState( false );
                items.get( 1 ).setState( false );
                items.get( 2 ).setState( true );
                items.get( 3 ).setState( false );
                break;
            case TOUCHSCREEN_INPUT_TYPE:
                items.get( 0 ).setState( false );
                items.get( 1 ).setState( false );
                items.get( 2 ).setState( false );
                items.get( 3 ).setState( true );
                break;
            default:
                break;
        }
        CURRENT_INPUT_TYPE = item;
    }
}
