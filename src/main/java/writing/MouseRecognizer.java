package writing;

import both.MousePath;
import both.Vec2D;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Marcel on 05.08.2014.
 */
public class MouseRecognizer {

    public static final String MOUSE_BUTTON_LEFT = "left";
    public static final String MOUSE_BUTTON_RIGHT = "right";
    public static final String MOUSE_BUTTON_MIDDLE = "middle";

    public static final String REMOTE_URL = "https://github.com/MousePath/MousePathCollection.git";

    private GitConnection git;

    private MousePath currentPath;
    private int counter;
    private boolean recording;
    private String folderToSaveTo;

    public MouseRecognizer ( String folderToSaveTo ) {
        counter = 0;
        recording = false;
        this.folderToSaveTo = folderToSaveTo;

        try {
            this.git = new GitConnection( REMOTE_URL );
        } catch ( IOException e ) {
            e.printStackTrace();
        } catch ( GitAPIException e ) {
            e.printStackTrace();
        }
    }

    public void down ( Vec2D location, int mouseButton ) {
        counter++;
        if ( counter == 1 ) {
            recording = true;
            currentPath = new MousePath();
            currentPath.add( new Date().getTime(), location );
            System.out.println( "Started a path" );
        }
        if ( counter > 1 ) {
            //recording = false;
            //counter = 0;
            currentPath.add( new Date().getTime(), location );
            currentPath.finish();
            currentPath.export( folderToSaveTo );

            gitExport();

            currentPath.clear();
            System.out.println( "Finished a path" );

            currentPath = new MousePath();
            currentPath.add( new Date().getTime(), location );
            System.out.println( "Started a path" );
        }
    }

    private void gitExport() {
        currentPath.export( this.git.getFolder() + File.separator + this.git.getFolder() );
        try {
            System.out.println( "Adding and Commiting " + currentPath.getOriginalFileName()  );
            git.addAndCommitFile( git.getFolder() + File.separator + currentPath.getOriginalFileName() );
            //git.push();
        } catch ( GitAPIException e ) {
            e.printStackTrace();
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
