package writing;

import both.MousePath;
import both.Vec2D;

import java.util.Date;

/**
 * Created by Marcel on 05.08.2014.
 */
public class MouseRecognizer {

    public static final String MOUSE_BUTTON_LEFT = "left";
    public static final String MOUSE_BUTTON_RIGHT = "right";
    public static final String MOUSE_BUTTON_MIDDLE = "middle";

    private MousePath currentPath;
    private int counter;
    private boolean recording;

    public MouseRecognizer () {
        counter = 0;
        recording = false;
    }

    public void down ( Vec2D location, int mouseButton ) {
        counter++;
        if ( counter == 1 ) {
            recording = true;
            currentPath = new MousePath();
            currentPath.add( new Date().getTime(), location );
        }
        if ( counter == 2 ) {
            recording = false;
            counter = 0;
            currentPath.add( new Date().getTime(), location );
            currentPath.finish();
            currentPath.export();
            currentPath.clear();
        }
    }

    public void up ( Vec2D location, int mouseButton ) {

    }

    public void moved ( Vec2D location ) {
        if ( currentPath != null && recording == true ) {
            this.currentPath.add( new Date().getTime(), location );
        }
    }
}
