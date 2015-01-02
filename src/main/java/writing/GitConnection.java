package writing;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by mar on 02.01.15.
 */
public class GitConnection {
    private final static String COMPUTER_NAME = System.getProperty( "user.name" );
    private static String LOCALHOST_NAME, LOCAL_URL, REMOTE_URL;
    private Git git;
    private Repository repository;
    public static final String GIT_LOGIN = "MrzlMousePaths";
    public static final String GIT_PASS = "publ1c_passw0rd";

    private GitPushThread pushThread;

    public GitConnection( String remoteUrl ) throws IOException, GitAPIException {
        this.LOCALHOST_NAME = getLocalHostName();
        this.REMOTE_URL = remoteUrl;
        this.LOCAL_URL = COMPUTER_NAME + "_" + LOCALHOST_NAME;

        System.out.println( "Git Connection started..." );
        System.out.println( "ComputerName: " + COMPUTER_NAME + " LocalhostName: " + LOCALHOST_NAME );

        File localPath = new File( LOCAL_URL );

        if ( !localPath.exists() ) {
            System.out.println( "doesnt exist yet" );
            // if repository doesn't exist, create the directory and clone
            localPath.mkdirs();
            Git.cloneRepository().setURI( REMOTE_URL ).setDirectory( localPath ).call();
            repository = openLocalRepository( LOCAL_URL );
        } else {
            // otherwise pull all commits
            repository = openLocalRepository( LOCAL_URL );
            PullResult resultPull = new Git( repository ).pull().call();
            System.out.println( "Pull successfull: " + resultPull.isSuccessful() );
        }

        git = new Git( repository );

        pushThread = new GitPushThread( this );
    }

    public String getFolder() {
        return LOCAL_URL;
    }

    public void addAndCommitFile( String fileName ) throws GitAPIException {
        git.add().addFilepattern( fileName ).call();
        git.commit().setMessage( "adds a mousepath from " + this.COMPUTER_NAME ).call();
    }

    public void push() throws GitAPIException {
        RefSpec spec = new RefSpec("refs/heads/master:refs/heads/master");
        CredentialsProvider cp = new UsernamePasswordCredentialsProvider( GIT_LOGIN, GIT_PASS );
        git.push().setRemote( REMOTE_URL ).setRefSpecs(spec).setCredentialsProvider( cp ).call();
        System.out.println( "Finished Pushing." );
    }

    private String getLocalHostName() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch ( UnknownHostException e ) {
            e.printStackTrace();
        }

        return "unknown_host";
    }

    private Repository openLocalRepository( String localUrl ) throws IOException {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        repositoryBuilder.setMustExist( true );
        repositoryBuilder.setGitDir( new File( localUrl + File.separator + ".git" ) );
        Repository repository = repositoryBuilder.build();
        return repository;

    }
}
