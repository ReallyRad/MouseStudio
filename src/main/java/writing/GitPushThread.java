package writing;

import org.eclipse.jgit.api.errors.GitAPIException;

/**
 * Created by mar on 02.01.15.
 */
public class GitPushThread extends Thread {
    private boolean running;
    private GitConnection gitConnection;

    public GitPushThread(GitConnection gitConnection) {
        this.running = false;
        this.gitConnection = gitConnection;
        start();
    }

    public void start() {
        this.running = true;
        super.start();
    }

    public void run() {
        while( running ) {
            try {
                Thread.sleep( 1000 );
                try {
                    this.gitConnection.push();
                } catch ( GitAPIException e ) {
                    e.printStackTrace();
                }
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
        }
    }
}
